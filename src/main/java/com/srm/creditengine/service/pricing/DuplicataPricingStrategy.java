package com.srm.creditengine.service.pricing;

/**
 * @author DennisFerreira
 * @since 2026-09-21 12:42
 */
import com.srm.creditengine.domain.ReceivableType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

@Component
public class DuplicataPricingStrategy implements PricingStrategy {

    @Override
    public ReceivableType getType() {
        return ReceivableType.DUPLICATA;
    }

    @Override
    public BigDecimal calculatePresentValue(BigDecimal faceValue, Integer termMonths, BigDecimal baseRate, BigDecimal spread) {
        BigDecimal totalRate = BigDecimal.ONE.add(baseRate).add(spread);
        BigDecimal discountFactor = totalRate.pow(termMonths, MathContext.DECIMAL128);
        return faceValue.divide(discountFactor, MathContext.DECIMAL128);
    }
}