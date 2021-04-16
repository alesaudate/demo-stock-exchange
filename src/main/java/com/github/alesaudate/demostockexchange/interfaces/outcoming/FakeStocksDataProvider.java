package com.github.alesaudate.demostockexchange.interfaces.outcoming;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Will provide random data where 10 >= data <= 200, each second
 */
@RequiredArgsConstructor
@Getter
public class FakeStocksDataProvider implements StocksDataProvider{

    private static final Random RANDOM = new Random();
    private final String stock;

    public Flux<Stock> findStocks() {
        return Flux.fromStream(this::emitStockData).delayElements(Duration.of(1, ChronoUnit.SECONDS)).share();
    }

    private Stream<Stock> emitStockData() {
        return LongStream.range(0, Long.MAX_VALUE).mapToObj(l -> new Stock(stock, randomPrice()));
    }


    private BigDecimal randomPrice() {
        double value = RANDOM.nextDouble() * 190;
        return BigDecimal.valueOf(value).add(BigDecimal.TEN).setScale(2, RoundingMode.HALF_UP);
    }

}
