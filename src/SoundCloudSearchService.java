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
 * Pesquisa faixas na API oficial do SoundCloud.
 *
 * Configure SOUNDCLOUD_ACCESS_TOKEN no ambiente antes de usar esta classe.
 * O token não deve ser colocado no código-fonte, no Git ou em arquivos públicos.
 * A classe devolve metadados e URLs; a reprodução continua sujeita às regras do SoundCloud.
 */
public final class SoundCloudSearchService {
    private static final String API_BASE = "https://api.soundcloud.com";
    private final HttpClient httpClient;
    private final ObjectMapper json;
    private final String accessToken;

    /** Usa o token configurado no ambiente e um cliente HTTP com timeout. */
    public SoundCloudSearchService() {
        this(System.getenv("SOUNDCLOUD_ACCESS_TOKEN"),
                HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build(),
                new ObjectMapper());
    }

    /** Construtor injetável para facilitar testes e configuração. */
    SoundCloudSearchService(String accessToken, HttpClient httpClient, ObjectMapper json) {
        this.accessToken = accessToken;
        this.httpClient = httpClient;
        this.json = json;
    }

    /**
     * Busca faixas públicas por título, artista ou palavras-chave.
     *
     * @param query termo de busca
     * @param limit quantidade desejada entre 1 e 50
     * @return resultados com artista, capa e URL original quando disponíveis
     */
    public List<SoundCloudTrack> search(String query, int limit) throws IOException, InterruptedException {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Digite o nome da música ou do artista.");
        }
        if (limit < 1 || limit > 50) {
            throw new IllegalArgumentException("O limite deve ficar entre 1 e 50.");
        }
        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalStateException(
                    "SoundCloud não configurado: defina a variável SOUNDCLOUD_ACCESS_TOKEN.");
        }

        String encodedQuery = URLEncoder.encode(query.strip(), StandardCharsets.UTF_8);
        URI uri = URI.create(API_BASE + "/tracks?q=" + encodedQuery
                + "&limit=" + limit + "&linked_partitioning=true");

        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .header("Authorization", "OAuth " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        if (response.statusCode() == 401 || response.statusCode() == 403) {
            throw new IOException("SoundCloud recusou o acesso. Verifique token e permissões.");
        }
        if (response.statusCode() == 429) {
            throw new IOException("Limite de requisições do SoundCloud atingido. Tente mais tarde.");
        }
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException("A busca no SoundCloud falhou (HTTP " + response.statusCode() + ").");
        }

        JsonNode root = json.readTree(response.body());
        // A API pode retornar uma coleção paginada ou uma lista JSON direta.
        JsonNode collection = root.isArray() ? root : root.path("collection");
        List<SoundCloudTrack> tracks = new ArrayList<>();
        if (!collection.isArray()) {
            return List.of();
        }

        for (JsonNode item : collection) {
            JsonNode user = item.path("user");
            String title = textOrNull(item, "title");
            String artist = textOrNull(user, "username");
            if (title == null || artist == null) {
                continue; // Ignora resultados incompletos em vez de inventar metadados.
            }
            tracks.add(new SoundCloudTrack(
                    textOrNull(item, "urn"),
                    title,
                    artist,
                    textOrNull(item, "artwork_url"),
                    textOrNull(item, "permalink_url"),
                    item.path("duration").asLong(0)
            ));
        }
        return List.copyOf(tracks);
    }

    /** Lê um campo textual opcional do JSON, devolvendo null quando ausente. */
    private static String textOrNull(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isMissingNode() || value.isNull() || value.asText().isBlank()
                ? null : value.asText();
    }

    /** Modelo enxuto de resultado, sem guardar ou redistribuir o áudio da plataforma. */
    public record SoundCloudTrack(
            String id,
            String title,
            String artist,
            String artworkUrl,
            String permalinkUrl,
            long durationMillis
    ) { }
}
