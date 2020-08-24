package com.smart.springcloud.appb.base.config.security.auth.oauth

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer


@Suppress("SpringKotlinAutowiredMembers")
//@Configuration
class CXAuthorizationServerConfigurerAdapter : AuthorizationServerConfigurerAdapter() {

    private val logger: Logger = LogManager.getLogger(CXAuthorizationServerConfigurerAdapter::class.java.name)

    private val resourceId = "clientA"

    @Autowired
    private var authenticationManager: AuthenticationManager? = null

    @Value("\${clientA.oauth.tokenTimeout:30}")
    private val expiration: Int = 0

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        logger.error("configure with clients:" + clients)
        logger.error("configure with clients:expiration:" + expiration)
        clients?.inMemory()?.withClient("clientA")?.resourceIds(resourceId)?.authorizedGrantTypes("password", "refresh_token")?.accessTokenValiditySeconds(expiration)?.scopes("read", "write")?.authorities("krmao")?.secret("123456")
    }

    @Throws(Exception::class)
    override fun configure(endpointsConfigurer: AuthorizationServerEndpointsConfigurer?) {
        logger.error("configure with endpointsConfigurer:" + endpointsConfigurer)
        endpointsConfigurer?.authenticationManager(authenticationManager)
        //endpointsConfigurer.tokenStore(new RedisTokenStore(redisConnectionFactory));
        //endpointsConfigurer.userDetailsService(userDetailsService);
    }

    @Throws(Exception::class)
    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer?) {
        logger.error("configure with oauthServer:" + oauthServer)
        oauthServer?.allowFormAuthenticationForClients()//允许表单认证
    }
}
