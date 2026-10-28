import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Camada de serviço responsável por criar, listar e localizar playlists.
 * O LinkedHashMap mantém as playlists na ordem em que foram criadas.
 */
public class PlaylistService {

    // A chave e o ID da Playlist usam int, evitando a incompatibilidade de tipos anterior.
    private final Map<Integer, Playlist> playlists = new LinkedHashMap<>();
    private int proximoId = 1;

    /**
     * Cria e registra uma playlist com nome válido.
     *
     * @param nome nome informado pelo usuário
     * @return playlist criada e já armazenada no serviço
     * @throws IllegalArgumentException se o nome for nulo ou vazio
     */
    public Playlist criar(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da playlist não pode ser vazio.");
        }

        // O ID é incrementado somente durante a criação de uma nova playlist.
        Playlist nova = new Playlist(proximoId++, nome.strip());
        playlists.put(nova.getId(), nova);
        return nova;
    }

    /** Devolve uma cópia da coleção para impedir alterações externas no mapa interno. */
    public List<Playlist> listar() {
        return new ArrayList<>(playlists.values());
    }

    /**
     * Procura uma playlist pelo ID.
     *
     * @return Optional preenchido quando existe; vazio caso contrário
     */
    public Optional<Playlist> buscarPorId(int id) {
        return Optional.ofNullable(playlists.get(id));
    }
}
