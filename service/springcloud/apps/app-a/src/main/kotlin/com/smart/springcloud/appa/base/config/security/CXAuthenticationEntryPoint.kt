package com.smart.springcloud.appa.base.config.security

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class CXAuthenticationEntryPoint : AuthenticationEntryPoint {

    val logger: Logger = LogManager.getLogger(CXAuthenticationEntryPoint::class.java.name)

    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, ex: AuthenticationException) {
        logger.error("[身份验证错误,请重新登录] ${request.requestURL}", ex)
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "身份验证错误,请重新登录")
    }

}
