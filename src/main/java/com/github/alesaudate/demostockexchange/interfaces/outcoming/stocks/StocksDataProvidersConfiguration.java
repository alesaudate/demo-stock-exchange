package com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.fake.FakeStocksDataProvider;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.rapidapi.RapidAPIStockClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
@Configuration("StocksDataProvidersConfiguration")
@ConfigurationProperties("domain")
@EnableConfigurationProperties(StocksDataProvidersConfiguration.class)
public class StocksDataProvidersConfiguration {

    List<String> stocks;


    @Value("${interfaces.outcoming.rapidapi.key}")
    String rapidApiKey;


    @Autowired
    ConfigurableBeanFactory configurableBeanFactory;

    @PostConstruct
    public void registerProviders() {
        stocks.stream()
                .map(this::createDataProvider)
                .forEach(this::registerStockProvider);
    }

    private StocksDataProvider createDataProvider(String stock) {
        return rapidAPIStockClient(stock);
    }

    private RapidAPIStockClient rapidAPIStockClient(String stock) {
        return new RapidAPIStockClient(stock, rapidApiKey, createWebClientTemplate());
    }

    private FakeStocksDataProvider fakeStocksDataProvider(String stock) {
        return new FakeStocksDataProvider(stock);
    }

    private void registerStockProvider(StocksDataProvider stocksDataProvider) {
        if (!configurableBeanFactory.containsSingleton(StocksDataProvider.getStockBeanName(stocksDataProvider.getStock()))) {
            configurableBeanFactory.registerSingleton(StocksDataProvider.getStockBeanName(stocksDataProvider.getStock()), stocksDataProvider);
        }
    }

    private WebClient.Builder createWebClientTemplate() {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn
                                .addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)
                        )
                );

        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
    }
}
