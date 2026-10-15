export interface StorageLike {
  getItem(key: string): string | null;
  setItem(key: string, value: string): void;
}

const STORAGE_KEY = "sonora:favorites:v1";

/** Reads only valid track IDs. A storage failure falls back to an empty library. */
export function loadFavorites(storage: StorageLike, validTrackIds: readonly string[]): string[] {
  try {
    const serialized = storage.getItem(STORAGE_KEY);
    if (!serialized) return [];
    const parsed: unknown = JSON.parse(serialized);
    if (!Array.isArray(parsed)) return [];
    const validIds = new Set(validTrackIds);
    return [...new Set(parsed.filter(
      (value): value is string => typeof value === "string" && validIds.has(value),
    ))];
  } catch {
    return [];
  }
}

/** Adds or removes a favorite without mutating the original list. */
export function toggleFavorite(currentFavorites: readonly string[], trackId: string): string[] {
  return currentFavorites.includes(trackId)
    ? currentFavorites.filter((id) => id !== trackId)
    : [...currentFavorites, trackId];
}

/** Storage errors are ignored because favorites are a local convenience, not a server backup. */
export function saveFavorites(storage: StorageLike, favoriteIds: readonly string[]): boolean {
  try {
    storage.setItem(STORAGE_KEY, JSON.stringify([...new Set(favoriteIds)]));
    return true;
  } catch {
    return false;
  }
}
