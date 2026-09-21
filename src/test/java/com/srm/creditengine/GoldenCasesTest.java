package com.srm.creditengine;

/**
 * @author DennisFerreira
 * @since 2026-09-21 12:48
 */
import com.srm.creditengine.domain.Currency;
import com.srm.creditengine.domain.ReceivableType;
import com.srm.creditengine.dto.CalculationResult;
import com.srm.creditengine.service.pricing.PricingEngineService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GoldenCasesTest {

    @Autowired
    private PricingEngineService pricingEngine;

    @Test
    @DisplayName("Caso C1: Duplicata Mercantil - R$ 100.000,00 | 3 meses | BRL")
    void testCaseC1() {
        CalculationResult result = pricingEngine.calculate(
            ReceivableType.DUPLICATA,
            new BigDecimal("100000.00"),
            3,
            Currency.BRL,
            null
        );

        assertEquals(new BigDecimal("92859.94"), result.getPresentValue());
        assertEquals(new BigDecimal("7140.06"), result.getDiscountAmount());
    }

    @Test
    @DisplayName("Caso C2: Cheque Pré-datado - R$ 25.000,00 | 2 meses | BRL")
    void testCaseC2() {
        CalculationResult result = pricingEngine.calculate(
            ReceivableType.CHEQUE,
            new BigDecimal("25000.00"),
            2,
            Currency.BRL,
            null
        );

        assertEquals(new BigDecimal("23337.77"), result.getPresentValue());
        assertEquals(new BigDecimal("1662.23"), result.getDiscountAmount());
    }

    @Test
    @DisplayName("Caso C3: Duplicata Cross-Currency - R$ 100.000,00 | 3 meses | USD (Cambio 5.4321)")
    void testCaseC3() {
        CalculationResult result = pricingEngine.calculate(
            ReceivableType.DUPLICATA,
            new BigDecimal("100000.00"),
            3,
            Currency.USD,
            new BigDecimal("5.4321")
        );

        assertEquals(new BigDecimal("17094.67"), result.getPresentValue());
        assertEquals(new BigDecimal("7140.06"), result.getDiscountAmount());
    }
}
