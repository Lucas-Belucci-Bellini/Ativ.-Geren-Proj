# Regras de Construção do Projeto

**Status:** proposta inicial em revisão  
**Responsável pela recomendação tecnológica:** Lucas Belucci Bellini  
**Regra central:** documentar e decidir a arquitetura antes de iniciar a implementação.

Este documento estabelece o processo que pessoas e agentes de IA devem seguir ao planejar, implementar, revisar e manter este projeto. Estas regras complementam o `AGENTS.md` na raiz; em caso de conflito, não ignore silenciosamente nenhuma regra: registre o conflito e peça decisão ao responsável pelo projeto.

## 1. Antes de escrever código

Não iniciar implementação de funcionalidades, criação de estruturas de aplicação ou instalação de grandes conjuntos de dependências antes de documentar e revisar o plano técnico correspondente.

Primeiro, produzir ou atualizar a documentação necessária para que outra pessoa consiga entender:
- qual problema o projeto resolve e para quem;
- escopo, objetivos, requisitos funcionais e não funcionais;
- arquitetura proposta e responsabilidades de cada componente;
- linguagem, framework e ferramentas escolhidos para frontend e backend;
- modelo de dados, persistência e migrações;
- contratos de APIs, eventos e comunicação entre componentes;
- autenticação, autorização, privacidade e modelo de ameaças;
- estratégia de testes, qualidade, observabilidade e tratamento de erros;
- implantação, configuração, segredos, backup e recuperação;
- etapas de implementação, critérios de aceitação, riscos e decisões pendentes.

A documentação inicial pode conter decisões marcadas como **PENDENTE**, mas deve declarar explicitamente o que ainda não foi decidido. Não apresentar uma proposta como se fosse uma decisão aprovada.

## 2. Decidir a stack antes de implementar

A escolha de linguagem e runtime para frontend e backend é uma decisão de arquitetura prioritária. Deve ser feita antes de criar a estrutura principal da aplicação.

A decisão precisa registrar:
1. opções consideradas e motivos;
2. responsabilidades de frontend, backend e serviços auxiliares;
3. compatibilidade com hospedagem, equipe, manutenção e dependências;
4. requisitos de segurança, desempenho, acessibilidade e distribuição;
5. custo de integração entre linguagens;
6. critérios objetivos para validar a escolha;
7. responsável, data, status e consequências da decisão.

Não adotar múltiplas linguagens só por preferência ou novidade. Cada linguagem adicional cria custos de manutenção, integração, testes, build, deploy e observabilidade. Só introduzir outra linguagem quando houver uma responsabilidade clara e benefícios justificáveis.

## 3. Recomendação tecnológica de Lucas Belucci Bellini

Lucas Belucci Bellini recomenda considerar a seguinte divisão de responsabilidades. É uma **recomendação inicial**, não uma decisão automática nem uma obrigação de usar todas as tecnologias neste projeto. A stack final deve ser avaliada para as necessidades reais deste repositório e registrada em uma decisão arquitetural aprovada.

### 3.1 TypeScript

Preferido para interfaces web, orquestração no cliente, clientes de API tipados, comportamento no navegador e integração de aplicação adequada. TypeScript no frontend não deve ser considerado a única barreira de autorização.

### 3.2 Rust

Candidato para runtime local, componentes sensíveis a CPU/memória, integração segura com o sistema e serviços nativos bem definidos. Usar somente quando propriedade de memória, empacotamento, manutenção e benefícios mensuráveis justificarem a fronteira adicional. Definir contratos de FFI/IPC antes de cruzar linguagens.

### 3.3 Python

Candidato para integração de IA, adaptadores de modelos/provedores, processamento de dados, prototipagem e workers de automação. Fixar versões de dependências e isolar workers. Código Python que manipula ferramentas ou arquivos deve respeitar os mesmos contratos de autorização e auditoria.

### 3.4 PostgreSQL

Preferido como fonte durável de verdade para dados estruturados e persistentes. Alterações de schema exigem migrações, restrições, propriedade explícita, isolamento por usuário/tenant quando aplicável, estratégia de backup/restauração e testes de integração. Fallbacks em LocalStorage ou memória não podem se passar por gravações duráveis.

### 3.5 Origem da recomendação

Esta orientação é baseada nas diretrizes arquiteturais que Lucas utiliza no repositório **Cosmos**:  
https://github.com/Lucas-Belucci-Bellini/Cosmos

A referência ao Cosmos serve como origem da recomendação e ponto de comparação. Ela **não** significa que a arquitetura do Cosmos deva ser copiada integralmente: este projeto precisa de avaliação própria, proporcional ao seu escopo.

## 4. Registros de decisão arquitetural (ADR)

Toda decisão estrutural relevante deve ter um registro em `docs/architecture/decisions/` usando, quando possível, nomes sequenciais, por exemplo `ADR-0001-stack-frontend-backend.md`.

Cada ADR deve incluir:
- título e data;
- status: proposta, aprovada, rejeitada ou substituída;
- contexto e problema;
- opções avaliadas;
- decisão e justificativa;
- vantagens, desvantagens e riscos;
- impactos sobre segurança, testes, manutenção e implantação;
- plano de validação e critérios para reconsiderar a decisão.

