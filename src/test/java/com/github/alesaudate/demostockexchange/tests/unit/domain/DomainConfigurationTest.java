package com.github.alesaudate.demostockexchange.tests.unit.domain;

import com.github.alesaudate.demostockexchange.domain.DomainConfiguration;
import com.github.alesaudate.demostockexchange.domain.PricingService;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvider;
import com.github.alesaudate.demostockexchange.utils.MapBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.alesaudate.demostockexchange.fixtures.SpringBeansMockProvider.applicationContextMock;
import static com.github.alesaudate.demostockexchange.fixtures.Randoms.randomNYSEStock;
import static com.github.alesaudate.demostockexchange.fixtures.StocksDataProviderFixture.makeStocksDataProvider;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DomainConfigurationTest {


    @DisplayName("Given " +
            "An empty list of stocks " +
            "When " +
            "A domain is configured " +
            "Then " +
            "No Pricing Service is registered")
    @ParameterizedTest()
    @MethodSource("listOfEmptyStocksProvider")
    public void testDoNothingIfHasNoStocks(List<String> stocks) {
        var mockApplicationContext = applicationContextMock();
        var mockBeanFactory = (ConfigurableBeanFactory)mockApplicationContext.getParentBeanFactory();

        var domainConfiguration = new DomainConfiguration(mockBeanFactory, mockApplicationContext);
        domainConfiguration.setStocks(stocks);

        domainConfiguration.makePricingServices();

        verify(mockBeanFactory, never()).registerSingleton(anyString(), any());

    }


    @DisplayName("Given " +
            "A random stock " +
            "When " +
            "A domain is configured " +
            "Then " +
            "The matching Pricing Services are registered")
    @Test
    public void testRegisterPricingServices() {
        var mockApplicationContext = applicationContextMock();
        var mockBeanFactory = (ConfigurableBeanFactory)mockApplicationContext.getParentBeanFactory();
        var stock = randomNYSEStock();
        var mockStocksDataProvider = makeStocksDataProvider(stock);

        when(mockApplicationContext.getBean(eq(StocksDataProvider.getStockBeanName(stock)), eq(StocksDataProvider.class))).thenReturn(mockStocksDataProvider);

        var domainConfiguration = new DomainConfiguration(mockBeanFactory, mockApplicationContext);

        domainConfiguration.setStocks(List.of(stock));

        domainConfiguration.makePricingServices();

        verify(mockBeanFactory).registerSingleton(eq(stock), any(PricingService.class));

    }


    @DisplayName("Given " +
            "A list of random stocks " +
            "When " +
            "A domain is configured " +
            "Then " +
            "The matching Pricing Services are registered")
    @Test
    public void testRegisterSeveralPricingServices() {
        var mockApplicationContext = applicationContextMock();
        var mockBeanFactory = (ConfigurableBeanFactory)mockApplicationContext.getParentBeanFactory();

        Map<String, StocksDataProvider> map = MapBuilder.<String,StocksDataProvider>map()
                .entry(randomNYSEStock(), stock -> makeStocksDataProvider((String) stock))
                .entry(randomNYSEStock(), stock -> makeStocksDataProvider((String) stock))
                .entry(randomNYSEStock(), stock -> makeStocksDataProvider((String) stock))
                .entry(randomNYSEStock(), stock -> makeStocksDataProvider((String) stock))
                .entry(randomNYSEStock(), stock -> makeStocksDataProvider((String) stock))
                .build();

        map.entrySet().stream().forEach(entry -> {
            when(mockApplicationContext
                    .getBean(eq(StocksDataProvider.getStockBeanName(entry.getKey())), eq(StocksDataProvider.class)))
            .thenReturn(entry.getValue());
        });

        var domainConfiguration = new DomainConfiguration(mockBeanFactory, mockApplicationContext);

        domainConfiguration.setStocks(map.keySet().stream().collect(Collectors.toList()));

        domainConfiguration.makePricingServices();

        map.keySet().stream().forEach(key -> verify(mockBeanFactory).registerSingleton(eq(key), any(PricingService.class)));

    }


    @DisplayName("Given " +
            "A random stock " +
            "When " +
            "A domain is configured twice " +
            "Then " +
            "The matching Pricing Service is registered only once")
    @Test
    public void testMakePricingServicesSeveralTimes() {
        var mockApplicationContext = applicationContextMock();
        var mockBeanFactory = (ConfigurableBeanFactory)mockApplicationContext.getParentBeanFactory();
        var stock = randomNYSEStock();
        var mockStocksDataProvider = makeStocksDataProvider(stock);

        when(mockApplicationContext.getBean(eq(StocksDataProvider.getStockBeanName(stock)), eq(StocksDataProvider.class))).thenReturn(mockStocksDataProvider);

        var domainConfiguration = new DomainConfiguration(mockBeanFactory, mockApplicationContext);

        domainConfiguration.setStocks(List.of(stock));

        domainConfiguration.makePricingServices();
        domainConfiguration.makePricingServices();
        domainConfiguration.makePricingServices();

        verify(mockBeanFactory, times(1)).registerSingleton(eq(stock), any(PricingService.class));

    }



    private static Stream<List<String>> listOfEmptyStocksProvider() {
        List<String> emptyList = emptyList();
        return Stream.of(emptyList, null);
    }
}
