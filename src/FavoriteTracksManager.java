import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Gerenciador simples de favoritos que armazena apenas IDs numéricos de faixas.
 * É uma alternativa leve ao FavoriteService, que armazena objetos Track.
 */
public class FavoriteTracksManager {

    private final Set<Long> favoriteIds = new LinkedHashSet<>();

    /** Adiciona um ID e retorna false quando ele já estava nos favoritos. */
    public boolean add(long trackId) {
        return favoriteIds.add(trackId);
    }

    /** Remove um ID e informa se ele existia na coleção. */
    public boolean remove(long trackId) {
        return favoriteIds.remove(trackId);
    }

    /** Alterna o favorito de um ID e retorna o novo estado (true = favorito). */
    public boolean toggle(long trackId) {
        if (favoriteIds.remove(trackId)) {
            return false;
        }
        favoriteIds.add(trackId);
        return true;
    }

    /** Consulta se um ID está favoritado. */
    public boolean isFavorite(long trackId) {
        return favoriteIds.contains(trackId);
    }

    /** Retorna os IDs favoritos em uma lista imutável, preservando a ordem de inclusão. */
    public List<Long> getFavoriteIds() {
        return List.copyOf(favoriteIds);
    }

    /** Quantidade de IDs favoritados. */
    public int count() {
        return favoriteIds.size();
    }

    /** Indica se a coleção está vazia. */
    public boolean isEmpty() {
        return favoriteIds.isEmpty();
    }

    /** Limpa todos os favoritos cadastrados. */
    public void clear() {
        favoriteIds.clear();
    }
}
