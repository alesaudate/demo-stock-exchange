package com.github.alesaudate.demostockexchange.tests.contract;

import com.github.alesaudate.demostockexchange.utils.FileUtils;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ScenarioMappingBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.time.Duration;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.matching.RequestPattern.everything;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.awaitility.Awaitility.await;

@AutoConfigureWireMock(port = WireMockConfiguration.DYNAMIC_PORT)
public abstract class WiremockUtils {

    public static void elaborateSuccessResponsesFromRapidAPI(WireMockServer wireMockServer, String initialFile, String... furtherFiles) {

        wireMockServer.resetAll();

        var listOfFiles = new ArrayList<>(List.of(initialFile));
        listOfFiles.addAll(Optional.ofNullable(furtherFiles).map(Arrays::asList).orElseGet(Collections::emptyList));

        var scenarios = buildScenarioManagementFromFiles(listOfFiles);

        var scenario = scenarios.get(0);
        wireMockServer.stubFor(createStubForRapidAPIFirstResponse(wireMockServer, scenario));

        scenarios.subList(1, scenarios.size()).stream().forEach(s -> wireMockServer.stubFor(createStubForRapidAPIResponse(wireMockServer, s)));
    }

    private static ScenarioMappingBuilder createStubForRapidAPIResponse(WireMockServer wireMockServer, ScenarioManagement scenario) {
        return createStubForRapidAPIFirstResponse(wireMockServer, scenario).whenScenarioStateIs(scenario.currentState);
    }

    private static ScenarioMappingBuilder createStubForRapidAPIFirstResponse(WireMockServer wireMockServer, ScenarioManagement scenario) {
        return get(urlPathEqualTo("/market/v2/get-quotes"))
                .withQueryParam("symbols", WireMock.equalTo("PAGS"))
                .withQueryParam("region", WireMock.equalTo("BR"))
                .inScenario(scenario.scenarioName)
                .whenScenarioStateIs(STARTED)
                .willReturn(okJson(scenario.scenarioData))
                .willSetStateTo(scenario.leadsToState);
    }

    public static void awaitForResponses(WireMockServer wireMockServer, String url, Duration time) {
        await().atMost(time).until(() ->
                wireMockServer.findRequestsMatching(everything())
                        .getRequests()
                        .stream()
                        .filter(request -> request.getUrl().startsWith(url))
                        .findAny()
                        .isPresent()
        );
    }


    /**
     * This logic will make wiremock cycle through the list of files provided
     * @param files
     * @return
     */
    private static List<ScenarioManagement> buildScenarioManagementFromFiles(List<String> files) {

        var scenarioName = UUID.randomUUID().toString();
        List<ScenarioManagement> scenarios = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            var currentFile = files.get(i);
            var nextFile = (i == files.size() - 1) ? STARTED : files.get(i + 1);
            var fileContents = FileUtils.getResponseDataForRapidAPI(currentFile);

            var scenario = new ScenarioManagement();
            scenario.scenarioName = scenarioName;
            scenario.currentState = currentFile;
            scenario.leadsToState = nextFile;
            scenario.scenarioData = fileContents;
            scenarios.add(scenario);
        }

        return scenarios;
    }


    private static class ScenarioManagement {

        String scenarioName, currentState, leadsToState, scenarioData;
    }
}
