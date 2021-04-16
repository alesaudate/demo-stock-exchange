package com.github.alesaudate.demostockexchange.interfaces.incoming;


import com.github.alesaudate.demostockexchange.domain.AveragePricing;
import com.github.alesaudate.demostockexchange.domain.PricingServiceProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/stocks")
@AllArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StocksController {

    PricingServiceProvider pricingServiceProvider;

    @GetMapping("/{stock}")
    public AveragePricing getAveragePricingOfStock(@PathVariable("stock") String stock) {
        return pricingServiceProvider.getPricingService(stock).getAveragePricing();
    }
}
