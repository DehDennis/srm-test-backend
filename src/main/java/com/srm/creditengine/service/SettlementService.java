package com.srm.creditengine.service;

/**
 * @author DennisFerreira
 * @since 2026-09-21 13:27
 */
import com.srm.creditengine.domain.Receivable;
import com.srm.creditengine.domain.Settlement;
import com.srm.creditengine.dto.CalculationResult;
import com.srm.creditengine.dto.SettlementRequest;
import com.srm.creditengine.repository.ReceivableRepository;
import com.srm.creditengine.repository.SettlementRepository;
import com.srm.creditengine.service.pricing.PricingEngineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final ReceivableRepository receivableRepository;
    private final PricingEngineService pricingEngineService;

    public SettlementService(SettlementRepository settlementRepository,
        ReceivableRepository receivableRepository,
        PricingEngineService pricingEngineService) {
        this.settlementRepository = settlementRepository;
        this.receivableRepository = receivableRepository;
        this.pricingEngineService = pricingEngineService;
    }

    @Transactional
    public Settlement executeSettlement(String idempotencyKey, SettlementRequest request) {
        // 1. Checa Idempotência: Se já foi processado com este Header, retorna a liquidação existente sem re-executar
        Optional<Settlement> existingSettlement = settlementRepository.findByIdempotencyKey(idempotencyKey);
        if (existingSettlement.isPresent()) {
            return existingSettlement.get();
        }

        // 2. Busca o Recebível
        Receivable receivable = receivableRepository.findById(request.getReceivableId())
            .orElseThrow(() -> new IllegalArgumentException("Recebível não encontrado ID: " + request.getReceivableId()));

        if (receivable.getStatus() == Receivable.Status.SETTLED) {
            throw new IllegalStateException("Recebível já foi liquidado anteriormente.");
        }

        // 3. Executa o Cálculo de Precificação
        CalculationResult calculation = pricingEngineService.calculate(
            receivable.getType(),
            receivable.getFaceValue(),
            receivable.getTermMonths(),
            request.getPaymentCurrency(),
            request.getExchangeRate()
        );

        // 4. Grava a Liquidação
        Settlement settlement = new Settlement(
            idempotencyKey,
            receivable.getId(),
            calculation.getPresentValue(),
            calculation.getDiscountAmount(),
            request.getPaymentCurrency(),
            calculation.getExchangeRate()
        );

        // 5. Atualiza o status do Recebível
        receivable.setStatus(Receivable.Status.SETTLED);
        receivableRepository.save(receivable);

        return settlementRepository.save(settlement);
    }
}

