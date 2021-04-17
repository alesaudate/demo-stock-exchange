package com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.providers.rapidapi;

import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.Stock;
import com.github.alesaudate.demostockexchange.interfaces.outcoming.stocks.StocksDataProvider;
import com.jayway.jsonpath.JsonPath;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.minidev.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RapidAPIStockClient implements StocksDataProvider {

    @Getter
    String stock;

    String rapidApiKey;

    WebClient.Builder webClientTemplate;

    String host;

    private static final String X_RAPIDAPI_HOST = "apidojo-yahoo-finance-v1.p.rapidapi.com";

    @Override
    public Flux<Stock> findStocks() {
        return Flux
                .interval(Duration.ZERO, Duration.of(30, ChronoUnit.SECONDS))
                .flatMap(n ->
                    webClientTemplate
                            .baseUrl(String.format("https://%s/", X_RAPIDAPI_HOST))
                            .build()
                            .get()
                            .uri(uriBuilder -> uriBuilder.path("/market/v2/get-quotes")
                                    .queryParam("symbols", stock)
                                    .queryParam("region", "BR")
                                    .build()
                            )
                            .header("x-rapidapi-host", X_RAPIDAPI_HOST)
                            .header("x-rapidapi-key", rapidApiKey)
                            .retrieve()
                            .toEntity(String.class)
                            .map(ResponseEntity::getBody)
                            .map(this::responseToStock)
                            .cache(Duration.of(30, ChronoUnit.SECONDS))
                            .flatMapMany(Flux::just)
                );
    }

    private Stock responseToStock(String responseBody) {
        JSONArray jsonArray = JsonPath.parse(responseBody).read("$..regularMarketPrice");
        BigDecimal stockPrice = BigDecimal.valueOf((Double) jsonArray.get(0));
        return new Stock(stock, stockPrice);
    }

}
