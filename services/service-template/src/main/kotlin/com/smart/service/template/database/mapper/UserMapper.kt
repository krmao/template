package com.smart.service.template.database.mapper

import com.smart.service.template.database.model.UserModel
import com.smart.service.template.database.provider.UserSqlProvider
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.SelectProvider

@Suppress("unused")
@Mapper
interface UserMapper {

    @Select("select * from user")
    fun list(): List<UserModel>

    @SelectProvider(type = UserSqlProvider::class, method = "listByNickName")
    fun listByNickName(nickName: String): List<UserModel>

    @Select(""" select name from user where nick_name like CONCAT(#{nickName},'%') """)
    fun listByNickNameV2(nickName: String): List<UserModel>
}
