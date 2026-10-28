import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Procura letras e confere se título/artista correspondem à faixa pesquisada.
 *
 * Usa a API LRCLIB, que não exige chave de API. A aplicação identifica a fonte
 * e não trata uma correspondência aproximada como certeza. Respeite os limites
 * do provedor e suas condições de uso ao integrar a funcionalidade numa interface.
 */
public final class LyricsVerificationService {
    private static final String API_BASE = "https://lrclib.net/api";
    private static final String USER_AGENT =
            "Ativ-Geren-Proj/1.0 (https://github.com/Lucas-Belucci-Bellini/Ativ.-Geren-Proj)";
    private final HttpClient httpClient;
    private final ObjectMapper json;

    public LyricsVerificationService() {
        this(HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build(),
                new ObjectMapper());
    }

    LyricsVerificationService(HttpClient httpClient, ObjectMapper json) {
        this.httpClient = httpClient;
        this.json = json;
    }

    /**
     * Pesquisa por título e artista, classifica resultados e devolve o melhor candidato.
     * Duração zero significa que a aplicação ainda não conhece a duração e não a compara.
     */
    public VerificationResult findLyrics(String title, String artist, int durationSeconds)
            throws IOException, InterruptedException {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("O título da música é obrigatório.");
        }
        if (artist == null || artist.isBlank()) {
            throw new IllegalArgumentException("O nome do artista é obrigatório para conferir a faixa.");
        }
        if (durationSeconds < 0) {
            throw new IllegalArgumentException("A duração não pode ser negativa.");
        }

        String query = "track_name=" + encode(title.strip())
                + "&artist_name=" + encode(artist.strip());
        URI uri = URI.create(API_BASE + "/search?" + query);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 429) {
            throw new IOException("O serviço de letras pediu para aguardar antes de pesquisar novamente.");
        }
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("A pesquisa de letras falhou (HTTP " + response.statusCode() + ").");
        }

        JsonNode root = json.readTree(response.body());
        if (!root.isArray() || root.isEmpty()) {
            return VerificationResult.notFound(title, artist);
        }

        List<LyricsCandidate> candidates = new ArrayList<>();
        for (JsonNode item : root) {
            candidates.add(new LyricsCandidate(
                    item.path("trackName").asText(""),
                    item.path("artistName").asText(""),
                    item.path("albumName").asText(""),
                    item.path("duration").asInt(0),
                    nullableText(item, "plainLyrics"),
                    nullableText(item, "syncedLyrics"),
                    item.path("instrumental").asBoolean(false)
            ));
        }

        // Primeiro prioriza igualdade de título/artista; depois, menor diferença de duração.
        LyricsCandidate best = candidates.stream()
                .max(Comparator.comparingInt((LyricsCandidate c) -> matchScore(title, artist, durationSeconds, c))
                        .thenComparingInt(c -> -Math.abs(durationSeconds == 0
                                ? 0 : c.durationSeconds() - durationSeconds)))
                .orElse(null);

        if (best == null || matchScore(title, artist, durationSeconds, best) < 4) {
            return VerificationResult.notFound(title, artist);
        }

        int difference = durationSeconds == 0 ? 0 : Math.abs(best.durationSeconds() - durationSeconds);
        boolean exactTitle = normalize(title).equals(normalize(best.title()));
        boolean exactArtist = normalize(artist).equals(normalize(best.artist()));
        boolean durationMatches = durationSeconds == 0 || difference <= 2;
        boolean verified = exactTitle && exactArtist && durationMatches;
        return new VerificationResult(
                true, verified, verified ? "Correspondência forte de título, artista e duração."
                        : "Resultado aproximado: confira título, artista e duração antes de usar.",
                best.title(), best.artist(), best.album(), best.durationSeconds(),
                best.plainLyrics(), best.syncedLyrics(), best.instrumental(), "LRCLIB"
        );
    }

    /** Pontuação simples e explicável: título e artista valem mais que duração aproximada. */
    static int matchScore(String title, String artist, int durationSeconds, LyricsCandidate candidate) {
        int score = 0;
        if (normalize(title).equals(normalize(candidate.title()))) score += 3;
        if (normalize(artist).equals(normalize(candidate.artist()))) score += 3;
        if (durationSeconds > 0 && Math.abs(durationSeconds - candidate.durationSeconds()) <= 2) score += 2;
        else if (durationSeconds > 0 && Math.abs(durationSeconds - candidate.durationSeconds()) <= 5) score += 1;
        return score;
    }

    /** Normaliza caixa, acentos e espaços para comparar nomes com menos falsos negativos. */
    static String normalize(String value) {
        String decomposed = Normalizer.normalize(value == null ? "" : value, Normalizer.Form.NFD);
        return decomposed.replaceAll("\\p{M}+", "")
                .toLowerCase(Locale.ROOT).replaceAll("\\s+", " ").strip();
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private static String nullableText(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() || value.asText().isBlank()
                ? null : value.asText();
    }

    /** Um candidato vindo do provedor de letras. */
    record LyricsCandidate(String title, String artist, String album, int durationSeconds,
                           String plainLyrics, String syncedLyrics, boolean instrumental) { }

    /** Resultado explícito, inclusive quando não existe letra ou a correspondência é fraca. */
    public record VerificationResult(
            boolean found,
            boolean verified,
            String message,
            String title,
            String artist,
            String album,
            int durationSeconds,
            String plainLyrics,
            String syncedLyrics,
            boolean instrumental,
            String source
    ) {
        static VerificationResult notFound(String title, String artist) {
            return new VerificationResult(false, false,
                    "Nenhuma correspondência confiável foi encontrada para o título e artista informados.",
                    title, artist, null, 0, null, null, false, "LRCLIB");
        }
    }
}
