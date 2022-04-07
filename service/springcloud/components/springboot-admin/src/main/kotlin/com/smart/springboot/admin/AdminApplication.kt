package com.smart.springboot.admin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableAdminServer
@EnableEurekaClient
class AdminApplication

fun main(args: Array<String>) {
    runApplication<AdminApplication>(*args)
}
