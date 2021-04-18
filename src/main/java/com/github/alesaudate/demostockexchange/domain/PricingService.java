package com.github.alesaudate.demostockexchange.domain;


import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class PricingService {

    final StocksDataProvider dataProvider;

    @Getter
    AveragePricing averagePricing;

    public void init() {
        averagePricing = new AveragePricing(dataProvider.getStock(), BigDecimal.ZERO, 0);
        dataProvider.findStocks()
                .doOnNext(stock -> log.debug("New stock price registered: {}", stock))
                .map(stock -> averagePricing = averagePricing.registerNewPrice(stock.getPrice()))
                .doOnNext(averagePricing1 -> log.debug("New average price registered: {}", averagePricing1))
                .subscribe();
    }

    public String getStock() {
        return dataProvider.getStock();
    }

    /**
     * If the application had two or more subscribers to the data provider,
     * there would be more than one subscriber updating the stateful information
     * (in this case, the field {@link PricingService#averagePricing}). So, in order to
     * prevent weird updates on this field, this method acts like a watcher,
     * checking if the value of the field has changed and, only if it does,
     * emits the chnage in the data.
     *
     * @return a Flux containing the changes in the average pricing
     */
    public Flux<AveragePricing> streamData() {
        return Flux.interval(Duration.ZERO, Duration.ofSeconds(2L))
                .map(n -> averagePricing)
                .distinctUntilChanged();
    }

}
