package com.smart.springcloud.library.common.base.config.listener

import org.slf4j.LoggerFactory
import javax.servlet.ServletRequestEvent
import javax.servlet.ServletRequestListener
import javax.servlet.annotation.WebListener

@WebListener
class STServletRequestListener : ServletRequestListener {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun requestInitialized(servletRequestEvent: ServletRequestEvent?) {
        var count = servletRequestEvent?.servletContext?.getAttribute("count") as? Int ?: 0
        log.info("****|||| 一次请求开始  历史访问次数:$count")
        servletRequestEvent?.servletContext?.setAttribute("count", ++count)
    }


    override fun requestDestroyed(servletRequestEvent: ServletRequestEvent?) {
        log.info("****|||| 一次请求结束  当前访问次数:" + servletRequestEvent?.servletContext?.getAttribute("count"))
    }

}
