package com.smart.service.template

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.ServletComponentScan

@ServletComponentScan
@SpringBootApplication
class ServiceTemplateApplication

fun main(args: Array<String>) {
    SpringApplication.run(ServiceTemplateApplication::class.java, *args)
}
