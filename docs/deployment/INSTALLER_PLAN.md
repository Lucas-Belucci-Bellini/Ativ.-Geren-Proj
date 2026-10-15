# Plano de construção e distribuição do instalável Sonora

**Status:** plano proposto; não existe instalador publicado ainda  
**Produto:** Sonora — aplicativo musical demonstrativo  
**Alvo inicial:** Windows 10/11 x64

## 1. Objetivo

A pessoa deve conseguir baixar um arquivo, abri-lo, clicar em **Instalar** e usar o Sonora sem precisar instalar Node.js, Git, Rust ou aprender comandos de terminal. Este plano prioriza um instalador Windows convencional e deixa a opção de instalação pelo navegador como alternativa.

## 2. Experiência desejada

1. Abrir a página de Releases do GitHub ou uma futura página oficial de downloads.
2. Baixar um único instalador estável, com nome claro e versão visível.
3. Abrir o instalador e seguir um assistente curto.
4. Abrir o app pelo menu Iniciar ou por um atalho escolhido na instalação.
5. Receber instruções claras quando uma atualização estiver disponível, sem precisar consultar um tutorial técnico.

A página de download deve destacar um único arquivo recomendado. Builds de teste, logs, checksums e opções para desenvolvedores ficam separados da instrução principal.

## 3. Estratégia recomendada

**Prioridade 1: instalador Windows EXE usando Tauri 2 e NSIS.**

O Sonora já tem frontend React, TypeScript e Vite. O Tauri empacota os arquivos construídos pelo Vite como aplicação desktop e pode produzir um instalador Windows. A máquina do usuário final não deve precisar de ferramentas de desenvolvimento.

