import { describe, expect, it } from "vitest";
import { loadFavorites, saveFavorites, toggleFavorite } from "./favorites";

function createMemoryStorage(initial: Record<string, string> = {}) {
  const values = new Map(Object.entries(initial));
  return {
    getItem(key: string) { return values.get(key) ?? null; },
    setItem(key: string, value: string) { values.set(key, value); },
  };
}

describe("toggleFavorite", () => {
  it("adds a missing favorite immutably", () => {
    const initial = ["one"];
    expect(toggleFavorite(initial, "two")).toEqual(["one", "two"]);
    expect(initial).toEqual(["one"]);
  });
  it("removes an existing favorite", () => {
    expect(toggleFavorite(["one", "two"], "one")).toEqual(["two"]);
  });
});

describe("favorite storage", () => {
  it("loads known IDs, removes duplicates and ignores unknown IDs", () => {
    const storage = createMemoryStorage({
      "sonora:favorites:v1": JSON.stringify(["one", "one", "removed", 7, "two"]),
    });
    expect(loadFavorites(storage, ["one", "two"])).toEqual(["one", "two"]);
  });
  it("handles missing or invalid data safely", () => {
    expect(loadFavorites(createMemoryStorage(), ["one"])).toEqual([]);
    expect(loadFavorites(createMemoryStorage({ "sonora:favorites:v1": "{malformed" }), ["one"])).toEqual([]);
    expect(loadFavorites(createMemoryStorage({ "sonora:favorites:v1": JSON.stringify({ favorite: "one" }) }), ["one"])).toEqual([]);
  });
  it("round-trips a saved favorite list", () => {
    const storage = createMemoryStorage();
    expect(saveFavorites(storage, ["one", "two", "one"])).toBe(true);
    expect(loadFavorites(storage, ["one", "two"])).toEqual(["one", "two"]);
  });
  it("returns false when the storage adapter throws", () => {
    const brokenStorage = {
      getItem() { throw new Error("storage unavailable"); },
      setItem() { throw new Error("storage unavailable"); },
    };
    expect(loadFavorites(brokenStorage, ["one"])).toEqual([]);
    expect(saveFavorites(brokenStorage, ["one"])).toBe(false);
  });
});
