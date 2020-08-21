package com.smart.template.base.config.config

import com.smart.template.base.config.security.auth.token.jwt.JWTConfig
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.config.Configurator
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.io.File

/**
 * 这里对 properties 的读取 必须加 @Component 注解,不然单例 object 类 无法读取到值
 */
@Suppress("FinalClassOrFunSpring")
@Component
@ConfigurationProperties(prefix = "jwt", ignoreUnknownFields = true)
object CXConfigProperties {

    var jwt: JWTConfig = JWTConfig()

    var logging: LoggingConfig = LoggingConfig()
        set(value) {
            field = value
            Configurator.setAllLevels(LogManager.getRootLogger().name, Level.valueOf(value.level.root))
        }

    override fun toString(): String {
        return """
            --
            jwt:$jwt
            logging:$logging
            --
            """
    }

    data class LoggingConfig(val level: LoggingLevelConfig = LoggingLevelConfig())

    data class LoggingLevelConfig(val root: String = "WARN")
}
