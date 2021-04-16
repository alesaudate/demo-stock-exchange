package com.github.alesaudate.demostockexchange.interfaces.outcoming;

import reactor.core.publisher.Flux;

public interface StocksDataProvider {

    Flux<Stock> findStocks() ;

    String getStock();
}
