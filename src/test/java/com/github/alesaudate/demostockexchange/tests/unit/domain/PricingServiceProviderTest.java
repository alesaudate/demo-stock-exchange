package com.github.alesaudate.demostockexchange.tests.unit.domain;

import com.github.alesaudate.demostockexchange.domain.PricingService;
import com.github.alesaudate.demostockexchange.domain.PricingServiceProvider;
import com.github.alesaudate.demostockexchange.domain.exceptions.StockNotMonitoredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static com.github.alesaudate.demostockexchange.fixtures.Randoms.randomNYSEStock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PricingServiceProvider.class)
public class PricingServiceProviderTest {


    @MockBean
    ApplicationContext mockApplicationContext;

    @DisplayName("Given " +
            "   Some random stock " +
            "And " +
            "   A pricing service exists for this stock " +
            "When " +
            "   I ask for the pricing service for this stock" +
            "Then " +
            "   I receive the pricing service")
    @Test
    public void testGetExistingPricingService() {
        var stock = randomNYSEStock();
        var mockPricingService = mock(PricingService.class);
        var pricingServiceProvider = new PricingServiceProvider(mockApplicationContext);
        when(mockApplicationContext.getBean(eq(stock), eq(PricingService.class))).thenReturn(mockPricingService);

        var foundPricingService = pricingServiceProvider.getPricingService(stock);
        assertEquals(mockPricingService, foundPricingService);
        verify(mockApplicationContext, atMostOnce()).getBean(eq(stock), eq(PricingService.class));
    }


    @DisplayName("Given " +
            "   Some random stock " +
            "And " +
            "   A pricing service does not exist for this stock " +
            "When " +
            "   I ask for the pricing service for this stock" +
            "Then " +
            "   I receive a StockNotMonitoredException")
    @Test
    public void testGetNonExistentPricingService() {
        var stock = randomNYSEStock();
        var mockPricingService = mock(PricingService.class);
        var pricingServiceProvider = new PricingServiceProvider(mockApplicationContext);
        when(mockApplicationContext.getBean(eq(stock), eq(PricingService.class))).thenThrow(new NoSuchBeanDefinitionException(""));

        assertThrows(StockNotMonitoredException.class, () -> pricingServiceProvider.getPricingService(stock));
        verify(mockApplicationContext, atMostOnce()).getBean(eq(stock), eq(PricingService.class));
    }

}
