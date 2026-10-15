import type { ReactNode } from "react";

export type IconName = "home" | "search" | "library" | "heart" | "play" | "pause" | "previous" | "next" | "volume" | "clock" | "sparkles" | "plus" | "close" | "wave" | "headphones" | "shuffle" | "repeat" | "list" | "arrow" | "music" | "check";

const paths: Record<IconName, ReactNode> = {
  home: <><path d="m3 10 9-7 9 7"/><path d="M5 9v11h14V9"/><path d="M9 20v-6h6v6"/></>,
  search: <><circle cx="10.8" cy="10.8" r="6.8"/><path d="m16 16 5 5"/></>,
  library: <><path d="M4 5v15"/><path d="M9 3v17"/><path d="M14 5v15"/><path d="M19 8v12"/></>,
  heart: <path d="M20.8 8.8c0 5.1-8.8 10.2-8.8 10.2S3.2 13.9 3.2 8.8A4.6 4.6 0 0 1 12 6.2a4.6 4.6 0 0 1 8.8 2.6Z"/>,
  play: <path d="m7 4 13 8-13 8z" fill="currentColor" stroke="none"/>,
  pause: <><path d="M8 5h3v14H8z" fill="currentColor" stroke="none"/><path d="M15 5h3v14h-3z" fill="currentColor" stroke="none"/></>,
  previous: <><path d="M6 5v14"/><path d="m19 5-10 7 10 7z" fill="currentColor" stroke="none"/></>,
  next: <><path d="M18 5v14"/><path d="m5 5 10 7-10 7z" fill="currentColor" stroke="none"/></>,
  volume: <><path d="M4 9v6h4l5 4V5L8 9z"/><path d="M16 9a5 5 0 0 1 0 6"/><path d="M18 6a9 9 0 0 1 0 12"/></>,
  clock: <><circle cx="12" cy="12" r="9"/><path d="M12 7v5l3 2"/></>,
  sparkles: <><path d="m12 3 1.8 5.2L19 10l-5.2 1.8L12 17l-1.8-5.2L5 10l5.2-1.8z"/><path d="m19 15 .9 2.1L22 18l-2.1.9L19 21l-.9-2.1L16 18l2.1-.9z"/></>,
  plus: <><path d="M12 5v14"/><path d="M5 12h14"/></>,
  close: <><path d="m6 6 12 12"/><path d="M18 6 6 18"/></>,
  wave: <><path d="M3 12h2l2-6 4 12 4-12 2 6h4"/><path d="M3 17h2m14 0h2"/></>,
  headphones: <><path d="M3 13v-1a9 9 0 0 1 18 0v1"/><path d="M3 13h4v7H5a2 2 0 0 1-2-2z"/><path d="M21 13h-4v7h2a2 2 0 0 0 2-2z"/></>,
  shuffle: <><path d="m18 14 3 3-3 3"/><path d="M3 7h2c5 0 7 10 12 10h4"/><path d="m18 4 3 3-3 3"/><path d="M3 17h2c1.7 0 3-1.3 4.2-3.1"/><path d="M14 7c1-.1 1.9 0 3 0h4"/></>,
  repeat: <><path d="m17 2 4 4-4 4"/><path d="M3 11V8a2 2 0 0 1 2-2h16"/><path d="m7 22-4-4 4-4"/><path d="M21 13v3a2 2 0 0 1-2 2H3"/></>,
  list: <><path d="M8 6h13M8 12h13M8 18h13"/><path d="M3 6h.01M3 12h.01M3 18h.01"/></>,
  arrow: <><path d="M5 12h14"/><path d="m13 6 6 6-6 6"/></>,
  music: <><path d="M9 18V5l12-2v13"/><circle cx="6" cy="18" r="3"/><circle cx="18" cy="16" r="3"/></>,
  check: <path d="m5 12 4 4L19 6"/>,
};

export default function Icon({ name, size = 18, className }: { name: IconName; size?: number; className?: string }) {
  return <svg aria-hidden="true" className={className} width={size} height={size} viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round" focusable="false">{paths[name]}</svg>;
}
