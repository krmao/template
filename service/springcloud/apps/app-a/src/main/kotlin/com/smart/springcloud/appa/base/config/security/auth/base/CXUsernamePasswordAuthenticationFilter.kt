package com.smart.springcloud.appa.base.config.security.auth.base

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.CharStreams
import com.smart.springcloud.appa.database.model.UserModel
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * 常用于表单验证
 */
@Suppress("DEPRECATION")
@Deprecated("基于SESSION 已经不被推荐使用，基于无状态的 TOKEN 认证机制请使用 JWT Filter")
class CXUsernamePasswordAuthenticationFilter : AbstractAuthenticationProcessingFilter(AntPathRequestMatcher("/auth/login", "POST")) {

    private val log: Logger = LogManager.getLogger(CXUsernamePasswordAuthenticationFilter::class.java.name)

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        log.warn("[attemptAuthentication] ${request.pathInfo}")
        var tmpUser: UserModel? = null

        if ("POST".equals(request.method, ignoreCase = true)) {
            val body = CharStreams.toString(request.reader)
            tmpUser = ObjectMapper().readValue(body, UserModel::class.java)
            log.warn("[attemptAuthentication:post request body] $body")
        } else {
            log.error("[attemptAuthentication:not post request]")
        }
        log.warn("[attemptAuthentication:tmpUser] $tmpUser")

        val authRequest = UsernamePasswordAuthenticationToken(tmpUser?.userName, tmpUser?.password)
        this.setDetails(request, authRequest)
        return this.authenticationManager.authenticate(authRequest)
    }

    private fun setDetails(request: HttpServletRequest, authRequest: UsernamePasswordAuthenticationToken) {
        authRequest.details = this.authenticationDetailsSource.buildDetails(request)
        log.warn("[setDetails]" + authRequest.details)
    }

    override fun afterPropertiesSet() {
        super.afterPropertiesSet()
        log.warn("[afterPropertiesSet] setAuthenticationFailureHandler")
        this.setAuthenticationFailureHandler(HKAuthenticationFailureHandler())
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, failed: AuthenticationException?) {
        super.unsuccessfulAuthentication(request, response, failed)
        log.warn("[unsuccessfulAuthentication]")

    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        super.successfulAuthentication(request, response, chain, authResult)
        log.warn("[successfulAuthentication]")
    }

    class HKAuthenticationFailureHandler : AuthenticationFailureHandler {
        private val log: Logger = LogManager.getLogger(HKAuthenticationFailureHandler::class.java.name)

        @Throws(IOException::class, ServletException::class)
        override fun onAuthenticationFailure(request: HttpServletRequest, response: HttpServletResponse, exception: AuthenticationException) {
            log.error("[HKAuthenticationFailureHandler:onAuthenticationFailure]")
            exception.printStackTrace()
        }
    }
}
