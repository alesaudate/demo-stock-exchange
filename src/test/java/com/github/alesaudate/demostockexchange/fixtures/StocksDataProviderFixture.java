package com.github.alesaudate.demostockexchange.fixtures;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.Stock;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvider;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class StocksDataProviderFixture {


    public static StocksDataProvider makeStocksDataProvider(String stock) {
        var stocksDataProvider = mock(StocksDataProvider.class);
        given(stocksDataProvider.getStock()).willReturn(stock);

        var flux = Flux.interval(Duration.ZERO, Duration.ofSeconds(1)).map(n -> StockFixture.randomStockFixture(stock));
        given(stocksDataProvider.findStocks()).willReturn(flux);
        return stocksDataProvider;
    }



}
