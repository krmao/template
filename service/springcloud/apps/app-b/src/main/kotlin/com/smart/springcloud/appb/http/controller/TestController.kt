package com.smart.springcloud.appb.http.controller

import com.smart.springcloud.appb.http.model.HKCode
import com.smart.springcloud.appb.http.model.HKResponse
import com.smart.springcloud.library.common.base.util.STEnvironmentUtil
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

    @Autowired
    var environment: org.springframework.core.env.Environment? = null

    @RequestMapping("/messageB")
    fun message(): ResponseEntity<HKResponse<String>> {
        STEnvironmentUtil.toString(environment)
        return ResponseEntity(HKResponse(HKCode.OK, "I am b, from ${STEnvironmentUtil.getLocalHostNameWithHostAddressAndPort(environment)}"), HttpStatus.OK)
    }

    @RequestMapping("/callA")
    fun callA(): ResponseEntity<HKResponse<String>>? {
        STEnvironmentUtil.toString(environment)
        return serviceAFeignClient?.testAController()
    }
}
