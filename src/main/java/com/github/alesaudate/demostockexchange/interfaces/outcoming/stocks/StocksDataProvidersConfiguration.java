package com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.fake.FakeStocksDataProvider;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import javax.annotation.PostConstruct;
import java.util.List;

@Data
public abstract class StocksDataProvidersConfiguration {

    List<String> stocks;

    @Autowired
    ConfigurableBeanFactory configurableBeanFactory;

    @PostConstruct
    public void registerProviders() {
        stocks.stream()
                .map(this::createDataProvider)
                .forEach(this::registerStockProvider);
    }

    protected abstract StocksDataProvider createDataProvider(String stock) ;


    private void registerStockProvider(StocksDataProvider stocksDataProvider) {
        if (!configurableBeanFactory.containsSingleton(StocksDataProvider.getStockBeanName(stocksDataProvider.getStock()))) {
            configurableBeanFactory.registerSingleton(StocksDataProvider.getStockBeanName(stocksDataProvider.getStock()), stocksDataProvider);
        }
    }


}
