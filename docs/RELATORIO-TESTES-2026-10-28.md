# Relatório de correção e testes
# Relatório de implementação e testes — Cards 4 e 5

- **Data:** 28/10/2026
- **Branch de correção:** `fix/java-build-and-documentation`
- **Base:** `test/relatorio-verificacao-2026-10-28`
- **Escopo:** correções de tipos, alinhamento da pesquisa com a estrutura Java, documentação do código, build e testes automatizados.

## Alterações realizadas

1. **Playlists:** padronizado o ID como `int` em `Playlist` e `PlaylistService`, eliminando o conflito entre `Integer` e `String`.
2. **Pesquisa:** `SearchService` foi colocado em `src/SearchService.java` e o pacote/import incompatível foi removido para usar o mesmo pacote padrão de `Track`.
3. **Ponto de entrada:** `Main` usa agora `public static void main(String[] args)` e `System.out.println`, compatíveis com Java 17.
4. **Documentação do código:** classes, métodos públicos, validações e decisões importantes receberam JavaDoc/comentários em português.
5. **Build reproduzível:** adicionado `pom.xml` com Java 17 e JUnit 5.
6. **Testes:** adicionados testes para playlists, busca, catálogo, favoritos, duração e igualdade por ID.
7. **Integração contínua:** adicionado workflow `.github/workflows/java-tests.yml` para compilar e executar os testes no GitHub Actions.
8. **README:** documentados requisitos, funcionalidades existentes e comandos de execução.

## Issues do GitHub

