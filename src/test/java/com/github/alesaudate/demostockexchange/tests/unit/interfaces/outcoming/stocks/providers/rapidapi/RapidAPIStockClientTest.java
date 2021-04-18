package com.github.alesaudate.demostockexchange.tests.unit.interfaces.outcoming.stocks.providers.rapidapi;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.rapidapi.RapidAPIStockClient;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.math.BigDecimal;

import static com.github.alesaudate.demostockexchange.fixtures.Randoms.*;
import static com.github.alesaudate.demostockexchange.fixtures.WebClientMockProvider.mockWebClientBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RapidAPIStockClientTest {


    @DisplayName("Given " +
            "   a live stocks data provider " +
            "When " +
            "   I request a stream of stocks values " +
            "Then " +
            "   I receive a stream of stocks values")
    @Test
    public void testRetrievalOfAPIData() {

        var rapidAPIStocksClient = new RapidAPIStockClient(randomNYSEStock(),
                randomRapidAPIKey(),
                mockWebClientBuilder(getResponseData("sample-01.json")),
                randomInternetHost());

        var stock = rapidAPIStocksClient.findStocks().blockFirst();
        assertEquals(rapidAPIStocksClient.getStock(), stock.getStock());
        assertEquals(BigDecimal.valueOf(46.31), stock.getPrice());


    }

    public String getResponseData(String file) {
        var classpathResource = new ClassPathResource(String.format("/api/responses/rapidapi/%s", file));
        try {
            return FileUtils.readFileToString(classpathResource.getFile(), "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
