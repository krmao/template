package com.smart.springcloud.security.authserver.config

import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import com.smart.springcloud.security.authserver.jose.Jwks.generateRsa
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.ClientSettings
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings
import org.springframework.security.web.SecurityFilterChain
import java.util.*

@Configuration(proxyBeanMethods = false)
class AuthorizationServerConfig {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Throws(Exception::class)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        return http.formLogin(Customizer.withDefaults()).build()
    }

    /**
     * 创建默认的登录客户端
     */
    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("messaging-client") // 客户端 id
            .clientSecret("secret") // 客户端和服务端互信的秘钥
            .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC) // 本案例使用用户名密码做最基本的验证
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // 允许客户端生成授权码
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // 允许客户端刷新令牌
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc") // 重定向
            .redirectUri("http://127.0.0.1:8080/authorized")
            .scope(OidcScopes.OPENID) // 客户端可以拥有的授权范围
            .scope("message.read")
            .scope("message.write")
            .clientSettings { clientSettings: ClientSettings -> clientSettings.requireUserConsent(true) }
            .build()
        return InMemoryRegisteredClientRepository(registeredClient)
    }

    /**
     * 指定 token 加解密秘钥
     */
    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val rsaKey = generateRsa()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector: JWKSelector, securityContext: SecurityContext? -> jwkSelector.select(jwkSet) }
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext?>?): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    /**
     * 授权服务器唯一的发行者 URL
     */
    @Bean
    fun providerSettings(): ProviderSettings {
        return ProviderSettings().issuer("http://127.0.0.1:9000")
    }
}