Não alterar silenciosamente uma decisão aprovada. Criar ou atualizar um ADR explicando a mudança e os efeitos nos documentos e no código.

## 5. Contratos antes da integração

Antes de conectar frontend, backend, banco de dados ou serviços de IA, documentar os contratos relevantes:
- formatos de entrada e saída;
- validação e erros;
- autenticação e autorização;
- timeouts, retries e idempotência quando necessários;
- limites de tamanho e taxa;
- versionamento e compatibilidade;
- logs e trilhas de auditoria sem expor segredos.

Para comunicação entre linguagens, definir primeiro a fronteira (API, IPC, FFI, fila ou outro mecanismo), seu esquema e sua estratégia de testes.

## 6. Documentação durante a implementação

- Manter README, diagramas, especificações de API, exemplos de configuração e decisões sincronizados com o comportamento real.
- Documentar APIs públicas, contratos, configurações não óbvias, invariantes e decisões difíceis.
- Comentários devem explicar intenção, restrições ou motivos, e não apenas repetir o código.
- Cada alteração relevante deve dizer qual documentação foi atualizada; se nenhuma foi necessária, explicar brevemente por quê.
- Não afirmar que uma funcionalidade existe se houver apenas um plano ou mock.

## 7. Regras para pessoas e agentes de IA

- Ler `AGENTS.md` e a documentação aplicável antes de alterar arquivos.
- Inspecionar o estado atual do repositório e preservar alterações alheias.
- Não inventar requisitos, resultados de testes, arquivos, permissões ou decisões.
- Não adicionar dependências, mudar stack, criar migrações destrutivas ou mudar contratos públicos sem justificar e registrar a decisão.
- Não inserir segredos, tokens, credenciais ou dados pessoais nos arquivos, testes ou logs.
- Não contornar autenticação, autorização, validações ou testes para fazer uma implementação parecer concluída.
- Manter mudanças pequenas e relacionadas à tarefa; separar refatorações e correções independentes.
- Não fazer push forçado, apagar dados ou executar operações destrutivas sem autorização explícita.
- Relatar com honestidade o que foi feito, o que foi testado, o que falhou e o que não foi possível validar.

## 8. Testes e critérios de conclusão

Uma tarefa só pode ser marcada como concluída quando:
- os critérios de aceitação documentados forem atendidos;
- os testes relevantes tiverem sido executados, ou as limitações estiverem explicitadas;
- a documentação refletir a implementação;
- riscos e pendências conhecidos estiverem registrados;
- a revisão não identificar regressões conhecidas.

Nunca afirmar que todo o projeto está livre de erros com base em testes parciais.

## 9. Fluxo de trabalho recomendado

1. Entender requisitos e inspecionar o repositório.
2. Documentar escopo e requisitos.
3. Propor arquitetura e comparar linguagens/frameworks.
4. Aprovar e registrar a stack frontend/backend.
5. Definir contratos, dados, segurança e estratégia de testes.
6. Dividir o plano em tarefas verificáveis.
7. Só então implementar em pequenos passos.
8. Testar e atualizar a documentação a cada etapa.
9. Relatar resultados e abrir Issues para problemas confirmados ou pendências relevantes.
10. Solicitar revisão antes de integrar à branch principal.

## 10. Estado atual

Este arquivo inaugura o processo de governança. Ele não afirma que a stack do projeto já foi escolhida nem que a implementação pode começar. A primeira entrega de planejamento deve avaliar o contexto real do repositório e produzir a decisão formal de frontend/backend antes de começar a codificação.


## 11. Verificação automatizada e integração segura

- Toda mudança enviada ao GitHub deve passar pelo workflow `.github/workflows/repository-guard.yml` quando acionado pelos eventos configurados.
- O workflow atual valida a presença da documentação essencial, procura marcadores de conflito Git nos arquivos de texto rastreados e verifica whitespace do diff em pull requests.
- Toda contribuição por pull request deve preencher `.github/pull_request_template.md` com objetivo, documentação, verificações, evidências e riscos.
- Falhas de CI devem ser corrigidas na causa; não desabilitar checks nem mascarar falhas para obter um status verde.
- A CI atual é apenas uma primeira barreira neutra em relação à linguagem. Ela não substitui testes unitários, integração, análise estática, build ou revisão humana.
- Após aprovar a stack em ADR, registrar e implementar os gates específicos da stack (formatador, lint, testes, build e verificações adicionais justificadas).
- Para tornar o gate obrigatório para merges, o responsável deve configurar a proteção da branch-base no GitHub e marcar o check **Governance + repository integrity** como obrigatório. A criação do workflow não configura essa proteção automaticamente.
- O desenho toma como referência os princípios observados nos workflows de [rea](https://github.com/morluto/rea/tree/main/.github/workflows), [NEXORA](https://github.com/Lucas-Belucci-Bellini/NEXORA/blob/main/.github/workflows/ci.yml) e [Project-Vanguard](https://github.com/Lucas-Belucci-Bellini/Project-Vanguard/blob/main/.github/workflows/ci.yml); os comandos específicos desses repositórios não devem ser copiados sem verificar compatibilidade com este projeto.
