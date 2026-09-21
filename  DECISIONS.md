# DECISIONS.md — Decisões de Engenharia e Trade-offs de Escopo

## 1. Justificativa de Escopo (Perfil Tech Lead / Staff)

Conforme prevê a Seção 6 da especificação do desafio técnico v2, o nível Staff/Tech Lead foca na **mudança de eixo**: menos código repetitivo de CRUD e maior peso em decisões de arquitetura, prevenção de incidentes e governança.

- **Foco Principal:** Corretude matemática absoluta (`BigDecimal`), idenpotência transacional, resiliência contra condições de corrida (*Optimistic Locking*) e documentação estratégica de alta escala.
- **Simplificação Deliberada:** O frontend foi construído como um painel único focado na simulação em tempo real e na execução da liquidação, abrindo mão de rotas e tabelas complexas que agregariam apenas código genérico.

---

## 2. Arquivos e Registros de Arquitetura (ADRs)

- **ADR-001 (Precisão Financeira):** Adoção obrigatória de `BigDecimal` com *Banker's Rounding* (`HALF_EVEN`) aplicado apenas na entrega final do valor líquido.
- **ADR-002 (Idempotência):** Garantia de não duplicidade através do cabeçalho `X-Idempotency-Key` persistido com restrição de unicidade (`UNIQUE`).
- **ADR-003 (Concorrência):** Uso de controle de concorrência otimista via atributo `@Version` na entidade `Receivable`.
