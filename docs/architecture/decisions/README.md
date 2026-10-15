# Decisões de Arquitetura

Este diretório reúne decisões técnicas relevantes do projeto por meio de Architecture Decision Records (ADRs).

## Estado atual

**Stack do frontend:** React + TypeScript + Vite — aprovada em 2026-10-14  
**Stack do backend:** não definida; não necessária para o MVP frontend proposto  
**Banco de dados/persistência remota:** não definido; não necessário para a primeira demonstração  
**Contratos de integração:** pendentes de requisitos aprovados  
**Implementação principal:** MVP iniciado na branch feature/music-app-mvp-planning

## ADRs

- [ADR-0001 — Stack inicial do aplicativo de música](ADR-0001-music-app-stack.md) — aprovada por Lucas; SPA responsiva com estado local e áudio demonstrativo sintetizado.
- [ADR-0002 — Distribuição de uma aplicação instalável para Windows](ADR-0002-windows-installer.md) — proposta Tauri 2 + NSIS EXE; aguarda aprovação.

## Regra de aprovação

Uma proposta não é decisão aprovada até que seu status e a aprovação responsável estejam registrados. A stack do MVP web está aprovada. O shell desktop, o backend, o banco remoto e integrações externas exigem requisitos e decisões próprios antes da implementação.
