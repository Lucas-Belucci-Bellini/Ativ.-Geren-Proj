import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Busca faixas do catálogo por trecho do título ou do artista. */
public class SearchService {

    private final CatalogService catalogService;

    public SearchService(CatalogService catalogService) {
        this.catalogService = Objects.requireNonNull(catalogService, "catalogService");
    }

    /**
     * Retorna as faixas cujo título ou artista contém o termo informado,
     * sem diferenciar maiúsculas/minúsculas. Entradas nulas ou em branco
     * retornam uma lista vazia para permitir que a interface continue ativa.
     */
    public List<Track> search(String term) {
        if (term == null || term.isBlank()) {
            return List.of();
        }

        String normalizedTerm = term.trim().toLowerCase(Locale.ROOT);
        return catalogService.getAllTracks().stream()
                .filter(track -> track.title().toLowerCase(Locale.ROOT).contains(normalizedTerm)
                        || track.artist().toLowerCase(Locale.ROOT).contains(normalizedTerm))
                .toList();
    }
}
