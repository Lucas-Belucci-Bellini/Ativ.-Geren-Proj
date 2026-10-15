# Brief de Produto — Aplicativo de Música

**Status:** MVP aprovado para implementação em 2026-10-14; requisitos podem evoluir via PR  
**Nome de trabalho:** Sonora (pode ser refinado antes de uma publicação)  
**Tipo de produto:** aplicação web de descoberta e reprodução de música  
**Referência funcional:** [Spotify-clone-app-react](https://github.com/thiagosprestes/Spotify-clone-app-react)  
**Regra:** esta referência inspira a análise de funcionalidades; o código será desenvolvido de forma própria.

## 1. Visão

Criar um aplicativo musical com identidade visual própria, no qual uma pessoa possa descobrir faixas, pesquisar um catálogo, organizar favoritos e usar um player simples. A primeira entrega deve ser pequena, demonstrável e fácil de testar, sem depender de credenciais externas para abrir a interface.

A referência foi escolhida por demonstrar telas de início, busca, álbuns, artistas, playlists, músicas curtidas e histórico. A implementação será reavaliada com uma stack moderna, em vez de copiar a estrutura antiga.

## 2. Usuários e problema

**Público inicial:** avaliadores, desenvolvedores e pessoas testando uma demonstração de aplicativo musical.

**Problema a resolver:** demonstrar um fluxo consistente de descoberta, organização e reprodução de música em uma interface web responsiva.

## 3. Escopo proposto do MVP

### Funcionalidades incluídas

1. **Página inicial:** faixas, álbuns e playlists demonstrativos.
2. **Busca:** filtrar o catálogo local por título, artista e álbum.
3. **Biblioteca:** mostrar faixas favoritas e playlists demonstrativas.
4. **Detalhes:** visualizar informações de uma faixa, álbum ou artista.
5. **Player:** reproduzir/pausar, avançar/voltar faixa, mostrar duração e progresso e ajustar volume, desde que exista um arquivo de áudio autorizado.
6. **Favoritos:** adicionar/remover faixas e preservar a seleção localmente neste navegador.
7. **Interface responsiva:** desktop, tablet e celular.
8. **Estados claros:** carregamento, lista vazia, busca sem resultados e erro de reprodução.

### Fora do escopo inicial

- Conta própria, cadastro, login ou sincronização entre dispositivos.
- Backend, banco de dados e armazenamento remoto.
- Pagamentos, assinaturas e anúncios.
- Upload público de música.
- Recomendação algorítmica personalizada.
- Reprodução de músicas obtidas sem autorização.
- Integração com a API do Spotify antes de uma análise de conformidade e da aprovação de uma funcionalidade independente.
- Cópia de identidade visual, marca, logotipo, código ou assets proprietários do Spotify ou do repositório de referência.

## 4. Regras para catálogo e áudio

- Usar metadados fictícios, conteúdo criado pela equipe ou material cuja licença permita explicitamente o uso pretendido.
- Guardar a origem, licença e atribuição exigida de cada arquivo de áudio e imagem.
- Não presumir que um arquivo encontrado na internet pode ser redistribuído.
- Não tratar previews ou metadados de uma API como um catálogo musical independente.
- Não colocar tokens, secrets ou credenciais nos arquivos do frontend.

## 5. Requisitos não funcionais

- Navegação utilizável por teclado, foco visível e nomes acessíveis nos controles.
- Contraste legível e layout adaptável a diferentes larguras.
- Separação entre componentes de interface, dados demonstrativos e lógica do player.
- Tipos explícitos para faixas, artistas, álbuns e playlists.
- Erros de mídia tratados sem travar a interface.
- Testes para interações centrais e para os estados de erro relevantes.
- Documentação de instalação e execução sincronizada com a implementação real.

## 6. Critérios de aceitação do MVP

- A aplicação abre sem precisar de uma conta externa ou chave de API.
- O usuário consegue pesquisar as faixas do catálogo de demonstração.
- Favoritos podem ser adicionados/removidos e continuam disponíveis após atualizar a página no mesmo navegador.
- Os controles do player refletem o estado real da reprodução; se não houver mídia configurada, a interface informa a limitação em vez de fingir que está tocando.
- As telas essenciais funcionam em viewport de celular e desktop.
- Os estados vazios e erros têm mensagens compreensíveis.
- Os testes e o build definidos depois da aprovação da stack passam, ou as falhas e limitações são documentadas.

## 7. Privacidade e integração externa

A integração com o Spotify é uma decisão futura, não um requisito do MVP. A política de desenvolvedores do Spotify restringe aplicações que imitam ou tentam substituir a experiência principal do Spotify sem permissão escrita. Qualquer uso de sua plataforma precisa ser avaliado de acordo com os termos e a política oficiais.

Referências oficiais:
- [Spotify Developer Policy](https://developer.spotify.com/policy)
- [Spotify Developer Terms](https://developer.spotify.com/terms)
- [Authorization e OAuth](https://developer.spotify.com/documentation/web-api/concepts/authorization)
- [Authorization Code com PKCE](https://developer.spotify.com/documentation/web-api/tutorials/code-pkce-flow)
- [Quota modes](https://developer.spotify.com/documentation/web-api/concepts/quota-modes)

Na documentação consultada em 14/10/2026, o modo de desenvolvimento da API do Spotify está sujeito a limites específicos, incluindo até cinco usuários autorizados por app e requisito Premium para o proprietário do app. Esses limites devem ser conferidos novamente no momento de implementar uma integração.

## 8. Decisões futuras

- Refinar o nome e a identidade visual próprios antes de uma eventual publicação.
- Stack aprovada no ADR-0001; futuras mudanças estruturais exigem nova decisão.
- Ampliar o catálogo apenas quando a origem e as licenças de cada mídia forem registradas.
- Avaliar publicação e requisitos de produto depois da validação do MVP.
- Confirmar se existe um caso de uso independente que justifique avaliar uma integração com o Spotify.
