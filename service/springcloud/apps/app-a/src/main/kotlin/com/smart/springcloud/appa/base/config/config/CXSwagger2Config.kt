package com.smart.springcloud.appa.base.config.config

import com.fasterxml.classmate.TypeResolver
import com.google.common.collect.Lists
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.ApiKeyVehicle
import springfox.documentation.swagger.web.SecurityConfiguration
import springfox.documentation.swagger.web.UiConfiguration
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.*


/**
 * docs : http://springfox.github.io/springfox/docs/current/#configuring-the-objectmapper
 */
@Suppress("unused")
@Configuration
@EnableSwagger2
class CXSwagger2Config {
    @Autowired
    private val typeResolver: TypeResolver? = null

    private fun apiInfo(): ApiInfo {
        return ApiInfoBuilder()
            .title("后台接口文档")
            .termsOfServiceUrl("http://47.96.2.1:8080/template")
            .description(description)
            .version("1.0")
            .contact(Contact("Michael Mao", "https://github.com/krmao", "767709667@qq.com"))
            .build()
    }

    /**
    //            .globalOperationParameters(mutableListOf(
    //                ParameterBuilder().name("Authorization").description("TOKEN").defaultValue("Bearer {accessToken}")
    //                    .modelRef(ModelRef("string")).parameterType("header").required(true).build()
    //            ))
    //            .genericModelSubstitutes(HKRequest::class.java, HKResponse::class.java) 这个会不展示 父类的 version 之类的变量，请求也会出错
    //            .alternateTypeRules(newRule(
    //                typeResolver?.resolve(DeferredResult::class.java, typeResolver?.resolve(ResponseEntity::class.java, WildcardType::class.java)),
    //                typeResolver?.resolve(WildcardType::class.java))
    //            )
     */
    @Bean
    fun createRestfulApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.smart.springcloud.appa.http.controller"))
            .paths(PathSelectors.any())
            .build()
            .pathMapping("/")
            .groupName("template")
            .produces(setOf(APPLICATION_JSON_VALUE))
            .consumes(setOf(APPLICATION_JSON_VALUE))
            .directModelSubstitute(LocalDateTime::class.java, Date::class.java)
            .directModelSubstitute(OffsetDateTime::class.java, Date::class.java)
            .directModelSubstitute(Instant::class.java, Date::class.java)
            .directModelSubstitute(HttpStatus::class.java, String::class.java)
            .directModelSubstitute(LocalDate::class.java, String::class.java)
            .useDefaultResponseMessages(false)
            .enableUrlTemplating(true)
            .securityContexts(Lists.newArrayList(securityContext()))
            .securitySchemes(Lists.newArrayList(apiKey()))
    }

    private fun apiKey(): ApiKey {
        return ApiKey(AUTHORIZATION, AUTHORIZATION, "header")
    }

    private fun securityContext(): SecurityContext {
        return SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("/anyPath.*"))
            .build()
    }

    fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOfNulls<AuthorizationScope>(1)
        authorizationScopes[0] = authorizationScope
        return Lists.newArrayList(
            SecurityReference("Authorization", authorizationScopes))
    }

    @Bean
    fun security(): SecurityConfiguration {
        return SecurityConfiguration(null, null,
            null,                          // realm Needed for authenticate button to work
            null,                       // appName Needed for authenticate button to work
            "Bearer access_token",        // apiKeyValue
            ApiKeyVehicle.HEADER,
            AUTHORIZATION,                       //apiKeyName
            null)
    }

    @Bean
    fun uiConfig(): UiConfiguration {
        return UiConfiguration(
            null,                                  // url
            "list",                              // 接口列表是否默认展开 => none | list
            "alpha",                                // apiSorter         => alpha
            "schema",                     // defaultModelRendering => schema
            arrayListOf("get", "post").toTypedArray(),
            true,                             // enableJsonEditor      => true | false
            true)                          // showRequestHeaders    => true | false
        // 20000L)                                           // requestTimeout => in milliseconds, defaults to null (uses jquery xh timeout)
    }

    private val description = """
        <br>============================================================
        <br>** request
        <br>------------------------------------------------------------
        <br>head -> "Authorization":"Bearer "+TOKEN
        <br>------------------------------------------------------------
        <br>{
        <br>&nbsp&nbsp&nbsp&nbsp    "version": 1,           &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp// 版本号,   默认为1
        <br>&nbsp&nbsp&nbsp&nbsp    "platform": 1,          &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp// 平台号,   1=ANDROID, 2=IOS, 3=WECHAT, 4=PC
        <br>&nbsp&nbsp&nbsp&nbsp    "data": {} or []        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp// 数据对象  JsonObject or JsonArray
        <br>}
        <br>============================================================
        <br>** response
        <br>------------------------------------------------------------
        <br>{
        <br>&nbsp&nbsp&nbsp&nbsp    "code": 200,            &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp// 状态码    200 为OK, 其余都为错误码
        <br>&nbsp&nbsp&nbsp&nbsp    "message": "响应成功",   &nbsp&nbsp&nbsp&nbsp// 返回消息
        <br>&nbsp&nbsp&nbsp&nbsp    "data": {} or []        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp// 数据对象  JsonObject or JsonArray
        <br>}
        <br>============================================================
        <br>
    """.trimIndent()
}
