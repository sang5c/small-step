package com.sang5c.springrestclient.infrastructure

import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

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
