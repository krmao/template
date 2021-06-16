package com.smart.springcloud.discovery.eurekaserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

/**
 * 用于内部 eureka-client 之间互相访问
 */
@SpringBootApplication
//@EnableEurekaClient
@EnableEurekaServer // 开启 eureka 服务
class EurekaServerApplication

fun main(args: Array<String>) {
    runApplication<EurekaServerApplication>(*args)
}