Referências oficiais:
- [Tauri — Windows installer](https://v2.tauri.app/distribute/windows-installer/)
- [Tauri — GitHub Actions e releases](https://v2.tauri.app/distribute/pipelines/github/)
- [Tauri — assinatura de código Windows](https://v2.tauri.app/distribute/sign/windows/)

### Alternativas avaliadas

| Opção | Vantagem | Limitação | Proposta |
|---|---|---|---|
| PWA | Distribuição por URL e instalação mediada pelo navegador | Depende de HTTPS e da experiência de instalação suportada pelo navegador; não oferece o EXE convencional como download primário | Alternativa complementar |
| Tauri 2 + NSIS | Reaproveita o frontend e oferece instalador EXE para Windows | Exige shell desktop, toolchain Rust no build e validação do WebView2 | Preferida para o instalador Windows |
| Electron | Ecossistema desktop amplo | Adiciona runtime e configuração desktop que ainda não se justificam para o MVP | Não escolher sem necessidade concreta |

A PWA poderá ser adicionada separadamente para quem prefere abrir um link e instalar pelo navegador. Isso não deve atrasar o instalador Windows.

## 4. Escopo do primeiro instalador

Incluído:
- Shell desktop Tauri 2 dentro da pasta music-app.
- Instalador Windows x64 no formato NSIS EXE.
- Nome, versão, identificador e ícone originais.
- Atalho no menu Iniciar e opção clara para atalho na área de trabalho.
- Instalação, inicialização, upgrade e desinstalação testados.
- Catálogo fictício e som demonstrativo sintetizado; sem login, API key ou streaming comercial.
- GitHub Release com número da versão, notas e checksum SHA-256.
- CI com typecheck, testes, build web e build desktop.
- Instrução curta para usuário final: “Baixe o instalador, abra-o e siga as etapas exibidas”.

Não incluído inicialmente:
- Instaladores Linux/macOS.
- Microsoft Store.
- Contas, backend, banco remoto ou sincronização.
- Integração com API do Spotify.
- Atualização automática silenciosa antes de assinatura e testes.
- Serviço residente, início automático com Windows ou telemetria desnecessária.

## 5. Fases de implementação

### Fase 0 — Preparação reproduzível

- [ ] Aprovar ADR-0002.
- [ ] Criar e revisar package-lock.json.
- [ ] Trocar o CI de npm install para npm ci.
- [ ] Resolver as fontes importadas do Google Fonts: usar fontes de sistema ou empacotar fontes licenciadas para que a tela inicial não dependa de rede.
- [ ] Preparar ícones nos tamanhos e formatos necessários.
- [ ] Confirmar versões mínimas de Windows e a estratégia de WebView2.

**Saída:** dependências reproduzíveis e recursos necessários incluídos no app.

### Fase 1 — Empacotamento local

- [ ] Adicionar Tauri 2 dentro de music-app sem substituir o frontend atual.
- [ ] Configurar o caminho dos arquivos gerados pelo Vite.
- [ ] Configurar nome, identificador, versão, ícones e alvo NSIS.
- [ ] Não adicionar backend Rust de negócio sem requisito independente.
- [ ] Construir o pacote em ambiente Windows.
- [ ] Confirmar que o app abre sem servidor Vite de desenvolvimento.

**Saída:** instalador de teste local, sem anúncio de release pública.

### Fase 2 — CI de desktop

- [ ] Criar workflow em runner Windows.
- [ ] Executar npm ci, instalar a toolchain Rust e os pré-requisitos Tauri documentados.
- [ ] Executar typecheck, testes, build web e tauri build.
- [ ] Em pull requests, armazenar o instalador como artefato de teste, sem publicar release.
- [ ] Manter permissões mínimas e não expor secrets a código de pull requests não confiáveis.

**Saída:** instalador de teste reproduzível e vinculado ao commit.

### Fase 3 — Testes de instalação

- [ ] Testar numa máquina virtual Windows limpa, sem Node.js, Git, npm ou Rust.
- [ ] Testar primeiro início com e sem conexão à internet.
- [ ] Validar busca, favoritos, som sintetizado, controles e armazenamento local.
- [ ] Testar atualização manual sobre uma versão anterior e desinstalação.
- [ ] Documentar se os dados locais permanecem ou são removidos após desinstalação.
- [ ] Validar mensagens para falhas de instalação e WebView2 ausente.
- [ ] Garantir que a página inicial funciona sem buscar fontes ou recursos remotos.

**Saída:** smoke tests documentados e bugs críticos corrigidos.

### Fase 4 — Releases para usuários

- [ ] Usar tags versionadas, por exemplo v0.1.0.
- [ ] Publicar uma GitHub Release somente após CI e smoke tests aprovados.
- [ ] Anexar instalador, notas da versão e checksum SHA-256.
- [ ] Identificar builds de pull request e pré-lançamentos como testes.
- [ ] Atualizar o README para apontar a seção “Baixar para Windows” à página de Releases.
- [ ] Criar uma página curta para problemas comuns, sem exigir comandos para instalação normal.

**Saída:** caminho de download simples e confiável.

### Fase 5 — Assinatura e atualização

- [ ] Avaliar certificado de assinatura de código Windows antes da divulgação pública ampla.
- [ ] Se existir certificado, guardar certificado e senha apenas em GitHub Actions Secrets ou armazenamento seguro equivalente.
- [ ] Nunca versionar certificados privados, senhas, tokens ou chaves.
- [ ] Explicar honestamente que um EXE não assinado pode provocar avisos do SmartScreen; nunca orientar o usuário a desativar proteções.
- [ ] Implementar updater automático só depois de definir assinatura de artefatos, chave pública, armazenamento seguro da chave privada, canais e rollback.
- [ ] Validar que atualizações aceitas têm metadados e assinatura válidos.

**Saída:** atualizações seguras ou, enquanto não houver assinatura, releases manuais com limitações comunicadas claramente.

## 6. Pipeline de release desejado

1. Um commit altera o app ou a configuração desktop.
2. CI executa typecheck, testes, build web e empacotamento Windows.
3. Pull requests produzem apenas artefatos de teste.
4. Uma tag de release, após revisão e gates aprovados, aciona o empacotamento de versão.
5. O workflow calcula checksum e publica as notas e o instalador.
6. O README aponta à última versão estável.

A publicação não pode acontecer em qualquer push. O workflow de release deve ser restrito a tags/eventos controlados, com permissões de escrita limitadas ao necessário.

## 7. WebView2 e funcionamento offline

O Tauri usa WebView2 no Windows. A implementação deve seguir as opções oficiais de instalação e decidir como tratar uma máquina sem runtime. Não presumir que todos os computadores possuem o mesmo ambiente.

A demonstração deve funcionar sem conexão para os recursos centrais. Componentes externos opcionais precisam de fallback e não podem impedir que a aplicação abra.

## 8. Segurança e confiança

- Publicar apenas artefatos gerados pelo workflow de release esperado.
- Revisar permissões dos workflows e actions de terceiros.
- Criar checksum SHA-256 para detectar downloads corrompidos; esclarecer que checksum, sozinho, não prova autoria.
- Assinar o instalador quando possível e testar a assinatura.
- Nunca pedir que o usuário desative antivírus, SmartScreen ou outras proteções.
- Não incluir secrets, arquivos .env, tokens ou dados pessoais no pacote.
- Exibir versão do aplicativo e manter changelog.
- Não instalar serviços ou inicializações automáticas que não estejam documentados.

## 9. Critérios de aceite

- [ ] Usuário instala numa máquina Windows limpa sem ferramentas de desenvolvimento.
- [ ] Nome, ícone, versão e publicador são coerentes com o release.
- [ ] O atalho abre a aplicação sem terminal ou servidor de desenvolvimento.
- [ ] Início, busca, favoritos, áudio demonstrativo, navegação e fechamento funcionam.
- [ ] A estratégia de WebView2 está validada e documentada.
- [ ] Instalação, upgrade e desinstalação foram testados.
- [ ] A Release contém versão, notas, checksum e instalador.
- [ ] CI está verde e os smoke tests estão registrados.
- [ ] A assinatura do pacote é informada com honestidade.
- [ ] Todo desenvolvimento ocorre em branch e PR; a main não recebe alterações diretas.

## 10. Referências oficiais

- [Tauri — Windows installer](https://v2.tauri.app/distribute/windows-installer/)
- [Tauri — GitHub Actions e releases](https://v2.tauri.app/distribute/pipelines/github/)
- [Tauri — assinatura no Windows](https://v2.tauri.app/distribute/sign/windows/)
- [Tauri — opções de distribuição](https://v2.tauri.app/distribute/)
- [MDN — tornar uma PWA instalável](https://developer.mozilla.org/en-US/docs/Web/Progressive_web_apps/Guides/Making_PWAs_installable)

## Estado atual

Este documento é um plano, não uma entrega. O frontend web existe; shell Tauri, workflow do instalador, instalador publicado, certificado de assinatura e atualização automática ainda não existem. O próximo passo é revisar e aprovar o ADR-0002.
