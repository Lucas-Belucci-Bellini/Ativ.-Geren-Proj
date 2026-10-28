import java.util.NoSuchElementException;
import java.util.Scanner;


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

  
    private void criar() {
        try {
            Playlist nova = playlists.criar(lerLinha("Nome da playlist: "));
            System.out.println("Playlist criada: [" + nova.getId() + "] " + nova.getNome());
        } catch (IllegalArgumentException e) {
            System.out.println("Não foi possível criar: " + e.getMessage());
        }
    }

 
    private void listar() {
        System.out.print(playlists.formatarListagem());
    }


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
