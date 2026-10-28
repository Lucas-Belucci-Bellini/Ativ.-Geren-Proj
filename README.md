# Ativ.-Geren-Proj

Projeto acadêmico de catálogo musical para a matéria de Gerenciamento de Projetos / Metodologias Ágeis.

## O que existe no código

- **Catálogo:** lista inicial de músicas e operações para consultar, adicionar e remover faixas.
- **Pesquisa:** procura músicas pelo título ou artista, sem diferenciar maiúsculas de minúsculas.
- **Favoritos:** mantém faixas favoritas e impede duplicatas.
- **Playlists:** cria playlists, lista e busca por ID e permite adicionar/remover IDs de faixas.
- **Testes:** testes automatizados com JUnit 5 para as regras principais.

Os caminhos de áudio e capa no catálogo são exemplos. O projeto atual não implementa reprodução de áudio nem interface gráfica.

## Requisitos

- JDK 17
- Maven 3.9 ou compatível

## Como compilar e testar

Na raiz do repositório, execute:

```bash
mvn clean verify
```

O comando compila os fontes e executa os testes automatizados. Para executar o exemplo de entrada:

```bash
mvn -q -DskipTests package
java -cp target/classes Main
```

## Organização atual

- `src/`: classes Java do projeto.
- `tests/`: testes automatizados JUnit.
- `docs/`: relatórios e documentação técnica.
- `.github/workflows/java-tests.yml`: compilação e testes no GitHub Actions.

> **Nota:** os arquivos Java permanecem no pacote padrão por enquanto, para manter compatibilidade com a estrutura acadêmica existente. Uma migração para pacotes nomeados pode ser feita em uma alteração separada.

## Referências de estudo

- [Atividade base e issues](https://github.com/jp-albano/Ativ-Met-Ageis/issues/3)
- [Catálogo de estudos de Python](https://github.com/Lucas-Belucci-Bellini/Projetos-Futuros/tree/estudos/catalogo-programacao/estudos/conteudos/05-python)
- [Catálogo de estudos de Java](https://github.com/Lucas-Belucci-Bellini/Projetos-Futuros/tree/estudos/catalogo-programacao/estudos/conteudos/06-java)
- [Catálogo de estudos de JavaScript](https://github.com/Lucas-Belucci-Bellini/Projetos-Futuros/tree/estudos/catalogo-programacao/estudos/conteudos/01-javascript)
- [Catálogo de estudos de HTML](https://github.com/Lucas-Belucci-Bellini/Projetos-Futuros/tree/estudos/catalogo-programacao/estudos/conteudos/03-html)
- [Catálogo de estudos de CSS](https://github.com/Lucas-Belucci-Bellini/Projetos-Futuros/tree/estudos/catalogo-programacao/estudos/conteudos/04-css)
