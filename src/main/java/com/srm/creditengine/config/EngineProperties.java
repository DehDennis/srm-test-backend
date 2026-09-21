package com.srm.creditengine.config;

/**
 * @author DennisFerreira
 * @since 2026-09-21 12:40
 */
import com.srm.creditengine.domain.Currency;
import com.srm.creditengine.domain.ReceivableType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "credit-engine")
public class EngineProperties {

    private BigDecimal baseRateMonthly;
    private Map<ReceivableType, BigDecimal> spreads;
    private Map<Currency, BigDecimal> currencies;

    public BigDecimal getBaseRateMonthly() { return baseRateMonthly; }
    public void setBaseRateMonthly(BigDecimal baseRateMonthly) { this.baseRateMonthly = baseRateMonthly; }

    public Map<ReceivableType, BigDecimal> getSpreads() { return spreads; }
    public void setSpreads(Map<ReceivableType, BigDecimal> spreads) { this.spreads = spreads; }

    public Map<Currency, BigDecimal> getCurrencies() { return currencies; }
    public void setCurrencies(Map<Currency, BigDecimal> currencies) { this.currencies = currencies; }
}

