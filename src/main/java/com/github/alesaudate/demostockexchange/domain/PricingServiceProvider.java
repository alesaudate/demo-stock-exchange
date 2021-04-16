package com.github.alesaudate.demostockexchange.domain;


import com.github.alesaudate.demostockexchange.domain.exceptions.StockNotMonitoredException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PricingServiceProvider {

    ApplicationContext applicationContext;

    public PricingService getPricingService(String stock) {
        try {
            return findPricingService(stock);
        }
        catch (NoSuchBeanDefinitionException e) {
            throw new StockNotMonitoredException();
        }
    }

    private PricingService findPricingService(String stock) {
        return applicationContext.getBean(stock, PricingService.class);
    }

}
