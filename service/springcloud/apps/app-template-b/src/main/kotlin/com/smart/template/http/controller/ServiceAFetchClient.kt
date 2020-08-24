package com.smart.template.http.controller

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping


/**
 * 填入注册中心中的应用名, 也就是要调用的微服务的应用名
 * 在eureka页面中可以找到
 */
@FeignClient("APP-TEMPLATE-A", path = "/app-template-a")
interface ServiceAFetchClient {
    @RequestMapping("/test/messageA")
    fun testAController(): String?
}
