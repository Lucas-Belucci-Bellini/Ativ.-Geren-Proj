package com.tify.model;

import java.io.File;
import java.util.Objects;

public record Track(
        String id,
        String title,
        String artist,
        int durationSeconds,
        File coverFile,
        File audioFile
) {

    public Track {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(title, "title");
        Objects.requireNonNull(artist, "artist");
        Objects.requireNonNull(audioFile, "audioFile");
        if (durationSeconds < 0) {
            throw new IllegalArgumentException("durationSeconds must be >= 0");
        }
    }

    public String formattedDuration() {
        return "%02d:%02d".formatted(durationSeconds / 60, durationSeconds % 60);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Track t && id.equals(t.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Track{id=%s, title=%s, artist=%s, duration=%s}"
                .formatted(id, title, artist, formattedDuration());
    }
}
