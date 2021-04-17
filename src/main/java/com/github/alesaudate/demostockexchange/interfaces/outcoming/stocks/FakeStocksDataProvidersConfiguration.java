package com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.fake.FakeStocksDataProvider;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration("StocksDataProvidersConfiguration")
@ConditionalOnExpression("#{environment.getProperty('interfaces.outcoming.rapidapi.host') == null}")
@ConfigurationProperties("domain")
@EnableConfigurationProperties(FakeStocksDataProvidersConfiguration.class)
public class FakeStocksDataProvidersConfiguration extends StocksDataProvidersConfiguration {
    @Override
    protected StocksDataProvider createDataProvider(String stock) {
        return new FakeStocksDataProvider(stock);
    }

}
