package com.srm.creditengine.controller;

/**
 * @author DennisFerreira
 * @since 2026-09-21 12:45
 */
import com.srm.creditengine.domain.Currency;
import com.srm.creditengine.domain.Receivable;
import com.srm.creditengine.domain.Settlement;
import com.srm.creditengine.dto.CalculationResult;
import com.srm.creditengine.dto.SettlementRequest;
import com.srm.creditengine.dto.SimulateRequest;
import com.srm.creditengine.repository.ReceivableRepository;
import com.srm.creditengine.service.pricing.PricingEngineService;
import com.srm.creditengine.service.SettlementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credit")
@CrossOrigin(origins = "*")
public class CreditEngineController {

    private final PricingEngineService pricingEngineService;
    private final SettlementService settlementService;
    private final ReceivableRepository receivableRepository;

    public CreditEngineController(PricingEngineService pricingEngineService, SettlementService settlementService,
        final ReceivableRepository receivableRepository) {
        this.pricingEngineService = pricingEngineService;
        this.settlementService = settlementService;
        this.receivableRepository = receivableRepository;
    }

    @PostMapping("/simulate")
    public ResponseEntity<CalculationResult> simulate(@RequestBody SimulateRequest request) {
        CalculationResult result = pricingEngineService.calculate(
            request.getType(),
            request.getFaceValue(),
            request.getTermMonths(),
            request.getPaymentCurrency(),
            request.getExchangeRate()
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/settle")
    public ResponseEntity<Settlement> settle(
        @RequestHeader("X-Idempotency-Key") String idempotencyKey,
        @RequestBody SettlementRequest request) {

        Settlement settlement = settlementService.executeSettlement(idempotencyKey, request);
        return ResponseEntity.ok(settlement);
    }
    @GetMapping("/settlements")
    public ResponseEntity<Page<Settlement>> getSettlements(
        @RequestParam(required = false) Currency currency,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

        Page<Settlement> result = settlementService.listSettlements(currency, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }


    @GetMapping("/receivables/pending")
    public ResponseEntity<List<Receivable>> getPendingReceivables() {
        List<Receivable> pending = receivableRepository.findAll()
            .stream()
            .filter(r -> r.getStatus() == Receivable.Status.PENDING)
            .toList();
        return ResponseEntity.ok(pending);
    }

}

