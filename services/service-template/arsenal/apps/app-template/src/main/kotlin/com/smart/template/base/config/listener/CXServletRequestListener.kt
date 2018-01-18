package com.smart.template.base.config.listener

import com.smart.template.base.util.CXContextManager
import org.slf4j.LoggerFactory
import java.util.*
import javax.servlet.ServletRequestEvent
import javax.servlet.ServletRequestListener
import javax.servlet.annotation.WebListener

@WebListener
class CXServletRequestListener : ServletRequestListener {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun requestDestroyed(servletRequestEvent: ServletRequestEvent?) {
        CXContextManager.printContext()
        log.error(">>>>>>>> 一次请求结束  当前访问次数:" + servletRequestEvent?.servletContext?.getAttribute("count"))
    }

    override fun requestInitialized(servletRequestEvent: ServletRequestEvent?) {
        var count = servletRequestEvent?.servletContext?.getAttribute("count") as? Int ?: 0
        log.error(">>>>>>>> 一次请求开始  历史访问次数:" + count)
        servletRequestEvent?.servletContext?.setAttribute("count", ++count)
        CXContextManager.printContext()
    }
}
