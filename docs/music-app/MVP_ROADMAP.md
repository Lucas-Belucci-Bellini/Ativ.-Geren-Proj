# Roadmap do MVP — Aplicativo de Música

**Status:** stack aprovada por Lucas em 2026-10-14; primeira implementação publicada para validação.

## Fase 0 — Decisões e preparação

- [x] Aprovar a stack React + TypeScript + Vite no ADR-0001.
- [ ] Refinar nome e identidade visual antes de uma eventual publicação.
- [ ] Ampliar o catálogo apenas quando a origem e as licenças de cada mídia forem registradas.
- [x] Confirmar o primeiro alvo como aplicação web responsiva.
- [ ] Manter integrações externas separadas até revisar termos e limites.

**Saída esperada:** concluída para o MVP inicial; novos requisitos estruturais continuam sujeitos a ADR.

## Fase 1 — Estrutura visual

- [x] Criar layout responsivo com navegação principal, área de conteúdo e player persistente.
- [x] Implementar visões de Início, Busca e Biblioteca.
- [x] Criar tipos para faixas e catálogo demonstrativo; modelos dedicados de álbum/artista/playlist ficam para evolução.
- [x] Usar metadados fictícios e arte abstrata original em CSS.
- [x] Incluir estados vazios e tratamento de erros de áudio.

## Fase 2 — Interações principais

- [x] Busca local por título, artista, álbum e gênero.
- [x] Favoritar/desfavoritar uma faixa.
- [x] Persistir favoritos no navegador, sem sincronização entre dispositivos.
- [x] Implementar áudio demonstrativo sintetizado com Web Audio API.
- [x] Sincronizar play/pause, progresso, duração, volume e seleção de faixa.
- [x] Tratar incompatibilidade do Web Audio e transições entre faixas.

## Fase 3 — Qualidade e acessibilidade

- [x] Criar testes unitários de domínio para busca, navegação pelo catálogo e favoritos locais.
- [ ] Validar navegação por teclado e foco visível.
- [ ] Validar layouts pequenos e grandes manualmente.
- [x] Adicionar typecheck, testes Vitest e build ao GitHub Actions.
- [x] Documentar pré-requisitos, comandos e limitações.
- [ ] Adicionar lockfile npm e revisar dependências.

## Fase 4 — Persistência e serviços (somente se aprovados)

- [ ] Confirmar dados que precisariam sincronizar entre dispositivos.
- [ ] Definir modelo de conta, biblioteca e playlists.
- [ ] Aprovar ADR específico antes de backend e banco de dados.
- [ ] Definir contratos, auth, autorização, migrações e backups.
- [ ] Testar isolamento entre usuários e validação de entrada.

## Fase 5 — Integrações externas (opcional)

- [ ] Definir caso de uso independente.
- [ ] Revisar políticas e permissões atuais do provedor.
- [ ] Usar OAuth PKCE quando apropriado e documentado.
- [ ] Nunca incluir client secret no bundle do navegador.
- [ ] Tratar limites, erros, expiração e revogação.
- [ ] Não alegar suporte a reprodução completa sem confirmar permissões.

## Fase 6 — Aplicativo instalável para Windows

Plano: [docs/deployment/INSTALLER_PLAN.md](../deployment/INSTALLER_PLAN.md). ADR-0002 aguarda aprovação e nenhum instalador foi publicado.

- [ ] Aprovar ADR-0002: Tauri 2 + NSIS EXE para Windows x64.
- [ ] Criar e revisar package-lock.json; migrar CI para npm ci.
- [ ] Resolver dependências de fontes/assets externos para o primeiro início offline.
- [ ] Configurar Tauri, ícones, identificador, versão e instalador.
- [ ] Criar CI em runner Windows; PRs geram artefatos de teste, não releases públicas.
- [ ] Validar instalação, primeiro início, upgrade e desinstalação numa máquina virtual limpa.
- [ ] Publicar GitHub Release versionada com notas e checksum após CI e smoke tests aprovados.
- [ ] Avaliar assinatura de código antes de divulgação ampla.
- [ ] Planejar atualizações automáticas assinadas como etapa separada.

**Saída esperada:** uma pessoa sem ferramentas de desenvolvimento consegue baixar o instalador oficial, instalar e abrir o Sonora.

## Definition of Done

Uma tarefa só está concluída quando atende aos critérios, a documentação reflete o comportamento real e os checks disponíveis foram executados. Para release instalável, os critérios do plano de instalação também precisam passar. Limitações não testadas devem ser documentadas.
