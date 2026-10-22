package com.tify.service;

import com.tify.model.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Predicate;

public class CatalogService {

    private static final String TEST_AUDIO_PATH =
            "src/main/resources/audio/test-tone-440hz.wav";

    private final List<Track> tracks;

    public CatalogService() {
        this.tracks = new ArrayList<>(initialCatalog());
    }

    private static List<Track> initialCatalog() {
        return List.of(new Track(
                "test-tone-001",
                "Tom de teste 440 Hz",
                "Ativ-Met-Ageis (fixture sintético)",
                2,
                null,
                new File(TEST_AUDIO_PATH)
        ));
    }

    public List<Track> getAllTracks() {
        return List.copyOf(tracks);
    }

    public Optional<Track> findById(String id) {
        Objects.requireNonNull(id, "id");
        return tracks.stream()
                .filter(track -> track.id().equals(id))
                .findFirst();
    }

    public Track findByIdOrThrow(String id) {
        return findById(id).orElseThrow(() ->
                new NoSuchElementException("Faixa não encontrada para o ID: " + id));
    }

    public List<Track> findByTitle(String term) {
        String needle = Objects.requireNonNull(term, "term").toLowerCase(Locale.ROOT);
        return filterBy(track -> track.title().toLowerCase(Locale.ROOT).contains(needle));
    }

    public List<Track> findByArtist(String artist) {
        String needle = Objects.requireNonNull(artist, "artist").toLowerCase(Locale.ROOT);
        return filterBy(track -> track.artist().toLowerCase(Locale.ROOT).contains(needle));
    }

    public int getTrackCount() {
        return tracks.size();
    }

    public void addTrack(Track track) {
        Objects.requireNonNull(track, "track");
        if (findById(track.id()).isPresent()) {
            throw new IllegalArgumentException("ID de faixa duplicado: " + track.id());
        }
        tracks.add(track);
    }

    public boolean removeById(String id) {
        Objects.requireNonNull(id, "id");
        return tracks.removeIf(track -> track.id().equals(id));
    }

    private List<Track> filterBy(Predicate<Track> predicate) {
        return tracks.stream().filter(predicate).toList();
    }

    public static void main(String[] args) {
        new CatalogService().runMenu();
    }

    private void runMenu() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                System.out.println("\n=== CATÁLOGO DE MÚSICAS (DADOS DE TESTE) ===");
                System.out.println("1. Listar faixas");
                System.out.println("2. Buscar por ID");
                System.out.println("3. Buscar por título");
                System.out.println("4. Buscar por artista");
                System.out.println("0. Sair");
                System.out.print("Escolha uma opção: ");

                if (!scanner.hasNextLine()) {
                    break;
                }

                String option = scanner.nextLine().trim();
                switch (option) {
                    case "1" -> showTracks(getAllTracks());
                    case "2" -> searchById(scanner);
                    case "3" -> searchByTitle(scanner);
                    case "4" -> searchByArtist(scanner);
                    case "0" -> {
                        System.out.println("Programa encerrado.");
                        running = false;
                    }
                    default -> System.out.println("Opção inválida. Tente novamente.");
                }
            }
        }
    }

    private void searchById(Scanner scanner) {
        String id = askForText(scanner, "Digite o ID da faixa: ");
        if (id == null) {
            return;
        }

        Optional<Track> track = findById(id);
        if (track.isPresent()) {
            showTracks(List.of(track.get()));
        } else {
            System.out.println("Nenhuma faixa encontrada com esse ID.");
        }
    }

    private void searchByTitle(Scanner scanner) {
        String title = askForText(scanner, "Digite o título ou parte dele: ");
        if (title == null || title.isBlank()) {
            System.out.println("Digite um título para pesquisar.");
            return;
        }
        showTracks(findByTitle(title));
    }

    private void searchByArtist(Scanner scanner) {
        String artist = askForText(scanner, "Digite o artista ou parte dele: ");
        if (artist == null || artist.isBlank()) {
            System.out.println("Digite um artista para pesquisar.");
            return;
        }
        showTracks(findByArtist(artist));
    }

    private String askForText(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.hasNextLine() ? scanner.nextLine().trim() : null;
    }

    private void showTracks(List<Track> results) {
        if (results.isEmpty()) {
            System.out.println("Nenhuma faixa encontrada.");
            return;
        }

        results.forEach(track -> System.out.println(track.resumo()));
    }
}
