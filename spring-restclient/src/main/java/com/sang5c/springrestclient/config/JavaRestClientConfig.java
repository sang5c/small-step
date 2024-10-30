package com.sang5c.springrestclient.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Configuration
public class JavaRestClientConfig {

    private static final int CONNECTION_TIMEOUT_SECONDS = 1;
    private static final int READ_TIMEOUT_SECONDS = 5;

    private static final Logger log = LoggerFactory.getLogger(JavaRestClientConfig.class);

    @Bean
    public RestClient javaRestClient(RestClient.Builder restClientBuilder) {
        // Timeout settings
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT_SECONDS));
        requestFactory.setReadTimeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS));

        return restClientBuilder
                .requestFactory(requestFactory)
                .defaultStatusHandler(
                        statusCode -> statusCode.is4xxClientError() || statusCode.is5xxServerError(),
                        (request, response) -> {
                            log.error("HTTP request failed.");
                            log.error("Request: {} {}", request.getMethod(), request.getURI());
                            log.error("Response: {} {} {}", response.getStatusCode(), response.getStatusText(), response.getBody());

                            if (response.getStatusCode().is4xxClientError()) {
                                throw new RuntimeException("Client exception");
                            }
                            if (response.getStatusCode().is5xxServerError()) {
                                throw new RuntimeException("Server exception");
                            }
                            throw new RestClientException("Unexpected response status: " + response.getStatusCode());
                        }
                )
                .build();
    }
}
