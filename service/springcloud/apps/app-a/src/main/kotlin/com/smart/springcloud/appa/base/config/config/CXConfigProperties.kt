package com.smart.springcloud.appa.base.config.config

import com.smart.springcloud.appa.base.config.security.auth.token.jwt.JWTConfig
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.NestedConfigurationProperty
import org.springframework.boot.context.properties.bind.DefaultValue

/**
 * 注意事项:
 * 1. 注释将生成在 spring-configuration-metadata.json 中 description 字段
 * 2. 使用 val 就需要 @ConstructorBinding, 使用 var 且不在构造函数中不需要
 * 3. 使用 val 且想要默认值需要 @DefaultValue("") 注解
 * 4. apply plugin: 'kotlin-kapt' & kapt "org.springframework.boot:spring-boot-configuration-processor:${rootProject.ext.springBootConfigurationProcessorVersion}" & 不需要 compile/compileOnly
 * 5. 非嵌套类使用 @NestedConfigurationProperty https://docs.spring.io/spring-boot/docs/1.5.9.RELEASE/reference/html/configuration-metadata.html#configuration-metadata-nested-properties
 * 6. 不需要 @Component 注解
 * 7. 配置文件中中需要加上 @ConfigurationProperties.prefix 前缀, 列如 codesdancing.jwt.secret=
 *
 * 参考:
 * 1. https://github.com/spring-projects/spring-boot/issues/17560
 * 2. https://github.com/spring-projects/spring-boot/issues/15397
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "codesdancing", ignoreUnknownFields = true)
data class CXConfigProperties(
    @DefaultValue("")
    @NestedConfigurationProperty
    val jwt: JWTConfig
)
