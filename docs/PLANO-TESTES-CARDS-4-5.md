# Plano de testes — Cards 4 e 5

**Responsável:** Lucas — busca e testes  
**Projeto:** aplicativo de terminal inspirado no Spotify  
**Status:** testes unitários da busca executados em harness isolado; integração ao menu ainda pendente.

## Objetivo

Validar a busca por fragmentos do título ou artista, ignorando maiúsculas/minúsculas e espaços externos, e garantir que entradas nulas/em branco não provoquem encerramento do aplicativo. A decisão de Java para o terminal foi registrada em [ADR-0001](./architecture/decisions/ADR-0001-java-terminal.md).

## Como executar os testes da busca

Na raiz do repositório, com JDK instalado:

```bash
javac -d out src/Track.java src/CatalogService.java src/SearchService.java tests/SearchServiceTest.java
java -cp out SearchServiceTest
```

O teste usa apenas APIs padrão do Java, sem JUnit ou dependências externas. O comando compila os arquivos envolvidos na busca e o teste, não todos os recursos da aplicação.

## Matriz de testes

| ID | Cenário | Entrada/ação | Resultado esperado | Estado |
|---|---|---|---|---|
| CAT-01 | Catálogo inicial | Consultar todas as faixas | 8 faixas | PASSOU no teste isolado |
| CAT-02 | Campos obrigatórios | Inspecionar faixas | IDs/títulos/artistas presentes; duração válida | PASSOU no teste de fumaça anterior |
| CAT-03 | ID existente | Buscar `t-001` | Faixa correspondente | PASSOU no teste de fumaça anterior |
| CAT-04 | ID inexistente | Buscar `id-inexistente` | `Optional.empty()` | PASSOU no teste de fumaça anterior |
| CAT-05 | Duração | Formatar 354 segundos | `05:54` | PASSOU no teste de fumaça anterior |
| BUS-01 | Título parcial | `bohemian` | Encontra Bohemian Rhapsody | PASSOU |
| BUS-02 | Artista e caixa | `QUEEN` | Encontra faixa de Queen | PASSOU |
| BUS-03 | Espaços externos | `  queen  ` | Mesmo resultado sem espaços | PASSOU |
| BUS-04 | Fragmento case-insensitive | `eEn` | Encontra faixa de Queen | PASSOU |
| BUS-05 | Título completo | `imagine` | Encontra Imagine | PASSOU |
| BUS-06 | Sem correspondência | `artista inexistente` | Lista vazia | PASSOU |
| BUS-07 | Termo vazio | `""` | Lista vazia, chamador pode continuar | PASSOU no serviço; menu pendente |
| BUS-08 | Só espaços | `   ` | Lista vazia, chamador pode continuar | PASSOU no serviço; menu pendente |
| BUS-09 | Termo nulo | `null` | Lista vazia | PASSOU |
| BUS-10 | Catálogo nulo ao construir serviço | `new SearchService(null)` | Rejeitar com `NullPointerException` | PASSOU |
| BUS-11 | Campos apresentados | Pesquisar faixa pelo menu | Mostrar ID, título, artista e duração | PENDENTE: menu não integrado |
| BUS-12 | Busca após entrada inválida | Busca válida depois de vazio | Aplicação permanece ativa | PENDENTE: menu não integrado |

## Resultado observado

Em 28/10/2026, com `javac 21.0.12.1`, os quatro arquivos relevantes foram compilados em um diretório temporário de teste e o `SearchServiceTest` foi executado: **12/12 verificações passaram**. A execução foi isolada dos demais arquivos da aplicação; isso não é um build completo do repositório nem valida a integração no menu.

## Critério de conclusão

- Testes da busca reproduzíveis no repositório e aprovados.
- Menu integrado e exibição de ID, título, artista e duração validados manualmente.
- Build da aplicação completa executado e resultados registrados no relatório.
- O relatório distingue testes aprovados, falhos e não executados.
