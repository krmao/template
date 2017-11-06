package com.smart.service.template.api


import com.smart.service.template.mapper.UserMapper
import com.smart.service.template.mapper.model.User
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user/*")
@Component
class UserApi(private val userMapper: UserMapper) {

    @GetMapping("list")
    fun list(): List<User> {
        return userMapper.list()
    }

    @GetMapping("list/{username}")
    fun listByUsername(@PathVariable("username") username: String): List<User> {
        return userMapper.listByUsername(username)
    }

    @GetMapping("get/{username}/{password}")
    operator fun get(@PathVariable("username") username: String, @PathVariable("password") password: String): User {
        return userMapper[username, password]
    }

    @GetMapping("get/bad/{username}/{password}")
    fun getBadUser(@PathVariable("username") username: String, @PathVariable("password") password: String): User {
        return userMapper.getBadUser(username, password)
    }
}
