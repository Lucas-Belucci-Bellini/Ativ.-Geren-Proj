import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Testes de regressão para criação e busca de playlists. */
class PlaylistServiceTest {

    @Test
    void criaPlaylistComIdNumericoETrimNoNome() {
        PlaylistService service = new PlaylistService();

        Playlist playlist = service.criar("  Favoritas  ");

        assertEquals(1, playlist.getId());
        assertEquals("Favoritas", playlist.getNome());
        assertSame(playlist, service.buscarPorId(1).orElseThrow());
    }

    @Test
    void rejeitaNomeNuloOuVazio() {
        PlaylistService service = new PlaylistService();

        assertThrows(IllegalArgumentException.class, () -> service.criar(null));
        assertThrows(IllegalArgumentException.class, () -> service.criar("   "));
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void idsSaoIncrementaisEListaPreservaOrdem() {
        PlaylistService service = new PlaylistService();

        Playlist primeira = service.criar("Uma");
        Playlist segunda = service.criar("Duas");

        assertEquals(1, primeira.getId());
        assertEquals(2, segunda.getId());
        assertEquals(java.util.List.of(primeira, segunda), service.listar());
        assertTrue(service.buscarPorId(99).isEmpty());
    }

    @Test
    void playlistNaoDuplicaFaixas() {
        Playlist playlist = new Playlist(1, "Teste");

        assertTrue(playlist.adicionarFaixa("track-1"));
        assertFalse(playlist.adicionarFaixa("track-1"));
        assertEquals(1, playlist.getQuantidadeFaixas());
        assertTrue(playlist.removerFaixa("track-1"));
        assertFalse(playlist.removerFaixa("track-1"));
    }
}
