@file:Suppress("unused")

package com.smart.springcloud.appa.database.mapper

import com.smart.springcloud.appa.database.model.UserModel
import org.apache.ibatis.annotations.*

/**
 * http://www.bijishequ.com/detail/512059?p=11-54-68
 * @Select(""" select name from user where nick_name like CONCAT(#{nickName},'%') """)
 * 当 insert 语句中只有一个参数的，对应的void save方法不需要做任何特殊处理（不需要加@Param也是可以对应上的），当有多个参数的时候，需要使用@Param注解进行字段的对应
 */
@Mapper
interface UserMapper {

    @Select("select * from user")
    fun findAll(): List<UserModel>

    /**
     * (start,start+count]
     */
    @Select("select * from user where roleId = #{roleId} order by userId asc limit #{start},#{count}")
    fun findAllUsersByRoleId(@Param("roleId") roleId: Int, @Param("start") start: Int, @Param("count") count: Int): List<UserModel>

    @Select("select * from user where userName = #{userName}")
    fun findByUserName(userName: String?): UserModel?

    @Insert(""" INSERT INTO cloud_housekeeper.user
                    (userName, roleId, password, email, birthday, phone, identityNo, sex, image, joinTime, loginTime)
                VALUES (
                    #{userName},
                    #{roleId},
                    #{password},
                    #{email},
                    #{birthday},
                    #{phone},
                    #{identityNo},
                    #{sex},
                    #{image},
                    #{joinTime},
                    #{loginTime}
                )
            """)
    @Options(useGeneratedKeys = true, keyProperty = "userId", keyColumn = "userId")
    fun createUser(userModel: UserModel?): Int

    /**
     * @return 受影响的行数
     */
    @Delete(""" delete from user where userId = #{userId} """)
    fun deleteUser(userId: Int?): Int

    /**
     * @return 受影响的行数
     */
    @Update(""" UPDATE cloud_housekeeper.user
                    SET
                        userName = #{userName},
                        roleId = #{roleId},
                        password = #{password},
                        email = #{email},
                        birthday = #{birthday},
                        phone = #{phone},
                        identityNo = #{identityNo},
                        sex = #{sex},
                        image = #{image},
                        joinTime = #{joinTime},
                        loginTime = #{loginTime},
                        status = #{status}
                    WHERE
                        userId = #{userId}
            """)
    fun updateUser(userModel: UserModel?): Int
}

