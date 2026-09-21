package com.srm.creditengine.controller;

/**
 * @author DennisFerreira
 * @since 2026-09-21 12:45
 */
import com.srm.creditengine.dto.CalculationResult;
import com.srm.creditengine.dto.SimulateRequest;
import com.srm.creditengine.service.PricingEngineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credit")
@CrossOrigin(origins = "*")
public class CreditEngineController {

    private final PricingEngineService pricingEngineService;

    public CreditEngineController(PricingEngineService pricingEngineService) {
        this.pricingEngineService = pricingEngineService;
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
}
