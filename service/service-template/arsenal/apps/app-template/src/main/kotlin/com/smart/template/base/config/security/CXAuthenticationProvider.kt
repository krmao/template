package com.smart.template.base.config.security

import com.smart.template.base.config.security.user.CXUserDetailService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetails

class CXAuthenticationProvider(private var userDetailService: CXUserDetailService?) : AuthenticationProvider {
    val logger: Logger = LogManager.getLogger(CXAuthenticationProvider::class.java.name)

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication? {
        val username = authentication.principal
        val password = authentication.credentials
        val authorities = authentication.authorities
        logger.warn("[authenticate] username=$username , password=$password , authorities=$authorities  class=${authentication.javaClass.name}")

        // http://www.jianshu.com/p/42745873ebfb
        // 根据用户输入的用户名获取该用户名已经在服务器上存在的用户详情，如果没有则返回null
        val userDetails: UserDetails? = this.userDetailService?.loadUserByUsername(authentication.name)
        logger.warn("""
            username=${userDetails?.username}
            password=${userDetails?.password}
            authorities=${userDetails?.authorities}
            class=${authentication.javaClass.name}
        """)

        if (authentication.credentials == userDetails?.password) {
            logger.info("认证成功")
            return UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails?.password,
                userDetails?.authorities)
        } else {
            logger.info("认证失败")
        }
        return null
    }

    override fun supports(authentication: Class<*>): Boolean {
        val supports = UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
        logger.error(">>>> supports=true class=${authentication.name}")
        return supports
    }
}
