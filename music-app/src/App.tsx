import { useEffect, useMemo, useRef, useState } from "react";
import type { CSSProperties } from "react";
import Icon, { type IconName } from "./components/Icon";
import { findNextTrack, formatTime, GENRES, searchTracks, TRACKS } from "./domain/catalog";
import { loadFavorites, saveFavorites, toggleFavorite } from "./domain/favorites";
import type { CoverStyle, Genre, Track } from "./domain/types";
import { ToneEngine } from "./lib/ToneEngine";
import "./App.css";

type View = "home" | "search" | "library";

const PLAYLISTS: Array<{ title: string; description: string; cover: CoverStyle; trackId: string }> = [
  { title: "Acorda devagar", description: "Indie para começar sem pressa", cover: "aurora", trackId: "aurora-dourada" },
  { title: "Noite neon", description: "Batidas para depois do pôr do sol", cover: "violet", trackId: "cidade-de-vidro" },
  { title: "Sol na janela", description: "Um respiro no meio da rotina", cover: "citrus", trackId: "cafe-com-sol" },
  { title: "Modo foco", description: "Som ambiente para entrar no fluxo", cover: "midnight", trackId: "notas-no-escuro" },
];

function readStoredFavorites(): string[] {
  if (typeof window === "undefined") return [];
  try {
    return loadFavorites(window.localStorage, TRACKS.map((track) => track.id));
  } catch {
    return [];
  }
}

function Cover({ track, className = "" }: { track: Track; className?: string }) {
  return (
    <div className={"cover art-" + track.cover + (className ? " " + className : "")} role="img" aria-label={"Arte ilustrativa de " + track.album}>
      <div className="cover-orbit" /><div className="cover-sun" /><div className="cover-lines" />
      <span className="cover-label">{track.colorName}</span>
      <Icon name="wave" size={22} className="cover-wave" />
    </div>
  );
}

function Brand() {
  return (
    <div className="brand" aria-label="Sonora">
      <span className="brand-mark" aria-hidden="true"><i /><i /><i /><i /></span>
      <span>sonora<span className="brand-period">.</span></span>
    </div>
  );
}

function NavButton({ icon, label, active, onClick }: { icon: IconName; label: string; active: boolean; onClick: () => void }) {
  return (
    <button type="button" className={"nav-item" + (active ? " nav-item--active" : "")} aria-current={active ? "page" : undefined} onClick={onClick}>
      <Icon name={icon} size={20} /><span>{label}</span>
    </button>
  );
}

function FavoriteButton({ active, onClick, label }: { active: boolean; onClick: () => void; label: string }) {
  return (
    <button
      type="button"
      className={"icon-button favorite-button" + (active ? " favorite-button--active" : "")}
      onClick={onClick}
      aria-label={active ? "Remover " + label + " dos favoritos" : "Adicionar " + label + " aos favoritos"}
      title={active ? "Remover dos favoritos" : "Adicionar aos favoritos"}
    >
      <Icon name="heart" size={18} />
    </button>
  );
}

function TrackRow({ track, index, isCurrent, isPlaying, isFavorite, onPlay, onFavorite }: {
  track: Track; index: number; isCurrent: boolean; isPlaying: boolean; isFavorite: boolean; onPlay: () => void; onFavorite: () => void;
}) {
  return (
    <div className={"track-row" + (isCurrent ? " track-row--current" : "")}>
      <button type="button" className="track-number" aria-label={"Tocar " + track.title} onClick={onPlay}>
        {isCurrent && isPlaying ? <Icon name="wave" size={17} /> : <><span className="track-index">{String(index + 1).padStart(2, "0")}</span><Icon name="play" size={15} /></>}
      </button>
      <button type="button" className="track-main" onClick={onPlay}>
        <Cover track={track} className="cover--track" />
        <span className="track-copy"><strong className={isCurrent ? "text-lime" : ""}>{track.title}</strong><span>{track.artist}</span></span>
      </button>
      <span className="track-album">{track.album}</span>
      <span className="track-genre">{track.genre}</span>
      <span className="track-duration">{formatTime(track.duration)}</span>
      <FavoriteButton active={isFavorite} onClick={onFavorite} label={track.title} />
    </div>
  );
}

