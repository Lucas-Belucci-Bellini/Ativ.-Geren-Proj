# Plano de testes — Cards 4 e 5

**Responsável:** Lucas — busca e testes  
**Projeto:** clone de terminal inspirado no Spotify  
**Status:** casos de catálogo parcialmente verificados; busca integrada pendente de correção de pacotes/build.

## Objetivo

Definir verificações reproduzíveis para a busca de músicas e para o catálogo, incluindo validação de entrada e consulta por ID. A busca deve localizar por fragmentos do título ou do artista, ignorar maiúsculas/minúsculas e espaços no início/fim, e não encerrar a aplicação quando a entrada for inválida.

## Pré-condições

- Resolver a estrutura de pacotes entre `SearchService.java` e `Track.java`.
- Definir um comando reproduzível de compilação/teste para o projeto.
- Usar o catálogo inicial de oito faixas como conjunto de dados conhecido.
- Não considerar um caso aprovado até executá-lo no projeto integrado.

## Matriz de testes

| ID | Cenário | Entrada/ação | Resultado esperado | Estado |
|---|---|---|---|---|
| CAT-01 | Listar catálogo | Solicitar todas as faixas | O catálogo inicial contém 8 faixas | PASSOU no teste de fumaça isolado |
| CAT-02 | Campos obrigatórios | Inspecionar todas as faixas | ID, título e artista não vazios; duração não negativa | PASSOU no teste de fumaça isolado |
| CAT-03 | ID existente | Buscar `t-001` | Retorna a faixa correspondente | PASSOU no teste de fumaça isolado |
| CAT-04 | ID inexistente | Buscar `id-inexistente` | Retorna `Optional.empty()` | PASSOU no teste de fumaça isolado |
| CAT-05 | Duração | Formatar 354 segundos | `05:54` | PASSOU no teste de fumaça isolado |
| BUS-01 | Título completo | Pesquisar `Bohemian Rhapsody` | Encontra a faixa | PENDENTE: SearchService não compila integrado |
| BUS-02 | Título parcial | Pesquisar `bohemian` | Encontra a faixa | PASSOU no serviço de catálogo isolado; pendente em SearchService |
| BUS-03 | Artista completo | Pesquisar `Queen` | Encontra a faixa | PENDENTE em SearchService |
| BUS-04 | Artista parcial | Pesquisar `que` | Encontra a faixa | PENDENTE em SearchService |
| BUS-05 | Caixa diferente | Pesquisar `qUeEn` | Mesmo resultado que `Queen` | PENDENTE em SearchService |
| BUS-06 | Espaços externos | Pesquisar `  Queen  ` | Mesmo resultado que `Queen` | PENDENTE em SearchService |
| BUS-07 | Sem correspondência | Pesquisar `artista inexistente` | Coleção vazia; aplicação continua ativa | PENDENTE em SearchService/menu |
| BUS-08 | Termo vazio | Pesquisar string vazia | Mensagem de validação; menu continua ativo | PENDENTE em SearchService/menu |
| BUS-09 | Apenas espaços | Pesquisar `   ` | Mensagem de validação; menu continua ativo | PENDENTE em SearchService/menu |
| BUS-10 | Termo nulo | Chamar serviço com `null` | Erro de entrada controlado; aplicação chamadora continua ativa | PENDENTE em SearchService/menu |
| BUS-11 | Resultado exibido | Pesquisar faixa encontrada | Mostrar ID, título, artista e duração | PENDENTE: menu não integrado |
| BUS-12 | Ordem de resultados | Buscar termo que corresponda a várias faixas | Preservar a ordem do catálogo | PENDENTE em SearchService |

## Procedimento de execução manual

1. Iniciar a aplicação pelo comando documentado do projeto.
2. Escolher a opção de busca no menu.
3. Executar cada entrada BUS-01 a BUS-12.
4. Conferir a lista e os quatro campos exibidos.
5. Para BUS-07 a BUS-10, verificar que a aplicação mostra uma mensagem compreensível e retorna ao menu sem encerrar o processo.
6. Repetir uma busca válida após uma entrada inválida para confirmar que o estado da aplicação foi preservado.
7. Registrar saída real, comando, versão do Java e commit testado no relatório.

## Critério de conclusão

- Todos os casos BUS-01 a BUS-12 executados no projeto integrado.
- Nenhum erro de compilação.
- Entradas inválidas não encerram o menu.
- Os testes automatizados ficam no repositório e podem ser repetidos por outra pessoa.
- O relatório distingue claramente testes aprovados, falhos e não executados.
