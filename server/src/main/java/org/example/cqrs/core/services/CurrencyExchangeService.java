package org.example.cqrs.core.services;

import org.example.cqrs.core.enums.Currency;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CurrencyExchangeService {

    // Fixed Currencies Rate
    private static final Map<String, Double> RATES = Map.ofEntries(
            Map.entry("USD_EUR", 0.93),
            Map.entry("USD_MAD", 10.0),
            Map.entry("EUR_USD", 1.07),
            Map.entry("EUR_MAD", 11.0),
            Map.entry("MAD_USD", 0.10),
            Map.entry("MAD_EUR", 0.09)
    );

    public double convert(double amount, Currency source, Currency destination) {
        if (source.equals(destination)) {
            return amount;
        }

        double rate = getRate(source, destination);
        return amount * rate;
    }

    private double getRate(Currency source, Currency destination) {
        if (source.equals(destination)) return 1.0;

        Double rate = RATES.get(source + "_" + destination);
        if (rate == null) {
            throw new IllegalArgumentException("Exchange rate not defined");
        }
        return rate;
    }
}
