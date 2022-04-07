package com.smart.springcloud.security.resourceserver.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@Suppress("SpringElInspection", "ELValidationInJSP")
@EnableWebSecurity
class ResourceServerConfig {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .mvcMatcher("/messages/**")
            .authorizeRequests()
            .mvcMatchers("/messages/**")
            .access("hasAuthority('SCOPE_message.read')")
            .and()
            .oauth2ResourceServer() // 根据 application.yml 配置认证服务连接
            .jwt()
        return http.build()
    }

}
