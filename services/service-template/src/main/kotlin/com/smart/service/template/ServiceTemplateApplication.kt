package com.smart.service.template

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.ServletComponentScan
import org.springframework.boot.web.support.SpringBootServletInitializer

@ServletComponentScan
@SpringBootApplication
class ServiceTemplateApplication : SpringBootServletInitializer() {
    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder {
        return builder.sources(ServiceTemplateApplication::class.java)
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(ServiceTemplateApplication::class.java, *args)
}
