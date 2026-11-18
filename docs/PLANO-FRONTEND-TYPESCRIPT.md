# Plano de construção do frontend TypeScript

## 1. Objetivo

Evoluir a interface web do catálogo musical usando **Next.js + React + TypeScript**, mantendo o catálogo Java existente e as rotas de servidor responsáveis por conversar com os provedores externos. Este documento é um plano de implementação; não significa que todas as telas e funcionalidades abaixo já estejam prontas.

## 2. Estado e decisões atuais

- Aplicação web localizada em `frontend/`.
- Next.js 15, React 19 e TypeScript 5 já estão declarados em `frontend/package.json`.
- O `frontend/tsconfig.json` já usa `strict: true`; manter essa configuração.
- Rotas de servidor existentes: `/api/search` para pesquisa no SoundCloud e `/api/verify` para conferir metadados/letras.
- O token do SoundCloud deve permanecer exclusivamente no servidor, em variável de ambiente `SOUNDCLOUD_ACCESS_TOKEN`.
- O catálogo Java e seus testes continuam fazendo parte do projeto. A interface web não deve alegar reproduzir áudio enquanto essa função não existir.

## 3. Arquitetura proposta

```text
frontend/src/
  app/
    page.tsx
    layout.tsx
    globals.css
    api/
      search/route.ts
      verify/route.ts
      admin/                 # somente se houver necessidade real de administração
  components/
    search/
      SearchForm.tsx
      SearchResults.tsx
      TrackCard.tsx
      SearchStatus.tsx
    verification/
      VerificationPanel.tsx
  lib/
    api-client.ts
    validation.ts
    resilient-fetch.ts
  types/
    music.ts
    api.ts
```

A árvore é uma proposta: antes de criar arquivos, conferir o que já existe e reaproveitar componentes e rotas, evitando duplicações.

### Responsabilidades

- **Componentes React:** apresentação, formulários, estados de carregamento, erros e resultados.
- **Tipos TypeScript:** formatos de faixa, respostas da API, avisos e resultados de verificação.
- **Rotas Next.js no servidor:** validação de entrada, chamadas externas, limites de tempo e tratamento de falhas.
- **Cliente interno da API:** chamadas tipadas para as rotas `/api/search` e `/api/verify`.
- **Serviços externos:** SoundCloud para descoberta; LRCLIB e MusicBrainz para conferência, respeitando disponibilidade, limites e termos dos provedores.

## 4. Tipos iniciais sugeridos

Os campos definitivos devem refletir as respostas reais das rotas e ser validados antes de serem adotados.

```ts
export interface MusicTrack {
  id: string;
  title: string;
  artist: string;
  url: string;
  artworkUrl?: string;
  durationMs?: number;
}

export type ApiWarning = {
  provider: "soundcloud" | "lrclib" | "musicbrainz" | "internal";
  message: string;
};

export type SearchResponse = {
  tracks: MusicTrack[];
  warnings?: ApiWarning[];
};

export type VerificationResponse = {
  matched: boolean;
  title?: string;
  artist?: string;
  durationMs?: number;
  warnings: ApiWarning[];
};
```

Esses tipos são exemplos para orientar a implementação, não uma afirmação de que as APIs já retornam exatamente essa estrutura. Adaptar os mapeamentos aos formatos efetivos e validar dados externos em tempo de execução.

## 5. Funcionalidades e experiência de uso

1. **Pesquisa:** aceitar título, artista ou termos de busca; validar entrada vazia e limitar o tamanho do texto.
2. **Resultados:** mostrar título, artista, duração e capa quando disponíveis; links externos devem ser claramente identificados.
3. **Estados da interface:** diferenciar carregamento, resultados, lista vazia, erro e resposta parcial de um provedor.
4. **Verificação:** apresentar separadamente os resultados do LRCLIB e MusicBrainz, distinguindo correspondência forte de aproximada.
5. **Concorrência:** evitar disparar chamadas duplicadas, definir limites de tempo e concorrência no servidor e respeitar limites de cada serviço.
6. **Acessibilidade:** rótulos associados aos campos, navegação por teclado, foco visível e mensagens de erro compreensíveis.
7. **Responsividade:** funcionar em telas de celular e desktop.
8. **Privacidade:** não registrar tokens, cabeçalhos de autorização ou dados secretos em logs.

## 6. Resiliência e limites das APIs

- Reutilizar a biblioteca `frontend/src/lib/resilient-fetch.ts` se ela estiver presente e passar nos testes.
- Testar deduplicação, cache, limite de concorrência, timeout, respostas inválidas e falhas dos provedores.
- Garantir que uma falha no LRCLIB não impeça a apresentação de um resultado válido do MusicBrainz, e vice-versa.
- Não tratar cache em memória de um processo como limite global em ambientes com múltiplas instâncias.
- Não afirmar que a aplicação suporta qualquer volume de tráfego sem testes de carga e limites de infraestrutura documentados.

## 7. Segurança

- Manter credenciais somente em variáveis de ambiente do servidor; nunca usar prefixo `NEXT_PUBLIC_` para segredos.
- Validar e limitar todas as entradas das rotas.
- Definir limites de taxa para endpoints públicos conforme a infraestrutura disponível.
- Evitar redirecionamentos abertos, exposição de erros internos e registro de dados sensíveis.
- Usar HTTPS no ambiente publicado e revisar permissões/CORS conforme necessário.
- Não baixar nem republicar automaticamente capas ou letras; mostrar metadados e links de origem respeitando os termos dos provedores.

## 8. Testes e critérios de aceite

### Testes automatizados
- Testes unitários de validação, normalização e classificação das correspondências.
- Testes das rotas para sucesso, entrada inválida, timeout, indisponibilidade e resposta inesperada dos provedores.
- Testes dos componentes para carregamento, vazio, erro e resultados.
- Testes da deduplicação e do limite de concorrência de `resilient-fetch.ts`.
- Executar `npm run lint` e `npm run build` dentro de `frontend/`.
- Executar `mvn clean verify` na raiz para garantir que as alterações web não quebrem o projeto Java.

### Critérios de aceite
- TypeScript continua em modo estrito, sem contornar erros com `any` desnecessário.
- Nenhuma credencial real é adicionada ao Git.
- A pesquisa e a verificação exibem estados de erro e resultados parciais com clareza.
- Os testes cobrem os principais caminhos de sucesso e falha.
- Os comandos de verificação foram executados e seus resultados foram registrados antes de considerar a tarefa concluída.

## 9. Ordem de implementação

1. Auditar a estrutura atual e documentar os contratos reais das rotas.
2. Criar tipos compartilhados e validação de entrada.
3. Implementar o cliente tipado das rotas.
4. Construir a tela de pesquisa e os componentes de resultado.
5. Integrar a visualização da verificação e dos avisos dos provedores.
6. Adicionar testes unitários e de integração.
7. Revisar acessibilidade, segurança e desempenho.
8. Executar lint, build, testes Java e testes de carga controlados.
9. Atualizar a documentação com o que foi realmente entregue.

## 10. Fora do escopo inicial

- Reprodução de áudio.
- Download de faixas, letras ou imagens de terceiros.
- Promessas de disponibilidade ou escalabilidade sem medições.
- Painel administrativo até que exista uma necessidade concreta e requisitos de acesso definidos.
