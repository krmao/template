package com.smart.springcloud.security.resourceserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class OAuth2ResourceServerApplication

fun main(args: Array<String>) {
    runApplication<OAuth2ResourceServerApplication>(*args)
}
