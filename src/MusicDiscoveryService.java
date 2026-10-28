import java.io.IOException;
import java.util.List;

/**
 * Fachada de descoberta musical: reúne a pesquisa de faixas e a conferência
 * de metadados/letras numa API simples para uma futura interface de usuário.
 *
 * Não reproduz áudio nem promete que todas as fontes tenham os mesmos registros.
 * Cada integração continua sujeita a disponibilidade, autenticação e limites próprios.
 */
public final class MusicDiscoveryService {
    private final SoundCloudSearchService soundCloud;
    private final LyricsVerificationService lyrics;
    private final MusicMetadataVerificationService metadata;

    /** Inicializa os provedores padrão configurados para o projeto. */
    public MusicDiscoveryService() {
        this(new SoundCloudSearchService(),
                new LyricsVerificationService(),
                new MusicMetadataVerificationService());
    }

    /** Construtor injetável para facilitar testes e integração com outras camadas. */
    MusicDiscoveryService(SoundCloudSearchService soundCloud,
                          LyricsVerificationService lyrics,
                          MusicMetadataVerificationService metadata) {
        this.soundCloud = soundCloud;
        this.lyrics = lyrics;
        this.metadata = metadata;
    }

    /**
     * Pesquisa faixas públicas no SoundCloud, incluindo URLs de capa e faixa quando disponíveis.
     * O resultado mantém a origem e não baixa nem armazena áudio/imagens.
     */
    public List<SoundCloudSearchService.SoundCloudTrack> search(String query, int limit)
            throws IOException, InterruptedException {
        return soundCloud.search(query, limit);
    }

    /**
     * Confere uma faixa selecionada em duas fontes independentes.
     * A verificação de letras é executada primeiro; se falhar, o erro é propagado
     * para a interface decidir como apresentar/repetir a consulta.
     */
    public TrackVerification verify(String title, String artist, int durationSeconds)
            throws IOException, InterruptedException {
        LyricsVerificationService.VerificationResult lyricsResult =
                lyrics.findLyrics(title, artist, durationSeconds);
        List<MusicMetadataVerificationService.RecordingMetadata> recordings =
                metadata.search(title, artist);
        return new TrackVerification(lyricsResult, recordings);
    }

    /** Resultado combinado para a interface, sem afirmar titularidade legal da música. */
    public record TrackVerification(
            LyricsVerificationService.VerificationResult lyrics,
            List<MusicMetadataVerificationService.RecordingMetadata> metadataMatches) {
        public TrackVerification {
            metadataMatches = metadataMatches == null ? List.of() : List.copyOf(metadataMatches);
        }

        /** Existe ao menos uma fonte de metadados para comparar com a faixa selecionada. */
        public boolean hasMetadataCandidates() {
            return !metadataMatches.isEmpty();
        }

        /** Letras verificadas pelo critério técnico do LRCLIB; não é verificação de direitos autorais. */
        public boolean hasStrongLyricsMatch() {
            return lyrics != null && lyrics.found() && lyrics.verified();
        }
    }
}
