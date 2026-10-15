# Roadmap do MVP — Aplicativo de Música

**Status:** planejamento proposto; implementação principal aguarda aprovação do ADR-0001.

## Fase 0 — Decisões e preparação

- [ ] Aprovar ou revisar a proposta de stack no ADR-0001.
- [ ] Escolher o nome do produto e uma identidade visual original.
- [ ] Definir a origem do catálogo e registrar licença/atribuição de cada mídia.
- [ ] Confirmar se o primeiro alvo é uma aplicação web responsiva.
- [ ] Manter qualquer integração externa separada até revisar seus termos e limites.

**Saída esperada:** requisitos e arquitetura aprovados antes da criação da estrutura de aplicação.

## Fase 1 — Estrutura visual

- [ ] Criar layout responsivo com navegação principal, área de conteúdo e player persistente.
- [ ] Implementar páginas/visões de Início, Busca e Biblioteca.
- [ ] Criar componentes tipados para faixa, álbum, artista e playlist.
- [ ] Usar dados demonstrativos e assets originais ou devidamente licenciados.
- [ ] Incluir estados vazios, carregamento e erro.

**Saída esperada:** interface consistente e navegável, sem depender de login ou credenciais externas.

## Fase 2 — Interações principais

- [ ] Busca local por título, artista e álbum.
- [ ] Favoritar/desfavoritar uma faixa.
- [ ] Persistir favoritos no navegador, explicando que não existe sincronização entre dispositivos.
- [ ] Implementar player de áudio usando apenas mídia autorizada.
- [ ] Sincronizar controles, progresso, duração e faixa selecionada com o estado de reprodução.
- [ ] Tratar erros de mídia e transições entre faixas.

**Saída esperada:** MVP demonstrável com interações reais, não apenas botões decorativos.

## Fase 3 — Qualidade e acessibilidade

- [ ] Testar componentes e interações críticas.
- [ ] Validar navegação por teclado e foco visível.
- [ ] Validar layouts pequenos e grandes.
- [ ] Adicionar lint, formatação, testes e build reais ao GitHub Actions após aprovar a stack.
- [ ] Atualizar README com pré-requisitos, configuração e comandos efetivamente existentes.
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
