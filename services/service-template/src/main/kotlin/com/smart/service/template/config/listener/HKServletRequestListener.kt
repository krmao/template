package com.smart.service.template.config.listener

import javax.servlet.ServletRequestEvent
import javax.servlet.ServletRequestListener
import javax.servlet.annotation.WebListener

@WebListener
class HKServletRequestListener : ServletRequestListener {
    override fun requestInitialized(sre: ServletRequestEvent?) {
    }

    override fun requestDestroyed(sre: ServletRequestEvent?) {
    }
}