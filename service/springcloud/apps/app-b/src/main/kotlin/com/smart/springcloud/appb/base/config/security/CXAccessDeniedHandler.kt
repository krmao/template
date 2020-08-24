package com.smart.springcloud.appb.base.config.security

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CXAccessDeniedHandler : AccessDeniedHandler {

    private val logger: Logger = LogManager.getLogger(CXAccessDeniedHandler::class.java.name)

    override fun handle(request: HttpServletRequest?, response: HttpServletResponse?, accessDeniedException: AccessDeniedException?) {
        logger.error("[权限不足,拒绝访问] ${request?.requestURL}", accessDeniedException)
        response?.sendError(HttpStatus.NOT_ACCEPTABLE.value(), "权限不足,拒绝访问; ${accessDeniedException?.message}")
    }

}

