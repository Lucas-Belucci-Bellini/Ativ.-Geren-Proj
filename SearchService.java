package spotifyclone.search;

import spotifyclone.model.Track;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/** Search use-cases for the music catalogue, independent of the terminal UI. */
public final class SearchService {

    /**
     * Finds tracks whose title or artist contains the supplied term.
     * Matching is case-insensitive; leading/trailing whitespace is ignored.
     * The returned list preserves catalogue order and is immutable.
     *
     * @throws IllegalArgumentException when the search term is null or blank
     * @throws NullPointerException when the catalogue itself is null
     */
    public List<Track> search(String term, Collection<Track> catalogue) {
        if (term == null) {
            throw new IllegalArgumentException(
                    "O termo de pesquisa não pode ser nulo.");
        }

        String cleanedTerm = term.strip();
        if (cleanedTerm.isEmpty()) {
            throw new IllegalArgumentException(
                    "Digite um termo de pesquisa antes de continuar.");
        }

        Objects.requireNonNull(catalogue, "O catálogo de músicas é obrigatório.");
        String normalizedTerm = cleanedTerm.toLowerCase(Locale.ROOT);

        return catalogue.stream()
                .filter(Objects::nonNull)
                .filter(track -> containsIgnoreCase(track.title(), normalizedTerm)
                        || containsIgnoreCase(track.artist(), normalizedTerm))
                .toList();
    }

    private static boolean containsIgnoreCase(String value, String normalizedTerm) {
        return value.toLowerCase(Locale.ROOT).contains(normalizedTerm);
    }
}
