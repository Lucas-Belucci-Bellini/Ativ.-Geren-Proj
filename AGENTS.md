# AGENTS.md — Regras obrigatórias para agentes de IA

Este arquivo define as regras de trabalho para qualquer agente de IA que contribua com este repositório.

## 1. Leia antes de agir

Antes de analisar, explicar, criar, editar ou executar código:

1. Leia este arquivo integralmente.
2. Leia o `README.md` e a documentação relevante.
3. Inspecione a estrutura do projeto e os arquivos relacionados à tarefa.
4. Verifique o estado do Git e preserve alterações existentes.
5. Leia instruções adicionais encontradas em subpastas e respeite as regras mais específicas.

Se não conseguir acessar um arquivo, ferramenta ou issue, informe a limitação. Nunca afirme que leu ou verificou algo que não acessou.

## 2. Documentação do código

Documente o código novo ou alterado de forma clara e proporcional à complexidade.

- Explique o propósito de módulos, classes, funções, métodos, componentes e estruturas importantes.
- Documente parâmetros, retornos, efeitos colaterais, exceções e restrições relevantes.
- Explique regras de negócio e decisões técnicas não óbvias.
- Para APIs públicas, siga as convenções de documentação da linguagem.
- Atualize README, exemplos e documentação técnica quando o comportamento ou a forma de uso mudar.
- Preserve o idioma e o estilo do projeto; se não houver padrão, use português claro.
- Prefira comentários que expliquem o “porquê”, não comentários que apenas repitam a linha de código.
- Evite comentários redundantes, incorretos ou inventados.
- Confira o comportamento real antes de corrigir documentação desatualizada.

Não é obrigatório comentar cada linha: a documentação deve melhorar a compreensão e a manutenção.

## 3. Detecção e classificação de problemas

Durante cada tarefa, examine os arquivos envolvidos e os fluxos diretamente afetados em busca de defeitos relevantes.

Classifique cada achado como:
- **Confirmado:** evidência suficiente de comportamento incorreto.
- **Suspeito:** há indícios, mas não foi possível confirmar.
- **Risco:** possibilidade fundamentada de falha.
- **Melhoria:** sugestão opcional que não é necessariamente um bug.

Quando possível e seguro, reproduza o problema, examine logs e dependências e execute testes. Não apresente hipóteses como fatos nem declare um bug apenas porque um trecho parece estranho.

## 4. Bugs e GitHub Issues

Quando confirmar um bug:

1. Pesquise Issues abertas e fechadas para evitar duplicatas.
2. Se já houver uma Issue correspondente, acrescente evidências úteis somente se tiver permissão.
3. Se não houver Issue equivalente, crie uma nova Issue se a ferramenta permitir.
4. Se não tiver permissão ou acesso para criar/atualizar a Issue, não finja que o fez: forneça um rascunho completo para publicação manual.
5. Informe no relatório final o número e o link da Issue, quando existirem.

### Modelo para Issue de bug

**Título:** [BUG] descrição curta e específica

**Resumo:** o que está errado e em qual contexto.

**Ambiente:** sistema operacional, versões, dependências e commit afetado, quando conhecidos.

**Passos para reproduzir:**
1. Primeiro passo.
2. Segundo passo.
3. Como observar o problema.

**Resultado atual:** comportamento observado.

**Resultado esperado:** comportamento correto esperado.

**Evidências:** logs, mensagens de erro, stack trace ou screenshots, sem credenciais ou dados pessoais.

**Impacto:** funcionalidades e usuários afetados; justifique a prioridade quando possível.

**Diagnóstico:** causa confirmada ou hipótese claramente identificada como tal.

**Testes realizados:** comandos executados e resultados reais.

**Solução sugerida:** inclua apenas quando houver fundamento.

Não invente versões, logs, causas, passos ou resultados. Marque informações desconhecidas como “não verificado” ou “não informado”. Não abra Issues para preferências pessoais como se fossem defeitos.

## 5. Correções e alterações

- Verifique se o problema já foi corrigido no estado atual do código.
- Faça alterações pequenas, focadas e coerentes com a arquitetura existente.
- Corrija apenas o necessário para a tarefa autorizada.
- Crie ou atualize testes de regressão quando aplicável.
- Considere segurança, compatibilidade, acessibilidade e desempenho quando pertinentes.
- Evite dependências desnecessárias e preserve funcionalidades existentes.
- Não apague arquivos, dados ou alterações do usuário sem justificativa e autorização.
- Não faça force-push, reescreva histórico ou exclua branches sem autorização explícita.
- Nunca exponha tokens, senhas, chaves de API, arquivos `.env` ou outros segredos em commits, Issues ou relatórios.
- Bugs não relacionados devem ser registrados separadamente; não misture automaticamente suas correções à tarefa atual.

## 6. Testes e verificação

Após alterar código, identifique e execute as verificações pertinentes, quando disponíveis:

- testes unitários e de integração;
- análise estática e lint;
- verificação de formatação;
- build ou compilação;
- testes de regressão.

Informe os comandos executados e os resultados. Não diga que um teste passou se não foi executado com sucesso. Se não puder executá-lo, explique por quê e o que permanece sem validação. Revise o diff em busca de alterações acidentais, segredos e documentação desatualizada.

## 7. Relatório obrigatório ao concluir

Toda tarefa deve terminar com um relatório contendo:

1. **Análise:** arquivos e funcionalidades examinados.
2. **Alterações:** arquivos modificados e justificativa.
3. **Documentação:** o que foi criado ou atualizado.
4. **Erros:** problemas confirmados, suspeitas, riscos ou nenhum problema identificado dentro do escopo analisado.
5. **Issues:** links das Issues criadas/atualizadas ou motivo da impossibilidade; inclua o rascunho se necessário.
6. **Testes:** comandos e resultados reais.
7. **Limitações:** verificações não realizadas e riscos restantes.
8. **Próximos passos:** ações pendentes, se houver.

Nunca afirme que o projeto inteiro está livre de erros com base em uma análise parcial.

## 8. Honestidade e limites de permissão

É proibido inventar resultados de execução, testes, revisões, commits, links, Issues ou validações. Se algo não foi verificado, diga isso claramente.

Estas regras não concedem permissões que a ferramenta não possui nem substituem políticas de segurança, controles do GitHub ou aprovação humana. Se uma ação exigir acesso ou autorização indisponível, informe a limitação e forneça o material necessário para o responsável concluir a tarefa.

**Regra final: leia as instruções antes de trabalhar, documente as alterações relevantes, verifique os erros, registre bugs confirmados em Issues quando houver permissão e seja transparente sobre tudo o que não conseguiu validar.**
