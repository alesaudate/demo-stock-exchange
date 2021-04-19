package com.github.alesaudate.demostockexchange.tests.contract;

import com.github.alesaudate.demostockexchange.DemoStockExchangeApplication;
import com.github.alesaudate.demostockexchange.utils.FileUtils;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
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

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.matching.RequestPattern.everything;
import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DemoStockExchangeApplication.class)
@ActiveProfiles("contract-test")
@AutoConfigureWireMock(port = WireMockConfiguration.DYNAMIC_PORT)
public class ServiceTestIT {

    @Autowired
    WireMockServer wireMockServer;

    @LocalServerPort
    Integer port;


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
        elaborateSuccessResponsesFromAPI("sample-01.json");

        //Must await for a fair long time because the first request will actually fail,
        // due to the fact that Wiremock starts after the class itself - therefore,
        // after the first request to the API
        await().atMost(Duration.ofSeconds(35)).until(() ->
                wireMockServer.findRequestsMatching(everything())
                        .getRequests()
                        .stream()
                        .filter(request -> request.getUrl().startsWith("/market/v2/get-quotes"))
                        .findAny()
                        .isPresent()
        );

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


    public void elaborateSuccessResponsesFromAPI(String file) {
        var fileContents = FileUtils.getResponseData(file);
        wireMockServer.stubFor(
                get(urlPathEqualTo("/market/v2/get-quotes"))
                        .withQueryParam("symbols", WireMock.equalTo("PAGS"))
                        .withQueryParam("region", WireMock.equalTo("BR"))
                        .willReturn(okJson(fileContents))
        );
    }


}
