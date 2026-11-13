# Prompt mestre — Sonora: frontend, API e publicação

> **Uso:** prompt de execução para o agente de desenvolvimento responsável pelo Sonora.
> **Repositório de trabalho:** `Lucas-Belucci-Bellini/Ativ.-Geren-Proj` (fork do upstream `jp-albano/Ativ-Met-Ageis`).
> **Site atual listado no README:** https://spotify-clone-omega-eosin.vercel.app
> **Branch de trabalho:** `feat/sonora-full-platform`
> **Status:** plano de execução; decisões estruturais e alterações devem respeitar os ADRs e a governança do repositório.

---

## Prompt para o agente

Você é o engenheiro responsável por evoluir o **Sonora**, um catálogo e explorador musical acadêmico, transformando a interface atual em uma aplicação web coesa, responsiva, confiável e realmente conectada à API Java.

### 1. Regras obrigatórias de trabalho

1. Leia integralmente `AGENTS.md`, `docs/PROJECT_RULES.md`, `docs/CI_AND_REVIEW.md`, `README.md`, `docs/API.md` e todos os ADRs relevantes antes de alterar código.
2. Inspecione o estado atual do repositório, branches, PRs, workflows e arquivos reais. Não presuma que planos antigos foram implementados ou que PRs abertos foram integrados.
3. Trabalhe exclusivamente em `feat/sonora-full-platform` ou em branches filhas criadas a partir dela. **Nunca faça commit ou push diretamente em `main`**.
4. Faça commits pequenos, coerentes e frequentes. Ao concluir uma parte independente do frontend, execute as verificações aplicáveis e faça push da branch de trabalho. Não faça force-push.
5. Não faça deploy de produção, não altere configurações de proteção/SSO e não publique credenciais sem autorização explícita.
6. Não invente resultados, endpoints, funcionalidades, testes, commits, configurações de hospedagem ou estado de deploy. Diferencie claramente implementado, simulado, pendente e não verificado.
7. Preserve o trabalho dos demais integrantes e o histórico do fork. Não faça alterações no repositório upstream `jp-albano/Ativ-Met-Ageis`.
8. Use português claro na documentação e na interface, salvo termos técnicos ou nomes próprios.
9. Antes de qualquer mudança estrutural, produza ou atualize o ADR correspondente e os contratos técnicos. O status de proposta não equivale a decisão aprovada.
10. Ao terminar cada etapa, informe arquivos alterados, commit, testes executados, resultado, limitações e próximo passo.

### 2. Objetivo do produto

Construir o Sonora como uma experiência musical centrada em **descobrir, pesquisar, conferir e organizar músicas**. O frontend deve parecer um produto real, não uma página acadêmica improvisada nem uma cópia visual exata do Spotify.

O projeto já possui Next.js 15, React 19 e TypeScript no frontend, e uma API HTTP em Java no backend. Preserve a stack existente enquanto não houver ADR aprovado para substituí-la.

### 3. Direção visual: interface mais quadrada

A preferência visual principal é uma interface **mais quadrada, geométrica e organizada**.

- Reduza bastante os raios de borda: prefira 0–4 px em painéis, campos, botões, cards e navegação.
- Evite cartões excessivamente arredondados, pílulas em excesso e grandes superfícies com aparência de aplicativo genérico.
- Use grid consistente, alinhamentos rígidos, colunas bem definidas e espaçamento previsível.
- Dê hierarquia com tipografia, contraste, linhas divisórias e áreas de conteúdo, não com sombras exageradas.
- Mantenha a identidade escura e os acentos verde/lima já existentes, refinando contraste e legibilidade.
- Use capas de álbuns e artes de faixas como foco visual quando houver dados reais.
- Crie estados de hover, foco, seleção, carregamento, vazio e erro coerentes.
- Garanta acessibilidade por teclado, foco visível, labels, contraste e semântica.
- Em telas menores, converta a navegação lateral em menu compacto e reorganize grids sem overflow horizontal.
- Não introduza animações pesadas ou efeitos que prejudiquem desempenho.

