import type { CoverStyle, Genre, Track } from "./types";

export const TRACKS: Track[] = [
  { id: "aurora-dourada", title: "Aurora Dourada", artist: "Maré Baixa", album: "Entre Estações", duration: 214, genre: "Indie", mood: "Leve", cover: "aurora", colorName: "Aurora" },
  { id: "cidade-de-vidro", title: "Cidade de Vidro", artist: "Nuvem Azul", album: "Luzes Acesas", duration: 188, genre: "Eletrônica", mood: "Noturna", cover: "violet", colorName: "Violeta" },
  { id: "cafe-com-sol", title: "Café com Sol", artist: "Lia Campos", album: "Pequenas Manhãs", duration: 201, genre: "MPB", mood: "Solar", cover: "citrus", colorName: "Cítrico" },
  { id: "orbita-lenta", title: "Órbita Lenta", artist: "Satélite 8", album: "Sinais Distantes", duration: 232, genre: "Eletrônica", mood: "Espacial", cover: "ocean", colorName: "Oceano" },
  { id: "quase-domingo", title: "Quase Domingo", artist: "Quintal Elétrico", album: "Dias de Dentro", duration: 196, genre: "Indie", mood: "Nostálgica", cover: "rose", colorName: "Rosa" },
  { id: "janela-chuvosa", title: "Janela Chuvosa", artist: "Estúdio Sereno", album: "Fundo de Tela", duration: 245, genre: "Lo-fi", mood: "Calma", cover: "forest", colorName: "Floresta" },
  { id: "linha-do-horizonte", title: "Linha do Horizonte", artist: "Tom Avelar", album: "Mapas Invisíveis", duration: 223, genre: "MPB", mood: "Reflexiva", cover: "ember", colorName: "Âmbar" },
  { id: "notas-no-escuro", title: "Notas no Escuro", artist: "Casa de Ecos", album: "Quarto 404", duration: 207, genre: "Instrumental", mood: "Concentrada", cover: "midnight", colorName: "Meia-noite" },
];

export const GENRES: Array<"Todos" | Genre> = ["Todos", "Indie", "Eletrônica", "MPB", "Lo-fi", "Instrumental"];

function normalizeText(value: string): string {
  return value.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLocaleLowerCase("pt-BR").trim();
}

export function searchTracks(tracks: readonly Track[], query: string, genre: "Todos" | Genre = "Todos"): Track[] {
  const normalizedQuery = normalizeText(query);

  return tracks.filter((track) => {
    const matchesGenre = genre === "Todos" || track.genre === genre;
    const searchableFields = [track.title, track.artist, track.album, track.genre];
    const matchesQuery = normalizedQuery.length === 0 ||
      searchableFields.some((field) => normalizeText(field).includes(normalizedQuery));
    return matchesGenre && matchesQuery;
  });
}

export function formatTime(seconds: number): string {
  const safeSeconds = Number.isFinite(seconds) ? Math.max(0, Math.floor(seconds)) : 0;
  return Math.floor(safeSeconds / 60) + ":" + String(safeSeconds % 60).padStart(2, "0");
}

export function findNextTrack(tracks: readonly Track[], currentTrackId: string, direction: 1 | -1 = 1): Track {
  if (tracks.length === 0) throw new Error("Não é possível avançar sem faixas no catálogo.");
  const currentIndex = tracks.findIndex((track) => track.id === currentTrackId);
  if (currentIndex === -1) return tracks[0];
  return tracks[(currentIndex + direction + tracks.length) % tracks.length];
}

export function coverLabel(cover: CoverStyle): string {
  const labels: Record<CoverStyle, string> = {
    aurora: "Aurora", citrus: "Cítrico", violet: "Violeta", ocean: "Oceano",
    rose: "Rosa", forest: "Floresta", ember: "Âmbar", midnight: "Meia-noite",
  };
  return labels[cover];
}
