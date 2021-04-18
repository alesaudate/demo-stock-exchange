package com.github.alesaudate.demostockexchange.tests.unit.interfaces.outcoming.stocks.providers.fake;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.fake.FakeStocksDataProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.github.alesaudate.demostockexchange.fixtures.Randoms.randomNYSEStock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FakeStocksDataProviderTest {


    @DisplayName("Given " +
            "   a fake stocks data provider " +
            "When " +
            "   I request a stream of stocks values " +
            "Then " +
            "   I receive a stream of stocks values")
    @Test
    public void testEmitFakeStocks() {
        var randomStock = randomNYSEStock();
        var fakeStocksDataProvider = new FakeStocksDataProvider(randomStock);

        var flux = fakeStocksDataProvider.findStocks();
        var listStock = flux.buffer(5).blockFirst();

        listStock.stream().forEach(stock -> {
            assertNotNull(stock.getPrice());
            assertEquals(randomStock, stock.getStock());
        });
    }

}
