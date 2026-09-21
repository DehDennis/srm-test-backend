package com.srm.creditengine.dto;

/**
 * @author DennisFerreira
 * @since 2026-09-21 12:39
 */
import com.srm.creditengine.domain.Currency;
import com.srm.creditengine.domain.ReceivableType;

import java.math.BigDecimal;

public class SimulateRequest {
    private ReceivableType type;
    private BigDecimal faceValue;
    private Integer termMonths;
    private Currency paymentCurrency;
    private BigDecimal exchangeRate;

    public SimulateRequest() {}

    public SimulateRequest(ReceivableType type, BigDecimal faceValue, Integer termMonths, Currency paymentCurrency, BigDecimal exchangeRate) {
        this.type = type;
        this.faceValue = faceValue;
        this.termMonths = termMonths;
        this.paymentCurrency = paymentCurrency;
        this.exchangeRate = exchangeRate;
    }

    public ReceivableType getType() { return type; }
    public void setType(ReceivableType type) { this.type = type; }

    public BigDecimal getFaceValue() { return faceValue; }
    public void setFaceValue(BigDecimal faceValue) { this.faceValue = faceValue; }

    public Integer getTermMonths() { return termMonths; }
    public void setTermMonths(Integer termMonths) { this.termMonths = termMonths; }

    public Currency getPaymentCurrency() { return paymentCurrency; }
    public void setPaymentCurrency(Currency paymentCurrency) { this.paymentCurrency = paymentCurrency; }

    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
}

