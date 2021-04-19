package com.github.alesaudate.demostockexchange.domain;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvidersConfiguration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(onConstructor_ = @Autowired)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagementService {

    DomainConfiguration domainConfiguration;

    StocksDataProvidersConfiguration stocksDataProvidersConfiguration;

    public void addStock(String stock) {
        stocksDataProvidersConfiguration.addManagedStock(stock);
        domainConfiguration.addManagedStock(stock);
    }

    public List<String> getManagedStocksList() {
        return domainConfiguration.getStocks();
    }

}
