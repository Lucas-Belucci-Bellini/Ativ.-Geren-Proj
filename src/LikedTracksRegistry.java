import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Registro de curtidas que guarda IDs numéricos em memória.
 * Mantém a ordem de inclusão e impede IDs duplicados.
 */
public class LikedTracksRegistry {

    private final Set<Long> favoriteIds = new LinkedHashSet<>();

    /** Registra uma curtida; retorna false se o ID já estiver registrado. */
    public boolean addToFavorites(long trackId) {
        return favoriteIds.add(trackId);
    }

    /** Desfaz a curtida e informa se um registro foi removido. */
    public boolean removeFromFavorites(long trackId) {
        return favoriteIds.remove(trackId);
    }

    /** Consulta se a faixa está curtida. */
    public boolean isFavorite(long trackId) {
        return favoriteIds.contains(trackId);
    }

    /** Retorna uma cópia imutável dos IDs curtidos. */
    public List<Long> listFavorites() {
        return List.copyOf(favoriteIds);
    }
}
