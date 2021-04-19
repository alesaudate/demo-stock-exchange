package com.github.alesaudate.demostockexchange.interfaces.incoming;


import com.github.alesaudate.demostockexchange.domain.AveragePricing;
import com.github.alesaudate.demostockexchange.domain.PricingServiceProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(path = "/stocks")
@AllArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StocksController {

    PricingServiceProvider pricingServiceProvider;

    @GetMapping("/{stock}")
    @Operation(description = "Retrieve the current average price for a given stock",
            parameters = {@Parameter(name = "stock", example = "PAGS")}
    )
    public AveragePricing getAveragePricingOfStock(@PathVariable("stock") String stock) {
        return pricingServiceProvider.getPricingService(stock).getAveragePricing();
    }


    @Operation(description = "Streams the current average prices for a given stock",
            parameters = {@Parameter(name = "stock", example = "PAGS")}
    )
    @GetMapping(path = "/{stock}/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<AveragePricing> streamData(@PathVariable("stock") String stock) {
        return Flux.from(pricingServiceProvider.getPricingService(stock).streamData());
    }
}
