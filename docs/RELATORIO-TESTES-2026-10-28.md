# Relatório de Verificação do Projeto

- **Projeto:** Ativ.-Geren-Proj
- **Data:** 28/10/2026
- **Branch analisada:** `main`
- **Commit analisado:** `3e8a13cbca5435681fe4cae67cb13412fa6ed1dd`
- **Branch deste relatório:** `test/relatorio-verificacao-2026-10-28`
- **Escopo:** inspeção estática dos arquivos disponíveis no GitHub e consulta do resultado mais recente do GitHub Actions.

## Resumo

**Status geral: precisa de correções antes de considerar o projeto compilável.**

O workflow de integridade do repositório terminou com sucesso, mas ele verifica documentação, marcadores de conflito e, em pull requests, espaços em branco do diff. Ele **não compila o Java nem executa testes funcionais**. A revisão estática identificou incompatibilidades que provavelmente impedem a compilação conjunta dos fontes atuais.

## Verificações realizadas

| Verificação | Resultado | Evidência / observação |
|---|---|---|
| Repositório acessível e branch padrão | OK | Repositório público; branch padrão `main`. |
| Workflow GitHub Actions mais recente consultado | PASSOU | [Execução 40758714508](https://github.com/Lucas-Belucci-Bellini/Ativ.-Geren-Proj/actions/runs/40758714508), conclusão `success`. |
| Guard de governança e integridade | PASSOU na execução consultada | Workflow `Repository Guard`; não é um teste de compilação Java. |
| Compilação Java | NÃO EXECUTADA neste ambiente | Não foi executado `javac` ou um build do projeto; as incompatibilidades abaixo foram identificadas por inspeção estática. |
| Testes unitários/integração | NÃO LOCALIZADOS na árvore revisada | Não foi identificada configuração de testes ou ferramenta de build como Maven/Gradle no inventário consultado. |
| Teste de reprodução da aplicação | NÃO EXECUTADO | Não há evidência suficiente para afirmar que a aplicação inicia ou que todos os fluxos funcionam. |

## Problemas identificados

### 1. Incompatibilidade de tipos em `src/PlaylistService.java`

**Classificação:** confirmado por inspeção estática — bloqueador provável de compilação.

O serviço declara `Map<Integer, Playlist>`, mas cria uma playlist com `new Playlist(proximoId++, nome)`, enquanto o construtor de `Playlist` recebe um `String` como primeiro argumento. Além disso, `Playlist.getId()` retorna `String`, mas esse valor é usado como chave no mapa de tipo `Integer`.

**Impacto:** os tipos não são compatíveis e o código não deve compilar como está.

**Correção sugerida:** escolher um único tipo de ID e aplicá-lo de forma consistente em `Playlist`, no construtor, em `getId()`, no mapa e nos métodos de busca. Fazer essa mudança em conjunto para não quebrar os chamadores.

### 2. Pacotes e imports de `SearchService.java` não correspondem à árvore visível

**Classificação:** risco alto / provável bloqueador de compilação conjunta.

`SearchService.java` declara o pacote `spotifyclone.search` e importa `spotifyclone.model.Track`. Na árvore do commit analisado, o arquivo `Track.java` está em `src/Track.java`, sem declaração de pacote, e não foi encontrado um arquivo em `spotifyclone/model/Track.java`.

**Impacto:** se todos os fontes forem compilados como um único projeto, o import de `spotifyclone.model.Track` não será resolvido com a estrutura atual. Um arquivo em pacote nomeado também não pode importar uma classe do pacote padrão.

**Correção sugerida:** definir a estrutura de pacotes oficial e mover/ajustar os arquivos e imports de forma coordenada; evitar manter classes equivalentes em estruturas incompatíveis.

### 3. Ponto de entrada e versão mínima do Java precisam ser definidos

**Classificação:** risco.

`src/Main.java` usa a forma compacta de classe principal e `IO.println`, recursos que exigem uma versão recente do Java. Não foi identificada configuração de build que fixe e valide a versão mínima do JDK.

**Impacto:** ambientes com JDK mais antigo podem falhar ao compilar ou executar o ponto de entrada.

**Correção sugerida:** decidir e documentar a versão do JDK e configurar um build reproduzível (por exemplo, Maven ou Gradle) antes de adicionar testes automatizados.

### 4. Cobertura de testes insuficiente para atestar o funcionamento

**Classificação:** melhoria prioritária.

Não foi possível confirmar testes automatizados para catálogo, pesquisa, favoritos e playlists. O workflow atual não testa esses comportamentos.

**Correção sugerida:** depois de padronizar os pacotes e o build, criar testes para:
- busca por título/artista sem diferenciar maiúsculas e minúsculas;
- termo nulo/vazio e catálogo vazio;
- adicionar/remover alternar favoritos e impedir duplicatas;
- criar playlist com nome válido e rejeitar nome vazio;
- adicionar/remover faixas e buscar playlist por ID;
- duração formatada e igualdade de faixas por ID.

## Próximos passos recomendados

1. Corrigir a incompatibilidade de IDs em `PlaylistService` e `Playlist`.
2. Definir e aplicar uma única estrutura de pacotes Java, incluindo `SearchService` e `Track`.
3. Definir o JDK mínimo e adicionar configuração de build reproduzível.
4. Adicionar testes unitários para os serviços.
5. Atualizar o GitHub Actions para executar compilação e testes em cada pull request.
6. Reexecutar os testes e atualizar este relatório com comandos e resultados reais.

## Limitações e honestidade dos resultados

Este relatório não declara que a aplicação foi executada com sucesso. O resultado verde observado é apenas do workflow de governança/integridade. Os problemas de código foram apontados por inspeção dos arquivos do commit informado; a compilação real ainda deve ser executada após as correções.

---
Relatório preparado com assistência de monitor de programação, com foco em evidências verificáveis e sem alterar a branch `main`.
