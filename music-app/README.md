# Sonora — aplicativo musical demonstrativo

Sonora é uma aplicação web responsiva criada com React, TypeScript e Vite. A referência Spotify-clone-app-react ajudou a mapear funcionalidades, mas este projeto usa código, nome, marca, arte e catálogo fictício próprios.

## Funcionalidades implementadas

- Página inicial com seleções demonstrativas.
- Busca por título, artista, álbum ou gênero, ignorando maiúsculas e acentos. O atalho Ctrl/⌘ + K abre a busca.
- Filtros por gênero.
- Biblioteca de faixas favoritas, persistida no armazenamento local do navegador.
- Player com tocar/pausar, faixa anterior/próxima, progresso e volume.
- Áudio demonstrativo sintetizado localmente com Web Audio API; não é streaming de gravações comerciais.
- Interface responsiva e estados vazios.
- Testes automatizados para busca, navegação pelo catálogo e persistência local.

## Pré-requisitos para desenvolvimento

- Node.js 22.12 ou superior.
- npm.

## Executar localmente

Na pasta music-app, execute:

    npm install
    npm run dev

## Verificações

    npm run typecheck
    npm test
    npm run build

A CI executa as três verificações em alterações à aplicação.

## Instalador desktop

Um instalador não está publicado ainda. O plano proposto prioriza EXE para Windows, construído com Tauri 2 e distribuído por GitHub Releases. A decisão técnica aguarda aprovação: [ADR-0002](../docs/architecture/decisions/ADR-0002-windows-installer.md). Veja o [plano de instalação](../docs/deployment/INSTALLER_PLAN.md).

Até a primeira release, esta pasta é a versão web para desenvolvedores; a execução exige Node.js/npm e não é o fluxo esperado para usuários finais.

## Limitações da versão 0.1.0

- Não existem contas, login, backend ou sincronização entre dispositivos.
- Favoritos ficam apenas neste navegador.
- Catálogo e artistas são fictícios.
- O player gera pequenos padrões sonoros originais com os osciladores do navegador. Não fornece músicas completas nem serviço de streaming.
- Não há integração com API do Spotify. Qualquer integração futura exige caso de uso independente e revisão de políticas e permissões.

## Estrutura

- src/App.tsx: interface e coordenação das interações.
- src/domain/catalog.ts: catálogo, busca, formatação e navegação.
- src/domain/favorites.ts: persistência local defensiva.
- src/lib/ToneEngine.ts: sintetizador local.
- src/domain/*.test.ts: testes das regras de domínio.

## Privacidade e uso de mídia

Nenhuma chave externa é necessária. Não adicione tokens, arquivos de áudio de terceiros ou imagens sem verificar licenças e redistribuição. Política do Spotify: https://developer.spotify.com/policy
