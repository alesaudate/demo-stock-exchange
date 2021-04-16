package com.github.alesaudate.demostockexchange.interfaces.outcoming;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;

/**
 * Will provide random data where the stock price is 10 < price < 500, each second
 */
@RequiredArgsConstructor
@Getter
public class FakeStocksDataProvider implements StocksDataProvider{

    private static final Random RANDOM = new Random();
    private final String stock;

    public Flux<Stock> findStocks() {
        return Flux.interval(Duration.of(1, ChronoUnit.SECONDS)).map(n -> randomStock());
    }

    private Stock randomStock() {
        return new Stock(stock, randomPrice());
    }

    private BigDecimal randomPrice() {
        double value = RANDOM.nextDouble() * 490;
        return BigDecimal.valueOf(value).add(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP);
    }

}
