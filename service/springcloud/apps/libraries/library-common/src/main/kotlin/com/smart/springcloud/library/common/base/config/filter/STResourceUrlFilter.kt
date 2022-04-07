package com.smart.springcloud.library.common.base.config.filter

import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

@Order(1)
@WebFilter(filterName = "static-filter", urlPatterns = ["/*"])
class STResourceUrlFilter : ResourceUrlEncodingFilter() {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun doFilter(request: ServletRequest?, response: ServletResponse, filterChain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        log.error("---------->doFilter start(${response.contentType}) " + httpRequest.requestURI)
        response.contentType = "UTF-8"

        super.doFilter(request, response, filterChain)
        log.error("---------->doFilter end(${response.contentType}) " + httpRequest.requestURI)
    }
}
