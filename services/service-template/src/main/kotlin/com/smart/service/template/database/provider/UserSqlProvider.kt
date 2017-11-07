package com.smart.service.template.database.provider

import org.slf4j.LoggerFactory

/**
 * 主要用途：根据复杂的业务需求来动态生成SQL.
 * 目标：使用Java工具类来替代传统的XML文件.(例如：UserSqlProvider.java <-- UserMapper.xml)
 */
@Suppress("unused")
class UserSqlProvider {

    private val log = LoggerFactory.getLogger(UserSqlProvider::class.java)!!

    fun listByNickName(nickName: String): String {
        log.debug("listByNickName:$nickName")
        return "select * from user where nick_name like CONCAT(#{nickName},'%')"
    }
}
