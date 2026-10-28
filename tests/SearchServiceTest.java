import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Testes da pesquisa por título e artista. */
class SearchServiceTest {

    private final SearchService service = new SearchService();
    private final List<Track> tracks = List.of(
            new Track("1", "Bohemian Rhapsody", "Queen", 354, null, new File("a.mp3")),
            new Track("2", "Imagine", "John Lennon", 183, null, new File("b.mp3"))
    );

    @Test
    void pesquisaTituloIgnorandoMaiusculasEEspacos() {
        assertEquals("1", service.search("  BOHEMIAN ", tracks).getFirst().id());
    }

    @Test
    void pesquisaArtista() {
        assertEquals("2", service.search("lennon", tracks).getFirst().id());
    }

    @Test
    void rejeitaTermoNuloOuVazio() {
        assertThrows(IllegalArgumentException.class, () -> service.search(null, tracks));
        assertThrows(IllegalArgumentException.class, () -> service.search("  ", tracks));
    }

    @Test
    void retornaListaVaziaQuandoNaoHaCorrespondencia() {
        assertTrue(service.search("artista inexistente", tracks).isEmpty());
    }
}
