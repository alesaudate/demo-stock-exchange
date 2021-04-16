package com.github.alesaudate.demostockexchange.domain;


import com.github.alesaudate.demostockexchange.interfaces.outcoming.FakeStocksDataProvider;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.StocksDataProvider;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

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
        dataProvider.findStocks().subscribe(stock -> {
            log.debug("New stock price received: {}", stock);
            averagePricing = averagePricing.registerNewPrice(stock.getPrice());
            log.debug("New average price registered: {}", averagePricing);
        });
    }

    public String getStock() {
        return dataProvider.getStock();
    }


    public static void main(String[] args) throws InterruptedException {
        var pricingService = new PricingService(new FakeStocksDataProvider("PAGS"));
        pricingService.init();
        Thread.sleep(Long.MAX_VALUE);
    }

}
