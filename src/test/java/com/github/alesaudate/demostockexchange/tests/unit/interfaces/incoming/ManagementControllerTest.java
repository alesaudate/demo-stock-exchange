package com.github.alesaudate.demostockexchange.tests.unit.interfaces.incoming;

import com.github.alesaudate.demostockexchange.domain.ManagementService;
import com.github.alesaudate.demostockexchange.interfaces.incoming.ManagementController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static com.github.alesaudate.demostockexchange.fixtures.Randoms.randomNYSEStock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ManagementController.class)
public class ManagementControllerTest {


    @MockBean
    ManagementService managementService;


    @Autowired
    ManagementController managementController;


    @DisplayName("Given " +
            "   a management controller " +
            "When " +
            "   I register a new stock " +
            "Then " +
            "   the management service gets called to register the new stock ")
    @Test
    public void testAddNewManagedStock() {

        var stock = randomNYSEStock();
        managementController.createManagedStock(stock);
        verify(managementService).addStock(eq(stock));
    }


    @DisplayName("Given " +
            "   a management controller " +
            "When " +
            "   I ask for the list of managed stocks " +
            "Then " +
            "   the management service gets called to retrieve the stocks list ")
    @Test
    public void testRetrieveManagedStocks() {


        var list = List.of(randomNYSEStock(), randomNYSEStock(), randomNYSEStock());
        when(managementService.getManagedStocksList()).thenReturn(list);

        var returnedList = managementController.getListOfManagedStocks();
        assertEquals(list, returnedList);

    }
}