Foi tentada a abertura de uma Issue para registrar o defeito, mas o GitHub respondeu que Issues estão desabilitadas neste repositório (HTTP 410). O defeito e a correção estão descritos neste relatório e no [Pull Request #1](https://github.com/Lucas-Belucci-Bellini/Ativ.-Geren-Proj/pull/1).

## Integrações musicais adicionadas

9. **SoundCloud:** `src/SoundCloudSearchService.java` consulta a API oficial, mapeia título, artista/usuário, capa, URL e duração; exige `SOUNDCLOUD_ACCESS_TOKEN` via ambiente e trata respostas de autorização/limite.
10. **Letras:** `src/LyricsVerificationService.java` consulta LRCLIB, normaliza acentos/caixa/espaços e compara título, artista e duração; separa resultado forte de resultado aproximado.
11. **Créditos de artista:** `src/MusicMetadataVerificationService.java` consulta MusicBrainz para obter gravações e créditos independentes.
12. **Testes adicionais:** cobrem normalização e pontuação da correspondência de letras.
13. **Segredos e documentação:** criado `.env.example` sem token real e atualizada a documentação de uso e limitações.

## Como validar localmente

Na raiz do projeto:

```bash
java -version
mvn -version
mvn --batch-mode clean verify
```

A execução local de `mvn clean verify` não foi realizada por esta integração do GitHub. O workflow Java do GitHub Actions deve ser consultado para confirmar a compilação e os testes da revisão mais recente; não considerar a integração validada até a conclusão bem-sucedida.

## Limitações conhecidas

- O catálogo usa caminhos de arquivos de áudio e capa de exemplo; não foi implementada reprodução de áudio.
- As integrações externas são serviços de backend/consulta; ainda não existe interface gráfica que mostre resultados, capas ou letras ao usuário.
- SoundCloud requer token autorizado e a disponibilidade dos campos depende da API/da faixa.
- LRCLIB pode não possuir letras para todas as faixas; letras e metadados retornados permanecem sujeitos aos termos do provedor.
- MusicBrainz limita a taxa de requisições; chamadas em lote devem ser espaçadas.
- Não há interface gráfica nesta versão.
- A estrutura usa o pacote padrão Java; a migração para pacotes nomeados ficou fora deste reparo.
- Os testes automatizados cobrem as principais regras de negócio, mas não substituem teste manual de interface ou reprodução de mídia.

## Estado de merge

Nenhuma alteração foi feita na `main`. Esta branch está preparada para revisão; o merge deve ocorrer somente depois da confirmação do workflow de compilação/testes.
- **Projeto:** Ativ.-Geren-Proj
- **Branch:** `feat/lucas-cards-4-5-tests-report`
- **Responsável pela funcionalidade:** Lucas — busca e testes
- **Assistência:** monitor de programação
- **Escopo desta etapa:** registrar decisão Java, implementar busca por título/artista e adicionar testes de regressão.

## Resumo executivo

**Resultado: serviço de busca implementado e 12/12 testes isolados aprovados; integração ao menu e build completo continuam pendentes.**

A decisão de usar Java para o aplicativo de terminal foi registrada em ADR-0001, conforme autorização do responsável. Foi adicionado `src/SearchService.java`, com busca por trecho de título ou artista, sem diferenciar maiúsculas/minúsculas e removendo espaços externos. Entradas nulas ou em branco retornam uma lista vazia. Foi adicionado `tests/SearchServiceTest.java` com verificações automatizadas sem dependências externas.

## Arquivos alterados nesta etapa

- `docs/architecture/decisions/ADR-0001-java-terminal.md` — registra a decisão de Java para o aplicativo de terminal.
- `docs/architecture/decisions/README.md` — atualiza o estado das decisões de arquitetura e mantém frontend web/backend web/persistência fora do escopo aprovado.
- `src/SearchService.java` — novo serviço de busca, usando o modelo `Track` e `CatalogService` no pacote padrão, consistente com os fontes existentes.
- `tests/SearchServiceTest.java` — novo teste executável sem JUnit.
- `docs/PLANO-TESTES-CARDS-4-5.md` — matriz atualizada com resultados e comando de execução.
- `docs/RELATORIO-TESTES-2026-10-28.md` — este relatório atualizado.

## Decisão de arquitetura

A ADR-0001 registra Java puro para o aplicativo de terminal. A migração geral para pacotes nomeados e a adoção de Maven/Gradle/JUnit foram adiadas por serem mudanças transversais não necessárias para concluir a lógica de busca. Frontend web, backend web, banco de dados e contratos de integração permanecem pendentes, pois não foram aprovados como parte deste escopo.

## Testes executados

**Ambiente:** `javac 21.0.12.1`, em diretório temporário isolado. Como não foi possível fazer clone Git completo neste ambiente, foi montado um harness temporário reproduzindo o conteúdo consultado dos arquivos envolvidos. A compilação/teste isolados são evidência da lógica, mas não substituem uma execução diretamente sobre um clone real da branch.

Comando usado no harness:

```bash
javac -d out src/Track.java src/CatalogService.java src/SearchService.java tests/SearchServiceTest.java
java -cp out SearchServiceTest
```

| Verificação | Resultado |
|---|---|
| Compilação do modelo, catálogo, serviço de busca e teste isolado | PASSOU |
| Catálogo inicial com 8 faixas | PASSOU |
| Busca parcial por título | PASSOU |
| Busca por artista ignorando maiúsculas/minúsculas | PASSOU |
| Remoção de espaços externos | PASSOU |
| Busca parcial case-insensitive | PASSOU |
| Busca por título completo | PASSOU |
| Nenhuma correspondência retorna lista vazia | PASSOU |
| Termo vazio retorna lista vazia | PASSOU |
| Termo só com espaços retorna lista vazia | PASSOU |
| Termo nulo retorna lista vazia | PASSOU |
| Construtor rejeita catálogo nulo | PASSOU |

**Resultado observado: 12/12 testes passaram.** O teste de busca está persistido em `tests/SearchServiceTest.java` para repetição no ambiente do projeto.

O workflow [Repository Guard](https://github.com/Lucas-Belucci-Bellini/Ativ.-Geren-Proj/actions/runs/40779399800) terminou com **success** no commit atual. Esse workflow verifica documentação/governança e **não** executa compilação Java nem os testes funcionais descritos acima.

## Pendências e limitações

1. **Integração com menu:** `src/Main.java` ainda é um exemplo de inicialização e não chama o serviço. A responsabilidade de menu/integracão foi atribuída a outro integrante do grupo; não alterei esse arquivo para evitar conflito com o trabalho dele. A integração deverá exibir ID, título, artista e duração.
2. **Build completo:** não foi compilada toda a aplicação. Os testes cobrem apenas `Track`, `CatalogService`, `SearchService` e `SearchServiceTest`.
3. **CI:** o workflow de guard existente valida documentação/governança, não executa estes testes Java. Não declarar o CI como teste funcional aprovado.
4. **Plano de execução:** não há ainda configuração de Maven/Gradle/JUnit; o teste é uma classe Java com `main`.
5. **Validação de menu:** ainda é necessário verificar que uma entrada inválida não encerra o processo e que uma busca válida pode ser feita em seguida.

## Próximos passos

1. Coordenar com o responsável pelo menu para integrar `SearchService` sem sobrescrever trabalho alheio.
2. Executar os testes no clone real do projeto com o comando documentado no plano.
3. Compilar e testar o fluxo de aplicação integrado quando o menu estiver pronto.
4. Atualizar este relatório com os resultados de integração e, se necessário, corrigir regressões.
5. Manter as alterações nesta branch; `main` permanece intacta e nenhum merge foi realizado.

## Referências

- [Branch de trabalho](https://github.com/Lucas-Belucci-Bellini/Ativ.-Geren-Proj/tree/feat/lucas-cards-4-5-tests-report)
- [ADR-0001 — Java para terminal](./architecture/decisions/ADR-0001-java-terminal.md)
- [Plano de testes dos Cards 4 e 5](./PLANO-TESTES-CARDS-4-5.md)
- [SearchService.java](../src/SearchService.java)
- [SearchServiceTest.java](../tests/SearchServiceTest.java)

---
