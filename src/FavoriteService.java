import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.LinkedHashSet;

/**
 * Gerencia faixas favoritas usando o próprio objeto Track.
 * LinkedHashSet impede duplicatas e preserva a ordem de inclusão.
 */
public class FavoriteService {

    private final Set<Track> favorites = new LinkedHashSet<>();

    /** Adiciona uma faixa favorita; retorna false se ela já estiver na coleção. */
    public boolean add(Track track) {
        Objects.requireNonNull(track, "track nao pode ser nulo");
        return favorites.add(track);
    }

    /** Remove uma faixa favorita; retorna false se ela não estiver cadastrada. */
    public boolean remove(Track track) {
        Objects.requireNonNull(track, "track nao pode ser nulo");
        return favorites.remove(track);
    }

    /**
     * Alterna o estado de favorito: remove se já for favorita ou adiciona se não for.
     * @return false quando removeu; true quando adicionou
     */
    public boolean toggle(Track track) {
        Objects.requireNonNull(track, "track nao pode ser nulo");
        if (favorites.remove(track)) {
            return false;
        }
        favorites.add(track);
        return true;
    }

    /** Informa se a faixa está marcada como favorita. */
    public boolean isFavorite(Track track) {
        Objects.requireNonNull(track, "track nao pode ser nulo");
        return favorites.contains(track);
    }

    /** Devolve uma lista imutável e ordenada pela ordem em que as faixas foram favoritada. */
    public List<Track> getFavorites() {
        return List.copyOf(favorites);
    }

    /** Retorna o total de faixas favoritas. */
    public int count() {
        return favorites.size();
    }

    /** Informa se não há faixas favoritas. */
    public boolean isEmpty() {
        return favorites.isEmpty();
    }

    /** Remove todas as faixas favoritas. */
    public void clear() {
        favorites.clear();
    }
}
