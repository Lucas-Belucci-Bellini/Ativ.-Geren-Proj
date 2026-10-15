# Verificação automática e revisão de mudanças

**Status:** primeira camada de proteção; deve ser evoluída junto com a stack aprovada.

## O que o Repository Guard verifica

O workflow `.github/workflows/repository-guard.yml` roda em pushes, pull requests e execução manual. A verificação atual é intencionalmente independente da linguagem, porque a stack deste projeto ainda não foi aprovada.

1. Confirma a presença dos documentos essenciais de governança.
2. Procura marcadores comuns de conflito Git nos arquivos de texto rastreados.
3. Em pull requests, executa `git diff --check` contra a branch-base para detectar erros de whitespace no patch.
4. Usa permissões mínimas de leitura e não publica artefatos, releases ou segredos.

O script `scripts/ci/verify_repository.py` não instala dependências e termina com código diferente de zero quando encontra falhas.

## Como deve funcionar o fluxo

1. Criar uma branch para uma mudança focada.
2. Atualizar primeiro requisitos, decisões e documentação afetada.
3. Implementar em passos pequenos.
4. Abrir um pull request preenchendo o template.
5. Aguardar o Repository Guard e, depois que a stack for aprovada, os testes específicos de frontend/backend.
6. Corrigir falhas com evidências; não desativar ou contornar o gate apenas para obter verde.
7. Revisar o diff e os resultados antes de integrar.

## Limitações importantes

- GitHub Actions não impede por si só todo commit local. Ele verifica os eventos enviados ao GitHub.
- Para impedir merges com verificações vermelhas, o responsável precisa configurar a proteção da branch-base em **Settings → Rules → Rulesets** (ou Branch protection rules) e tornar obrigatório o check **Governance + repository integrity**.
- Não foi ativada proteção de branch por este commit. Isso exige configuração administrativa do repositório e deve ser confirmado na interface/API após a criação do workflow.
- O guard atual não substitui testes unitários, integração, build, análise estática, auditoria de dependências ou revisão humana.
- Assim que a ADR da stack for aprovada, adicionar gates reais e reproduzíveis (formatador, lint, testes e build) para as ferramentas escolhidas. Não adicionar comandos fictícios que sempre passam.
- Workflows de `rea`, `NEXORA` e `Project-Vanguard` serviram como referências de princípios: CI em PR/push, gates explícitos e falha real quando a validação falha. Os pipelines específicos desses projetos não devem ser copiados literalmente, pois dependem de linguagens, ferramentas e testes que este repositório ainda não declarou.

## Referências consultadas

- [rea — workflows](https://github.com/morluto/rea/tree/main/.github/workflows)
- [NEXORA — CI](https://github.com/Lucas-Belucci-Bellini/NEXORA/blob/main/.github/workflows/ci.yml)
- [Project-Vanguard — CI](https://github.com/Lucas-Belucci-Bellini/Project-Vanguard/blob/main/.github/workflows/ci.yml)


## Workflow do aplicativo Sonora

O workflow `.github/workflows/music-app-ci.yml` instala as dependências e executa typecheck TypeScript, testes Vitest e build de produção quando arquivos de `music-app/` ou o próprio workflow mudam. Ele usa Node.js 22.16.0, compatível com os requisitos documentados do Vite 8 e Vitest 5.

A instalação atual usa versões exatas das dependências diretas em `music-app/package.json`, mas ainda não possui `package-lock.json` versionado. Até adicionar e revisar esse lockfile, a resolução das dependências transitivas não é completamente reproduzível entre execuções; isso permanece como pendência antes de tratar o pipeline como gate de produção.
