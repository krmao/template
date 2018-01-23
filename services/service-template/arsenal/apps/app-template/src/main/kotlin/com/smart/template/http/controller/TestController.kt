package com.smart.template.http.controller

import com.smart.template.http.model.HKRequest
import com.smart.template.http.model.HKResponse
import io.swagger.annotations.ApiOperation
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/test")
class TestController {
    private val logger: Logger = LogManager.getLogger(TestController::class.java.name)

    @ApiOperation("根据角色ID获取所有的用户")
    @PostMapping("/message")
    fun message(@RequestBody request: HKRequest<String>): HKResponse<Any> {

        logger.debug("----")
        logger.debug("request -->" + request)
        logger.debug("----")

        return HKResponse.ok("hello hybird")
    }
}
