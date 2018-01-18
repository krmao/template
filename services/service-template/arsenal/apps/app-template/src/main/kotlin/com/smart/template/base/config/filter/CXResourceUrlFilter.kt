package com.smart.template.base.config.filter

import com.smart.template.base.util.CXContextManager
import org.slf4j.LoggerFactory
import org.springframework.core.annotation.Order
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter
import springfox.documentation.annotations.ApiIgnore
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

@ApiIgnore
@Order(1)
@WebFilter(filterName = "static-filter", urlPatterns = arrayOf("/*"))
class CXResourceUrlFilter : ResourceUrlEncodingFilter() {

    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, filterChain: FilterChain?) {
        val httpRequest = request as HttpServletRequest
        log.error("request stasrt " + httpRequest.requestURI)
        super.doFilter(request, response, filterChain)
        log.error("request   end " + httpRequest.requestURI)
    }
}
