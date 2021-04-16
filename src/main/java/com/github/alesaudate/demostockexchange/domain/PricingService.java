package com.github.alesaudate.demostockexchange.domain;


import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

        streamData().subscribe(averagePricing1 -> log.debug("New average price registered: {}", averagePricing1));

    }

    public String getStock() {
        return dataProvider.getStock();
    }

    public Flux<AveragePricing> streamData() {
        return dataProvider.findStocks()
                .map(stock ->  averagePricing.registerNewPrice(stock.getPrice()))
                .map(averagePricing1 -> averagePricing = averagePricing1)
                .share()
                ;
    }

}
