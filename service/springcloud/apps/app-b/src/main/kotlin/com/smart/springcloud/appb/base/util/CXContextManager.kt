package com.smart.springcloud.appb.base.util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.security.core.context.SecurityContextHolder

object CXContextManager {
    val logger: Logger = LogManager.getLogger(CXContextManager::class.java.name)

    fun printContext() {
        val authentication = SecurityContextHolder.getContext()?.authentication
        logger.error("""
            ----------
            SecurityContextHolder.getContext()?.authentication == null ? ${authentication == null}
            name                ${authentication?.name}
            principal           ${authentication?.principal}
            credentials         ${authentication?.credentials}
            authorities         ${authentication?.authorities}
            details             ${authentication?.details}
            isAuthenticated     ${authentication?.isAuthenticated}
            ----------
        """
        )
    }
}
