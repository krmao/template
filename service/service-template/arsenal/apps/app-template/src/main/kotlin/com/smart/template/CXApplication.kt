package com.smart.template

import com.fasterxml.jackson.databind.ObjectMapper
import com.smart.template.base.util.CXJsonUtil
import com.smart.template.http.controller.TestController
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
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
class CXApplication : SpringBootServletInitializer() {

    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        return builder.sources(CXApplication::class.java)
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return CXJsonUtil.JSON
    }
}

fun main(args: Array<String>) {
    val logger: Logger = LogManager.getLogger(TestController::class.java.name)
    logger.debug("🔵▶️ 入口第一步️")
    SpringApplication.run(CXApplication::class.java, *args)
    logger.debug("🔴◀️ 入口第一步")
}
