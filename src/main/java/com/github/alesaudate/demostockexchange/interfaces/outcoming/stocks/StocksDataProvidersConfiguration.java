package com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Setter
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class StocksDataProvidersConfiguration {

    List<String> stocks;

    @Autowired
    final ConfigurableBeanFactory configurableBeanFactory;

    @PostConstruct
    public void registerProviders() {
        Optional.ofNullable(stocks).orElseGet(Collections::emptyList).stream()
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