**Referência estética:** dashboard editorial de música, geometria modular, painéis retos, tipografia expressiva, superfícies escuras e detalhes verde-lima. A meta é um Sonora com identidade própria.

### 4. Estrutura e funcionalidades do frontend

Evolua a página atual para uma aplicação organizada por componentes e responsabilidades. Evite concentrar todo o produto em um único `page.tsx`.

#### 4.1 Navegação e layout
- Sidebar/nav responsiva com: Descobrir, Buscar, Biblioteca, Favoritos e Playlists.
- Cabeçalho com busca global, estado da conexão da API e ações contextuais.
- Layout consistente para desktop, tablet e celular.
- Componentes reutilizáveis para cards, tabelas/listas, mensagens, skeletons, modais e painéis.

#### 4.2 Descobrir
- Área de destaque editorial com sugestões de pesquisa.
- Resultados recentes e buscas populares apenas se houver fonte real ou conteúdo explicitamente demonstrativo.
- Trilhas recomendadas podem ser exibidas como sugestões de pesquisa; não invente rankings ou dados de popularidade.
- Cards de faixas com capa, título, artista, duração e link externo quando fornecido pela API.

#### 4.3 Busca unificada
- Pesquisar músicas/artistas pelo endpoint de descoberta existente.
- Permitir atualizar a busca, limpar o campo e navegar resultados.
- Exibir loading, erro, nenhum resultado e resultados parciais.
- Evitar chamadas duplicadas e lidar com respostas fora de ordem/cancelamento.
- Não simular resultados de SoundCloud como se fossem reais.

#### 4.4 Detalhes e verificação musical
- Ao selecionar uma faixa, exibir detalhes e fontes consultadas.
- Apresentar LRCLIB e MusicBrainz como fontes distintas, com status de correspondência compreensível.
- Diferenciar correspondência forte, aproximada e ausência de resultado.
- Letras sincronizadas devem ser renderizadas com timestamps apenas se o contrato e os dados permitirem; caso contrário, apresentar texto simples.
- Exibir avisos e limitações de verificação. Não afirmar que metadados confirmam autoria ou direitos autorais.

#### 4.5 Biblioteca, favoritos e playlists
- Conectar a biblioteca ao catálogo Java.
- Listar favoritos e permitir favoritar/desfavoritar com feedback e rollback visual quando a operação falhar.
- Listar, criar, abrir e excluir playlists usando os endpoints existentes.
- Adicionar/remover faixas em playlists se o contrato suportar.
- Tratar estados vazios e falhas sem apagar conteúdo visualmente antes de confirmação.
- Não afirmar persistência durável enquanto a API usar armazenamento em memória.

#### 4.6 Player
- Não criar um player funcional fictício.
- O README informa que a interface web ainda não reproduz áudio. Qualquer player web deve ser uma etapa separada, baseada em URLs de stream autorizadas e contratos reais, respeitando direitos e disponibilidade dos provedores.
- Até essa etapa ser aprovada, usar controles visuais apenas se estiverem claramente identificados como indisponíveis ou planejados.

#### 4.7 Novas ideias de produto
Avalie e priorize estas ideias, sem implementá-las todas de uma vez:
- **Painel de saúde das fontes:** disponibilidade da API e status de SoundCloud, LRCLIB e MusicBrainz, sem expor segredos.
- **Ficha técnica da faixa:** duração, álbum, identificadores, origem e grau de correspondência.
- **Histórico de pesquisas:** somente se houver estratégia de privacidade e persistência aprovada.
- **Comparador de metadados:** mostrar divergências entre fontes e explicar qual campo veio de onde.
- **Filtros de biblioteca:** favoritos, artista, duração e playlists, se os dados existentes permitirem.
- **Coleções temáticas pessoais:** playlists com nome, descrição e capa, após definição de contrato.
- **Atalhos de teclado:** foco na busca, limpar busca e navegação acessível.
- **Estados offline/degradados:** manter a interface compreensível quando uma fonte externa falhar.

