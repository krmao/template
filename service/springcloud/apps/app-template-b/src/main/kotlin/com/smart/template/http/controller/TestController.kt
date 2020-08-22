package com.smart.template.http.controller

import com.smart.template.http.model.HKResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@EnableEurekaClient // 添加注解声明是注册中心客户端
@EnableFeignClients // 实现不同子服务调用
@RestController
@RequestMapping("/test")
class TestController {
    @Autowired
    private var serviceAFeignClient: ServiceAFetchClient? = null

    @RequestMapping("/messageB")
    fun message(): HKResponse<Any> {
        return HKResponse.ok("hello b")
    }

    @RequestMapping("/callA")
    fun callA(): HKResponse<Any> {
        return HKResponse.ok("B直接访问A->结果为:${serviceAFeignClient?.testAController()}")
    }
}
