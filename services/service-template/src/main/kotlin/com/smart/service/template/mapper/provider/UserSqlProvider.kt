package com.smart.service.template.mapper.provider

import org.apache.ibatis.annotations.Param
import org.apache.ibatis.jdbc.SQL

/**
 * 主要用途：根据复杂的业务需求来动态生成SQL.
 *
 *
 * 目标：使用Java工具类来替代传统的XML文件.(例如：UserSqlProvider.java <-- UserMapper.xml)
 */
class UserSqlProvider {
    /**
     * 方式1：在工具类的方法里,可以自己手工编写SQL。
     */
    fun listByUsername(username: String): String {
        return "select * from t_user where username =#{username}"
    }

    /**
     * 方式2：也可以根据官方提供的API来编写动态SQL。
     */
    fun getBadUser(@Param("username") username: String?, @Param("password") password: String?): String {
        return object : SQL() {
            init {
                SELECT("*")
                FROM("t_user")
                if (username != null && password != null) {
                    WHERE("username like #{username} and password like #{password}")
                } else {
                    WHERE("1=2")
                }
            }
        }.toString()
    }
}