package com.smart.springcloud.appa

import com.fasterxml.jackson.databind.ObjectMapper
import com.smart.springcloud.appa.base.util.CXJsonUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.netflix.hystrix.EnableHystrix
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

//@EnableResourceServer
//@MapperScan("com.smart.springcloud.appa.database.mapper")
@EnableHystrix      // 开启 Hystrix
@EnableEurekaClient // 开启 Eureka
@EnableFeignClients(basePackages = ["com.smart.springcloud.appa.http.controller"]) // 开启 Feign
@ServletComponentScan           // 开启 自动扫描 Component
@SpringBootApplication
@ConfigurationPropertiesScan
/*(exclude = arrayOf(DataSourceAutoConfiguration::class, DruidDataSourceAutoConfigure::class))*/ // 开启 SpringBoot
@EnableConfigurationProperties  // 开启 local.properties 中读取的变量自动绑定到 application.yml 中
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) // 开启 安全策略
class AppAApplication : SpringBootServletInitializer() {

    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        return builder.sources(AppAApplication::class.java)
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return CXJsonUtil.JSON
    }
}

//@EnableAuthorizationServer

fun main(args: Array<String>) {
    val logger: Logger = LoggerFactory.getLogger(AppAApplication::class.java.name)
    logger.error("🔵▶️ >>>>>>>>>>==============================>>>>>>>>>>️")
    logger.error("🔵▶️ 入口第一步️")
    logger.error("🔵▶️ a. 创建/初始化/配置并运行应用程序 new SpringApplication('com.smart.springcloud.appa.CXApplication').run()")
    logger.error("🔵▶️ b. 初始化 SpringApplication#initialize")
    logger.error("🔵▶️ b.1 设置初始化器 SpringApplication#setInitializers")
    logger.error("🔵▶️ b.2 设置监听器 SpringApplication#setListeners")
    logger.error("🔵▶️ c. 运行 Spring 应用 SpringApplication#run 创建并返回 ApplicationContext")
    logger.error("🔵▶️ >>>>>>>>>>==============================>>>>>>>>>>")
    SpringApplication.run(AppAApplication::class.java, *args)
    logger.error("🔴◀️ <<<<<<<<<<==============================<<<<<<<<<<️")
    logger.error("🔴◀️ 入口第一步")
    logger.error("🔴◀️ <<<<<<<<<<==============================<<<<<<<<<<️")
}
