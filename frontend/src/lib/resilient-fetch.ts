/**
 * Controles locais de resiliência para chamadas a APIs externas.
 *
 * Limita a concorrência por processo, compartilha requisições idênticas em andamento
 * e mantém cache TTL curto para reduzir pressão sobre os provedores. Em serverless,
 * cada instância possui seu próprio limite/cache; um limite global exige infraestrutura
 * compartilhada (por exemplo Redis ou gateway).
 */
type CacheEntry = { expiresAt: number; response: { status: number; statusText: string; headers: [string, string][]; body: string } };

const MAX_CONCURRENT_UPSTREAM = 8;
const DEFAULT_CACHE_TTL_MS = 15_000;
const MAX_CACHE_ENTRIES = 300;
const cache = new Map<string, CacheEntry>();
const inFlight = new Map<string, Promise<Response>>();
let activeRequests = 0;
const waiters: Array<() => void> = [];

async function acquireSlot(): Promise<() => void> {
  if (activeRequests >= MAX_CONCURRENT_UPSTREAM) {
    // O slot é transferido pelo release() que acorda este waiter.
    await new Promise<void>((resolve) => waiters.push(resolve));
  } else {
    activeRequests += 1;
  }
  let released = false;
  return () => {
    if (released) return;
    released = true;
    const next = waiters.shift();
    if (next) next();
    else activeRequests = Math.max(0, activeRequests - 1);
  };
}

function cacheKey(url: string, init: RequestInit): string {
  const headers = new Headers(init.headers);
  // Evita compartilhar cache entre credenciais distintas sem armazenar o token na chave em texto.
  const authorization = headers.get("authorization") ?? "";
  let hash = 0;
  for (let i = 0; i < authorization.length; i++) hash = ((hash << 5) - hash + authorization.charCodeAt(i)) | 0;
  return `${url}|auth:${hash}|accept:${headers.get("accept") ?? ""}`;
}

function remember(key: string, response: Response, body: string, ttlMs: number) {
  if (ttlMs <= 0 || !response.ok) return;
  cache.set(key, {
    expiresAt: Date.now() + ttlMs,
    response: {
      status: response.status,
      statusText: response.statusText,
      headers: Array.from(response.headers.entries()),
      body,
    },
  });
  if (cache.size > MAX_CACHE_ENTRIES) {
    const oldest = cache.keys().next().value;
    if (oldest) cache.delete(oldest);
  }
}

/** Busca externa com concorrência limitada, coalescência, cache breve e timeout. */
export async function resilientFetch(
  input: URL | string,
  init: RequestInit = {},
  options: { cacheTtlMs?: number; timeoutMs?: number } = {},
): Promise<Response> {
  const url = String(input);
  const ttlMs = options.cacheTtlMs ?? DEFAULT_CACHE_TTL_MS;
  const key = cacheKey(url, init);
  const cached = cache.get(key);
  if (cached && cached.expiresAt > Date.now()) {
    return new Response(cached.response.body, {
      status: cached.response.status,
      statusText: cached.response.statusText,
      headers: cached.response.headers,
    });
  }
  if (cached) cache.delete(key);

  const existing = inFlight.get(key);
  if (existing) return existing.then((response) => response.clone());

  const pending = (async () => {
    const release = await acquireSlot();
    const controller = new AbortController();
    const externalSignal = init.signal;
    const abortFromCaller = () => controller.abort(externalSignal?.reason);
    if (externalSignal?.aborted) abortFromCaller();
    else externalSignal?.addEventListener("abort", abortFromCaller, { once: true });
    const timeout = setTimeout(() => controller.abort(new Error("Upstream timeout")), options.timeoutMs ?? 10_000);
    try {
      const response = await fetch(url, { ...init, signal: controller.signal, cache: "no-store" });
      const body = await response.text();
      remember(key, response, body, ttlMs);
      return new Response(body, { status: response.status, statusText: response.statusText, headers: response.headers });
    } finally {
      clearTimeout(timeout);
      externalSignal?.removeEventListener("abort", abortFromCaller);
      release();
    }
  })();
  inFlight.set(key, pending);
  try {
    return (await pending).clone();
  } finally {
    inFlight.delete(key);
  }
}
