package com.smart.springcloud.appa.http.controller

import com.smart.springcloud.appa.http.model.HKResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping

/**
 * 填入注册中心中的应用名, 也就是要调用的微服务的应用名
 * 在eureka页面中可以找到
 */
@FeignClient("APP-B", path = "/app-b", decode404 = true, fallback = ServiceBFetchClientFallback::class)
interface ServiceBFetchClient {
    @RequestMapping("/test/messageB")
    fun testBController(): ResponseEntity<HKResponse<String>>
}