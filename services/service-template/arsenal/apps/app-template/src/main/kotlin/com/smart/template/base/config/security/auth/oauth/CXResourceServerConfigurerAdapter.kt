package com.smart.template.base.config.security.auth.oauth

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer


/**
 * http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html
 */
//@Configuration
class CXResourceServerConfigurerAdapter : ResourceServerConfigurerAdapter() {
    private val logger: Logger = LogManager.getLogger(CXResourceServerConfigurerAdapter::class.java.name)

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        logger.warn("configure with resources:" + resources)
        resources?.stateless(true)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        logger.warn("configure with http:" + http)
        http
            // Since we want the protected resources to be accessible in the UI as well we need
            // session creation to be allowed (it's disabled by default in 2.0.6)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
            .authorizeRequests()
            //http://d.hiphotos.baidu.com/image/pic/item/d833c895d143ad4b3ae286d88e025aafa50f06de.jpg
            .antMatchers("/static/image/*").permitAll()
            .and()
            .requestMatchers()
            .anyRequest()
        // .antMatchers("/product/**").access("#oauth2.hasScope('select') and hasRole('ROLE_USER')")
//            .and()
//            .anonymous().disable()
        //.requestMatchers().anyRequest()
    }
}
