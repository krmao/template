package com.smart.springcloud.security.authserver.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class DefaultSecurityConfig {
    /**
     * 定义 spring security 拦截规则
     */
    @Bean
    @Throws(Exception::class)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeRequests { authorizeRequests ->
            // 任何请求都需要认证
            authorizeRequests.anyRequest().authenticated()
        }.formLogin(Customizer.withDefaults()).build()
    }

    /**
     * 创建默认登录的用户
     */
    @Bean
    fun users(): UserDetailsService {
        @Suppress("DEPRECATION")
        val user = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("password")
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }
}
