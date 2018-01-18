package com.smart.template

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.smart.template.base.util.CXJsonUtil
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity


@ServletComponentScan
@SpringBootApplication
//@EnableResourceServer
//@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class ServiceHousekeeperApplication : SpringBootServletInitializer() {

    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        return builder.sources(ServiceHousekeeperApplication::class.java)
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return CXJsonUtil.JSON
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ServiceHousekeeperApplication::class.java, *args)
}
