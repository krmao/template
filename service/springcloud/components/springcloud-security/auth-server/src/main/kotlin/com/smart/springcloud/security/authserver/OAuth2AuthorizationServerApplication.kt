package com.smart.springcloud.security.authserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class OAuth2AuthorizationServerApplication

fun main(args: Array<String>) {
    runApplication<OAuth2AuthorizationServerApplication>(*args)
}
