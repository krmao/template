package com.smart.springcloud.library.common.base.util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.core.env.Environment
import java.net.InetAddress

/**
 * @Autowired var environment: org.springframework.core.env.Environment? = null
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
object STEnvironmentUtil {
    private val logger: Logger by lazy { LogManager.getLogger(STEnvironmentUtil::class.java.name) }

    fun getPort(environment: Environment?): String? {
        val port = environment?.getProperty("local.server.port")
        logger.error("🔵▶️ port=$port️")
        return port
    }

    fun getLocalHostAddress(): String {
        val localHostAddress = InetAddress.getLocalHost().hostAddress
        logger.error("🔵▶️ localHostAddress=$localHostAddress️")
        return localHostAddress
    }

    fun getLocalHostName(): String {
        val localHostName = InetAddress.getLocalHost().hostName
        logger.error("🔵▶️ localHostName=$localHostName️")
        return localHostName
    }

    fun getLoopbackHostAddress(): String {
        val loopbackHostAddress = InetAddress.getLoopbackAddress().hostAddress
        logger.error("🔵▶️ loopbackHostAddress=$loopbackHostAddress️")
        return loopbackHostAddress
    }

    fun getLoopbackHostName(): String {
        val loopbackHostName = InetAddress.getLoopbackAddress().hostName
        logger.error("🔵▶️ loopbackHostName=$loopbackHostName️")
        return loopbackHostName
    }

    fun getLocalHostAddressWithPort(environment: Environment?): String {
        return "${getLocalHostAddress()}:${getPort(environment)}"
    }

    fun getLocalHostNameWithHostAddressAndPort(environment: Environment?): String {
        return "${getLocalHostName()}:${getLocalHostAddress()}:${getPort(environment)}"
    }

    fun getLoopbackHostAddressWithPort(environment: Environment?): String {
        return "${getLoopbackHostAddress()}:${getPort(environment)}"
    }

    fun getLoopbackHostNameWithHostAddressAndPort(environment: Environment?): String {
        return "${getLoopbackHostName()}:${getLoopbackHostAddress()}:${getPort(environment)}"
    }

    fun toString(environment: Environment?): String {
        val tmpString = "(localHostNameAddress=${InetAddress.getLocalHost().hostName}:${InetAddress.getLocalHost().hostAddress}, loopbackHostNameAddress=${InetAddress.getLoopbackAddress().hostName}:${InetAddress.getLoopbackAddress().hostAddress}, port=${environment?.getProperty("local.server.port")})"
        logger.error("🔵▶️ $tmpString️")
        return tmpString
    }
}
