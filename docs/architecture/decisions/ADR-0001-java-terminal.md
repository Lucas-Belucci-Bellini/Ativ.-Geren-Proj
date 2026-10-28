# ADR-0001 — Aplicativo de terminal em Java

- **Status:** Aceita pelo responsável do projeto
- **Data:** 28/10/2026
- **Responsável pela decisão:** Lucas Belucci Bellini
- **Registro:** decisão confirmada em conversa com o responsável; assistência de monitor de programação.

## Contexto

O escopo atual da disciplina é um aplicativo de catálogo musical executado no terminal. O repositório já contém classes Java (Track, CatalogService, serviços de favoritos e playlists) e o responsável autorizou explicitamente continuar os Cards 4 e 5 com Java. A prioridade imediata é manter a solução simples, compilável, testável e adequada ao trabalho acadêmico.

## Decisão

Adotar **Java para a aplicação de terminal** neste escopo. Manter os fontes existentes no pacote padrão (src/*.java) durante esta entrega, evitando introduzir uma migração ampla de pacotes enquanto os serviços existentes ainda dependem dessa estrutura. A busca será implementada em serviço Java e conectada ao menu de terminal.

Para os testes imediatos, usar testes automatizados executáveis sem dependências externas, até que o projeto escolha e configure formalmente uma ferramenta de build/testes. Uma futura adoção de Maven/Gradle e JUnit poderá ser registrada em decisão separada.

## Alternativas consideradas

- **Java puro com interface de terminal:** escolhida; aproveita o código existente e evita dependências desnecessárias para o escopo acadêmico.
- **Java com Spring Boot:** não escolhida para esta entrega; acrescentaria estrutura de servidor web/API que não é necessária para um aplicativo de terminal.
- **Migrar agora todos os fontes para pacotes nomeados e estrutura Maven/Gradle:** adiada; é uma mudança transversal maior que os Cards 4 e 5 e pode afetar o trabalho dos demais integrantes.
- **Outra linguagem ou frontend web:** não escolhida para esta entrega; não atende melhor ao estado atual do código e exigiria reescrita.

## Consequências

- O menu e os serviços desta entrega devem permanecer em Java e ser executáveis localmente pelo terminal.
- A busca deve aceitar trecho de título ou artista, ignorar diferenças de maiúsculas/minúsculas e espaços externos, e não encerrar o aplicativo por uma entrada inválida.
- Os testes desta etapa devem ser reproduzíveis sem depender de serviços externos.
- A estrutura de pacotes e o sistema de build poderão ser revistos em ADR futuro, caso o grupo aprove a mudança.

## Fora do escopo desta decisão

Esta decisão **não** define uma stack de frontend web, backend web, banco de dados ou contratos de integração. Esses campos permanecem pendentes enquanto não fizerem parte do escopo aprovado.
