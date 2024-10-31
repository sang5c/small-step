package com.sang5c.springrestclient.infrastructure

import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.io.IOException

@Component
class SampleApi(
    private val restClient: RestClient
) {

    fun get200(): String {
        return restClient.get()
            .uri("http://localhost:8080/200")
            .retrieve()
            .body(String::class.java)!!
    }

    @Retryable(
        maxAttempts = 5,
        backoff = Backoff(
            delay = 1000,
            multiplier = 1.5,
            maxDelay = 2000,
        ),
    )
    fun get400(): String {
        return restClient.get()
            .uri("http://localhost:8080/400")
            .retrieve()
            .body(String::class.java)!!
    }

    fun get500(): String {
        return restClient.get()
            .uri("http://localhost:8080/500")
            .retrieve()
            .body(String::class.java)!!
    }
}
