package com.smart.template.http.controller

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.smart.template.http.model.HKCode
import com.smart.template.http.model.HKResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
@RequestMapping("/test")
class TestController {
    @Autowired
    private var serviceBFeignClient: ServiceBFetchClient? = null

    @HystrixCommand(fallbackMethod = "fallback")
    @RequestMapping("/messageA")
    fun message(): ResponseEntity<HKResponse<String>> {
        return ResponseEntity(HKResponse(HKCode.OK, "hello a"), HttpStatus.OK)
    }

    @RequestMapping("/callB")
    fun callB(): HKResponse<Any> {
        return HKResponse.ok("A直接访问B->结果为:${serviceBFeignClient?.testBController()}")
    }

    fun fallback(): ResponseEntity<HKResponse<String>> {
        return ResponseEntity(HKResponse(HKCode.ERROR_TOO_MANY_REQUESTS, "SERVER IS BUSY"), HttpStatus.TOO_MANY_REQUESTS)
    }
}
