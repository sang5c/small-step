package com.sang5c.springrestclient.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.retry.annotation.EnableRetry
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientException
import java.time.Duration

private const val CONNECTION_TIMEOUT_SECONDS = 1L
private const val READ_TIMEOUT_SECONDS = 2L

@EnableRetry
@Configuration
class RestClientConfig {

    @Bean
    fun restClient(restClientBuilder: RestClient.Builder): RestClient {
        // Timeout settings
        val simpleClientHttpRequestFactory = SimpleClientHttpRequestFactory()
        simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(CONNECTION_TIMEOUT_SECONDS))
        simpleClientHttpRequestFactory.setReadTimeout(Duration.ofSeconds(READ_TIMEOUT_SECONDS))

        return restClientBuilder.requestFactory(simpleClientHttpRequestFactory)
            .defaultStatusHandler({ it.isError || !it.is2xxSuccessful }) { request, response ->
                log.error("http request fail.")
                log.error("request: ${request.method} ${request.uri}")
                log.error(
                    "response: ${response.statusCode} ${response.statusText} ${
                        response.body.bufferedReader().use { it.readText() }
                    }"
                )
                when {
                    response.statusCode.is4xxClientError -> throw RuntimeException("client exception")
                    response.statusCode.is5xxServerError -> throw RuntimeException("server error")
                    else -> throw RestClientException("Unexpected error")
                }
            }
            .build()
    }

    companion object {
        private val log = LoggerFactory.getLogger(RestClientConfig::class.java)
    }
}
