package com.srm.creditengine;

/**
 * @author DennisFerreira
 * @since 2026-09-21 13:28
 */
import com.srm.creditengine.domain.Currency;
import com.srm.creditengine.domain.Receivable;
import com.srm.creditengine.domain.ReceivableType;
import com.srm.creditengine.domain.Settlement;
import com.srm.creditengine.dto.SettlementRequest;
import com.srm.creditengine.repository.ReceivableRepository;
import com.srm.creditengine.repository.SettlementRepository;
import com.srm.creditengine.service.SettlementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class IdempotencyTest {

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private ReceivableRepository receivableRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Test
    @DisplayName("Garante que chamadas duplicadas com a mesma Idempotency-Key retornem o mesmo registro sem reprocessar")
    void testIdempotentSettlement() {
        // Criar recebível no banco
        Receivable receivable = new Receivable(ReceivableType.DUPLICATA, new BigDecimal("100000.00"), 3, Currency.BRL);
        receivable = receivableRepository.save(receivable);

        String idempotencyKey = UUID.randomUUID().toString();
        SettlementRequest request = new SettlementRequest(receivable.getId(), Currency.BRL, null);

        // Primeira Chamada
        Settlement firstCall = settlementService.executeSettlement(idempotencyKey, request);

        // Segunda Chamada (com a mesma chave)
        Settlement secondCall = settlementService.executeSettlement(idempotencyKey, request);

        // Valida que retornaram exatamente a mesma ID de liquidação
        assertEquals(firstCall.getId(), secondCall.getId());
        assertEquals(1, settlementRepository.count());
    }
}

