import { describe, expect, it } from "vitest";
import { findNextTrack, formatTime, searchTracks } from "./catalog";
import type { Track } from "./types";

const sampleTracks: Track[] = [
  { id: "one", title: "Café da Manhã", artist: "Lia Campos", album: "Pequenas Manhãs", duration: 201, genre: "MPB", mood: "Solar", cover: "citrus", colorName: "Cítrico" },
  { id: "two", title: "Órbita Lenta", artist: "Satélite 8", album: "Sinais Distantes", duration: 232, genre: "Eletrônica", mood: "Espacial", cover: "ocean", colorName: "Oceano" },
];

describe("searchTracks", () => {
  it("matches accented text without requiring the accent", () => {
    expect(searchTracks(sampleTracks, "cafe").map((track) => track.id)).toEqual(["one"]);
  });
  it("searches title, artist, album and genre case-insensitively", () => {
    expect(searchTracks(sampleTracks, "SATÉLITE").map((track) => track.id)).toEqual(["two"]);
    expect(searchTracks(sampleTracks, "mpb").map((track) => track.id)).toEqual(["one"]);
  });
  it("combines the query and genre filter", () => {
    expect(searchTracks(sampleTracks, "", "Eletrônica").map((track) => track.id)).toEqual(["two"]);
    expect(searchTracks(sampleTracks, "orbita", "MPB")).toEqual([]);
  });
  it("returns all tracks for an empty query and default genre", () => {
    expect(searchTracks(sampleTracks, "")).toHaveLength(2);
  });
});

describe("formatTime", () => {
  it("formats seconds as minutes and two-digit seconds", () => {
    expect(formatTime(0)).toBe("0:00");
    expect(formatTime(65)).toBe("1:05");
    expect(formatTime(201.9)).toBe("3:21");
  });
  it("safely formats negative and invalid durations", () => {
    expect(formatTime(-5)).toBe("0:00");
    expect(formatTime(Number.NaN)).toBe("0:00");
  });
});

describe("findNextTrack", () => {
  it("advances and wraps to the first track", () => {
    expect(findNextTrack(sampleTracks, "one").id).toBe("two");
    expect(findNextTrack(sampleTracks, "two").id).toBe("one");
  });
  it("supports previous track and safely handles an unknown ID", () => {
    expect(findNextTrack(sampleTracks, "two", -1).id).toBe("one");
    expect(findNextTrack(sampleTracks, "missing").id).toBe("one");
  });
  it("rejects an empty catalog", () => {
    expect(() => findNextTrack([], "missing")).toThrow(/sem faixas/);
  });
});
