package com.smart.housekeeper.api

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
class UserApi {

    @RequestMapping("/{id}", method = arrayOf(RequestMethod.GET))
    fun get(@PathVariable id: Long) = "User(id=$id, name=admin, password=123)"


    @RequestMapping("")
    fun home(): String = "hello api !"
}
