package com.github.alesaudate.demostockexchange.domain;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.FakeStocksDataProvider;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
@EnableConfigurationProperties(DomainConfiguration.class)
@ConfigurationProperties("domain")
public class DomainConfiguration {

    List<String> stocks;

    @Autowired
    ConfigurableBeanFactory configurableBeanFactory;

    @PostConstruct
    public void makePricingServices() {
        stocks.stream()
                .map(FakeStocksDataProvider::new)
                .map(PricingService::new)
                .forEach(this::registerPricingService);
    }


    private PricingService registerPricingService(PricingService pricingService) {
        if (!configurableBeanFactory.containsSingleton(pricingService.getStock())) {
            pricingService.init();
            configurableBeanFactory.registerSingleton(pricingService.getStock(), pricingService);
        }
        return pricingService;
    }
}
