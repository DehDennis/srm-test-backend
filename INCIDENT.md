# INCIDENT.md — Post-Mortem de Incidente de Liquidação Duplicada

**Data do Incidente:** Sexta-feira, 18h40  
**Gravidade:** SEV-1 (Perda Financeira Operacional)  
**Sistemas Afetados:** Motor de Liquidação / Mesa de Operações  
**Status:** Resolvido (Contenção & Correção Definitiva Aplicadas)

---

## 1. Linha do Tempo

- **18:40** — A mesa de operações relata que três cedentes receberam créditos duplicados relativos à mesma operação de liquidação.
- **18:45** — O time de engenharia confirma a duplicidade de transações no banco de dados.
- **18:50** — **Contenção Imediata:** Ativação da *Feature Flag* `DISABLE_SETTLEMENT_ENDPOINT` para interromper novas liquidações no ambiente.
- **19:15** — Identificação do código do Anexo A em produção, demonstrando falta de transacionalidade e ausência de chave de idempotência.
- **20:30** — Geração do relatório com os valores pagos em duplicidade para alinhamento com a mesa de operações (estorno/compensação).
- **22:00** — Deploy do *hotfix* com suporte a `@Transactional` e `X-Idempotency-Key`.

---

## 2. Causa Raiz

A requisição enviada pelo frontend/operador sofreu um *timeout* de rede no cliente, disparando uma retentativa automática do navegador (retry). Como o endpoint do Anexo A não possuía validação de idempotência nem controle transacional:
1. A primeira requisição inseriu o extrato em `settlements`.
2. A atualização da tabela `receivables` falhou momentaneamente por *lock* de linha.
3. O bloco `catch` engoliu o erro sem realizar o *rollback*.
4. A segunda requisição processou novamente o recebível que permaneceu como `PENDING`.

---

## 3. Plano de Ação & Prevenção Sistêmica

| Ação | Tipo | Responsável | Prazo |
| :--- | :--- | :--- | :--- |
| Trava de Idempotência no Gateway/API via `X-Idempotency-Key` | Correção Definitiva | Engenharia Backend | Concluído |
| Implementação de *Optimistic Locking* (`@Version`) nas entidades | Prevenção | Engenharia Backend | Concluído |
| Atualização da pipeline CI/CD com linter bloqueando queries SQL concatenadas | Governança | DevOps / SRE | 3 dias |
| Implementação de Testes de Carga/Concorrência automatizados no pipeline | Testes | QA / Engenharia | 5 dias |
