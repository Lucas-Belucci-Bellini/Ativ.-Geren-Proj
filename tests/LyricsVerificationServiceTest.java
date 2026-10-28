import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Testes unitários das regras de comparação do verificador de letras. */
class LyricsVerificationServiceTest {

    @Test
    void normalizaAcentosMaiusculasEEspacos() {
        assertEquals(LyricsVerificationService.normalize("  ÁRTISTA   X "),
                LyricsVerificationService.normalize("artista x"));
    }

    @Test
    void pontuaCorrespondenciaExataDeTituloArtistaEDuracao() {
        LyricsVerificationService.LyricsCandidate candidate =
                new LyricsVerificationService.LyricsCandidate(
                        "Minha Música", "Artista", "Álbum", 180, "letra", null, false);

        assertEquals(8, LyricsVerificationService.matchScore("Minha Música", "Artista", 180, candidate));
    }

    @Test
    void reduzPontuacaoQuandoArtistaNaoConfere() {
        LyricsVerificationService.LyricsCandidate candidate =
                new LyricsVerificationService.LyricsCandidate(
                        "Minha Música", "Outro Artista", "Álbum", 180, "letra", null, false);

        assertEquals(5, LyricsVerificationService.matchScore("Minha Música", "Artista", 180, candidate));
    }
}
