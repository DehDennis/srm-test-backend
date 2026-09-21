# SPEC.md — Especificação Técnica e Alinhamento de Premissas

## 1. Mapeamento de Ambiguidades e Premissas Adotadas

O enunciado apresenta ambiguidades propositais típicas de sistemas financeiros em evolução. As premissas assumidas são:

| Ambuidade Identificada | Opção A | Opção B | Premissa Adotada (Justificativa) |
| :--- | :--- | :--- | :--- |
| **Unidade do Prazo** | Dias úteis (252) / Corridos (360) | Meses inteiros | **Meses inteiros**, conforme especificação dos *Golden Cases*. Fórmula: $VP = \frac{VF}{(1 + i_{base} + spread)^{prazo}}$. |
| **Representação da Taxa Base** | Taxa anual (a.a.) convertida | Taxa mensal (a.m.) absoluta | **Taxa mensal absoluta** (ex: 1,00% a.m. = `0.01`). O Anexo A errou ao usar `BASE_RATE = 1.0` (100%). |
| **Câmbio na Liquidação Cross-Currency** | Cotação no vencimento | Cotação no momento da operação | **Cotação no momento da execução da liquidação** (*snapshot* imutável gravado na transação). |
| **Momento do Arredondamento** | A cada etapa do cálculo | Apenas no resultado final | **Apenas no resultado final** (evita acúmulo de erro de arredondamento em operações intermediárias). |

---

## 2. Perguntas para o Negócio (Business Alignment)

Se este projeto estivesse sendo conduzido em ambiente de produção com a mesa de operações da SRM Asset, as seguintes definições seriam formalizadas:

1. **Janela de Cotação Cambial:** Qual é a tolerância máxima (SLA/Slippage) para variações de câmbio entre a simulação exibida ao operador e a efetivação da liquidação?
2. **Tratamento de Dias Não Úteis:** Caso o vencimento de uma duplicata caia em final de semana ou feriado, o prazo deve ser estendido até o próximo dia útil com ajuste de juros pró-rata dia?
3. **Limite de Alçada:** Existe valor máximo de face por lote de liquidação que exija dupla aprovação (*maker-checker*) antes da execução?

---

## 3. Decisões de Precisão Numérica

Para evitar falhas de precisão binária (ex: `0.1 + 0.2 = 0.30000000000000004` com `float`/`double`), o motor segue as diretrizes:

- **Aplicação (Java):** `java.math.BigDecimal` com `MathContext.DECIMAL128` para todos os cálculos intermediários.
- **Banco de Dados (PostgreSQL):** `NUMERIC(15, 4)` para valores monetários e `NUMERIC(10, 6)` para taxas de câmbio.
- **Política de Arredondamento:** *Banker's Rounding* (`RoundingMode.HALF_EVEN`) aplicado em 2 casas decimais estritamente no encerramento da precificação.

---

## 4. Critérios de Aceite (Quality Gates)

- **Usabilidade:** O operador simula qualquer recebível e obtém o valor presente em tempo real (< 200ms).
- **Segurança & ACID:** Nenhuma liquidação é registrada sem garantir atomicidade entre atualização de status e gravação do extrato imutável.
- **Idempotência:** A mesma requisição de liquidação reenviada (`X-Idempotency-Key`) não gera duplo crédito nem altera dados previamente consolidados.
