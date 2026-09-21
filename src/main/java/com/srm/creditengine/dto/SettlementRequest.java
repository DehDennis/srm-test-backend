package com.srm.creditengine.dto;

/**
 * @author DennisFerreira
 * @since 2026-09-21 13:30
 */
import com.srm.creditengine.domain.Currency;
import java.math.BigDecimal;

public class SettlementRequest {

    private Long receivableId;
    private Currency paymentCurrency;
    private BigDecimal exchangeRate;

    public SettlementRequest() {}

    public SettlementRequest(Long receivableId, Currency paymentCurrency, BigDecimal exchangeRate) {
        this.receivableId = receivableId;
        this.paymentCurrency = paymentCurrency;
        this.exchangeRate = exchangeRate;
    }

    public Long getReceivableId() { return receivableId; }
    public void setReceivableId(Long receivableId) { this.receivableId = receivableId; }

    public Currency getPaymentCurrency() { return paymentCurrency; }
    public void setPaymentCurrency(Currency paymentCurrency) { this.paymentCurrency = paymentCurrency; }

    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }
}
