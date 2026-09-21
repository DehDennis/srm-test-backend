# REVIEW.md — Code Review Reverso (Anexo A)

**Pull Request:** `feature/settlement-endpoint`  
**Autor:** Dev / IA  
**Revisor:** Tech Lead  
**Status:** 🔴 **REPROVADO (Blocker)**

---

## Executive Summary
O código do Anexo A apresenta falhas críticas de segurança, concorrência, integridade transacional e precisão financeira. Ele não pode ser implantado em ambiente produtivo.

---

## Vulnerabilidades e Problemas por Severidade

### 1. 🔴 CRÍTICO: Vulnerabilidade de SQL Injection (OWASP Top 10)
- **Trecho:** `WHERE id = ${receivableId}` e `VALUES (${receivableId}, ...)`
- **Impacto em Produção:** Injeção direta de comandos SQL maliciosos, permitindo vazamento total de dados, manipulação não autorizada de saldo ou destruição do banco.
- **Correção:** Utilizar *Prepared Statements* / Queries parametrizadas (`WHERE id = :id` ou `JpaRepository`).

### 2. 🔴 CRÍTICO: Falha de Integridade Transacional (Violação do ACID)
- **Trecho:** `INSERT INTO settlements` e `UPDATE receivables` em chamadas isoladas com `try/catch` que engole exceções (`// se falhar aqui... segue o jogo`).
- **Impacto em Produção:** Inconsistência de dados. Se o `UPDATE` falhar, o extrato é inserido, mas o título continua com status `PENDING`, permitindo reliquidação e gerando prejuízo financeiro.
- **Correção:** Agrupar as operações em um bloco transacional atômico (`@Transactional` no Spring ou `BEGIN/COMMIT/ROLLBACK`).

### 3. 🔴 CRÍTICO: Ausência de Idempotência e Concorrência (Duplo Clique)
- **Trecho:** Não há chave de idempotência nem trava otimista/pessimista durante a busca e alteração do status.
- **Impacto em Produção:** Requisições simultâneas ou re-tentativas de rede duplicam os pagamentos ao cedente.
- **Correção:** Exigir o cabeçalho `X-Idempotency-Key` com restrição de chave única no banco e aplicar *Optimistic Locking* (`@Version`) no recebível.

### 4. 🔴 CRÍTICO: Erro de Domínio Financeiro / Taxa Destruída
- **Trecho:** `const BASE_RATE = 1.0;`
- **Impacto em Produção:** `1.0` equivale a **100% de taxa mensal** (em vez de 1,00% = `0.01`). O cálculo deságia quase a totalidade do valor de face do cliente.
- **Correção:** Tratar taxas como percentuais decimais (`0.0100`).

### 5. 🟡 ALTO: Perda de Precisão em Ponto Flutuante Binário
- **Trecho:** Uso de `number` do TypeScript, `Math.pow()` e `.toFixed(2)` prematuro.
- **Impacto em Produção:** Dízimas periódicas binárias geram erros acumulados de centavos em grandes volumes de transações.
- **Correção:** Utilizar `BigDecimal` no Java ou bibliotecas de precisão arbitrária (`bignumber.js` / `decimal.js`).
