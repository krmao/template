package com.smart.springcloud.security.authclient

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class OAuth2ClientApplication

fun main(args: Array<String>) {
    runApplication<OAuth2ClientApplication>(*args)
}
