package com.smart.service.template.config.filter

import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

@Order(1)
@WebFilter(filterName = "static-filter", urlPatterns = arrayOf("/*"))
class HKResourceUrlFilter : ResourceUrlEncodingFilter() {

    val log = LoggerFactory.getLogger(this.javaClass)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, filterChain: FilterChain?) {
        super.doFilter(request, response, filterChain)
        val httpRequest = request as HttpServletRequest
        log.info("-----------------------------------")
        log.info("request:" + httpRequest.requestURI)
        log.debug("request:" + httpRequest.requestURI)
        log.warn("request:" + httpRequest.requestURI)
        log.error("request:" + httpRequest.requestURI)
        log.trace("request:" + httpRequest.requestURI)
    }
}
