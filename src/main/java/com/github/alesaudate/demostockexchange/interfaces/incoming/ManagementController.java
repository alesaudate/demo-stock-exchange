package com.github.alesaudate.demostockexchange.interfaces.incoming;

import com.github.alesaudate.demostockexchange.domain.DomainConfiguration;
import com.github.alesaudate.demostockexchange.domain.ManagementService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/management")
@AllArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagementController {



    ManagementService managementService;

    @GetMapping("/stocks")
    public List<String> getListOfManagedStocks() {
        return managementService.getManagedStocksList();
    }

    @PostMapping("/stocks/{stock}")
    public void createManagedStock(@PathVariable("stock") String stock) {
        managementService.addStock(stock);
    }

}
