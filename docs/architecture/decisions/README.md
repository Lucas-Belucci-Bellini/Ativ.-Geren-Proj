# Decisões de Arquitetura

Este diretório reúne decisões técnicas relevantes do projeto por meio de Architecture Decision Records (ADRs).

## Estado atual

**Aplicativo acadêmico de terminal:** Java, conforme ADR-0001.  
**Frontend web:** PENDENTE (fora do escopo atual).  
**Backend web:** PENDENTE (fora do escopo atual).  
**Banco de dados/persistência:** PENDENTE.  
**Contratos de integração:** PENDENTE.

A implementação dos Cards 4 e 5 pode prosseguir dentro do escopo aprovado em ADR-0001. Mudanças transversais de arquitetura, como migração de pacotes, introdução de framework web ou configuração de banco, continuam exigindo decisão registrada antes da implementação.

## Decisões registradas

- [ADR-0001 — Aplicativo de terminal em Java](./ADR-0001-java-terminal.md): Java puro para o app de terminal, mantendo os fontes atuais no pacote padrão e adiando a decisão de build/testes externos.

## Próximas decisões possíveis

Se o grupo decidir ampliar o escopo, criar ADRs separados para:
- ferramenta de build e testes (Maven/Gradle e eventual JUnit);
- migração para pacotes nomeados;
- frontend/backend web e persistência, caso se tornem requisitos.
