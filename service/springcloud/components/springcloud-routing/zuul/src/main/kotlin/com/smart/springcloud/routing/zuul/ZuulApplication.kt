package com.smart.springcloud.routing.zuul

import com.smart.springcloud.routing.zuul.filter.TokenFilter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.zuul.EnableZuulProxy
import org.springframework.context.annotation.Bean

/**
 * 用于外部收口访问内部 eureka-clients
 * 网关就是做一下过滤或拦截操作 让我们的服务更加安全
 * 用户访问我们服务的时候就要先通过网关 然后再由网关转发到我们的微服务
 *
 * 运行后访问 http://127.0.0.1:5390/service-a/app-template-a/test/callB?token=123 成功请求A的服务
 * 从而隐藏 app-template-a/app-template-b/eureka-server 的真实地址
 *
 * 这种网关转发之后的请求 就叫做反向代理你可以隐藏你本地的服务器的真实地址
 * 只暴露给外界网关的地址 然后由网关转发给服务器 从而做到安全性更高
 */
@EnableZuulProxy // 开启网关
@EnableEurekaClient
@SpringBootApplication
class ZuulApplication

fun main(args: Array<String>) {
    runApplication<ZuulApplication>(*args)

    /**
     * 实例化 tokenFilter, 否则网关不生效
     */
    @Bean
    fun tokenFilter(): TokenFilter {
        return TokenFilter()
    }
}
