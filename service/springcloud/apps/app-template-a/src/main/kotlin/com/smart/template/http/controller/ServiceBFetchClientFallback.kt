package com.smart.template.http.controller

import com.smart.template.http.model.HKCode
import com.smart.template.http.model.HKResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

/**
 * 加 @Component 注解避免如下错误
 * java.lang.IllegalStateException: No fallback instance of type class com.smart.template.http.controller.ServiceBFetchClientFallback found
 * for feign client APP-TEMPLATE-B
 */
@Component
class ServiceBFetchClientFallback : ServiceBFetchClient {
    override fun testBController(): ResponseEntity<HKResponse<String>> {
        return ResponseEntity(HKResponse(HKCode.ERROR_TOO_MANY_REQUESTS, "FetchClientB IS VERY BUSY"), HttpStatus.TOO_MANY_REQUESTS)
    }
}
