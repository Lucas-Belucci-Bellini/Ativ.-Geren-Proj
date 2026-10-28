import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlaylistService {
    private final Map<Integer, Playlist> playlists = new LinkedHashMap<>();

     private int proximoId = 1;

    public Playlist criar(String nome){

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da playlist não pode ser vazio.");
        }

        int id = proximoId++;
        Playlist nova = new Playlist(String.valueOf(id), nome.strip());
        Playlist put = playlists.put(id, nova);
        return nova;
    }

    public List<Playlist> listar() {

        return new ArrayList<>(playlists.values());
    }


    public Optional<Playlist> buscarPorId(int id) {
        return Optional.ofNullable(playlists.get(id));
    }
    
}
