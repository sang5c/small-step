package com.sang5c.springrestclient.infrastructure

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class SampleApiTest(
    @Autowired private val sampleApi: SampleApi
) {

    @Test
    fun get200() {
        println(sampleApi.get200())
    }

    @Test
    fun get400() {
        println(sampleApi.get400())
    }

    @Test
    fun get500() {
        println(sampleApi.get500())
    }
}
