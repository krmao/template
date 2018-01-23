@file:Suppress("HasPlatformType")

package com.smart.template.base.config.security

import com.smart.template.base.config.security.auth.token.jwt.JWTAuthenticationFilter
import com.smart.template.base.config.security.user.CXUserDetailService
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.access.expression.SecurityExpressionHandler
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.FilterInvocation
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

/**
 * read me
 *
 * 无状态/有状态
 * http://blog.csdn.net/Jmilk/article/details/50461577
 * https://www.cnblogs.com/binyue/p/4812798.html
 * http://www.jianshu.com/p/576dbf44b2ae
 *
 * http://www.jianshu.com/p/ec9b7bc47de9
 * http://www.jianshu.com/p/beea4bc9056b
 *
 * http://www.mossle.com/docs/springsecurity3/html/authorization-common.html
 * https://stackoverflow.com/questions/29888458/spring-security-role-hierarchy-not-working-using-java-config
 * http://blog.csdn.net/u012373815/article/details/55047285 跨域拦截 POST-MAN 无法测试
 *
 * oauth
 * http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html
 * http://blog.didispace.com/spring-security-oauth2-xjf-1/
 */
@Configuration
class CXWebSecurityConfigurerAdapter : WebSecurityConfigurerAdapter() {

    private val logger: Logger = LogManager.getLogger(CXWebSecurityConfigurerAdapter::class.java.name)

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        logger.warn("====>>>>configure start")
        http
            // 由于使用的是JWT，这里不需要csrf
            .csrf().disable()

            // 基于token，所以不需要session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

            .authorizeRequests()
            .expressionHandler(webExpressionHandler())

            // 允许对于网站静态资源的无授权访问
            .antMatchers(HttpMethod.GET,
                "/",
                "/*.html",
                "/favicon.ico",
                "/**/*.json",
                "/**/*.zip",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
            ).permitAll()
            .antMatchers("/api", "/api/**").permitAll()
            .antMatchers("/file/upload").permitAll() //TODO 正式环境删除
            .antMatchers("/test/**").permitAll() //TODO 正式环境删除
            .antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**", "/swagger-ui.html", "/webjars/**")
            .permitAll()

            .antMatchers("/auth/**").permitAll()            // 对于获取token的rest api允许匿名访问
            .anyRequest().authenticated()                               // 除上面外的所有请求全部需要鉴权认证

            //添加未授权处理 && 权限不足处理
            .and()
            .exceptionHandling()
            .accessDeniedHandler(CXAccessDeniedHandler())               // 权限不足处理
            .authenticationEntryPoint(CXAuthenticationEntryPoint())     // 未授权处理

            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .headers().cacheControl()                                   // 禁用缓存

        // http.httpBasic() // 访问一个需要HTTP Basic Authentication的URL的时候，如果你没有提供用户名和密码，服务器就会返回401
        logger.warn("====>>>>configure end")
    }

    //==================================================================================================================================
    // 用户权限相关 start
    //==================================================================================================================================
    @Bean
    fun roleHierarchy(): RoleHierarchyImpl {
        val roleHierarchy = RoleHierarchyImpl()
        roleHierarchy.setHierarchy("ROLE_1 > ROLE_2 ROLE_2 > ROLE_3  ROLE_3 > ROLE_4 ROLE_4 > ROLE_5 ROLE_5 > ROLE_6 ROLE_6 > ROLE_7")
        return roleHierarchy
    }

    private fun webExpressionHandler(): SecurityExpressionHandler<FilterInvocation> {
        val defaultWebSecurityExpressionHandler = DefaultWebSecurityExpressionHandler()
        defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy())
        return defaultWebSecurityExpressionHandler
    }

    //==================================================================================================================================
    // 用户权限相关 end
    //==================================================================================================================================
    //==================================================================================================================================
    // 用户数据相关 start
    //==================================================================================================================================
    @Autowired
    private var userService: CXUserDetailService? = null
    @Autowired
    private var jwtAuthenticationFilter: JWTAuthenticationFilter? = null

    /**
     * @Bean and @Autowired do two very different things. The other answers here explain in a little more detail, but at a simpler level:
     * @Bean tells Spring 'here is an instance of this class, please keep hold of it and give it back to me when I ask'.
     * @Autowired says 'please give me an instance of this class, for example, one that I created with an @Bean annotation earlier'.
     */
    public override fun userDetailsService(): UserDetailsService? = userService

    /**
     * 加密算法	PasswordEncoder 实现类
     * http://wiki.jikexueyuan.com/project/spring-security/authenticationProvider.html
     *
     * BCryptPasswordEncoder
     * 加密 bcryptPasswordEncoder.encoder(password)
     * 解密 bcryptPasswordEncoder.matches(rawPassword,encodedPassword)
     *
     * 其它加密算法
     * plaintext	PlaintextPasswordEncoder
     * sha	        ShaPasswordEncoder
     * sha-256	    ShaPasswordEncoder，使用时new ShaPasswordEncoder(256)
     * md4	        Md4PasswordEncoder
     * md5	        Md5PasswordEncoder
     * {sha}	    LdapShaPasswordEncoder
     * {ssha}	    LdapShaPasswordEncoder
     */
    private fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Autowired  // @Autowired 对方法参数 auth 进行注入
    @Throws(Exception::class)
    private fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder())
        auth.authenticationProvider(CXAuthenticationProvider(userService))
    }
    //==================================================================================================================================
    // 用户数据相关 end
    //==================================================================================================================================
    //==================================================================================================================================
    // 跨域相关 start
    //==================================================================================================================================
    /**
     * 跨域问题解决
     * http://www.jianshu.com/p/4ef9881090ec
     * https://segmentfault.com/a/1190000012117774
     */
    @Bean
    fun corsFilter(): FilterRegistrationBean {
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.allowedOrigins = arrayListOf("*") // 设置允许的网站域名，如果全允许则设为 *    例如:"http://localhost:4200"
        config.allowedMethods = arrayListOf("GET", "POST")

        // setAllowedHeaders is important! Without it, OPTIONS preflight request
        // will fail with 403 Invalid CORS request
        config.allowedHeaders = arrayListOf("Authorization", "Cache-Control", "Content-Type");

        config.addAllowedHeader("*")
        config.addExposedHeader("Location") //http://www.jianshu.com/p/87e1ef68794c


        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        val bean = FilterRegistrationBean(CorsFilter(source))
        bean.order = 0 // 这个顺序很重要哦，为避免麻烦请设置在最前
        return bean
    }
    //==================================================================================================================================
    // 跨域相关 end
    //==================================================================================================================================

    /*@Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()


    //内存的方式与数据库的方式同时提供，则只会用内存的方式
    @Autowired
    @Throws(Exception::class)
    fun configureGlobalSecurity(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication().withUser("admin").password("admin").roles("1", "2", "3", "4", "5", "6", "7")
    }

    @Bean
    fun usernamePasswordAuthenticationFilter(): CXUsernamePasswordAuthenticationFilter {
        val tokenProcessingFilter = CXUsernamePasswordAuthenticationFilter()
        tokenProcessingFilter.setAuthenticationManager(authenticationManager())
        return tokenProcessingFilter
    }*/

}
