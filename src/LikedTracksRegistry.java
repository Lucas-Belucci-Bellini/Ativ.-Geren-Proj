import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LikedTracksRegistry {

    private final Set<Long> favoriteIds = new LinkedHashSet<>();

    public boolean addToFavorites(long trackId) {
        return favoriteIds.add(trackId);
    }

    public boolean removeFromFavorites(long trackId) {
        return favoriteIds.remove(trackId);
    }

    public boolean isFavorite(long trackId) {
        return favoriteIds.contains(trackId);
    }

    public List<Long> listFavorites() {
        return List.copyOf(favoriteIds);
    }
}
