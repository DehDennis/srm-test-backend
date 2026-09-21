package com.srm.creditengine.domain;

/**
 * @author DennisFerreira
 * @since 2026-09-21 13:30
 */
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlements")
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private Long receivableId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal presentValue;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal discountAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency paymentCurrency;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal exchangeRate;

    @Column(nullable = false)
    private LocalDateTime settledAt;

    public Settlement() {}

    public Settlement(String idempotencyKey, Long receivableId, BigDecimal presentValue,
        BigDecimal discountAmount, Currency paymentCurrency, BigDecimal exchangeRate) {
        this.idempotencyKey = idempotencyKey;
        this.receivableId = receivableId;
        this.presentValue = presentValue;
        this.discountAmount = discountAmount;
        this.paymentCurrency = paymentCurrency;
        this.exchangeRate = exchangeRate;
        this.settledAt = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public Long getReceivableId() { return receivableId; }
    public BigDecimal getPresentValue() { return presentValue; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public Currency getPaymentCurrency() { return paymentCurrency; }
    public BigDecimal getExchangeRate() { return exchangeRate; }
    public LocalDateTime getSettledAt() { return settledAt; }
}