Para cada ideia, registre valor, dependências, impacto de API/banco, risco e esforço. Priorize utilidade real sobre quantidade de funcionalidades.

### 5. Integração correta das APIs

O navegador deve chamar somente caminhos relativos da própria aplicação, como `/api/...`. O Next.js deve encaminhar as requisições para a API Java configurada em `BACKEND_URL`.

- Não chamar SoundCloud, LRCLIB ou MusicBrainz diretamente do browser quando a API Java já fornece esses adaptadores.
- Não expor `SOUNDCLOUD_CLIENT_ID`, `SOUNDCLOUD_CLIENT_SECRET`, tokens, `DATABASE_URL`, segredos JWT ou credenciais em variáveis públicas `NEXT_PUBLIC_*`.
- Manter contratos TypeScript alinhados com `docs/API.md` e com respostas reais.
- Centralizar tratamento de erros, timeouts, cancelamento e parsing no cliente de API.
- Tratar respostas HTTP não-JSON, 204, 400, 401, 403, 404, 409, 429, 500 e 503 quando aplicáveis.
- Mostrar no frontend se a API está indisponível, sem transformar indisponibilidade em lista vazia enganosa.
- Implementar chamadas de saúde com intervalo razoável, evitando polling agressivo.
- Não alterar silenciosamente contratos existentes. Mudanças necessárias devem ser documentadas e acompanhadas de testes.

### 6. Hospedagem e arquitetura-alvo

Arquitetura desejada:

- **Frontend:** Next.js hospedado na Vercel.
- **API:** serviço web Java hospedado no Render.
- **Banco:** PostgreSQL gerenciado no Render para dados duráveis.
- **Integrações externas:** chamadas realizadas pelo backend Java.

O frontend Vercel deve usar `BACKEND_URL` configurado no ambiente de produção/preview conforme o ambiente. O valor deve apontar para a URL HTTPS real do serviço Java no Render. Não usar `127.0.0.1` ou `localhost` em produção.

O serviço Java no Render deverá:
- escutar em `0.0.0.0`;
- usar a porta definida pela variável `PORT` fornecida pela plataforma;
- expor health check sem vazar informações sensíveis;
- manter logs úteis sem tokens, letras completas ou dados pessoais desnecessários;
- ter build/start commands reproduzíveis e documentados;
- receber segredos somente pela configuração segura do serviço.

Como frontend + API + PostgreSQL formam uma arquitetura com múltiplos recursos, avaliar Render Blueprint (`render.yaml`) e registrar a decisão antes de criar recursos. Conferir planos, disponibilidade, custos, região e limitações atuais antes de provisionar qualquer recurso pago. Não criar banco/serviço nem iniciar deploy sem aprovação do responsável sobre custos e configuração.

### 7. Segurança e persistência — bloqueadores para exposição pública

O ADR atual da API registra que o serviço não possui autenticação e mantém catálogo/favoritos/playlists em memória, além de declarar que não deve ser exposto publicamente nesse estado. O README também chama o backend de congelado.

A intenção de hospedar a API publicamente e ter persistência completa exige **revisão formal dessas decisões**. Não ignore nem contorne essas restrições.

Antes de tornar endpoints de escrita públicos:
- definir autenticação (sessão segura ou tokens com ciclo de vida definido);
- definir autorização por usuário para catálogo privado, favoritos e playlists;
- proteger operações de escrita contra acesso anônimo e IDOR;
- validar payloads, tamanhos, limites de taxa e origem;
- aplicar CORS estrito quando necessário, sem usá-lo como substituto de autenticação;
- armazenar senhas apenas com hash apropriado caso haja autenticação por senha;
- proteger cookies com HttpOnly, Secure e SameSite se usar sessões;
- evitar guardar tokens de sessão em LocalStorage;
- definir proteção CSRF quando aplicável;
- criar trilhas de auditoria sem registrar segredos;
- definir política de retenção, exclusão e backup.

