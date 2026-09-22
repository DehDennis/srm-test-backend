package com.srm.creditengine.config;

import com.srm.creditengine.domain.Currency;
import com.srm.creditengine.domain.Receivable;
import com.srm.creditengine.domain.ReceivableType;
import com.srm.creditengine.repository.ReceivableRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(ReceivableRepository repository) {
        return args -> {
            // Se o banco estiver vazio (ex: ao subir o container pela 1ª vez ou limpar volumes)
            if (repository.count() == 0) {
                List<Receivable> sampleReceivables = List.of(
                    // C1 / Golden Case 1
                    new Receivable(ReceivableType.DUPLICATA, new BigDecimal("100000.00"), 3, Currency.BRL),
                    // C2 / Golden Case 2
                    new Receivable(ReceivableType.CHEQUE, new BigDecimal("25000.00"), 2, Currency.BRL),
                    // C3 / Golden Case 3 (Cross-Currency)
                    new Receivable(ReceivableType.DUPLICATA, new BigDecimal("100000.00"), 3, Currency.USD),
                    // Outros títulos para testes variados na interface
                    new Receivable(ReceivableType.DUPLICATA, new BigDecimal("50000.00"), 1, Currency.BRL),
                    new Receivable(ReceivableType.DUPLICATA, new BigDecimal("180000.00"), 6, Currency.BRL),
                    new Receivable(ReceivableType.CHEQUE, new BigDecimal("15000.00"), 4, Currency.BRL),
                    new Receivable(ReceivableType.DUPLICATA, new BigDecimal("320000.00"), 12, Currency.BRL)
                );

                repository.saveAll(sampleReceivables);
                System.out.println(">>> Massa de dados inicial (7 títulos) carregada no banco de dados com sucesso!");
            }
        };
    }
}
