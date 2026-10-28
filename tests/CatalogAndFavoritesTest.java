import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/** Testes para catálogo, duração e favoritos. */
class CatalogAndFavoritesTest {

    @Test
    void catalogoInicialPossuiFaixasEPermiteBusca() {
        CatalogService catalog = new CatalogService();

        assertTrue(catalog.getTrackCount() > 0);
        assertTrue(catalog.findById("t-001").isPresent());
        assertFalse(catalog.findByTitle("BOHEMIAN").isEmpty());
        assertFalse(catalog.findByArtist("queen").isEmpty());
    }

    @Test
    void naoPermiteIdDuplicadoNoCatalogo() {
        CatalogService catalog = new CatalogService();
        Track original = catalog.getAllTracks().getFirst();
        Track duplicada = new Track(original.id(), "Outro título", "Outro artista",
                10, null, new File("outro.mp3"));

        assertThrows(IllegalArgumentException.class, () -> catalog.addTrack(duplicada));
    }

    @Test
    void favoritosNaoDuplicamEAlternamEstado() {
        Track track = new Track("fav-1", "Música", "Artista", 125, null, new File("audio.mp3"));
        FavoriteService favorites = new FavoriteService();

        assertTrue(favorites.add(track));
        assertFalse(favorites.add(track));
        assertTrue(favorites.isFavorite(track));
        assertFalse(favorites.toggle(track));
        assertFalse(favorites.isFavorite(track));
        assertEquals(0, favorites.count());
    }

    @Test
    void formataDuracaoEComparaFaixasPorId() {
        Track primeira = new Track("same", "Uma", "A", 194, null, new File("a.mp3"));
        Track segunda = new Track("same", "Outra", "B", 12, null, new File("b.mp3"));

        assertEquals("03:14", primeira.formattedDuration());
        assertEquals(primeira, segunda);
        assertEquals(primeira.hashCode(), segunda.hashCode());
    }
}
