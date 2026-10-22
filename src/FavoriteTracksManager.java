import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class FavoriteTracksManager {

    private final Set<Long> favoriteIds = new LinkedHashSet<>();

    public boolean add(long trackId) {
        return favoriteIds.add(trackId);
    }

    public boolean remove(long trackId) {
        return favoriteIds.remove(trackId);
    }

    public boolean toggle(long trackId) {
        if (favoriteIds.remove(trackId)) {
            return false;
        }
        favoriteIds.add(trackId);
        return true;
    }

    public boolean isFavorite(long trackId) {
        return favoriteIds.contains(trackId);
    }

    public List<Long> getFavoriteIds() {
        return List.copyOf(favoriteIds);
    }

    public int count() {
        return favoriteIds.size();
    }

    public boolean isEmpty() {
        return favoriteIds.isEmpty();
    }

    public void clear() {
        favoriteIds.clear();
    }
}
