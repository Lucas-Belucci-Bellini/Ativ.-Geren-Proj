# Compilação Java e importação Maven

O backend permanece em Java 17 e usa Maven para resolver dependências. O Jackson Databind já está declarado no `pom.xml` como versão `2.18.2`; não adicione JARs manualmente nem crie uma segunda dependência.

## IntelliJ IDEA

1. Abra a raiz do repositório usando o `pom.xml` como projeto Maven. Não crie um módulo Java genérico.
2. Se o projeto já estiver aberto, abra a janela **Maven** e selecione **Reload All Maven Projects**.
3. Em **Project Structure**, configure o **Project SDK** para JDK 17.
4. Em **Settings → Build Tools → Maven → Runner**, configure o JRE para JDK 17.
5. Na janela Maven, confirme que as dependências do projeto incluem `com.fasterxml.jackson.core:jackson-databind:2.18.2`.
6. Execute `mvn clean verify` na raiz do repositório.

## Linha de comando

```bash
mvn --batch-mode --no-transfer-progress clean verify
```

Se a linha de comando compilar e a IDE continuar acusando `package com.fasterxml.jackson.databind does not exist`, o projeto provavelmente ainda não foi recarregado como Maven. Reimporte o `pom.xml`; não altere a versão do Jackson para contornar um classpath desatualizado.

## Configuração compartilhada

Arquivos locais do IntelliJ (`.idea/` e `*.iml`) não devem ser versionados. Cada integrante deve gerar sua configuração a partir do POM, evitando SDKs, módulos e classpaths específicos de outra máquina.
