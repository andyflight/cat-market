package com.example.catsmarket.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    private final int responseTimeout;

    public RestClientConfig(@Value("${application.rest-client.response-timeout:5000}") int responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    @Bean("priceValidationRestClient")
    public RestClient restClient() {
        return RestClient.builder()
                .requestFactory(getClientHttpRequestFactory(responseTimeout))
                .build();
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory(int responseTimeout) {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withReadTimeout(Duration.ofMillis(responseTimeout));
        return ClientHttpRequestFactories.get(settings);
    }
}
