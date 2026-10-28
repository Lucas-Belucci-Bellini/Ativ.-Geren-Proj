import { NextRequest, NextResponse } from "next/server";
import { resilientFetch } from "@/lib/resilient-fetch";

export const runtime = "nodejs";

export async function GET(request: NextRequest) {
  const query = request.nextUrl.searchParams.get("q")?.trim();
  if (!query) return NextResponse.json({ error: "Digite o nome de uma música ou artista." }, { status: 400 });
  if (query.length > 120) return NextResponse.json({ error: "A pesquisa deve ter no máximo 120 caracteres." }, { status: 400 });
  const token = process.env.SOUNDCLOUD_ACCESS_TOKEN;
  if (!token) return NextResponse.json({ error: "A busca está pronta, mas falta configurar SOUNDCLOUD_ACCESS_TOKEN no ambiente do servidor." }, { status: 503 });

  try {
    const url = new URL("https://api.soundcloud.com/tracks");
    url.searchParams.set("q", query);
    url.searchParams.set("limit", "20");
    url.searchParams.set("linked_partitioning", "true");
    const response = await resilientFetch(url, { headers: { Authorization: `OAuth ${token}`, Accept: "application/json" } }, { timeoutMs: 12_000, cacheTtlMs: 15_000 });
    if (!response.ok) {
      const status = response.status === 429 ? 429 : response.status === 401 || response.status === 403 ? 503 : 502;
      return NextResponse.json({ error: response.status === 429 ? "Limite de buscas atingido. Aguarde um pouco." : "O SoundCloud não conseguiu atender à busca. Verifique o token e tente novamente." }, { status });
    }
    const data = await response.json();
    const collection = Array.isArray(data) ? data : Array.isArray(data.collection) ? data.collection : [];
    const tracks = collection.map((item: Record<string, unknown>) => {
      const user = item.user && typeof item.user === "object" ? item.user as Record<string, unknown> : {};
      return {
        id: String(item.id ?? ""),
        title: String(item.title ?? "Faixa sem título"),
        artist: String(user.username ?? "Artista desconhecido"),
        artworkUrl: typeof item.artwork_url === "string" ? item.artwork_url : null,
        permalinkUrl: typeof item.permalink_url === "string" ? item.permalink_url : null,
        durationMillis: Number(item.duration ?? 0),
      };
    }).filter((track: { id: string; permalinkUrl: string | null }) => track.id && track.permalinkUrl);
    return NextResponse.json({ tracks });
  } catch {
    return NextResponse.json({ error: "Não foi possível conectar ao SoundCloud agora." }, { status: 502 });
  }
}
