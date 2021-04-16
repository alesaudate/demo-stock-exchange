package com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks;

import reactor.core.publisher.Flux;

public interface StocksDataProvider {

    Flux<Stock> findStocks() ;

    String getStock();

    static String getStockBeanName(String stock) {
        return String.format("%s-dataprovider", stock);
    }
}
