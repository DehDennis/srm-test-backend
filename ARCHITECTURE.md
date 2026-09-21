# ARCHITECTURE.md — Arquitetura de Alta Escala & Event-Driven Architecture (EDA)

## 1. Visão de Alta Escala (1 Milhão de Transações / Minuto)

Para suportar volumetrias extremas com consistência financeira:

```text
[ API Gateway / Rate Limiter ]
              │
              ▼
    [ Redis Cluster ] ── (Verificação de Idempotência em Borda)
              │
              ▼
     [ Kafka Cluster ] ── (Tópico: settlement-requests)
              │
      ┌───────┴───────┐
      ▼               ▼
[ Worker 1 ]     [ Worker 2 ] ── (Consumidores com Sharding por Cedente)
      │               │
      └───────┬───────┘
              ▼
   [ PostgreSQL Cluster ] ── (Read/Write Splitting & Partitioning)


---

### 5. `AI_USAGE.md`

```markdown
# AI_USAGE.md — Relatório de Engenharia da Colaboração com IA

## 1. Escopo de Delegação à IA

A Inteligência Artificial foi utilizada como ferramenta de produtividade para geração de código repetitivo (*boilerplate* de DTOs, controllers REST e componentes visuais do React).

---

## 2. Caso Concreto de Detecção de Falha da IA

- **Cenário:** Durante a construção inicial da estratégia de cálculo, a IA gerou a fórmula utilizando a biblioteca padrão `Math.pow()` com variáveis do tipo `double`.
- **Falha Detectada:** Ao executar o teste automatizado dos *Golden Cases*, a precisão numérica divergiu nos centavos devido a dízimas de ponto flutuante em `double`.
- **Ação de Engenharia:** A sugestão da IA foi rejeitada e substituída pela implementação nativa utilizando `java.math.BigDecimal` com `MathContext.DECIMAL128` e `RoundingMode.HALF_EVEN`, garantindo 100% de precisão ao centavo nos casos $C1$, $C2$ e $C3$.

---

## 3. Decisões Preservadas (Não Delegadas)

A definição das estratégias de concorrência (*Optimistic Locking*), o design da arquitetura idempotente e a análise de causa raiz do incidente do Anexo B foram conduzidos exclusivamente com critérios de engenharia de software humana.
