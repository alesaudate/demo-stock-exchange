package com.github.alesaudate.demostockexchange.tests.unit.interfaces.outcoming.stocks;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.FakeStocksDataProvidersConfiguration;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvider;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.fake.FakeStocksDataProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.ArrayList;
import java.util.List;

import static com.github.alesaudate.demostockexchange.fixtures.SpringBeansMockProvider.applicationContextMock;
import static com.github.alesaudate.demostockexchange.fixtures.Randoms.randomNYSEStock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FakeStocksDataProvidersConfigurationTest {


    @DisplayName("Given " +
            "   a fake stocks data provider configuration " +
            "When " +
            "   I provide a list of stocks " +
            "Then " +
            "   I a corresponding list of fake stocks data providers are registered")
    @Test
    public void testRegisterFakeDataProviders() {
        var applicationContext = applicationContextMock();
        var beanFactory = (ConfigurableBeanFactory)applicationContext.getParentBeanFactory();
        var configuration = new FakeStocksDataProvidersConfiguration(beanFactory);

        var list = List.of(randomNYSEStock(), randomNYSEStock(), randomNYSEStock());
        var captorForStocksDataProvider = ArgumentCaptor.forClass(StocksDataProvider.class);

        configuration.setStocks(list);

        configuration.registerProviders();

        list.stream().forEach(stock -> {
            verify(beanFactory).registerSingleton(eq(StocksDataProvider.getStockBeanName(stock)), captorForStocksDataProvider.capture());
            var stocksDataProvider = captorForStocksDataProvider.getValue();
            assertEquals(FakeStocksDataProvider.class, stocksDataProvider.getClass());
        });

    }

    @DisplayName("Given " +
            "   a fake stocks data provider configuration " +
            "When " +
            "   I provide a list of two stocks with repeated value " +
            "Then " +
            "   I verify only one fake stocks data provider is registered")
    @Test
    public void testRegisterDuplicatedFakeDataProviders() {
        var applicationContext = applicationContextMock();
        var beanFactory = (ConfigurableBeanFactory)applicationContext.getParentBeanFactory();
        var configuration = new FakeStocksDataProvidersConfiguration(beanFactory);

        var stock = randomNYSEStock();
        var list = List.of(stock, stock);
        var captorForStocksDataProvider = ArgumentCaptor.forClass(StocksDataProvider.class);

        configuration.setStocks(list);

        configuration.registerProviders();

        verify(beanFactory, times(1)).registerSingleton(eq(StocksDataProvider.getStockBeanName(stock)), captorForStocksDataProvider.capture());

        assertEquals(FakeStocksDataProvider.class, captorForStocksDataProvider.getValue().getClass());
    }


    @DisplayName("Given " +
            "   a fake stocks data provider configuration " +
            "When " +
            "   I provide a null list of stocks " +
            "Then " +
            "   No data providers are registered")
    @Test
    public void testRegisterNoDataProvidersIfStockListIsNull() {
        var applicationContext = applicationContextMock();
        var beanFactory = (ConfigurableBeanFactory)applicationContext.getParentBeanFactory();
        var configuration = new FakeStocksDataProvidersConfiguration(beanFactory);

        configuration.setStocks(null);

        configuration.registerProviders();

        verify(beanFactory, never()).registerSingleton(anyString(), any());

    }


    @DisplayName("Given " +
            "   a fake stocks data provider configuration " +
            "When " +
            "   I provide a null list of stocks " +
            "Then " +
            "   No data providers are registered")
    @Test
    public void testRegisterNoDataProvidersIfStockListIsEmpty() {
        var applicationContext = applicationContextMock();
        var beanFactory = (ConfigurableBeanFactory)applicationContext.getParentBeanFactory();
        var configuration = new FakeStocksDataProvidersConfiguration(beanFactory);

        configuration.setStocks(new ArrayList<>());

        configuration.registerProviders();

        verify(beanFactory, never()).registerSingleton(anyString(), any());

    }


    @DisplayName("Given " +
            "   a fake stocks data provider configuration " +
            "When " +
            "   I add a new managed stock " +
            "Then " +
            "   A new data provider is registered ")
    @Test
    public void testAddManagedStock() {
        var applicationContext = applicationContextMock();
        var beanFactory = (ConfigurableBeanFactory)applicationContext.getParentBeanFactory();
        var configuration = new FakeStocksDataProvidersConfiguration(beanFactory);

        configuration.setStocks(new ArrayList<>());

        configuration.registerProviders();

        verify(beanFactory, never()).registerSingleton(anyString(), any());

        var stock = randomNYSEStock();
        configuration.addManagedStock(stock);
        configuration.registerProviders();

        verify(beanFactory).registerSingleton(eq(StocksDataProvider.getStockBeanName(stock)), any(StocksDataProvider.class));
    }



    @DisplayName("Given " +
            "   a fake stocks data provider configuration with null list of stocks " +
            "When " +
            "   I add a new managed stock " +
            "Then " +
            "   A new data provider is registered ")
    @Test
    public void testAddManagedStockWithNullListOfStocks() {
        var applicationContext = applicationContextMock();
        var beanFactory = (ConfigurableBeanFactory)applicationContext.getParentBeanFactory();
        var configuration = new FakeStocksDataProvidersConfiguration(beanFactory);

        configuration.setStocks(null);

        configuration.registerProviders();

        verify(beanFactory, never()).registerSingleton(anyString(), any());

        var stock = randomNYSEStock();
        configuration.addManagedStock(stock);
        configuration.registerProviders();

        verify(beanFactory).registerSingleton(eq(StocksDataProvider.getStockBeanName(stock)), any(StocksDataProvider.class));
    }

}
