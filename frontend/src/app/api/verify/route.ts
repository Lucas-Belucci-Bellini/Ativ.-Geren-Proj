import { NextRequest, NextResponse } from "next/server";

export const runtime = "nodejs";
const normalize = (value: string) => value.normalize("NFD").replace(/[\u0300-\u036f]/g, "").toLocaleLowerCase("pt-BR").replace(/[^a-z0-9]+/g, " ").trim();

export async function GET(request: NextRequest) {
  const title = request.nextUrl.searchParams.get("title")?.trim() ?? "";
  const artist = request.nextUrl.searchParams.get("artist")?.trim() ?? "";
  const duration = Number(request.nextUrl.searchParams.get("duration") ?? 0);
  if (!title || !artist) return NextResponse.json({ error: "Título e artista são necessários para verificar a faixa." }, { status: 400 });
  if (title.length > 200 || artist.length > 160) return NextResponse.json({ error: "Título ou artista excede o limite permitido." }, { status: 400 });
  if (!Number.isFinite(duration) || duration < 0 || duration > 86400) return NextResponse.json({ error: "Duração inválida para verificação." }, { status: 400 });

  const headers = { "User-Agent": "SonoraMusicCatalog/0.1 (academic project; https://github.com/Lucas-Belucci-Bellini/Ativ.-Geren-Proj)", Accept: "application/json" };
  const lyricsUrl = new URL("https://lrclib.net/api/search");
  lyricsUrl.searchParams.set("track_name", title);
  lyricsUrl.searchParams.set("artist_name", artist);
  if (duration > 0) lyricsUrl.searchParams.set("duration", String(duration));
  const musicUrl = new URL("https://musicbrainz.org/ws/2/recording/");
  musicUrl.searchParams.set("query", `recording:"${title.replace(/["\\\\]/g, " ")}" AND artist:"${artist.replace(/["\\\\]/g, " ")}"`);
  musicUrl.searchParams.set("fmt", "json");
  musicUrl.searchParams.set("limit", "5");

  const [lyricsResponse, musicResponse] = await Promise.allSettled([
    fetch(lyricsUrl, { headers, signal: AbortSignal.timeout(10000), cache: "no-store" }),
    fetch(musicUrl, { headers, signal: AbortSignal.timeout(10000), cache: "no-store" }),
  ]);
  let lyric: Record<string, unknown> | null = null;
  let recordings: Array<Record<string, unknown>> = [];
  if (lyricsResponse.status === "fulfilled" && lyricsResponse.value.ok) {
    let rows: unknown = null;
    try { rows = await lyricsResponse.value.json(); } catch { /* A fonte pode retornar uma resposta não JSON. */ }
    if (Array.isArray(rows)) {
      const ranked = rows.map((row: Record<string, unknown>) => {
        const titleMatch = normalize(String(row.trackName ?? "")) === normalize(title);
        const artistMatch = normalize(String(row.artistName ?? "")) === normalize(artist);
        const seconds = Number(row.duration ?? 0);
        const durationMatch = duration <= 0 || seconds <= 0 || Math.abs(seconds - duration) <= 2;
        return { row, titleMatch, artistMatch, durationMatch, score: Number(titleMatch) * 4 + Number(artistMatch) * 4 + Number(durationMatch) * 1 };
      }).sort((a, b) => b.score - a.score);
      lyric = ranked[0] ? { ...ranked[0].row, verified: ranked[0].titleMatch && ranked[0].artistMatch && ranked[0].durationMatch, matchType: ranked[0].titleMatch && ranked[0].artistMatch && ranked[0].durationMatch ? "strong" : "approximate" } : null;
    }
  }
  if (musicResponse.status === "fulfilled" && musicResponse.value.ok) {
    let data: Record<string, unknown> = {};
    try { data = await musicResponse.value.json(); } catch { /* A fonte pode retornar uma resposta não JSON. */ }
    recordings = Array.isArray(data.recordings) ? data.recordings.slice(0, 5).map((item: Record<string, unknown>) => ({ id: item.id, title: item.title, artist: Array.isArray(item["artist-credit"]) ? (item["artist-credit"] as Array<Record<string, unknown>>).map((credit) => String(credit.name ?? (credit.artist as Record<string, unknown> | undefined)?.name ?? "") + String(credit.joinphrase ?? "")).join("") : "Artista desconhecido", length: item.length ?? null })) : [];
  }
  return NextResponse.json({ lyrics: lyric ? { title: lyric.trackName, artist: lyric.artistName, album: lyric.albumName, duration: lyric.duration, verified: lyric.verified, matchType: lyric.matchType, plainLyrics: lyric.plainLyrics ?? null, syncedLyrics: lyric.syncedLyrics ?? null, instrumental: lyric.instrumental ?? false, source: "LRCLIB" } : null, recordings, warnings: [lyricsResponse.status === "rejected" || (lyricsResponse.status === "fulfilled" && !lyricsResponse.value.ok) ? "A consulta de letras não respondeu; os metadados podem continuar disponíveis." : null, musicResponse.status === "rejected" || (musicResponse.status === "fulfilled" && !musicResponse.value.ok) ? "A consulta MusicBrainz não respondeu; tente novamente mais tarde." : null].filter(Boolean) });
}
