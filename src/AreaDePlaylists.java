import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Área de playlists no terminal, com Criar, Listar, Consultar e Voltar.
 * Fica fora da Main para não alterar o menu dos outros arquivos do grupo.
 * Pode ser executada com {@code java AreaDePlaylists} ou aberta pelo método {@link #abrir()}.
 */
public class AreaDePlaylists {

    private final PlaylistService playlists;
    private final CatalogService catalogo;
    private final Scanner entrada;

    public AreaDePlaylists(PlaylistService playlists, CatalogService catalogo, Scanner entrada) {
        this.playlists = playlists;
        this.catalogo = catalogo;
        this.entrada = entrada;
    }

    public static void main(String[] args) {
        System.out.println("=== Área de playlists ===");
        try {
            new AreaDePlaylists(new PlaylistService(), new CatalogService(), new Scanner(System.in)).abrir();
        } catch (NoSuchElementException e) {
            // nextLine() lança esta exceção quando a entrada do terminal acaba.
            System.out.println("\nEntrada encerrada.");
        }
        System.out.println("Até logo!");
    }

    /**
     * Mantém o submenu aberto até a opção Voltar.
     */
    public void abrir() {
        while (true) {
            System.out.println("""

                    === Área de playlists ===
                    1. Criar
                    2. Listar
                    3. Consultar
                    0. Voltar
                    """);
            switch (lerLinha("Escolha uma opção: ").strip()) {
                case "1" -> criar();
                case "2" -> listar();
                case "3" -> consultar();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Opção inválida. Digite um número de 0 a 3.");
            }
        }
    }

    /**
     * Lê o nome e delega a rejeição de vazio ou só espaços ao PlaylistService.
     */
    private void criar() {
        try {
            Playlist nova = playlists.criar(lerLinha("Nome da playlist: "));
            System.out.println("Playlist criada: [" + nova.getId() + "] " + nova.getNome());
        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível criar: " + e.getMessage());
        }
    }

    /**
     * Mostra ID, nome e quantidade de faixas de cada playlist cadastrada.
     */
    private void listar() {
        System.out.print(playlists.formatarListagem());
    }

    /**
     * Pede o ID numérico, busca a playlist e imprime os detalhes de cada faixa no catálogo.
     */
    private void consultar() {
        String texto = lerLinha("ID da playlist: ").strip();
        if (texto.isEmpty()) {
            System.out.println("Informe o ID da playlist.");
            return;
        }

        final int id;
        try {
            id = Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            System.out.println("Id de playlist inválido: \"" + texto + "\" (use apenas números).");
            return;
        }

        System.out.print(playlists.formatarConsulta(id, catalogo));
    }

    private String lerLinha(String prompt) {
        System.out.print(prompt);
        return entrada.nextLine();
    }
}
