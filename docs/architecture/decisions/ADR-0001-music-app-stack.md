# ADR-0001 — Stack inicial do aplicativo de música

- **Data:** 2026-10-14
- **Status:** **PROPOSTA — aguardando aprovação de Lucas**
- **Escopo:** MVP web demonstrativo
- **Referência funcional:** [thiagosprestes/Spotify-clone-app-react](https://github.com/thiagosprestes/Spotify-clone-app-react)

## Contexto

O repositório está no início do planejamento. A referência usa React 16.13, Create React App / `react-scripts` 3.4, Redux 4 e bibliotecas antigas. Essas escolhas servem para entender a separação conceitual de páginas e funcionalidades, mas não são uma exigência nem serão copiadas automaticamente.

A primeira entrega precisa testar interface, busca, biblioteca local e player sem exigir uma conta Spotify ou infraestrutura remota.

## Requisitos de decisão

- Boa compatibilidade com desenvolvimento web e hospedagem estática.
- Tipos para entidades e estados centrais.
- Testes de componentes e interações.
- Build reproduzível e integração com GitHub Actions.
- Dependências proporcionais ao MVP.
- Nenhum segredo de serviço externo no cliente.
- Separação clara entre catálogo demonstrativo e futuras fontes externas de dados/áudio.

## Opções consideradas

### A. React + TypeScript + Vite (preferida para o MVP)

**Vantagens**
- Mantém React como paradigma conhecido da referência, adicionando tipagem estática.
- Vite oferece um caminho direto para desenvolvimento e build de SPA.
- Permite começar como frontend demonstrativo e evoluir componentes sem obrigar a introduzir um backend.
- Tem uma fronteira clara para adicionar APIs se um requisito real justificar isso.

**Desvantagens e riscos**
- A SPA não fornece, sozinha, autenticação segura de servidor, persistência multiusuário ou gerenciamento privado de arquivos.
- A equipe ainda precisará configurar testes, lint e regras de build.
- Estado salvo em LocalStorage é local ao navegador e não representa persistência remota segura.

### B. React + JavaScript + Create React App (estrutura similar à referência)

**Vantagens**
- Aproxima a estrutura de aprendizado do repositório de referência.

**Desvantagens e riscos**
- A referência usa versões antigas de React e ferramentas.
- Menos benefício de tipos para o modelo de domínio.
- Adotar essa opção apenas para imitar uma base existente aumentaria o custo de manutenção sem necessidade demonstrada.

### C. Next.js com backend no mesmo projeto

**Vantagens**
- Pode ser apropriado quando renderização no servidor, endpoints próprios e autenticação server-side forem requisitos reais.

**Desvantagens e riscos**
- Mais conceitos e configuração do que o MVP visual requer.
- Pode introduzir responsabilidades de servidor antes de existir um caso de uso que as justifique.

### D. React Native / app mobile nativo

Não selecionado para a primeira fase, pois o alvo inicial proposto é web responsiva. Pode ser reavaliado se houver um requisito explícito de aplicativo móvel nativo.

## Proposta de decisão

**Frontend:** React + TypeScript + Vite, com CSS próprio ou CSS Modules e sem framework visual obrigatório.  
**Estado:** React state/Context apenas onde necessário; adiar Redux/Zustand até demonstrar necessidade.  
**Backend do MVP:** nenhum. O primeiro incremento usa catálogo de demonstração e mídia autorizada.  
**Persistência do MVP:** LocalStorage somente para preferências/favoritos não sensíveis, com a limitação documentada.  
**Dados remotos:** não escolher banco ou API até que uma funcionalidade aprovada precise deles.  
**Qualidade:** propor Vitest + React Testing Library para testes unitários/componentes; definir ferramenta de lint/formatação e comandos exatos durante a estrutura inicial. Testes de ponta a ponta podem ser avaliados após a primeira tela funcional.

A recomendação TypeScript para frontend está alinhada às orientações de `docs/PROJECT_RULES.md`. Não há razão demonstrada para adicionar Rust, Python ou PostgreSQL nesta primeira fase. Caso surja uma necessidade de backend, ela deve ser avaliada em um ADR posterior, em vez de introduzir tecnologias por preferência.

## Uso do repositório de referência e conformidade

A referência foi consultada para reconhecer suas funcionalidades e organização. Não foi encontrado arquivo de licença no tree do repositório consultado; por isso, esta proposta não autoriza copiar seu código, assets ou design. A implementação deve ser própria.

Além disso, a [Spotify Developer Policy](https://developer.spotify.com/policy) restringe aplicações que mimetizam ou tentam substituir a experiência principal do Spotify sem permissão escrita. Por isso:
- criar nome, marca e interface próprios;
- usar áudio próprio ou autorizado;
- não distribuir conteúdo de terceiros sem licença;
- avaliar qualquer integração Spotify como uma funcionalidade independente e sujeita aos termos vigentes;
- não tratar a integração do Spotify como autorização para reproduzir o serviço inteiro.

## Consequências

Se aprovada, a equipe poderá criar a estrutura do frontend e os primeiros componentes. O MVP não será um serviço de streaming completo e não terá contas sincronizadas. Uma versão com backend, uploads, contas ou integração de terceiros precisa de escopo e decisões adicionais.

## Critérios de validação da proposta

- [ ] Lucas aprovou a escolha de React + TypeScript + Vite.
- [ ] Lucas confirmou que a primeira entrega será uma aplicação web responsiva.
- [ ] Foi definida a origem legal do áudio demonstrativo.
- [ ] Os critérios de aceitação em `docs/music-app/PRODUCT_BRIEF.md` foram aceitos ou revisados.
- [ ] Os gates de CI específicos da stack serão adicionados após a estrutura inicial.

**Importante:** até a aprovação explícita desta ADR, a decisão é uma proposta; a implementação principal permanece bloqueada pelas regras do repositório.
