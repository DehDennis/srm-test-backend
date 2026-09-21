# AI_USAGE.md — Relatório de Engenharia da Colaboração com IA

## 1. Contexto e Filosofia de Uso
Alinhado à Seção 2 e Seção 7 da especificação da versão v2 do Desafio Técnico SRM Credit Engine, o uso de Inteligência Artificial Generativa (LLMs) foi adotado como ferramenta de aceleração e apoio de engenharia (*scaffolding* e produtividade), sob supervisão e governança técnica contínua de um Tech Lead.

---

## 2. Prompts Estratégicos e Escopo de Delegação

### O que FOI delegado à IA:
1. **Geração de Boilerplate e Scaffolding:** Estrutura inicial de controllers Spring Boot (`@RestController`), classes DTO de requisição/resposta, e repositórios JPA.
2. **Interface Visual em React:** Estruturação dos componentes visuais de simulação e tabela de transações usando estados do React (`useState`, `useEffect`).
3. **Mapeamento de Infraestrutura:** Criação inicial dos arquivos de orquestração `Dockerfile` e `docker-compose.yml`.

---

## 3. Caso Concreto de Falha da IA e Detecção por Engenharia

### Cenário: Cálculo de Juros Compostos e Conversão Cambial
- **Sugestão Inicial da IA:** Ao implementar a fórmula de Valor Presente na classe de estratégia de precificação, a IA sugeriu o uso de tipos primitivos `double` e da função padrão `Math.pow()` da linguagem Java.
- **Detecção do Erro:** Ao rodar a suíte de testes automatizados dos *Golden Cases* (`GoldenCasesTest.java`), os casos $C1$, $C2$ e $C3$ apresentaram divergência de **R$ 0,02** nos resultados devido a arredondamentos incorretos resultantes da representação em ponto flutuante binário.
- **Ação do Eng. / Tech Lead:** A sugestão da IA foi **rejeitada imediatamente**. O código foi refatorado manualmente para utilizar `java.math.BigDecimal` com `MathContext.DECIMAL128` para cálculos intermediários e `RoundingMode.HALF_EVEN` (*Banker's Rounding*) aplicado estritamente no encerramento da transação, garantindo a aprovação de 100% dos testes ao centavo.

---

## 4. Decisões Preservadas (Não Delegadas à IA)

As seguintes decisões estratégicas e arquiteturais **não foram delegadas** e refletem o julgamento direto do engenheiro responsável:

1. **Estratégia de Concorrência e Resiliência:** Decisão por utilizar *Optimistic Locking* (`@Version`) na entidade `Receivable` em vez de *Pessimistic Locking* pesado no banco de dados.
2. **Desenho da Idempotência:** Arquitetura do mecanismo de unicidade por cabeçalho `X-Idempotency-Key` atrelado à tabela imutável `settlements`.
3. **Análise de Causa Raiz de Incidente (`INCIDENT.md`):** Condução do post-mortem do Anexo B identificando a vulnerabilidade do bloco `catch` silencioso no código legado.
4. **Trade-offs de Arquitetura (`DECISIONS.md`):** Escolha fundamentada pela Arquitetura em Camadas Parametrizada em detrimento da Arquitetura Hexagonal, evitando a complexidade desnecessária (*over-engineering*) no escopo do teste.
