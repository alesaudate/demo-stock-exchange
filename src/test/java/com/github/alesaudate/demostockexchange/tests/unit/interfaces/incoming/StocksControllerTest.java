package com.github.alesaudate.demostockexchange.tests.unit.interfaces.incoming;

import com.github.alesaudate.demostockexchange.domain.PricingService;
import com.github.alesaudate.demostockexchange.domain.PricingServiceProvider;
import com.github.alesaudate.demostockexchange.domain.exceptions.StockNotMonitoredException;
import com.github.alesaudate.demostockexchange.interfaces.incoming.StocksController;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;

import static com.github.alesaudate.demostockexchange.fixtures.AveragePricingFixture.averagePricingFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = StocksController.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StocksControllerTest {


    @Autowired
    StocksController stocksController;


    @MockBean
    PricingServiceProvider pricingServiceProvider;


    @DisplayName("Given " +
            "   a stocks controller " +
            "When " +
            "   I ask for the average price of a given stock " +
            "Then " +
            "   I get the average price of that stock ")
    @Test
    public void testRetrieveAveragePrice() {
        var pricingServiceMock = mock(PricingService.class);
        var randomAveragePricing = averagePricingFixture();
        when(pricingServiceMock.getAveragePricing()).thenReturn(randomAveragePricing);

        when(pricingServiceProvider.getPricingService(randomAveragePricing.getStock())).thenReturn(pricingServiceMock);

        var responseAveragePricing = stocksController.getAveragePricingOfStock(randomAveragePricing.getStock());

        assertEquals(randomAveragePricing, responseAveragePricing);

    }

    @DisplayName("Given " +
            "   a stocks controller " +
            "When " +
            "   I ask for the average price of a given stock that is not managed by the system " +
            "Then " +
            "   I get a StockNotMonitoredException")
    @Test
    public void testRetrieveAveragePriceOfStockNotFound() {
        var randomAveragePricing = averagePricingFixture();
        when(pricingServiceProvider.getPricingService(randomAveragePricing.getStock())).thenThrow(new StockNotMonitoredException());

        assertThrows(StockNotMonitoredException.class, () -> stocksController.getAveragePricingOfStock(randomAveragePricing.getStock()));

    }

    @DisplayName("Given " +
            "   a stocks controller " +
            "When " +
            "   I ask for a stream with the average prices of a given stock " +
            "Then " +
            "   I get the average price of that stock ")
    @Test
    public void testRetrieveAveragePriceStream() {
        var pricingServiceMock = mock(PricingService.class);
        var randomAveragePricing = averagePricingFixture();
        var flux = Flux.just(randomAveragePricing);
        when(pricingServiceMock.streamData()).thenReturn(flux);

        when(pricingServiceProvider.getPricingService(randomAveragePricing.getStock())).thenReturn(pricingServiceMock);

        var responseFlux = stocksController.streamData(randomAveragePricing.getStock());
        var responseAveragePricing = responseFlux.blockFirst();

        assertEquals(randomAveragePricing, responseAveragePricing);

    }

    @DisplayName("Given " +
            "   a stocks controller " +
            "When " +
            "   I ask for a stream with the average prices of a given stock that is not managed by the system " +
            "Then " +
            "   I get a StockNotMonitoredException")
    @Test
    public void testRetrieveAveragePriceStreamOfStockNotFound() {
        var randomAveragePricing = averagePricingFixture();
        when(pricingServiceProvider.getPricingService(randomAveragePricing.getStock())).thenThrow(new StockNotMonitoredException());

        assertThrows(StockNotMonitoredException.class, () -> stocksController.streamData(randomAveragePricing.getStock()));

    }






}
