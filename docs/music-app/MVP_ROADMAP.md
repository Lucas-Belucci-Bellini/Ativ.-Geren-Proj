# Roadmap do MVP — Aplicativo de Música

**Status:** stack aprovada por Lucas em 2026-10-14; primeira implementação publicada para validação.

## Fase 0 — Decisões e preparação

- [x] Aprovar a stack React + TypeScript + Vite no ADR-0001.
- [ ] Escolher o nome do produto e uma identidade visual original.
- [ ] Definir a origem do catálogo e registrar licença/atribuição de cada mídia.
- [x] Confirmar o primeiro alvo como aplicação web responsiva.
- [ ] Manter qualquer integração externa separada até revisar seus termos e limites.

**Saída esperada:** concluída para o MVP inicial; novos requisitos estruturais continuam sujeitos a ADR.

## Fase 1 — Estrutura visual

- [x] Criar layout responsivo com navegação principal, área de conteúdo e player persistente.
- [x] Implementar visões de Início, Busca e Biblioteca.
- [x] Criar tipos para faixas e catálogo demonstrativo; modelos dedicados de álbum/artista/playlist ficam para evolução.
- [x] Usar metadados fictícios e arte abstrata original em CSS.
- [x] Incluir estados vazios; erros de áudio são tratados, sem estado remoto de carregamento.

**Saída esperada:** interface consistente e navegável, sem depender de login ou credenciais externas.

## Fase 2 — Interações principais

- [x] Busca local por título, artista, álbum e gênero.
- [x] Favoritar/desfavoritar uma faixa.
- [x] Persistir favoritos no navegador, explicando que não existe sincronização entre dispositivos.
- [x] Implementar áudio demonstrativo sintetizado localmente com Web Audio API.
- [x] Sincronizar play/pause, progresso, duração, volume e seleção de faixa.
- [x] Tratar incompatibilidade do Web Audio, pausar, avançar e voltar entre faixas.

**Saída esperada:** MVP demonstrável com interações reais, não apenas botões decorativos.

## Fase 3 — Qualidade e acessibilidade

- [x] Criar testes de unidade para busca, navegação pelo catálogo e favoritos locais.
- [ ] Validar navegação por teclado e foco visível.
- [ ] Validar layouts pequenos e grandes.
- [x] Adicionar typecheck, testes Vitest e build ao GitHub Actions; lint/formatação dedicada ficam como melhoria próxima.
- [x] Documentar pré-requisitos, configuração, comandos e limitações nos READMEs.
- [ ] Revisar dependências e evitar credenciais no repositório.

**Saída esperada:** build reproduzível, critérios de aceitação verificados e resultados registrados.

## Fase 4 — Persistência e serviços (somente se aprovados)

- [ ] Confirmar quais dados precisam sincronizar entre dispositivos.
- [ ] Definir modelo de conta, biblioteca e playlists.
- [ ] Escolher backend e banco com ADR específico antes de implementá-los.
- [ ] Definir contratos, autenticação, autorização, migrações e estratégia de backup.
- [ ] Testar acesso indevido entre usuários e validação das entradas.

**Saída esperada:** escopo do backend justificado, com contratos e controles de segurança documentados.

## Fase 5 — Integrações externas (opcional)

- [ ] Definir o caso de uso independente da integração.
- [ ] Rever as políticas, termos, limitações de quota e permissões atuais do provedor.
- [ ] Se aplicável, utilizar OAuth PKCE para uma SPA quando o fluxo de autorização escolhido exigir e for compatível com a documentação oficial.
- [ ] Nunca incluir client secret no bundle do navegador.
- [ ] Implementar limites, erros 401/403/429, expiração e revogação de autorização.
- [ ] Não alegar suporte a reprodução completa sem confirmar as permissões e requisitos reais do serviço.

**Saída esperada:** integração separada, documentada e compatível com as condições aplicáveis.

## Definition of Done

Uma tarefa só está concluída quando atende a seus critérios, a documentação corresponde ao comportamento real, os checks disponíveis foram executados e os resultados são registrados. Se algo não puder ser testado, a limitação deve aparecer no PR; nunca se deve marcar um check como aprovado sem evidência.
