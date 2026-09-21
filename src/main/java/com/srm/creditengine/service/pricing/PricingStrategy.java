package com.srm.creditengine.service.pricing;

/**
 * @author DennisFerreira
 * @since 2026-09-21 12:42
 */
import com.srm.creditengine.domain.ReceivableType;
import java.math.BigDecimal;

public interface PricingStrategy {
    ReceivableType getType();
    BigDecimal calculatePresentValue(BigDecimal faceValue, Integer termMonths, BigDecimal baseRate, BigDecimal spread);
}
