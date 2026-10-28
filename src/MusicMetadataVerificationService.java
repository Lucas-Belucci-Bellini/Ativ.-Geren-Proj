import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Consulta MusicBrainz para verificar títulos e créditos de artista independentes
 * da fonte de streaming. O serviço público exige um User-Agent identificável e
 * limita o uso; esta implementação faz apenas uma requisição por chamada.
 */
public final class MusicMetadataVerificationService {
    private static final String API_BASE = "https://musicbrainz.org/ws/2/recording/";
    private static final String USER_AGENT =
            "Ativ-Geren-Proj/1.0 (https://github.com/Lucas-Belucci-Bellini/Ativ.-Geren-Proj)";
    private final HttpClient httpClient;
    private final ObjectMapper json;

    public MusicMetadataVerificationService() {
        this(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build(),
                new ObjectMapper());
    }

    MusicMetadataVerificationService(HttpClient httpClient, ObjectMapper json) {
        this.httpClient = httpClient;
        this.json = json;
    }

    /** Busca registros de gravação e devolve título, artista e álbum quando encontrados. */
    public List<RecordingMetadata> search(String title, String artist)
            throws IOException, InterruptedException {
        if (title == null || title.isBlank() || artist == null || artist.isBlank()) {
            throw new IllegalArgumentException("Informe título e artista para verificar os metadados.");
        }
        String query = "recording:" + quote(title.strip()) + " AND artist:" + quote(artist.strip());
        URI uri = URI.create(API_BASE + "?query="
                + URLEncoder.encode(query, StandardCharsets.UTF_8) + "&fmt=json&limit=5");
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 429) {
            throw new IOException("MusicBrainz limitou as consultas. Aguarde antes de tentar novamente.");
        }
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("A verificação de metadados falhou (HTTP " + response.statusCode() + ").");
        }

        JsonNode recordings = json.readTree(response.body()).path("recordings");
        List<RecordingMetadata> results = new ArrayList<>();
        if (!recordings.isArray()) return List.of();

        for (JsonNode recording : recordings) {
            String name = recording.path("title").asText("");
            String id = recording.path("id").asText("");
            String artistCredit = artistCredit(recording.path("artist-credit"));
            String album = firstReleaseTitle(recording.path("releases"));
            long durationMillis = recording.path("length").asLong(0);
            if (!name.isBlank() && !artistCredit.isBlank()) {
                results.add(new RecordingMetadata(id, name, artistCredit, album, durationMillis));
            }
        }
        return List.copyOf(results);
    }

    /** Junta os créditos de artista na ordem retornada pelo MusicBrainz. */
    private static String artistCredit(JsonNode credits) {
        StringBuilder result = new StringBuilder();
        if (!credits.isArray()) return "";
        for (JsonNode credit : credits) {
            String name = credit.path("name").asText("");
            if (name.isBlank()) name = credit.path("artist").path("name").asText("");
            result.append(name).append(credit.path("joinphrase").asText(""));
        }
        return result.toString().strip();
    }

    /** Retorna o primeiro lançamento associado, quando houver. */
    private static String firstReleaseTitle(JsonNode releases) {
        return releases.isArray() && !releases.isEmpty()
                ? releases.get(0).path("title").asText(null) : null;
    }

    /** Escapa aspas em termos da sintaxe de busca do MusicBrainz. */
    private static String quote(String value) {
        return "\"" + value.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    /** Metadados normalizados da gravação; não representa confirmação de direitos autorais. */
    public record RecordingMetadata(String id, String title, String artist,
                                    String album, long durationMillis) { }
}
