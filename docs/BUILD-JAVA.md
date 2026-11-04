# Compilação Java e importação Maven

O backend mantém **compatibilidade de bytecode/API com Java 17** por meio de `maven.compiler.release=17`. O JDK usado para executar Maven/IntelliJ pode ser o 17 ou o 24; usar JDK 24 não exige alterar o alvo de compilação. O Jackson Databind já está declarado no `pom.xml` como versão `2.18.2`; não adicione JARs manualmente nem crie uma segunda dependência.

## IntelliJ IDEA

1. Abra a raiz do repositório usando o `pom.xml` como projeto Maven. Não crie um módulo Java genérico.
2. Se o projeto já estiver aberto, abra a janela **Maven** e selecione **Reload All Maven Projects**.
3. Em **Project Structure**, selecione um JDK instalado e suportado no computador (JDK 24 no ambiente do Lucas; JDK 17 também é compatível).
4. Em **Settings → Build Tools → Maven → Runner**, selecione o mesmo JDK instalado. O alvo de compilação continua sendo Java 17 por causa do `maven.compiler.release`.
5. Na janela Maven, confirme que as dependências do projeto incluem `com.fasterxml.jackson.core:jackson-databind:2.18.2`.
6. Execute `mvn clean verify` na raiz do repositório.

## Linha de comando

```bash
mvn --batch-mode --no-transfer-progress clean verify
```

Se a linha de comando compilar com JDK 17 ou JDK 24 e a IDE continuar acusando `package com.fasterxml.jackson.databind does not exist`, o projeto provavelmente ainda não foi recarregado como Maven. Reimporte o `pom.xml`; não altere a versão do Jackson para contornar um classpath desatualizado.

## Configuração compartilhada

Arquivos locais do IntelliJ (`.idea/` e `*.iml`) não devem ser versionados. Cada integrante deve gerar sua configuração a partir do POM, evitando SDKs, módulos e classpaths específicos de outra máquina.
