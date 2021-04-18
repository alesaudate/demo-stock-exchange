package com.github.alesaudate.demostockexchange.fixtures;

import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebClientMockProvider {

    public static WebClient.Builder mockWebClientBuilder(String response) {
        var webClientBuilder = mock(WebClient.Builder.class);
        var webClient = mockWebClient(response);

        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        return webClientBuilder;
    }


    public static WebClient mockWebClient(String response) {
        var webClient = mock(WebClient.class);
        var requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        var responseSpec = mock(WebClient.ResponseSpec.class);
        var responseEntity = mock(ResponseEntity.class);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(Function.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.header(anyString(), anyString())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.toEntity(eq(String.class))).thenReturn(Mono.just(responseEntity));
        when(responseEntity.getBody()).thenReturn(response);

        return webClient;
    }

}
