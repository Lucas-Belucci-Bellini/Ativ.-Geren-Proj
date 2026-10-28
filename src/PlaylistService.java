import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PlaylistService {
    private final Map<Integer, Playlist> playlists = new LinkedHashMap<>();

     private int proximoId = 1;

    /**
     * Cria a playlist a partir do nome digitado.
     * Nome nulo, vazio ou formado só por espaços é recusado antes de gravar.
     */
    public Playlist criar(String nome){

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da playlist não pode ser vazio.");
        }

        int id = proximoId++;
        Playlist nova = new Playlist(String.valueOf(id), nome.strip());
        playlists.put(id, nova);
        return nova;
    }

    public List<Playlist> listar() {

        return new ArrayList<>(playlists.values());
    }

    /**
     * Listagem geral organizada com ID, nome e quantidade de faixas.
     */
    public String formatarListagem() {
        List<Playlist> todas = listar();
        if (todas.isEmpty()) {
            return "Nenhuma playlist cadastrada." + System.lineSeparator();
        }

        StringBuilder saida = new StringBuilder();
        saida.append("ID | Nome | Quantidade de faixas").append(System.lineSeparator());
        for (Playlist playlist : todas) {
            saida.append(playlist.getId())
                    .append(" | ")
                    .append(playlist.getNome())
                    .append(" | ")
                    .append(playlist.getQuantidadeFaixas())
                    .append(System.lineSeparator());
        }
        return saida.toString();
    }

    public Optional<Playlist> buscarPorId(int id) {
        return Optional.ofNullable(playlists.get(id));
    }

    /**
     * Consulta a playlist pelo ID e, para cada faixa, busca no catálogo
     * o título, o artista e a duração. Uma faixa ausente não interrompe as outras.
     */
    public String formatarConsulta(int id, CatalogService catalogo) {
        Objects.requireNonNull(catalogo, "catalogo");

        Optional<Playlist> encontrada = buscarPorId(id);
        if (encontrada.isEmpty()) {
            return "Playlist não encontrada: " + id + System.lineSeparator();
        }

        Playlist playlist = encontrada.get();
        StringBuilder saida = new StringBuilder();
        saida.append("Playlist [").append(playlist.getId()).append("] ")
                .append(playlist.getNome()).append(System.lineSeparator());
        if (playlist.getQuantidadeFaixas() == 0) {
            saida.append("Nenhuma faixa associada.").append(System.lineSeparator());
            return saida.toString();
        }

        saida.append("Faixas associadas:").append(System.lineSeparator());
        for (String faixaId : playlist.getFaixaIds()) {
            catalogo.findById(faixaId).ifPresentOrElse(
                    faixa -> saida.append("  Título: ").append(faixa.title())
                            .append(" | Artista: ").append(faixa.artist())
                            .append(" | Duração: ").append(faixa.formattedDuration())
                            .append(System.lineSeparator()),
                    () -> saida.append("  ").append(faixaId)
                            .append(" (não encontrada no catálogo)")
                            .append(System.lineSeparator()));
        }
        return saida.toString();
    }
}
