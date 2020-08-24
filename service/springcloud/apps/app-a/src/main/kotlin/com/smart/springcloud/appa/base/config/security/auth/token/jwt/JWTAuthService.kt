package com.smart.springcloud.appa.base.config.security.auth.token.jwt

import com.smart.springcloud.appa.base.config.security.user.CXUserDetailService
import com.smart.springcloud.appa.base.config.security.user.CXUserDetails
import com.smart.springcloud.appa.database.mapper.UserMapper
import com.smart.springcloud.appa.database.model.UserModel
import com.smart.springcloud.appa.database.model.UserModelWithToken
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

//http://www.jianshu.com/p/6307c89fe3fa
@Suppress("unused")
@Service
class JWTAuthService {
    private val logger: Logger = LogManager.getLogger(JWTAuthService::class.java.name)

    @Autowired
    private var authenticationManager: AuthenticationManager? = null

    @Suppress("PrivatePropertyName", "SpringKotlinAutowiring")
    @Autowired
    @Value("\${jwt.tokenPrefix}")
    private var token_prefix: String? = null

    @Suppress("SpringKotlinAutowiring")
    @Autowired
    private var userDetailsService: CXUserDetailService? = null

    @Suppress("SpringKotlinAutowiring")
    @Autowired
    private var userMapper: UserMapper? = null

    fun register(userToAdd: UserModel?): Int? {
        if (userToAdd == null)
            return 0
        val username = userToAdd.userName
        val userDetails = userDetailsService?.loadUserByUsername(username) as? CXUserDetails
        if (userDetails != null) {
            return null
        }
        val encoder = BCryptPasswordEncoder()
        val rawPassword = userToAdd.password
        userToAdd.password = encoder.encode(rawPassword)
        return userMapper?.createUser(userToAdd)
    }

    @Throws(AuthenticationException::class)
    fun login(username: String?, password: String?): UserModelWithToken? {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            logger.error("username or password is null")
            return null
        }
        val authentication = authenticationManager?.authenticate(UsernamePasswordAuthenticationToken(username, password))
        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = userDetailsService?.loadUserByUsername(username) as? CXUserDetails
        return if (userDetails != null) UserModelWithToken(userDetails.userModel, JWTUtil.generateAccessToken(userDetails)) else null
    }

    @Throws(AuthenticationException::class)
    fun loginV2(username: String?, password: String?): String? {
        return login(username, password)?.accessToken
    }

    @Throws(ExpiredJwtException::class, IllegalArgumentException::class, MalformedJwtException::class, SignatureException::class, UnsupportedJwtException::class)
    fun refresh(oldToken: String?): String? {
        if (token_prefix != null && !StringUtils.isEmpty(oldToken)) {
            val token = oldToken?.substring(token_prefix?.length ?: 0)
            val username = JWTUtil.getUsernameFromToken(token)
            val userDetails = userDetailsService?.loadUserByUsername(username) as? CXUserDetails
            if (JWTUtil.canTokenBeRefreshed(token, userDetails?.userModel?.lastPasswordResetTime) == true) {
                return JWTUtil.refreshToken(token)
            }
        }
        return null
    }
}
