# SRM Credit Engine — Plataforma de Cessão de Crédito Multimoedas (v2)

[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot 3.2](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![React 18](https://img.shields.io/badge/React-18-blue.svg)](https://react.dev/)

Plataforma de precificação e liquidação de recebíveis (duplicatas mercantis e cheques pré-datados) com suporte multimoeda (BRL e USD), precisão decimal rigorosa, transacionalidade ACID e controle de idempotência.

Desenvolvido como solução do desafio técnico **SRM Credit Engine (v2)**, focado nas expectativas de liderança técnica (**Tech Lead / Staff**).

---

## 🚀 Como Executar o Projeto Localmente

O projeto foi configurado com um **`DataLoader` em memória (H2)** que carrega automaticamente uma massa de recebíveis reais ao iniciar, permitindo executar e testar todo o fluxo sem dependências externas ou configurações de banco de dados locais.

### 1. Inicializar o Backend (Spring Boot)
Requisitos: **Java 17+** instalado.

```bash
cd backend

# Executar a suíte de testes (Aferição dos Golden Cases e Idempotência)
./mvnw test

# Iniciar o servidor backend (Porta 8080)
./mvnw spring-boot:run
