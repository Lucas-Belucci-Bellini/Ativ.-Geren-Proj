# ADR-0002 — Distribuição de uma aplicação instalável para Windows

- **Data:** 2026-10-14
- **Status:** **PROPOSTA — aguardando aprovação**
- **Relacionada a:** ADR-0001 e docs/deployment/INSTALLER_PLAN.md

## Contexto

O Sonora é uma aplicação web responsiva criada com React, TypeScript e Vite. O objetivo é reduzir a fricção para quem só quer usar o aplicativo: baixar um instalador e terminar a instalação sem seguir instruções técnicas.

A primeira plataforma considerada é Windows 10/11 x64. O frontend web continuará existindo; adicionar uma aplicação desktop não equivale a adicionar backend ou serviço de streaming.

## Critérios

- Reaproveitar o frontend atual.
- Fornecer um arquivo de instalação familiar para usuários Windows.
- Não exigir Node.js, Git, Rust ou terminal no computador do usuário.
- Permitir CI e releases reproduzíveis, rastreáveis ao commit.
- Evitar serviços e dependências desnecessários.
- Permitir assinatura e atualização segura numa fase posterior.

## Opções

### A. PWA hospedada por HTTPS

Vantagens: distribuição por link e pouca complexidade adicional; o navegador cuida da instalação em plataformas compatíveis.

Limitações: requer hospedagem HTTPS e depende da experiência de instalação do navegador; não fornece o EXE convencional como download primário.

### B. Tauri 2 com instalador NSIS

Vantagens: mantém React/Vite na interface, empacota o frontend como aplicativo desktop e permite gerar um instalador EXE para Windows.

Limitações: exige configuração do shell desktop, toolchain Rust no build e validação de WebView2. Assinatura e updater exigem configuração e gestão segura de chaves.

### C. Electron

Vantagens: ecossistema desktop amplo e maduro.

Limitações: tende a incluir mais runtime e configuração do que o MVP exige, sem benefício demonstrado que justifique escolhê-lo primeiro.

## Proposta

1. Manter React + TypeScript + Vite como frontend.
2. Empacotar com Tauri 2 e priorizar NSIS EXE para Windows x64.
3. Construir o instalador num runner Windows do GitHub Actions.
4. Distribuir versões estáveis por GitHub Releases com tags versionadas, notas e checksum SHA-256.
5. Avaliar assinatura de código antes de divulgação pública ampla; sem certificado, comunicar que o arquivo não é assinado e nunca mandar usuários desativarem proteções.
6. Deixar updater automático para fase posterior, depois de definir assinatura, chave pública, armazenamento seguro da chave privada, canais e rollback.
7. Não introduzir backend, banco de dados, conta ou integração Spotify apenas para tornar o app instalável.
8. Manter a PWA como alternativa futura de distribuição por link.

## Segurança e publicação

- Publicar apenas após CI e smoke tests de instalação aprovados.
- Não versionar certificados, tokens, senhas ou chaves.
- Não expor secrets a workflows de pull request não confiáveis.
- Testar numa máquina virtual limpa, incluindo instalação, início, upgrade e desinstalação.
- Tratar WebView2 e recursos de rede com fallback documentado.

## Consequências esperadas

Se aprovada, esta ADR autoriza a implementação do shell Tauri e do workflow Windows de acordo com docs/deployment/INSTALLER_PLAN.md. Não autoriza ainda publicar um instalador estável.

## Critérios de aprovação

- [ ] Confirmada a prioridade Windows x64.
- [ ] Aprovados Tauri 2 e NSIS EXE.
- [ ] Aceitos o escopo e os critérios de aceite.
- [ ] Acordadas as estratégias de WebView2 e de assinatura.

## Referências oficiais

- [Tauri — Windows installer](https://v2.tauri.app/distribute/windows-installer/)
- [Tauri — pipelines do GitHub](https://v2.tauri.app/distribute/pipelines/github/)
- [Tauri — assinatura Windows](https://v2.tauri.app/distribute/sign/windows/)
- [MDN — PWAs instaláveis](https://developer.mozilla.org/en-US/docs/Web/Progressive_web_apps/Guides/Making_PWAs_installable)
