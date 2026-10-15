# Verificação automática e revisão de mudanças

**Status:** primeira camada de proteção; evoluída junto com a stack aprovada.

## Repository Guard

O workflow .github/workflows/repository-guard.yml roda em pushes, pull requests e execução manual.

1. Confirma os documentos essenciais.
2. Procura marcadores comuns de conflito Git nos arquivos de texto rastreados.
3. Em pull requests, executa git diff --check contra a branch-base.
4. Usa permissões mínimas de leitura.

O script scripts/ci/verify_repository.py não instala dependências e termina com código diferente de zero quando encontra falhas.

## Workflow da aplicação web

O workflow .github/workflows/music-app-ci.yml executa typecheck TypeScript, testes Vitest e build de produção para alterações em music-app/.

O projeto ainda não possui package-lock.json versionado. Até adicioná-lo e revisá-lo, as dependências transitivas não estão completamente travadas. A etapa inicial do plano de instalador corrige isso e troca npm install por npm ci.

## Plano de CI do instalador

O workflow de instalador desktop ainda não existe. Depois de aprovar ADR-0002, o plano é:

- Construir em runner Windows.
- Instalar dependências com npm ci a partir de lockfile versionado e usar a toolchain Rust documentada pelo Tauri.
- Executar typecheck, testes, build web e empacotamento desktop.
- Criar artefatos de teste em PRs, sem publicar Releases.
- Publicar instalador somente por tag/release controlada, depois de aprovação dos checks.
- Guardar segredos de assinatura fora do repositório e não expô-los a workflows de PR não confiáveis.

Consulte docs/deployment/INSTALLER_PLAN.md e docs/architecture/decisions/ADR-0002-windows-installer.md. Um workflow não substitui testes de instalação numa máquina Windows limpa nem configura automaticamente a proteção da branch.
