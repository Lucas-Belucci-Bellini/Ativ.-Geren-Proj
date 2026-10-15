export type Genre = "Indie" | "Eletrônica" | "MPB" | "Lo-fi" | "Instrumental";

export type CoverStyle =
  | "aurora"
  | "citrus"
  | "violet"
  | "ocean"
  | "rose"
  | "forest"
  | "ember"
  | "midnight";

export interface Track {
  id: string;
  title: string;
  artist: string;
  album: string;
  duration: number;
  genre: Genre;
  mood: string;
  cover: CoverStyle;
  colorName: string;
}
