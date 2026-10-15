# Ativ.-Geren-Proj

Repositório acadêmico para Gerenciamento de Projetos e para praticar um fluxo de desenvolvimento com documentação, decisões arquiteturais, pull requests e verificações automatizadas.

## Aplicativo em desenvolvimento: Sonora

A pasta [music-app](music-app/README.md) contém o MVP web responsivo com React + TypeScript + Vite. A aplicação inclui catálogo fictício, busca, gêneros, favoritos locais e player com som sintetizado no navegador. Ela não é um serviço de streaming nem exige conta do Spotify.

### Rodar localmente

Requer Node.js 22.12+ e npm. Na pasta music-app, execute `npm install` e depois `npm run dev`.

### Verificar alterações

Na pasta music-app, execute `npm run typecheck`, `npm test` e `npm run build`.

## Governança do projeto

- Leia [AGENTS.md](AGENTS.md) e [docs/PROJECT_RULES.md](docs/PROJECT_RULES.md) antes de contribuir.
- O [ADR-0001](docs/architecture/decisions/ADR-0001-music-app-stack.md) registra a stack aprovada.
- O [brief do produto](docs/music-app/PRODUCT_BRIEF.md) e o [roadmap do MVP](docs/music-app/MVP_ROADMAP.md) registram escopo e limitações.
- Os workflows executam verificações no GitHub Actions. Consulte os resultados reais antes de declarar uma alteração pronta.

## Referência funcional

[Spotify-clone-app-react](https://github.com/thiagosprestes/Spotify-clone-app-react) foi consultado para identificar funcionalidades comuns de um aplicativo musical. Sua estrutura, código e assets não foram copiados. O produto Sonora usa identidade visual e catálogo demonstrativo próprios.
