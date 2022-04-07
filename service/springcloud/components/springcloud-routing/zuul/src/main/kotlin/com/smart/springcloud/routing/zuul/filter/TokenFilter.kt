package com.smart.springcloud.routing.zuul.filter

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import org.springframework.stereotype.Component

@Component
class TokenFilter : ZuulFilter() {
    /**
     * 过滤器类型 pre表示在请求之前进行逻辑操作
     */
    override fun filterType(): String {
        return "pre"
    }

    /**
     * 过滤器执行顺序
     * 当一个请求在同一个阶段存在多个过滤器的时候 过滤器的执行顺序
     */
    override fun filterOrder(): Int {
        return 0
    }

    /**
     * 是否开启过滤
     */
    override fun shouldFilter(): Boolean {
        return true
    }

    /**
     * 编写过滤器拦截业务逻辑代码
     */
    override fun run(): Any? {
        val currentContext = RequestContext.getCurrentContext()
        val request = currentContext.request
        val token = request.getParameter("token")
        if (token == null) {
            currentContext.setSendZuulResponse(false)
            currentContext.responseBody = "token is null"
            currentContext.responseStatusCode = 401
        }
        return null
    }
}
