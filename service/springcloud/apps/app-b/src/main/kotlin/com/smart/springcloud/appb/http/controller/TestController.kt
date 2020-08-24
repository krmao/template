package com.smart.springcloud.appb.http.controller

import com.smart.springcloud.appb.http.model.HKCode
import com.smart.springcloud.appb.http.model.HKResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController {
    @Autowired
    private var serviceAFeignClient: ServiceAFetchClient? = null

    @RequestMapping("/messageB")
    fun message(): ResponseEntity<HKResponse<String>> {
        return ResponseEntity(HKResponse(HKCode.OK, "hello b"), HttpStatus.OK)
    }

    @RequestMapping("/callA")
    fun callA(): HKResponse<Any> {
        return HKResponse.ok("B直接访问A->结果为:${serviceAFeignClient?.testAController()}")
    }
}
