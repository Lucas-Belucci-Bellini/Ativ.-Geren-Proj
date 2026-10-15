# Sonora — aplicativo musical demonstrativo

Sonora é uma aplicação web responsiva criada com React, TypeScript e Vite. A referência Spotify-clone-app-react ajudou a mapear funcionalidades, mas este projeto usa código, nome, marca, arte e catálogo fictício próprios.

## Funcionalidades implementadas

- Página inicial com seleções demonstrativas.
- Busca por título, artista, álbum ou gênero, ignorando diferenças de maiúsculas e acentuação.
- Filtros por gênero.
- Biblioteca de faixas favoritas, persistida no armazenamento local do navegador.
- Player com tocar/pausar, faixa anterior/próxima, controle de progresso e volume.
- Áudio demonstrativo sintetizado localmente com Web Audio API; não é streaming de gravações comerciais.
- Interface responsiva e estados vazios para busca e biblioteca.
- Testes automatizados para busca, navegação pelo catálogo e persistência local de favoritos.

## Pré-requisitos

- Node.js 22.12 ou superior.
- npm.

## Executar localmente

Na pasta music-app, execute:

    npm install
    npm run dev

O Vite mostrará a URL local no terminal.

## Verificações

    npm run typecheck
    npm test
    npm run build

A integração contínua executa as três verificações em pushes e pull requests que alterem a aplicação ou seu workflow.

## Limitações da versão 0.1.0

- Não existem contas, login, backend ou sincronização entre dispositivos.
- Favoritos são armazenados apenas neste navegador e podem ser perdidos se os dados locais forem apagados.
- O catálogo e os artistas são fictícios.
- O player gera pequenos padrões sonoros originais com os osciladores do navegador. Ele valida controles e estado de reprodução, mas não fornece músicas completas nem um serviço de streaming.
- Não há integração com a API do Spotify. Qualquer integração futura precisa de um caso de uso independente e de uma revisão das políticas e permissões atuais do provedor.

## Estrutura

- src/App.tsx: interface e coordenação das interações do MVP.
- src/domain/catalog.ts: catálogo demonstrativo, busca, formatação e navegação entre faixas.
- src/domain/favorites.ts: persistência local defensiva de favoritos.
- src/lib/ToneEngine.ts: sintetizador local do áudio demonstrativo.
- src/domain/*.test.ts: testes automatizados das regras de domínio.

## Privacidade e uso de mídia

Nenhuma chave externa é necessária. Não adicione tokens, arquivos de áudio de terceiros ou imagens sem verificar a licença e as condições de redistribuição. A política de desenvolvedores do Spotify proíbe produtos que imitem ou tentem substituir sua experiência principal sem permissão escrita: https://developer.spotify.com/policy
