# Compilação Java e importação Maven

Este projeto usa **Java 17** como versão oficial de desenvolvimento, compilação e execução. O Maven Compiler está configurado com `maven.compiler.release=17`. Para evitar diferenças entre as máquinas da equipe, configure o IntelliJ IDEA e o Maven para usar um JDK 17.

O Jackson Databind já está declarado no `pom.xml` como versão `2.18.2`; não adicione JARs manualmente nem crie uma segunda dependência.

## Instalação e configuração

1. Instale um JDK 17.
2. Configure o JDK 17 como SDK do projeto em **Project Structure** no IntelliJ IDEA.
3. Abra a raiz do repositório usando o `pom.xml` como projeto Maven. Não crie um módulo Java genérico.
4. Se o projeto já estiver aberto, abra a janela **Maven** e selecione **Reload All Maven Projects**.
5. Em **Settings → Build Tools → Maven → Runner**, selecione o JDK 17.
6. Na janela Maven, confirme que as dependências incluem `com.fasterxml.jackson.core:jackson-databind:2.18.2`.
7. Execute `mvn clean verify` na raiz do repositório.

## Linha de comando

Confirme que o terminal está usando Java 17:

```bash
java -version
mvn --batch-mode --no-transfer-progress clean verify
```

Se a linha de comando compilar e a IDE continuar acusando `package com.fasterxml.jackson.databind does not exist`, reimporte o `pom.xml` e recarregue os projetos Maven. Não altere a versão do Jackson para contornar um classpath desatualizado.

## Configuração compartilhada

Arquivos locais do IntelliJ (`.idea/` e `*.iml`) não devem ser versionados. Cada integrante deve gerar sua configuração a partir do POM, evitando SDKs, módulos e classpaths específicos de outra máquina.
