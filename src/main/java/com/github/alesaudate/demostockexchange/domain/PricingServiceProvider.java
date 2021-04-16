package com.github.alesaudate.demostockexchange.domain;


import com.github.alesaudate.demostockexchange.domain.exceptions.StockNotMonitoredException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PricingServiceProvider {

    final ApplicationContext applicationContext;

    Map<String, PricingService> pricingServiceCache = new HashMap<>();

    public PricingService getPricingService(String stock) {
        try {
            return pricingServiceCache.computeIfAbsent(stock, (s) -> findPricingService(s));
        }
        catch (NoSuchBeanDefinitionException e) {
            throw new StockNotMonitoredException();
        }
    }

    private PricingService findPricingService(String stock) {
        return applicationContext.getBean(stock, PricingService.class);
    }

}
