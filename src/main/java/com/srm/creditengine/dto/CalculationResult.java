package com.srm.creditengine.dto;

/**
 * @author DennisFerreira
 * @since 2026-09-21 12:40
 */
import java.math.BigDecimal;

public class CalculationResult {
    private BigDecimal presentValue;
    private BigDecimal discountAmount;
    private BigDecimal exchangeRate;

    public CalculationResult() {}

    public CalculationResult(BigDecimal presentValue, BigDecimal discountAmount, BigDecimal exchangeRate) {
        this.presentValue = presentValue;
        this.discountAmount = discountAmount;
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getPresentValue() { return presentValue; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
}

