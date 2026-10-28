import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Testa o resultado agregado apresentado pela camada de descoberta musical. */
class MusicDiscoveryServiceTest {

    @Test
    void copiaListaDeMetadadosParaEvitarAlteracoesExternas() {
        var lyrics = new LyricsVerificationService.VerificationResult(
                true, true, "Correspondência forte.", "Imagine", "John Lennon",
                "Imagine", 183, "letra", null, false, "LRCLIB");
        var original = new ArrayList<MusicMetadataVerificationService.RecordingMetadata>();
        original.add(new MusicMetadataVerificationService.RecordingMetadata(
                "mbid-1", "Imagine", "John Lennon", "Imagine", 183_000));

        var result = new MusicDiscoveryService.TrackVerification(lyrics, original);
        original.clear();

        assertTrue(result.hasMetadataCandidates());
        assertTrue(result.hasStrongLyricsMatch());
        assertEquals(1, result.metadataMatches().size());
        assertThrows(UnsupportedOperationException.class, () -> result.metadataMatches().clear());
    }

    @Test
    void permiteResultadoSemMetadadosEComLetraNaoVerificada() {
        var lyrics = new LyricsVerificationService.VerificationResult(
                true, false, "Resultado aproximado.", "Imagine", "Outro artista",
                null, 183, "letra", null, false, "LRCLIB");

        var result = new MusicDiscoveryService.TrackVerification(lyrics, null);

        assertFalse(result.hasMetadataCandidates());
        assertFalse(result.hasStrongLyricsMatch());
        assertEquals(List.of(), result.metadataMatches());
    }
}
