import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FavoriteService {

    private final Set<Track> favorites = new LinkedHashSet<>();

    public boolean add(Track track) {
        Objects.requireNonNull(track, "track nao pode ser nulo");
        return favorites.add(track);
    }

    public boolean remove(Track track) {
        Objects.requireNonNull(track, "track nao pode ser nulo");
        return favorites.remove(track);
    }

    public boolean toggle(Track track) {
        Objects.requireNonNull(track, "track nao pode ser nulo");
        if (favorites.remove(track)) {
            return false;
        }
        favorites.add(track);
        return true;
    }

    public boolean isFavorite(Track track) {
        Objects.requireNonNull(track, "track nao pode ser nulo");
        return favorites.contains(track);
    }

    public List<Track> getFavorites() {
        return Collections.unmodifiableList(List.copyOf(favorites));
    }

    public int count() {
        return favorites.size();
    }

    public boolean isEmpty() {
        return favorites.isEmpty();
    }

    public void clear() {
        favorites.clear();
    }
}
