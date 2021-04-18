package com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.rapidapi.RapidAPIStockClient;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration("StocksDataProvidersConfiguration")
@ConditionalOnProperty("interfaces.outcoming.rapidapi.host")
@ConfigurationProperties("domain")
@EnableConfigurationProperties(LiveStocksDataProvidersConfiguration.class)
public class LiveStocksDataProvidersConfiguration extends StocksDataProvidersConfiguration{

    @Value("${interfaces.outcoming.rapidapi.key}")
    String rapidApiKey;

    @Value("${interfaces.outcoming.rapidapi.host}")
    String rapidApiHost;

    @Autowired
    public LiveStocksDataProvidersConfiguration(ConfigurableBeanFactory configurableBeanFactory) {
        super(configurableBeanFactory);
    }


    @Override
    protected StocksDataProvider createDataProvider(String stock) {
        return new RapidAPIStockClient(stock, rapidApiKey, createWebClientTemplate(), rapidApiHost);
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
