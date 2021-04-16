package com.github.alesaudate.demostockexchange.domain;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvider;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.fake.FakeStocksDataProvider;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.PostConstruct;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@EnableConfigurationProperties(DomainConfiguration.class)
@ConfigurationProperties("domain")
@DependsOn("StocksDataProvidersConfiguration")
public class DomainConfiguration {

    List<String> stocks;

    @Autowired
    ConfigurableBeanFactory configurableBeanFactory;

    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    public void makePricingServices() {
        stocks.stream()
                .map(this::getStocksDataProvider)
                .map(PricingService::new)
                .forEach(this::registerPricingService);
    }


    private StocksDataProvider getStocksDataProvider(String stock) {
        return applicationContext.getBean(StocksDataProvider.getStockBeanName(stock), StocksDataProvider.class);
    }


    private PricingService registerPricingService(PricingService pricingService) {
        if (!configurableBeanFactory.containsSingleton(pricingService.getStock())) {
            pricingService.init();
            configurableBeanFactory.registerSingleton(pricingService.getStock(), pricingService);
        }
        return pricingService;
    }
}
