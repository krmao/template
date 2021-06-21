package com.smart.springcloud.appa.base.config.security.auth.token.jwt

import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.context.properties.bind.DefaultValue

@ConstructorBinding
data class JWTConfig(
    /**
     * 密码(注释将生成在 spring-configuration-metadata.json 中 description 字段)
     */
    @DefaultValue("")
    val secret: String,
    @DefaultValue("")
    val header: String,
    @DefaultValue("")
    val tokenPrefix: String,
    @DefaultValue("")
    @NestedConfigurationProperty
    val accessToken: TokenConfig,
    @DefaultValue("")
    @NestedConfigurationProperty
    val refreshToken: TokenConfig,
    @DefaultValue("")
    @NestedConfigurationProperty
    val authRoute: AuthRouteConfig
) {
    @ConstructorBinding
    data class TokenConfig(
        @DefaultValue("3600")
        val expiration: Long
    )

    @ConstructorBinding
    data class AuthRouteConfig(
        @DefaultValue("")
        val login: String,
        @DefaultValue("")
        val register: String,
        @DefaultValue("")
        val refreshToken: String
    )
}
