package com.github.alesaudate.demostockexchange.tests.unit.domain;


import com.github.alesaudate.demostockexchange.domain.DomainConfiguration;
import com.github.alesaudate.demostockexchange.domain.ManagementService;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvidersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.github.alesaudate.demostockexchange.fixtures.Randoms.randomBlankString;
import static com.github.alesaudate.demostockexchange.fixtures.Randoms.randomNYSEStock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ManagementService.class)
public class ManagementServiceTest {


    @MockBean
    DomainConfiguration domainConfiguration;

    @MockBean
    StocksDataProvidersConfiguration stocksDataProvidersConfiguration;

    @Autowired
    ManagementService managementService;

    @DisplayName("Given " +
            "   a management service " +
            "When " +
            "   I add a new managed stock " +
            "Then " +
            "   the corresponding services are registered to start monitoring the new stock")
    @Test
    public void testAddManagedStock() {
        var stock = randomNYSEStock();

        managementService.addStock(stock);
        verify(domainConfiguration).addManagedStock(eq(stock));
        verify(stocksDataProvidersConfiguration).addManagedStock(eq(stock));

    }

    @DisplayName("Given " +
            "   a management service " +
            "When " +
            "   I retrieve the list of managed stocks " +
            "Then " +
            "   it fetches the list from the domain configuration")
    @Test
    public void testRetrieveListOfManagedStocks() {
        var listOfStocks = List.of(randomNYSEStock(), randomNYSEStock(), randomNYSEStock());

        when(domainConfiguration.getStocks()).thenReturn(listOfStocks);

        var returnedList = managementService.getManagedStocksList();
        assertEquals(listOfStocks, returnedList);
    }

    @DisplayName("Given " +
            "   a management service " +
            "When " +
            "   I add a blank managed stock " +
            "Then " +
            "   no new services are registered ")
    @Test
    public void testAddEmptyManagedStock() {

        var stock = randomBlankString();

        managementService.addStock(stock);
        verify(domainConfiguration, never()).addManagedStock(eq(stock));
        verify(stocksDataProvidersConfiguration, never()).addManagedStock(eq(stock));
    }


    @DisplayName("Given " +
            "   a management service " +
            "When " +
            "   I add a null managed stock " +
            "Then " +
            "   no new services are registered ")
    @Test
    public void testAddNullManagedStock() {

        managementService.addStock(null);
        verify(domainConfiguration, never()).addManagedStock(eq(null));
        verify(stocksDataProvidersConfiguration, never()).addManagedStock(eq(null));
    }
}
