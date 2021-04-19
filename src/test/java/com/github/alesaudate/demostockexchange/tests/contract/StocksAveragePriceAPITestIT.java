package com.github.alesaudate.demostockexchange.tests.contract;

import com.github.alesaudate.demostockexchange.DemoStockExchangeApplication;
import com.github.alesaudate.demostockexchange.domain.AveragePricing;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DemoStockExchangeApplication.class)
@ActiveProfiles("contract-test")
@AutoConfigureWireMock(port = WireMockConfiguration.DYNAMIC_PORT)
public class StocksAveragePriceAPITestIT extends WiremockUtils {

    @LocalServerPort
    Integer port;

    @Autowired
    WireMockServer wireMockServer;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = port;
    }


    @DisplayName("Given " +
            "   A service that returns stock average pricings " +
            "When " +
            "   I request the average pricing for PAGS " +
            "Then " +
            "   I receive the average price ")
    @Test
    public void testGetAveragePricingData() {
        elaborateSuccessResponsesFromRapidAPI(wireMockServer, "sample-01.json");

        //Must await for a fair long time because the first request will actually fail,
        // due to the fact that Wiremock starts after the class itself - therefore,
        // after the first request to the API
        awaitForResponses(wireMockServer, "/market/v2/get-quotes", Duration.ofSeconds(35));

        given()
                .contentType(ContentType.JSON)
                .get("/stocks/PAGS")
                .then()
                .statusCode(200)
                .body("stock", equalTo("PAGS"))
                .body("datapoints", equalTo(1))
                .body("currentAverage", equalTo(Float.valueOf("46.31")))
        ;
    }


    @DisplayName("Given " +
            "   A service that streams stock average pricings " +
            "When " +
            "   I request the average pricing for PAGS " +
            "Then " +
            "   I receive a stream with average prices ")
    @Test
    public void testGetStreamOfAveragePricingData() {

        elaborateSuccessResponsesFromRapidAPI(wireMockServer, "sample-01.json", getArrayOfFiles(2,5));

        //Must await for a fair long time because the first request will actually fail,
        // due to the fact that Wiremock starts after the class itself - therefore,
        // after the first request to the API
        awaitForResponses(wireMockServer, "/market/v2/get-quotes", Duration.ofSeconds(35));

        WebClient webClient = WebClient.builder().baseUrl(String.format("http://localhost:%d", port)).build();

        var averagePrices = webClient.get()
                .uri("/stocks/PAGS/stream")
                .retrieve()
                .bodyToFlux(Map.class)
                .buffer(5)
                .blockFirst();

        assertEquals(5, averagePrices.size());

        assertEquals(1, averagePrices.get(0).get("datapoints"));
        assertEquals(46.31, averagePrices.get(0).get("currentAverage"));

        assertEquals(2, averagePrices.get(1).get("datapoints"));
        assertEquals(46.33, averagePrices.get(1).get("currentAverage"));

        assertEquals(3, averagePrices.get(2).get("datapoints"));
        assertEquals(46.35, averagePrices.get(2).get("currentAverage"));

        assertEquals(4, averagePrices.get(3).get("datapoints"));
        assertEquals(46.36, averagePrices.get(3).get("currentAverage"));

        assertEquals(5, averagePrices.get(4).get("datapoints"));
        assertEquals(46.37, averagePrices.get(4).get("currentAverage"));
    }


    private String[] getArrayOfFiles(int starting, int end) {
        return IntStream
                .rangeClosed(starting, end)
                .boxed()
                .map(n -> String.format("sample-%s.json", leftPad(String.valueOf(n), 2, '0')))
                .collect(Collectors.toList())
                .stream()
                .toArray(n -> new String[n]);
    }








}