function EmptyState({ title, description, action }: { title: string; description: string; action?: () => void }) {
  return (
    <div className="empty-state">
      <span className="empty-icon"><Icon name="music" size={26} /></span>
      <h3>{title}</h3><p>{description}</p>
      {action && <button className="text-button" type="button" onClick={action}>Explorar catálogo <Icon name="arrow" size={16} /></button>}
    </div>
  );
}

export default function App() {
  const [activeView, setActiveView] = useState<View>("home");
  const [query, setQuery] = useState("");
  const [selectedGenre, setSelectedGenre] = useState<"Todos" | Genre>("Todos");
  const [favorites, setFavorites] = useState<string[]>(readStoredFavorites);
  const [, setRecentIds] = useState<string[]>([]);
  const [selectedTrackId, setSelectedTrackId] = useState(TRACKS[0].id);
  const [isPlaying, setIsPlaying] = useState(false);
  const [progress, setProgress] = useState(0);
  const [volume, setVolume] = useState(0.68);
  const engineRef = useRef<ToneEngine | null>(null);
  const searchInputRef = useRef<HTMLInputElement>(null);

  const selectedTrack = TRACKS.find((track) => track.id === selectedTrackId) ?? TRACKS[0];
  const favoriteTracks = useMemo(() => TRACKS.filter((track) => favorites.includes(track.id)), [favorites]);
  const visibleTracks = useMemo(() => searchTracks(TRACKS, query, selectedGenre), [query, selectedGenre]);

  useEffect(() => {
    try { saveFavorites(window.localStorage, favorites); } catch { /* Storage may be disabled by the browser. */ }
  }, [favorites]);

  useEffect(() => {
    if (!isPlaying) return undefined;
    const timerId = window.setInterval(() => setProgress((current) => current + 1), 1000);
    return () => window.clearInterval(timerId);
  }, [isPlaying]);

  useEffect(() => {
    if (!isPlaying || progress < selectedTrack.duration) return;
    const nextTrack = findNextTrack(TRACKS, selectedTrack.id);
    setSelectedTrackId(nextTrack.id);
    setProgress(0);
    engineRef.current?.start(nextTrack, volume);
    setRecentIds((current) => [nextTrack.id, ...current.filter((id) => id !== nextTrack.id)].slice(0, 5));
  }, [isPlaying, progress, selectedTrack, volume]);

  useEffect(() => () => { engineRef.current?.dispose(); }, []);

  function startTrack(track: Track, restart = false) {
    if (!restart && selectedTrackId === track.id && isPlaying) {
      engineRef.current?.pause();
      setIsPlaying(false);
      return;
    }
    try {
      if (!engineRef.current) engineRef.current = new ToneEngine();
      engineRef.current.start(track, volume);
      setSelectedTrackId(track.id);
      if (selectedTrackId !== track.id || restart) setProgress(0);
      setIsPlaying(true);
      setRecentIds((current) => [track.id, ...current.filter((id) => id !== track.id)].slice(0, 5));
    } catch (error) {
      const message = error instanceof Error ? error.message : "Não foi possível iniciar o áudio demonstrativo.";
      window.alert(message);
    }
  }

  function togglePlayPause() {
    if (isPlaying) {
      engineRef.current?.pause();
      setIsPlaying(false);
      return;
    }
    startTrack(selectedTrack);
  }

  function skipTrack(direction: 1 | -1) {
    startTrack(findNextTrack(TRACKS, selectedTrack.id, direction), true);
  }

  function changeView(view: View) {
    setActiveView(view);
    if (view !== "search") setSelectedGenre("Todos");
  }

  function onSearchChange(value: string) {
    setQuery(value);
    if (value.trim()) setActiveView("search");
  }

  function toggleFavoriteForTrack(trackId: string) {
    setFavorites((current) => toggleFavorite(current, trackId));
  }

  const listToRender = activeView === "library" ? favoriteTracks : activeView === "search" ? visibleTracks : TRACKS.slice(0, 6);
  const mainHeading = activeView === "home" ? "Feito para a sua vibe" : activeView === "search" ? (query.trim() ? "Resultados para você" : "Explore o catálogo") : "Sua biblioteca";

  return (
    <div className="app-shell">
      <aside className="sidebar" aria-label="Navegação principal">
        <Brand />
        <div className="sidebar-label">ESPAÇO</div>
        <nav className="nav-group">
          <NavButton icon="home" label="Início" active={activeView === "home"} onClick={() => changeView("home")} />
          <NavButton icon="search" label="Descobrir" active={activeView === "search"} onClick={() => changeView("search")} />
          <NavButton icon="library" label="Sua biblioteca" active={activeView === "library"} onClick={() => changeView("library")} />
        </nav>

        <div className="sidebar-divider" />
        <div className="sidebar-section-heading"><span className="sidebar-label">SUA COLEÇÃO</span><button type="button" className="small-icon-button" aria-label="Ver todos os favoritos" onClick={() => changeView("library")}><Icon name="plus" size={16} /></button></div>
        <button type="button" className="side-collection" onClick={() => changeView("library")}>
          <span className="liked-tile"><Icon name="heart" size={19} /></span>
          <span><strong>Faixas favoritas</strong><small>{favorites.length} faixas salvas</small></span>
        </button>
        <button type="button" className="side-collection" onClick={() => { setQuery(""); setActiveView("home"); }}>
          <span className="mini-cover art-forest"><Icon name="headphones" size={18} /></span>
          <span><strong>Descobertas</strong><small>Seleção Sonora</small></span>
        </button>

        <div className="sidebar-bottom">
          <div className="demo-status"><span className="status-dot" /> MODO DEMONSTRAÇÃO</div>
          <p>Um espaço musical experimental, feito para explorar novas ideias.</p>
          <span className="sidebar-version">SONORA WEB · 0.1.0</span>
        </div>
      </aside>

      <div className="main-column">
        <header className="topbar">
          <div className="history-buttons">
            <button type="button" className="round-button" aria-label="Ir para o início" onClick={() => changeView("home")}><span className="back-chevron">‹</span></button>
            <button type="button" className="round-button" aria-label="Abrir descoberta" onClick={() => changeView("search")}><span className="forward-chevron">›</span></button>
          </div>
          <label className="search-box">
            <Icon name="search" size={19} />
            <input ref={searchInputRef} type="search" placeholder="O que você quer ouvir?" aria-label="Buscar música, artista ou álbum" value={query} onFocus={() => { if (query.trim()) setActiveView("search"); }} onChange={(event) => onSearchChange(event.target.value)} />
            {query && <button type="button" className="search-clear" aria-label="Limpar busca" onClick={() => setQuery("")}><Icon name="close" size={16} /></button>}
            <kbd>Ctrl / ⌘ K</kbd>
          </label>
          <button type="button" className="profile-chip" onClick={() => setActiveView("library")} aria-label="Abrir biblioteca"><span className="profile-avatar">S</span><span>Visitante</span></button>
        </header>

        <main className="main-content">
          {activeView === "home" && <>
            <section className="welcome-row">
              <div><div className="eyebrow"><span className="eyebrow-line" /> SEU UNIVERSO, SUA TRILHA</div><h1>Oi, que som<br /><span>combina com hoje?</span></h1><p>Um clique e o clima muda.</p></div>
              <div className="welcome-badge"><span className="badge-orbit"><Icon name="wave" size={24} /></span><span>ESCUTE<br />DIFERENTE</span></div>
            </section>
            <section className="hero-card" aria-labelledby="hero-title">
              <div className="hero-visual" aria-hidden="true"><div className="hero-disc hero-disc--one" /><div className="hero-disc hero-disc--two" /><div className="hero-sun" /><div className="hero-star hero-star--one">✳</div><div className="hero-star hero-star--two">✳</div></div>
              <div className="hero-content">
                <span className="hero-kicker"><Icon name="sparkles" size={15} /> MIX DA SEMANA</span>
                <h2 id="hero-title">Dê play no<br />que ainda não conhece.</h2>
                <p>Oito faixas originais de demonstração para abrir novos caminhos sonoros.</p>
                <div className="hero-actions"><button type="button" className="primary-button" onClick={() => startTrack(TRACKS[0], true)}><Icon name="play" size={18} /> Tocar seleção</button><button type="button" className="hero-secondary" onClick={() => changeView("search")}>Explorar <Icon name="arrow" size={16} /></button></div>
              </div>
              <span className="hero-index">01 <span>/</span> 04</span>
            </section>
            <section className="section-block">
              <div className="section-heading"><div><span className="section-kicker">MONTAMOS PRA VOCÊ</span><h2>Atalhos para o seu dia</h2></div><button type="button" className="text-button" onClick={() => changeView("search")}>Ver catálogo <Icon name="arrow" size={16} /></button></div>
              <div className="playlist-grid">
                {PLAYLISTS.map((playlist, index) => {
                  const track = TRACKS.find((item) => item.id === playlist.trackId) ?? TRACKS[index];
                  return <button type="button" key={playlist.title} className="playlist-card" onClick={() => startTrack(track, true)}>
                    <Cover track={track} className="cover--playlist" />
                    <span className="playlist-card-copy"><strong>{playlist.title}</strong><small>{playlist.description}</small></span>
                    <span className="card-play"><Icon name="play" size={18} /></span>
                  </button>;
                })}
              </div>
            </section>
          </>}

          <section className="section-block track-section">
            <div className="section-heading">
              <div><span className="section-kicker">{activeView === "library" ? "GUARDADO POR VOCÊ" : activeView === "search" ? "ENCONTRE SEU PRÓXIMO SOM" : "UM POUCO DE TUDO"}</span><h2>{mainHeading}</h2></div>
              {activeView === "home" && <span className="count-label">CATÁLOGO DEMO <span>·</span> {TRACKS.length} FAIXAS</span>}
            </div>
            {activeView === "search" && <div className="genre-filters" role="group" aria-label="Filtrar por gênero">
              {GENRES.map((genre) => <button type="button" className={"genre-chip" + (selectedGenre === genre ? " genre-chip--active" : "")} key={genre} aria-pressed={selectedGenre === genre} onClick={() => setSelectedGenre(genre)}>{genre}</button>)}
            </div>}
            {listToRender.length > 0 ? <div className="track-table" role="list" aria-label={activeView === "library" ? "Faixas favoritas" : "Lista de faixas"}>
              <div className="track-table-head" aria-hidden="true"><span>#</span><span>FAIXA</span><span>ÁLBUM</span><span>GÊNERO</span><span><Icon name="clock" size={15} /></span><span /></div>
              {listToRender.map((track, index) => <div role="listitem" key={track.id}>
                <TrackRow track={track} index={index} isCurrent={selectedTrack.id === track.id} isPlaying={isPlaying} isFavorite={favorites.includes(track.id)} onPlay={() => startTrack(track)} onFavorite={() => toggleFavoriteForTrack(track.id)} />
              </div>)}
            </div> : <EmptyState
              title={activeView === "library" ? "Sua coleção começa aqui" : "Não encontramos esse som"}
              description={activeView === "library" ? "Guarde as faixas que combinam com você usando o coração ao lado de cada música." : "Tente outro título, artista ou gênero. Às vezes o próximo som está a uma busca de distância."}
              action={activeView === "library" ? () => changeView("search") : () => { setQuery(""); setSelectedGenre("Todos"); }}
            />}
          </section>

          {activeView === "home" && <section className="section-block discovery-note">
            <div className="discovery-icon"><Icon name="headphones" size={24} /></div>
            <div><span className="section-kicker">UMA NOTA SOBRE ESTA VERSÃO</span><h2>É só o começo.</h2><p>As faixas usam sons sintetizados no seu navegador. Não há streaming comercial, login ou conta conectada nesta demonstração.</p></div>
            <button type="button" className="text-button" onClick={() => changeView("search")}>Descobrir faixas <Icon name="arrow" size={16} /></button>
          </section>}
        </main>
        <footer className="app-footer"><span>SONORA <span className="footer-dot">●</span> FEITO PARA EXPLORAR</span><span>Conteúdo musical fictício · Versão demonstrativa</span></footer>
      </div>

      <nav className="mobile-nav" aria-label="Navegação móvel">
        <NavButton icon="home" label="Início" active={activeView === "home"} onClick={() => changeView("home")} />
        <NavButton icon="search" label="Descobrir" active={activeView === "search"} onClick={() => changeView("search")} />
        <NavButton icon="library" label="Biblioteca" active={activeView === "library"} onClick={() => changeView("library")} />
      </nav>

      <footer className="player-bar" aria-label="Player de música">
        <div className="now-playing"><Cover track={selectedTrack} className="cover--player" /><div className="now-playing-copy"><strong>{selectedTrack.title}</strong><span>{selectedTrack.artist}</span></div><FavoriteButton active={favorites.includes(selectedTrack.id)} onClick={() => toggleFavoriteForTrack(selectedTrack.id)} label={selectedTrack.title} /></div>
        <div className="player-center">
          <div className="player-controls">
            <button type="button" className="player-quiet-control" aria-label="Reprodução aleatória — em breve" title="Reprodução aleatória — em breve" onClick={() => window.alert("A reprodução aleatória será adicionada em uma próxima etapa.")}><Icon name="shuffle" size={17} /></button>
            <button type="button" className="player-quiet-control" aria-label="Faixa anterior" onClick={() => skipTrack(-1)}><Icon name="previous" size={19} /></button>
            <button type="button" className="player-play" aria-label={isPlaying ? "Pausar" : "Tocar"} onClick={togglePlayPause}><Icon name={isPlaying ? "pause" : "play"} size={19} /></button>
            <button type="button" className="player-quiet-control" aria-label="Próxima faixa" onClick={() => skipTrack(1)}><Icon name="next" size={19} /></button>
            <button type="button" className="player-quiet-control" aria-label="Repetição — em breve" title="Repetição — em breve" onClick={() => window.alert("A repetição será adicionada em uma próxima etapa.")}><Icon name="repeat" size={17} /></button>
          </div>
          <div className="progress-row"><span>{formatTime(Math.min(progress, selectedTrack.duration))}</span>
            <input aria-label="Progresso da faixa" className="range-input progress-input" type="range" min="0" max={selectedTrack.duration} value={Math.min(progress, selectedTrack.duration)} onChange={(event) => setProgress(Number(event.target.value))} style={{ "--range-progress": (Math.min(progress, selectedTrack.duration) / selectedTrack.duration) * 100 + "%" } as CSSProperties} />
            <span>{formatTime(selectedTrack.duration)}</span>
          </div>
        </div>
        <div className="player-extra"><span className="synth-indicator"><span className={isPlaying ? "synth-dot synth-dot--active" : "synth-dot"} /> SOM DEMO</span><Icon name="volume" size={18} />
          <input aria-label="Volume do player" className="range-input volume-input" type="range" min="0" max="1" step="0.01" value={volume} onChange={(event) => { const nextVolume = Number(event.target.value); setVolume(nextVolume); engineRef.current?.setVolume(nextVolume); }} style={{ "--range-progress": volume * 100 + "%" } as CSSProperties} />
        </div>
      </footer>
    </div>
  );
}
