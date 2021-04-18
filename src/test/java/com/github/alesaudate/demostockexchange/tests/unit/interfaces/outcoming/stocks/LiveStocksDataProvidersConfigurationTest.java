package com.github.alesaudate.demostockexchange.tests.unit.interfaces.outcoming.stocks;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.LiveStocksDataProvidersConfiguration;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvider;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.rapidapi.RapidAPIStockClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

import static com.github.alesaudate.demostockexchange.fixtures.SpringBeansMockProvider.applicationContextMock;
import static com.github.alesaudate.demostockexchange.fixtures.Randoms.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class LiveStocksDataProvidersConfigurationTest {

    @DisplayName("Given " +
            "   a live stocks data provider configuration " +
            "When " +
            "   I provide a list of stocks " +
            "Then " +
            "   I a corresponding list of live stocks data providers are registered")
    @Test
    public void testRegisterLiveDataProviders() {
        var applicationContext = applicationContextMock();
        var beanFactory = (ConfigurableBeanFactory)applicationContext.getParentBeanFactory();
        var configuration = new LiveStocksDataProvidersConfiguration(beanFactory);

        var list = List.of(randomNYSEStock(), randomNYSEStock(), randomNYSEStock());
        var captorForStocksDataProvider = ArgumentCaptor.forClass(StocksDataProvider.class);

        var apiKey = randomRapidAPIKey();
        var apiHost = randomInternetHost();

        configuration.setStocks(list);
        configuration.setRapidApiKey(apiKey);
        configuration.setRapidApiHost(apiHost);

        configuration.registerProviders();

        list.stream().forEach(stock -> {
            verify(beanFactory).registerSingleton(eq(StocksDataProvider.getStockBeanName(stock)), captorForStocksDataProvider.capture());
            var stocksDataProvider = captorForStocksDataProvider.getValue();
            assertEquals(RapidAPIStockClient.class, stocksDataProvider.getClass());
            var rapidAPIStockClient = (RapidAPIStockClient)stocksDataProvider;

            assertEquals(apiKey, rapidAPIStockClient.getRapidApiKey());
            assertEquals(apiHost, rapidAPIStockClient.getHost());
            assertNotNull(rapidAPIStockClient.getWebClientTemplate());

        });

    }


    @DisplayName("Given " +
            "   a live stocks data provider configuration " +
            "When " +
            "   I provide a null list of stocks " +
            "Then " +
            "   No data providers are registered")
    @Test
    public void testRegisterNoDataProvidersIfStockListIsNull() {
        var applicationContext = applicationContextMock();
        var beanFactory = (ConfigurableBeanFactory)applicationContext.getParentBeanFactory();
        var configuration = new LiveStocksDataProvidersConfiguration(beanFactory);

        configuration.setStocks(null);
        configuration.setRapidApiKey(randomRapidAPIKey());
        configuration.setRapidApiHost(randomInternetHost());

        configuration.registerProviders();

        verify(beanFactory, never()).registerSingleton(anyString(), any());

    }


    @DisplayName("Given " +
            "   a live stocks data provider configuration " +
            "When " +
            "   I provide a null list of stocks " +
            "Then " +
            "   No data providers are registered")
    @Test
    public void testRegisterNoDataProvidersIfStockListIsEmpty() {
        var applicationContext = applicationContextMock();
        var beanFactory = (ConfigurableBeanFactory)applicationContext.getParentBeanFactory();
        var configuration = new LiveStocksDataProvidersConfiguration(beanFactory);

        configuration.setStocks(new ArrayList<>());
        configuration.setRapidApiKey(randomRapidAPIKey());
        configuration.setRapidApiHost(randomInternetHost());

        configuration.registerProviders();

        verify(beanFactory, never()).registerSingleton(anyString(), any());

    }

}
