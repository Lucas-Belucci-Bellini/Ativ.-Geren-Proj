import type { Track } from "../domain/types";

/**
 * Produces original synthesized demo tones locally in the browser.
 * It does not stream, download, or redistribute commercial recordings.
 */
export class ToneEngine {
  private context: AudioContext | null = null;
  private masterGain: GainNode | null = null;
  private timerId: number | null = null;
  private currentTrackId: string | null = null;
  private step = 0;
  private volume = 0.62;

  start(track: Track, volume = this.volume): void {
    if (typeof window === "undefined" || typeof window.AudioContext === "undefined") {
      throw new Error("Este navegador não oferece suporte ao áudio demonstrativo.");
    }
    if (!this.context) {
      this.context = new window.AudioContext();
      this.masterGain = this.context.createGain();
      this.masterGain.gain.value = this.volume * 0.15;
      this.masterGain.connect(this.context.destination);
    }
    if (this.currentTrackId !== track.id) {
      this.currentTrackId = track.id;
      this.step = 0;
    }
    this.setVolume(volume);
    this.stopTimer();
    void this.context.resume();
    this.playStep(track);
    this.timerId = window.setInterval(() => this.playStep(track), 430);
  }

  pause(): void {
    this.stopTimer();
    if (this.context?.state === "running") void this.context.suspend();
  }

  setVolume(volume: number): void {
    this.volume = Math.min(1, Math.max(0, volume));
    if (this.context && this.masterGain) {
      this.masterGain.gain.setTargetAtTime(this.volume * 0.15, this.context.currentTime, 0.04);
    }
  }

  dispose(): void {
    this.stopTimer();
    if (this.context) void this.context.close();
    this.context = null;
    this.masterGain = null;
    this.currentTrackId = null;
  }

  private stopTimer(): void {
    if (this.timerId !== null) {
      window.clearInterval(this.timerId);
      this.timerId = null;
    }
  }

  private playStep(track: Track): void {
    const context = this.context;
    if (!context || !this.masterGain) return;
    const pattern = [0, 3, 7, 10, 7, 3, 5, 12];
    const rootNotes = [174.61, 196, 220, 246.94, 261.63, 293.66, 329.63, 349.23];
    const hash = [...track.id].reduce((total, char) => total + char.charCodeAt(0), 0);
    const root = rootNotes[hash % rootNotes.length];
    const note = root * Math.pow(2, pattern[this.step % pattern.length] / 12);
    const now = context.currentTime;
    this.playPluck(note, now, 0.42, "triangle");
    if (this.step % 2 === 0) this.playPluck(root / 2, now, 0.22, "sine");
    if (this.step % 4 === 2) this.playPluck(root * 1.5, now, 0.13, "sine");
    this.step += 1;
  }

  private playPluck(frequency: number, startAt: number, level: number, type: OscillatorType): void {
    if (!this.context || !this.masterGain) return;
    const oscillator = this.context.createOscillator();
    const envelope = this.context.createGain();
    oscillator.type = type;
    oscillator.frequency.setValueAtTime(frequency, startAt);
    envelope.gain.setValueAtTime(0.0001, startAt);
    envelope.gain.linearRampToValueAtTime(level * 0.2, startAt + 0.025);
    envelope.gain.exponentialRampToValueAtTime(0.0001, startAt + 0.37);
    oscillator.connect(envelope);
    envelope.connect(this.masterGain);
    oscillator.start(startAt);
    oscillator.stop(startAt + 0.39);
  }
}
