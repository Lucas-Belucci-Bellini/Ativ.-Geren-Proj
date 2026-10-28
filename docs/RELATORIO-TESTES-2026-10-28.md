# Relatório de testes e auditoria — Cards 4 e 5

- **Projeto:** Ativ.-Geren-Proj (clone/fork de trabalho)
- **Data:** 28/10/2026
- **Branch de trabalho:** `feat/lucas-cards-4-5-tests-report`
- **Branch-base inspecionada:** `main`
- **Escopo:** catálogo de músicas, busca por título/artista, validação de IDs, estado de compilação dos fontes e preparação de testes para os Cards 4 e 5.
- **Responsável pela tarefa:** Lucas — busca e testes, com assistência de monitor de programação.

## Resumo executivo

**Resultado: parcialmente verificado; a busca ainda não está integrada/compilável com a estrutura atual do projeto.**

Foi possível validar oito comportamentos do catálogo com um teste de fumaça local, compilando uma cópia temporária dos fontes `Track.java` e `CatalogService.java` consultados no GitHub. Isso não equivale a um build completo do repositório. A tentativa de compilar `SearchService.java` junto com o `Track.java` atual falhou porque o serviço importa `spotifyclone.model.Track`, mas o modelo existente está no pacote padrão em `src/Track.java`.

O repositório não apresenta uma configuração Maven/Gradle visível no inventário consultado, e o workflow atual é um guard de documentação/integridade — não compila Java nem executa testes funcionais. A estrutura de pacotes/stack também permanece pendente na documentação de arquitetura. Por isso, este relatório não declara que a aplicação inteira está funcionando.

## Arquivos e funcionalidades inspecionados

- `SearchService.java` — busca parcial por título ou artista, ignorando caixa e espaços externos; rejeita termo nulo/vazio por exceção e retorna lista imutável.
- `src/Track.java` — modelo de faixa, validação de campos obrigatórios, duração formatada e igualdade baseada no ID.
- `src/CatalogService.java` — catálogo inicial, listagem, busca por ID, busca por título/artista e operações de adição/remoção.
- `src/Main.java` — ainda contém um exemplo de inicialização do IntelliJ; não foi encontrada integração de menu de busca neste arquivo.
- `docs/PROJECT_RULES.md` e `docs/architecture/decisions/README.md` — a stack e a decisão de arquitetura permanecem pendentes.

## Testes executados

Ambiente local disponível: OpenJDK/Javac 21.0.12. Maven não está instalado. O clone Git por rede não foi possível neste ambiente; para o teste de fumaça, os fontes relevantes foram copiados manualmente a partir do conteúdo retornado pela API do GitHub para um diretório temporário e compilados isoladamente.

| # | Verificação | Resultado |
|---:|---|---|
| 1 | Compilar o modelo `Track` e `CatalogService` em conjunto com o teste de fumaça | PASSOU |
| 2 | Catálogo inicial contém 8 faixas | PASSOU |
| 3 | Listagem retorna 8 faixas | PASSOU |
| 4 | Todas as faixas listadas têm ID, título, artista e duração válida | PASSOU |
| 5 | Busca parcial por título (`bohemian`) | PASSOU |
| 6 | Busca parcial por artista (`queen`, sem diferenciar caixa no ambiente atual) | PASSOU |
| 7 | ID existente (`t-001`) é encontrado | PASSOU |
| 8 | ID inexistente retorna `Optional.empty()` | PASSOU |
| 9 | Duração de 354 segundos é formatada como `05:54` | PASSOU |
| 10 | Compilar `SearchService.java` com o modelo atual | **FALHOU** — pacote `spotifyclone.model` não existe na árvore inspecionada |

**Resultado do teste de fumaça do catálogo:** 8 verificações passaram.  
**Resultado da busca independente:** não passou na compilação de integração; os cenários funcionais de `SearchService` ainda não podem ser considerados aprovados.

## Problemas e riscos

### Confirmado — incompatibilidade de pacote na busca

`SearchService.java` declara `package spotifyclone.search` e importa `spotifyclone.model.Track`. O modelo do projeto está em `src/Track.java` sem declaração de pacote. O comando de compilação isolada falhou com erros de pacote/classe não encontrado.

**Impacto:** a busca não pode ser compilada junto com o modelo atualmente presente sem uma decisão consistente de pacotes/estrutura de fontes.

### Confirmado por inspeção — menu ainda não integrado

`src/Main.java` contém apenas um exemplo “Hello and welcome” e não chama `SearchService` nem oferece a busca no menu do terminal.

**Impacto:** mesmo corrigindo a compilação do serviço, o usuário ainda não consegue acessar a busca pelo fluxo principal da aplicação.

### Risco — falta de build e testes automatizados persistentes

Não foi localizada uma configuração de build Java nem uma suíte automatizada na estrutura inspecionada. O teste de fumaça foi temporário e não está registrado como teste do repositório.

**Impacto:** não há um comando padrão reproduzível para validar o projeto completo ou executar regressões no CI.

### Risco — escopo de arquitetura pendente

As regras do próprio repositório solicitam decisão formal de stack e estrutura antes da implementação principal. O diretório de ADRs consultado contém apenas o README de orientação, sem decisão de stack aprovada.

## Plano de validação para os Cards 4 e 5

A matriz detalhada está em [`docs/PLANO-TESTES-CARDS-4-5.md`](./PLANO-TESTES-CARDS-4-5.md). Os casos de busca que dependem do `SearchService` ficam **pendentes** até resolver a estrutura de pacotes e a integração com o menu.

## Próximos passos recomendados

1. Registrar/aprovar a estrutura de pacotes e o build Java conforme as regras do projeto.
2. Ajustar `SearchService` para usar o mesmo modelo `Track` e a estrutura de fontes definida — sem manter imports incompatíveis.
3. Integrar a busca ao menu do terminal, mostrando ID, título, artista e duração; capturar entradas inválidas para que o menu continue funcionando.
4. Adicionar os casos da matriz como testes automatizados reproduzíveis.
5. Executar compilação e todos os testes no projeto completo e, se possível, adicionar o comando real ao GitHub Actions.
6. Atualizar este relatório com os comandos e resultados finais reais.

## Limitações

- Não foi possível clonar o repositório por indisponibilidade de resolução de `github.com` no ambiente de execução.
- Não foi executado build completo de todos os arquivos.
- Os oito testes aprovados validam apenas uma cópia temporária dos fontes de modelo/catálogo, não a integração completa do projeto.
- Os testes de busca por título/artista usando `SearchService`, a integração no menu e os fluxos de interface ainda não foram aprovados.
- Não foi alterada a branch `main`; este relatório foi preparado para a branch de trabalho.

---
Relatório preparado com assistência de monitor de programação, registrando evidências e limitações sem afirmar resultados não verificados.
