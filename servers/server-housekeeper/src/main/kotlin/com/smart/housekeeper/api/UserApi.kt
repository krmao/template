package com.smart.housekeeper.api

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserApi {

    @RequestMapping("/")
    fun home(): String {
        return "hello world !"
    }
}
