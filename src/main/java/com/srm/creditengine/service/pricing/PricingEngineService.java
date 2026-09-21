package com.srm.creditengine.service.pricing;

/**
 * @author DennisFerreira
 * @since 2026-09-21 12:43
 */
import com.srm.creditengine.config.EngineProperties;
import com.srm.creditengine.domain.Currency;
import com.srm.creditengine.domain.ReceivableType;
import com.srm.creditengine.dto.CalculationResult;
import com.srm.creditengine.service.pricing.PricingStrategy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PricingEngineService {

    private final EngineProperties properties;
    private final Map<ReceivableType, PricingStrategy> strategies;

    public PricingEngineService(EngineProperties properties, List<PricingStrategy> strategyList) {
        this.properties = properties;
        this.strategies = strategyList.stream()
            .collect(Collectors.toMap(PricingStrategy::getType, Function.identity()));
    }

    public CalculationResult calculate(ReceivableType type, BigDecimal faceValue, Integer termMonths, Currency paymentCurrency, BigDecimal customFxRate) {
        PricingStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Tipo de recebível não suportado: " + type);
        }

        BigDecimal baseRate = properties.getBaseRateMonthly();
        BigDecimal spread = properties.getSpreads().get(type);

        // 1. Calcula Valor Presente em BRL
        BigDecimal presentValueBrl = strategy.calculatePresentValue(faceValue, termMonths, baseRate, spread);

        // 2. Transforma/Converte Moeda se necessário
        BigDecimal finalPresentValue;
        BigDecimal exchangeRate = BigDecimal.ONE;

        if (paymentCurrency == Currency.USD) {
            exchangeRate = customFxRate != null ? customFxRate : properties.getCurrencies().get(Currency.USD);
            // Arredonda BRL em 2 casas Half-Even ANTES de converter pra USD (regra de aferição)
            BigDecimal brlRounded = presentValueBrl.setScale(2, RoundingMode.HALF_EVEN);
            finalPresentValue = brlRounded.divide(exchangeRate, 2, RoundingMode.HALF_EVEN);
        } else {
            finalPresentValue = presentValueBrl.setScale(2, RoundingMode.HALF_EVEN);
        }

        BigDecimal discountAmount = faceValue.subtract(presentValueBrl).setScale(2, RoundingMode.HALF_EVEN);

        return new CalculationResult(finalPresentValue, discountAmount, exchangeRate);
    }
}

