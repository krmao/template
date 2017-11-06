package com.smart.service.template.mapper

import com.smart.service.template.mapper.model.User
import com.smart.service.template.mapper.provider.UserSqlProvider
import org.apache.ibatis.annotations.*

@Mapper
interface UserMapper {
    /**
     * 方式1：使用注解编写SQL。
     */
    @Select("select * from t_user")
    fun list(): List<User>

    /**
     * 方式2：使用注解指定某个工具类的方法来动态编写SQL.
     */
    @SelectProvider(type = UserSqlProvider::class, method = "listByUsername")
    fun listByUsername(username: String): List<User>

    /**
     * 延伸：上述两种方式都可以附加@Results注解来指定结果集的映射关系.
     *
     *
     * PS：如果符合下划线转驼峰的匹配项可以直接省略不写。
     */
    @Results(Result(property = "userId", column = "USER_ID"), Result(property = "username", column = "USERNAME"), Result(property = "password", column = "PASSWORD"), Result(property = "mobileNum", column = "PHONE_NUM"))
    @Select("select * from t_user")
    fun listSample(): List<User>

    /**
     * 延伸：无论什么方式,如果涉及多个参数,则必须加上@Param注解,否则无法使用EL表达式获取参数。
     */
    @Select("select * from t_user where username like #{username} and password like #{password}")
    operator fun get(@Param("username") username: String, @Param("password") password: String): User

    @SelectProvider(type = UserSqlProvider::class, method = "getBadUser")
    fun getBadUser(@Param("username") username: String, @Param("password") password: String): User

}