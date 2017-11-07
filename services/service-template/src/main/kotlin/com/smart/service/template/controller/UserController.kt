package com.smart.service.template.controller


import com.smart.service.template.database.UserMapper
import com.smart.service.template.database.model.UserModel
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@Component
class UserController(private val userMapper: UserMapper) {

    @GetMapping("/list")
    fun list(): List<UserModel> {
        return userMapper.list()
    }

    @GetMapping("/list/{nickName}")
    fun listByUsername(@PathVariable("nickName") nickName: String): List<UserModel> {
        return userMapper.listByNickName(nickName)
    }

    @GetMapping("/list2/{nickName}")
    fun listByUsernameV2(@PathVariable("nickName") nickName: String): List<UserModel> {
        return userMapper.listByNickNameV2(nickName)
    }
}
