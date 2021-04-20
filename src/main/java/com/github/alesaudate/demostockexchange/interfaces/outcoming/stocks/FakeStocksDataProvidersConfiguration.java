package com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.fake.FakeStocksDataProvider;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration("StocksDataProvidersConfiguration")
@ConditionalOnExpression("#{environment.getProperty('interfaces.outcoming.rapidapi.host') == null}")
@ConfigurationProperties("domain")
@EnableConfigurationProperties(FakeStocksDataProvidersConfiguration.class)
@Primary
public class FakeStocksDataProvidersConfiguration extends StocksDataProvidersConfiguration {

    @Autowired
    public FakeStocksDataProvidersConfiguration(ConfigurableBeanFactory configurableBeanFactory) {
        super(configurableBeanFactory);
    }

    @Override
    protected StocksDataProvider createDataProvider(String stock) {
        return new FakeStocksDataProvider(stock);
    }

}