Para PostgreSQL:
- definir entidades e relacionamentos antes da migração;
- separar dados globais de catálogo e dados privados por usuário;
- usar chaves, índices, constraints e migrações versionadas;
- testar isolamento entre usuários;
- definir backups e procedimento de restauração;
- não manter memória como fonte de verdade nem anunciar persistência antes de validar gravação e recuperação.

Não implementar login cosmético, autorização apenas no frontend ou endpoints públicos de escrita sem proteção. Não usar credenciais hardcoded, contas administrativas ocultas, bypass, backdoor ou usuário padrão inseguro.

### 8. Plano de execução por etapas

Execute em entregas pequenas, sem tentar reescrever tudo em um único commit:

1. **Auditoria e planejamento:** inventário do frontend, API, contratos, deploy atual, PRs abertos e restrições. Atualizar plano/ADRs.
2. **Fundação visual:** tokens, grid, geometria quadrada, responsividade, componentes e navegação.
3. **Busca e descoberta:** refinar fluxo de busca real e estados de API.
4. **Detalhes e verificação:** ficha da faixa, letras e metadados reais.
5. **Biblioteca:** catálogo e favoritos conectados à API.
6. **Playlists:** CRUD e gerenciamento de faixas conforme contratos reais.
7. **Qualidade:** acessibilidade, tratamento de erros, performance, testes de componentes e integração.
8. **API em produção:** ajustar bind/PORT/health check e configurar Render após revisão dos ADRs.
9. **Persistência e segurança:** PostgreSQL, migrações, autenticação, autorização e testes de isolamento.
10. **Integração Vercel ↔ Render:** configurar `BACKEND_URL`, CORS/origins e validar o site publicado.
11. **Revisão final:** documentação, riscos, evidências, CI e PR.

Não avance uma etapa que dependa de uma decisão pendente ou de uma etapa anterior não validada.

### 9. Testes e critérios de aceite

Em cada entrega, execute o que estiver disponível:
- `npm run lint`;
- `npm run build`;
- testes unitários/componentes e integração quando existirem;
- `mvn clean verify` para alterações do backend;
- testes de contrato para endpoints alterados;
- verificações de responsividade e acessibilidade;
- testes de falha de rede, timeout, resposta vazia, 503 e dados incompletos.

Critérios mínimos:
- frontend compila e não apresenta erros de lint;
- layout funciona em desktop e mobile, sem overflow;
- componentes possuem foco visível e navegação por teclado;
- busca, verificação, catálogo, favoritos e playlists consomem dados reais;
- falhas de API são explicadas e não mascaradas;
- segredos não chegam ao browser;
- nenhuma operação privada pode ser executada por outro usuário;
- persistência só é declarada quando sobrevive a reinício e foi testada;
- documentação e README descrevem o que realmente está funcionando.

### 10. Git, commits e PR

- Nome sugerido para commits: `feat(frontend): ...`, `fix(frontend): ...`, `docs(sonora): ...`, `feat(api): ...`.
- Um commit deve representar uma unidade pequena e revisável.
- Faça push ao terminar uma etapa independente e validada, sempre na branch de trabalho.
- Não agrupe alterações de backend, frontend, infraestrutura e banco sem necessidade.
- Abra/atualize PR contra `main), usando `.github/pull_request_template.md`.
- Não faça merge por conta própria.
- Não afirme que Vercel ou Render estão funcionando até verificar o deploy e executar chamadas HTTP reais.

### 11. Relatório obrigatório por etapa

Ao final de cada etapa, apresentar:
1. objetivo e escopo concluído;
2. arquivos criados/alterados;
3. funcionalidades realmente conectadas;
4. commit SHA e branch;
5. resultado dos testes com comandos;
6. limitações e problemas confirmados/suspeitos/riscos;
7. se houve push, link da branch/commit;
8. próximo passo recomendado.

**Resultado esperado:** um Sonora de identidade própria, visual mais quadrado e organizado, frontend conectado de verdade à API Java, e arquitetura preparada para Vercel + Render + PostgreSQL sem expor o serviço atual inseguro.
