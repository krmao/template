package com.smart.springcloud.security.resourceserver.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MessagesController {

    @GetMapping("/messages")
    fun getMessages(): Array<String> {
        return arrayOf("Message 1", "Message 2", "Message 3")
    }
}
