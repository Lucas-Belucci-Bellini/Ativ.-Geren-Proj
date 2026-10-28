# Ativ.-Geren-Proj

Projeto acadêmico de catálogo musical para a matéria de Gerenciamento de Projetos / Metodologias Ágeis.

## O que existe no código

- **Catálogo:** lista inicial de músicas e operações para consultar, adicionar e remover faixas.
- **Pesquisa local:** procura músicas pelo título ou artista, sem diferenciar maiúsculas de minúsculas.
- **Descoberta externa:** menu de terminal para buscar faixas no SoundCloud, visualizar URLs de capa/faixa e verificar título, artista e duração.
- **Favoritos:** mantém faixas favoritas e impede duplicatas.
- **Playlists:** cria playlists, lista e busca por ID e permite adicionar/remover IDs de faixas.
- **Testes:** testes automatizados com JUnit 5 para as regras principais.

O catálogo Java continua disponível pelo terminal. A interface web em Next.js/React está sendo construída na pasta `frontend/`; ela oferece pesquisa visual e conferência de metadados, mas ainda não implementa reprodução de áudio.

## Requisitos

- JDK 17
- Maven 3.9 ou compatível

## Como compilar e testar

Na raiz do repositório, execute:

```bash
mvn clean verify
```

O comando compila os fontes e executa os testes automatizados. Para iniciar o menu de terminal (busca externa requer token do SoundCloud):

```bash
mvn -q org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=Main
```

## Organização atual

- `src/`: classes Java do projeto.
- `tests/`: testes automatizados JUnit.
- `docs/`: relatórios e documentação técnica.
- `frontend/`: aplicação web Next.js + React com rotas de servidor para as integrações musicais.
- `.github/workflows/frontend.yml`: build automatizado da interface web.
- `.github/workflows/java-tests.yml`: compilação e testes no GitHub Actions.

> **Nota:** os arquivos Java permanecem no pacote padrão por enquanto, para manter compatibilidade com a estrutura acadêmica existente. Uma migração para pacotes nomeados pode ser feita em uma alteração separada.

## Referências de estudo

- [Atividade base e issues](https://github.com/jp-albano/Ativ-Met-Ageis/issues/3)
- [Catálogo de estudos de Python](https://github.com/Lucas-Belucci-Bellini/Projetos-Futuros/tree/estudos/catalogo-programacao/estudos/conteudos/05-python)
- [Catálogo de estudos de Java](https://github.com/Lucas-Belucci-Bellini/Projetos-Futuros/tree/estudos/catalogo-programacao/estudos/conteudos/06-java)
- [Catálogo de estudos de JavaScript](https://github.com/Lucas-Belucci-Bellini/Projetos-Futuros/tree/estudos/catalogo-programacao/estudos/conteudos/01-javascript)
- [Catálogo de estudos de HTML](https://github.com/Lucas-Belucci-Bellini/Projetos-Futuros/tree/estudos/catalogo-programacao/estudos/conteudos/03-html)
- [Catálogo de estudos de CSS](https://github.com/Lucas-Belucci-Bellini/Projetos-Futuros/tree/estudos/catalogo-programacao/estudos/conteudos/04-css)

## Integrações musicais (em desenvolvimento)

- **SoundCloud:** `SoundCloudSearchService` pesquisa faixas na API oficial e retorna título, usuário/artista, URL da faixa, capa e duração. Configure `SOUNDCLOUD_ACCESS_TOKEN` no ambiente; nunca grave o token no código ou no Git.
- **Letras e conferência:** `LyricsVerificationService` consulta LRCLIB e compara título, artista e duração. A resposta informa se a correspondência é forte ou aproximada; não se deve tratar resultados aproximados como confirmação.
- **Créditos independentes:** `MusicMetadataVerificationService` consulta MusicBrainz para comparar título e créditos de artista.
- **Fachada unificada Java:** `MusicDiscoveryService` combina busca e verificação para a interface de terminal.
- **Interface web:** veja `frontend/README.md` para executar a aplicação Next.js + React e configurar o token no servidor.
- **Configuração:** veja `.env.example` para o nome da variável de ambiente. O exemplo não contém credenciais reais.

As APIs dependem de conexão, disponibilidade, limites e termos de cada provedor. Capas e URLs são metadados de origem; o projeto não deve baixar/republicar imagens nem contornar controles de acesso. O uso da API não garante que toda faixa, letra ou capa esteja disponível. O verificador auxilia a conferência, mas não certifica direitos autorais.
