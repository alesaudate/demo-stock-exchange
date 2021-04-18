package com.github.alesaudate.demostockexchange.tests.unit.domain;


import com.github.alesaudate.demostockexchange.domain.PricingService;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static com.github.alesaudate.demostockexchange.fixtures.StockFixture.randomStockFixture;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class PricingServiceTest {


    @DisplayName("Given " +
            "   A pricing service " +
            "When " +
            "   I ask for the average price " +
            "And " +
            "   I ask for the average price after 5 seconds" +
            "Then " +
            "   the average price is updated")
    @Test
    public void testDataIsConstantlyUpdated(){

        var stocksDataProvider = mock(StocksDataProvider.class);

        var flux = Flux.interval(Duration.ZERO, Duration.ofSeconds(2)).map(n -> randomStockFixture());
        given(stocksDataProvider.findStocks()).willReturn(flux);
        var pricingService = new PricingService(stocksDataProvider);
        pricingService.init();

        await().atMost(Duration.ofSeconds(2)).until(() -> pricingService.getAveragePricing().getDatapoints() > 0);
        var averagePricing = pricingService.getAveragePricing();

        await().atMost(Duration.ofSeconds(2)).until(() -> !pricingService.getAveragePricing().equals(averagePricing));

    }


    @DisplayName("Given " +
            "   A pricing service " +
            "When " +
            "   I ask for the flux of average prices " +
            "Then " +
            "   I get a flux with average prices")
    @Test
    public void testDataGetsStreamedWithUpdates(){

        var stocksDataProvider = mock(StocksDataProvider.class);

        var flux = Flux.interval(Duration.ZERO, Duration.ofSeconds(2)).map(n -> randomStockFixture());
        given(stocksDataProvider.findStocks()).willReturn(flux);
        var pricingService = new PricingService(stocksDataProvider);
        pricingService.init();

        var data = pricingService.streamData().buffer(3).blockFirst();
        assertEquals(3, data.size());
        assertAll(
                () -> assertFalse(data.get(0).equals(data.get(1))),
                () -> assertFalse(data.get(1).equals(data.get(2))),
                () -> assertFalse(data.get(0).equals(data.get(2)))
                );
    }




}
