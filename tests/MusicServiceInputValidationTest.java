import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Testes de validação local: não fazem chamadas de rede. */
class MusicServiceInputValidationTest {

    @Test
    void soundCloudRejeitaConsultaVaziaAntesDeExigirToken() {
        var service = new SoundCloudSearchService();

        assertThrows(IllegalArgumentException.class, () -> service.search("  ", 10));
        assertThrows(IllegalArgumentException.class, () -> service.search("música", 0));
        assertThrows(IllegalArgumentException.class, () -> service.search("música", 51));
    }

    @Test
    void lyricsServiceValidaTituloArtistaEDuracao() {
        var service = new LyricsVerificationService();

        assertThrows(IllegalArgumentException.class,
                () -> service.findLyrics(" ", "Artista", 180));
        assertThrows(IllegalArgumentException.class,
                () -> service.findLyrics("Música", " ", 180));
        assertThrows(IllegalArgumentException.class,
                () -> service.findLyrics("Música", "Artista", -1));
    }

    @Test
    void musicBrainzExigeTituloEArtista() {
        var service = new MusicMetadataVerificationService();

        assertThrows(IllegalArgumentException.class, () -> service.search("", "Artista"));
        assertThrows(IllegalArgumentException.class, () -> service.search("Música", " "));
    }
}
