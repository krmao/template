package com.smart.housekeeper

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class HousekeeperApplication

fun main(args: Array<String>) {
    SpringApplication.run(HousekeeperApplication::class.java, *args)
}
