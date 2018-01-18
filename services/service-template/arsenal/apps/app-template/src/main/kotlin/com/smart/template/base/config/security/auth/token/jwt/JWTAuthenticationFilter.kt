package com.smart.template.base.config.security.auth.token.jwt

import com.smart.template.base.config.config.CXConfig
import com.smart.template.base.config.config.CXConfigProperties
import com.smart.template.base.config.security.user.CXUserDetailService
import com.smart.template.base.util.CXContextManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * JWT认证令牌过滤器
 *
 * Spring的注解是在Spring实例化的时候扫描注入，在Spring实例化完毕之后如果在new新的对象显然不受Spring管理了
 */
@Suppress("PrivatePropertyName", "SpringKotlinAutowiredMembers")
@Service
class JWTAuthenticationFilter : OncePerRequestFilter() {
    private val log: Logger = LogManager.getLogger(JWTAuthenticationFilter::class.java.name)

    /**
     * 例化后相应Class 相关的注入也就会集体失效，除非你所有步骤都用new的方式
     */
    @Autowired
    private var userService: CXUserDetailService? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        log.warn("authentication start -->")
        var token: String? = request.getHeader(CXConfigProperties.jwt.header)

        val logBuffer = StringBuffer()

        logBuffer.appendln("\n\ntoken_header = ${CXConfigProperties.jwt.header}")
        logBuffer.appendln("token_prefix = ${CXConfigProperties.jwt.tokenPrefix}")
        logBuffer.appendln("token = $token")

        if (token?.startsWith(CXConfigProperties.jwt.tokenPrefix) == true) {
            token = token.substring(CXConfigProperties.jwt.tokenPrefix.length)
            logBuffer.appendln("token has prefix -> '${CXConfigProperties.jwt.tokenPrefix}'")
            logBuffer.appendln("token(subString) = $token")
        } else {
            logBuffer.appendln("token has no prefix -> '${CXConfigProperties.jwt.tokenPrefix}'")
        }
        logBuffer.appendln("\ncheck global config : $CXConfig")

        log.warn(logBuffer)

        if (token?.isNotEmpty() == true) {
            var username: String? = null
            try {
                username = JWTUtil.getUsernameFromToken(token)
                log.warn("success to getUsernameFromToken : $username")
            } catch (exception: Exception) {
                log.warn("failure to getUsernameFromToken : ${exception.message}")
            }
            if (!StringUtils.isEmpty(username) && SecurityContextHolder.getContext().authentication == null) {
                // It is not compelling necessary to load the use details from the database. You could also store the information
                // in the token and read it from it. It's up to you ;)
                val userDetails = this.userService?.loadUserByUsername(username)
                log.warn("loadUserByUsername: userDetails=$userDetails")
                // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                // the database compellingly. Again it's up to you ;)
                if (userDetails != null && JWTUtil.validateToken(token, userDetails) == true) {
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, userDetails.password)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                    log.warn("身份验证成功:$username")
                } else {
                    log.warn("身份验证失败:查询不到用户或者token无效")
                }
            } else {
                log.warn("身份验证失败:username==$username  authentication==null?${SecurityContextHolder.getContext().authentication == null}")
            }
        } else {
            log.warn("token is empty")
        }
        log.warn("chain.doFilter start -->\n\n")
        CXContextManager.printContext()
        chain.doFilter(request, response)
        log.warn("chain.doFilter   end <--")
        CXContextManager.printContext()
        log.warn("authentication   end <--\n\n")
    }

}
