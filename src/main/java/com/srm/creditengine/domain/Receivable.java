package com.srm.creditengine.domain;

/**
 * @author DennisFerreira
 * @since 2026-09-21 13:25
 */
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "receivables")
public class Receivable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReceivableType type;

    private BigDecimal faceValue;
    private Integer termMonths;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Version // <--- Garante tratamento de concorrência simultânea
    private Long version;

    public enum Status { PENDING, SETTLED }

    public Receivable() {}

    public Receivable(ReceivableType type, BigDecimal faceValue, Integer termMonths, Currency currency) {
        this.type = type;
        this.faceValue = faceValue;
        this.termMonths = termMonths;
        this.currency = currency;
    }

    public Long getId() { return id; }
    public ReceivableType getType() { return type; }
    public BigDecimal getFaceValue() { return faceValue; }
    public Integer getTermMonths() { return termMonths; }
    public Currency getCurrency() { return currency; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public Long getVersion() { return version; }
}

