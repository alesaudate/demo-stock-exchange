package com.github.alesaudate.demostockexchange.interfaces.incoming;


import com.github.alesaudate.demostockexchange.domain.AveragePricing;
import com.github.alesaudate.demostockexchange.domain.PricingServiceProvider;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/stocks")
@AllArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StocksController {

    PricingServiceProvider pricingServiceProvider;

    SimpMessagingTemplate messagingTemplate;

    @GetMapping("/{stock}")
    public AveragePricing getAveragePricingOfStock(@PathVariable("stock") String stock) {
        return pricingServiceProvider.getPricingService(stock).getAveragePricing();
    }

    @GetMapping(path = "/{stock}/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AveragePricing> streamPricing(@PathVariable("stock") String stock) {
        return pricingServiceProvider.getPricingService(stock).streamAverage();
    }

    @PostMapping(path = "/{stock}/websockets")
    public void startWebsockets(@PathVariable("stock") String stock) {
        streamPricing(stock).subscribe(price -> {
           messagingTemplate.convertAndSend("/websockets/stocks/" + stock, price);
        });
    }


}
