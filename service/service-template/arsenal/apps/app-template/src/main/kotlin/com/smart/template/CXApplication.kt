package com.smart.template

import com.fasterxml.jackson.databind.ObjectMapper
import com.smart.template.base.util.CXJsonUtil
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
    val logger: Logger = LogManager.getLogger(CXApplication::class.java.name)
    logger.error("🔵▶️ >>>>>>>>>>==============================>>>>>>>>>>️")
    logger.error("🔵▶️ 入口第一步️")
    logger.error("🔵▶️ a. 创建/初始化/配置并运行应用程序 new SpringApplication('com.smart.template.CXApplication').run()")
    logger.error("🔵▶️ b. 初始化 SpringApplication#initialize")
    logger.error("🔵▶️ b.1 设置初始化器 SpringApplication#setInitializers")
    logger.error("🔵▶️ b.2 设置监听器 SpringApplication#setListeners")
    logger.error("🔵▶️ c. 运行 Spring 应用 SpringApplication#run 创建并返回 ApplicationContext")
    logger.error("🔵▶️ >>>>>>>>>>==============================>>>>>>>>>>")
    SpringApplication.run(CXApplication::class.java, *args)
    logger.error("🔴◀️ <<<<<<<<<<==============================<<<<<<<<<<️")
    logger.error("🔴◀️ 入口第一步")
    logger.error("🔴◀️ <<<<<<<<<<==============================<<<<<<<<<<️")
}
