package com.smart.springcloud.appb.http.controller

import com.smart.springcloud.appb.http.model.HKResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping


/**
 * 填入注册中心中的应用名, 也就是要调用的微服务的应用名
 * 在eureka页面中可以找到
 */
@FeignClient("APP-A", path = "/app-a")
interface ServiceAFetchClient {
    @RequestMapping("/test/messageA")
    fun testAController(): ResponseEntity<HKResponse<String>>
}
