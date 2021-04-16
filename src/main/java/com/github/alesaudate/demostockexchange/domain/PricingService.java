package com.github.alesaudate.demostockexchange.domain;


import com.github.alesaudate.demostockexchange.interfaces.outcoming.FakeStocksDataProvider;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.StocksDataProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class PricingService {

    final StocksDataProvider dataProvider;

    @Getter
    AveragePricing averagePricing;


    @PostConstruct
    public void init() {
        averagePricing = new AveragePricing(dataProvider.getStock(), BigDecimal.ZERO, 0);
        streamAverage().subscribe(price -> {
            averagePricing = price;
        });
    }

    public String getStock() {
        return dataProvider.getStock();
    }

    public Flux<AveragePricing> streamAverage() {
        return dataProvider.findStocks().map(stock -> {
            log.debug("New stock price received: {}", stock);
            var newPrice = averagePricing.registerNewPrice(stock.getPrice());
            log.debug("New average price registered: {}", newPrice);
            return newPrice;
        });
    }
}
