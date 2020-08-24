package com.smart.springcloud.appb.base.config.security.auth.token.jwt

data class JWTConfig(
    var secret: String = "",
    var header: String = "",
    var tokenPrefix: String = "",
    var accessToken: TokenConfig = TokenConfig(),
    var refreshToken: TokenConfig = TokenConfig(),
    var authRoute: AuthRouteConfig = AuthRouteConfig()
) {
    data class TokenConfig(var expiration: Long = 60 * 60)
    data class AuthRouteConfig(val login: String = "", val register: String = "", val refreshToken: String = "")
}
