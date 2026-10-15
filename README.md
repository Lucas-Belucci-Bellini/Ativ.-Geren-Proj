# Ativ.-Geren-Proj

Repositório acadêmico para Gerenciamento de Projetos e para praticar documentação, decisões arquiteturais, pull requests e verificações automatizadas.

## Aplicativo em desenvolvimento: Sonora

A pasta [music-app](music-app/README.md) contém o MVP web responsivo com React + TypeScript + Vite. A aplicação inclui catálogo fictício, busca, gêneros, favoritos locais e player com som sintetizado no navegador. Ela não é um serviço de streaming nem exige conta Spotify.

### Baixar e instalar

**O instalador ainda não está disponível.** O plano é publicar primeiro um instalador Windows EXE em GitHub Releases para que o usuário final não precise instalar Node.js, Git ou Rust. Veja o [plano do instalador](docs/deployment/INSTALLER_PLAN.md); a decisão técnica Tauri/NSIS aguarda aprovação no [ADR-0002](docs/architecture/decisions/ADR-0002-windows-installer.md).

### Rodar localmente (desenvolvimento)

Requer Node.js 22.12+ e npm. Na pasta music-app, execute npm install e depois npm run dev.

### Verificar alterações

Na pasta music-app, execute npm run typecheck, npm test e npm run build.

## Governança

- Leia AGENTS.md e docs/PROJECT_RULES.md antes de contribuir.
- ADR-0001 registra a stack web aprovada.
- O brief do produto e o roadmap registram escopo e limitações.
- Consulte os resultados reais do GitHub Actions antes de declarar uma alteração pronta.

## Referência funcional

[Spotify-clone-app-react](https://github.com/thiagosprestes/Spotify-clone-app-react) foi consultado para identificar funcionalidades comuns. Sua estrutura, código e assets não foram copiados. O Sonora usa identidade visual e catálogo demonstrativo próprios.
