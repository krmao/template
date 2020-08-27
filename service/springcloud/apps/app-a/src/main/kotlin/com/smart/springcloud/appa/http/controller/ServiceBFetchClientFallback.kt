package com.smart.springcloud.appa.http.controller

import com.smart.springcloud.appa.http.model.HKCode
import com.smart.springcloud.appa.http.model.HKResponse
import com.smart.springcloud.library.common.base.util.STEnvironmentUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

/**
 * 加 @Component 注解避免如下错误
 * java.lang.IllegalStateException: No fallback instance of type class com.smart.springcloud.appa.http.controller.ServiceBFetchClientFallback found
 * for feign client APP-TEMPLATE-B
 */
@Component
class ServiceBFetchClientFallback : ServiceBFetchClient {
    @Autowired
    var environment: org.springframework.core.env.Environment? = null

    override fun testBController(): ResponseEntity<HKResponse<String>> {
        return ResponseEntity(HKResponse(HKCode.ERROR_TOO_MANY_REQUESTS, "FetchClientB IS VERY BUSY, FROM ${STEnvironmentUtil.getLocalHostNameWithHostAddressAndPort(environment)}"), HttpStatus.TOO_MANY_REQUESTS)
    }
}
