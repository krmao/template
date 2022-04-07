package com.smart.springcloud.appa.http.controller

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand
import com.smart.springcloud.appa.http.model.HKCode
import com.smart.springcloud.appa.http.model.HKResponse
import com.smart.springcloud.library.common.base.util.STEnvironmentUtil
import org.springframework.beans.factory.annotation.Autowired
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

    @Autowired
    var environment: org.springframework.core.env.Environment? = null

    @HystrixCommand(fallbackMethod = "fallback")
    @RequestMapping("/messageA")
    fun message(): ResponseEntity<HKResponse<String>> {
        STEnvironmentUtil.toString(environment)
        return ResponseEntity(HKResponse(HKCode.OK, "I am a, from ${STEnvironmentUtil.getLocalHostNameWithHostAddressAndPort(environment)}"), HttpStatus.OK)
    }

    @RequestMapping("/callB")
    fun callB(): ResponseEntity<HKResponse<String>>? {
        STEnvironmentUtil.toString(environment)
        return serviceBFeignClient?.testBController()
    }

    fun fallback(): ResponseEntity<HKResponse<String>> {
        STEnvironmentUtil.toString(environment)
        return ResponseEntity(HKResponse(HKCode.ERROR_TOO_MANY_REQUESTS, "SERVER IS BUSY, FROM ${STEnvironmentUtil.getLocalHostNameWithHostAddressAndPort(environment)}"), HttpStatus.TOO_MANY_REQUESTS)
    }
}
