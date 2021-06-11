package com.smart.springcloud.appa.base.config.config

import com.smart.springcloud.appa.base.config.security.auth.token.jwt.JWTConfig
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

    override fun toString(): String {
        return """
            --
            jwt:$jwt
            --
            """
    }
}
