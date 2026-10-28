import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de terminal inicial para descobrir músicas e conferir metadados.
 * A reprodução de áudio e uma interface gráfica serão etapas posteriores.
 */
public class Main {
    private static final int RESULT_LIMIT = 10;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            MusicDiscoveryService music = new MusicDiscoveryService();
            boolean running = true;

            System.out.println("====================================");
            System.out.println("       CATÁLOGO MUSICAL");
            System.out.println("====================================");

            while (running) {
                System.out.println("\n1. Buscar músicas no SoundCloud");
                System.out.println("2. Verificar título, artista e letra");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");
                String option = scanner.nextLine().strip();

                switch (option) {
                    case "1" -> searchAndVerify(scanner, music);
                    case "2" -> verifyManual(scanner, music);
                    case "0" -> running = false;
                    default -> System.out.println("Opção inválida. Escolha 1, 2 ou 0.");
                }
            }
            System.out.println("Até mais!");
        }
    }

    private static void searchAndVerify(Scanner scanner, MusicDiscoveryService music) {
        System.out.print("Digite o título, artista ou palavras-chave: ");
        String query = scanner.nextLine().strip();
        if (query.isBlank()) {
            System.out.println("Digite algum termo para pesquisar.");
            return;
        }

        try {
            List<SoundCloudSearchService.SoundCloudTrack> tracks =
                    music.search(query, RESULT_LIMIT);
            if (tracks.isEmpty()) {
                System.out.println("Nenhuma faixa foi encontrada.");
                return;
            }

            System.out.println("\nResultados:");
            for (int i = 0; i < tracks.size(); i++) {
                var track = tracks.get(i);
                System.out.printf("%d. %s — %s (%s)%n", i + 1, track.title(),
                        track.artist(), formatDuration(track.durationMillis()));
                if (track.artworkUrl() != null) System.out.println("   Capa: " + track.artworkUrl());
                if (track.permalinkUrl() != null) System.out.println("   Abrir: " + track.permalinkUrl());
            }

            System.out.print("\nNúmero da faixa para verificar (0 para voltar): ");
            int selected = readNumber(scanner);
            if (selected == 0) return;
            if (selected < 1 || selected > tracks.size()) {
                System.out.println("Número fora da lista.");
                return;
            }

            var track = tracks.get(selected - 1);
            int durationSeconds = (int) Math.round(track.durationMillis() / 1000.0);
            showVerification(scanner, music, track.title(), track.artist(), durationSeconds);
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            System.out.println("Consulte docs/INTEGRACOES-MUSICAIS.md para configurar o token.");
        } catch (IOException e) {
            System.out.println("Não foi possível pesquisar no SoundCloud: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Pesquisa interrompida.");
        }
    }

    private static void verifyManual(Scanner scanner, MusicDiscoveryService music) {
        System.out.print("Título da música: ");
        String title = scanner.nextLine().strip();
        System.out.print("Nome do artista: ");
        String artist = scanner.nextLine().strip();
        System.out.print("Duração em segundos (0 se desconhecida): ");
        int duration = readNumber(scanner);

        try {
            showVerification(scanner, music, title, artist, duration);
        } catch (IllegalArgumentException e) {
            System.out.println("Confira os dados: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Não foi possível consultar os provedores: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Verificação interrompida.");
        }
    }

    private static void showVerification(Scanner scanner, MusicDiscoveryService music, String title,
                                         String artist, int durationSeconds)
            throws IOException, InterruptedException {
        var result = music.verify(title, artist, durationSeconds);
        var lyrics = result.lyrics();

        System.out.println("\n--- Conferência da faixa ---");
        System.out.println("Letras: " + lyrics.message());
        if (lyrics.found()) {
            System.out.println("Candidato LRCLIB: " + lyrics.title() + " — " + lyrics.artist());
            if (lyrics.album() != null) System.out.println("Álbum: " + lyrics.album());
            System.out.println("Instrumental: " + (lyrics.instrumental() ? "sim" : "não"));
            System.out.println("Letra disponível: "
                    + (lyrics.plainLyrics() != null || lyrics.syncedLyrics() != null ? "sim" : "não"));
        }

        System.out.println("Candidatos de metadados MusicBrainz: "
                + result.metadataMatches().size());
        result.metadataMatches().stream().limit(5).forEach(recording ->
                System.out.println("- " + recording.title() + " — " + recording.artist()
                        + (recording.album() == null ? "" : " | Álbum: " + recording.album())));

        System.out.println("Observação: correspondência técnica não comprova direitos autorais.");

        if (lyrics.found() && (lyrics.plainLyrics() != null || lyrics.syncedLyrics() != null)) {
            System.out.print("Deseja mostrar a letra retornada pela fonte? (s/n): ");
            String answer = scanner.nextLine().strip();
            if (answer.equalsIgnoreCase("s") || answer.equalsIgnoreCase("sim")) {
                String text = lyrics.plainLyrics() != null
                        ? lyrics.plainLyrics() : lyrics.syncedLyrics();
                System.out.println("\n--- Letra fornecida por " + lyrics.source() + " ---");
                System.out.println(text);
                System.out.println("\nFonte: " + lyrics.source()
                        + " (consulte os termos e a disponibilidade do provedor).");
            }
        } else {
            System.out.println("A fonte não retornou texto de letra para esta correspondência.");
        }
    }

    private static int readNumber(Scanner scanner) {
        String value = scanner.nextLine().strip();
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String formatDuration(long durationMillis) {
        long totalSeconds = Math.max(0, durationMillis / 1000);
        return "%02d:%02d".formatted(totalSeconds / 60, totalSeconds % 60);
    }
}
