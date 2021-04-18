package com.github.alesaudate.demostockexchange.fixtures;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.Stock;

import static com.github.alesaudate.demostockexchange.fixtures.Randoms.moneyValue;
import static com.github.alesaudate.demostockexchange.fixtures.Randoms.randomNYSEStock;

public class StockFixture {


    public static Stock randomStockFixture() {
        return randomStockFixture(randomNYSEStock());
    }

    public static Stock randomStockFixture(String stock) {
        return new Stock(stock, moneyValue(1000));
    }
}
