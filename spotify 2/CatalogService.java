package com.tify.service;

import com.tify.model.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public class CatalogService {

    private final List<Track> tracks;

    public CatalogService() {
        this.tracks = new ArrayList<>(initialCatalog());
    }

    private static List<Track> initialCatalog() {
        return List.of(
                new Track("t-001", "Bohemian Rhapsody", "Queen", 354,
                        new File("covers/bohemian_rhapsody.jpg"),
                        new File("audio/bohemian_rhapsody.mp3")),
                new Track("t-002", "Imagine", "John Lennon", 183,
                        new File("covers/imagine.jpg"),
                        new File("audio/imagine.mp3")),
                new Track("t-003", "Billie Jean", "Michael Jackson", 294,
                        new File("covers/billie_jean.jpg"),
                        new File("audio/billie_jean.mp3")),
                new Track("t-004", "Stairway to Heaven", "Led Zeppelin", 482,
                        new File("covers/stairway_to_heaven.jpg"),
                        new File("audio/stairway_to_heaven.mp3")),
                new Track("t-005", "Hotel California", "Eagles", 391,
                        new File("covers/hotel_california.jpg"),
                        new File("audio/hotel_california.mp3")),
                new Track("t-006", "Smells Like Teen Spirit", "Nirvana", 301,
                        new File("covers/smells_like_teen_spirit.jpg"),
                        new File("audio/smells_like_teen_spirit.mp3")),
                new Track("t-007", "Hey Jude", "The Beatles", 431,
                        new File("covers/hey_jude.jpg"),
                        new File("audio/hey_jude.mp3")),
                new Track("t-008", "Like a Rolling Stone", "Bob Dylan", 369,
                        new File("covers/like_a_rolling_stone.jpg"),
                        new File("audio/like_a_rolling_stone.mp3"))
        );
    }

    public List<Track> getAllTracks() {
        return List.copyOf(tracks);
    }

    public Optional<Track> findById(String id) {
        return tracks.stream()
                .filter(t -> t.id().equals(id))
                .findFirst();
    }

    public List<Track> findByTitle(String term) {
        String needle = term.toLowerCase();
        return filterBy(t -> t.title().toLowerCase().contains(needle));
    }

    public List<Track> findByArtist(String artist) {
        String needle = artist.toLowerCase();
        return filterBy(t -> t.artist().toLowerCase().contains(needle));
    }

    public int getTrackCount() {
        return tracks.size();
    }

    public void addTrack(Track track) {
        Objects.requireNonNull(track, "track");
        if (findById(track.id()).isPresent()) {
            throw new IllegalArgumentException("Duplicate track id: " + track.id());
        }
        tracks.add(track);
    }

    public boolean removeById(String id) {
        return tracks.removeIf(t -> t.id().equals(id));
    }

    private List<Track> filterBy(Predicate<Track> predicate) {
        return tracks.stream().filter(predicate).toList();
    }
}
