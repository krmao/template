package com.smart.housekeeper.mapper

import com.sun.org.apache.bcel.internal.generic.Select

@Mapper
interface UserMapper {

    @Select("SELECT * FROM users")
    @Results(Result(property = "userSex", column = "user_sex", javaType = UserSexEnum::class), Result(property = "nickName", column = "nick_name"))
    fun getAll(): List<UserEntity>

    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results(Result(property = "userSex", column = "user_sex", javaType = UserSexEnum::class), Result(property = "nickName", column = "nick_name"))
    fun getOne(id: Long?): UserEntity

    @Insert("INSERT INTO users(userName,passWord,user_sex) VALUES(#{userName}, #{passWord}, #{userSex})")
    fun insert(user: UserEntity)

    @Update("UPDATE users SET userName=#{userName},nick_name=#{nickName} WHERE id =#{id}")
    fun update(user: UserEntity)

    @Delete("DELETE FROM users WHERE id =#{id}")
    fun delete(id: Long?)


//     This example creates a prepared statement, something like select * from teacher where name = ?;
//    @Select("Select * from teacher where name = #{name}")
//    fun selectTeachForGivenName(@Param("name") name: String): Teacher

//     This example creates n inlined statement, something like select * from teacher where name = 'someName';
//    @Select("Select * from teacher where name = '\${name}'")
//    fun selectTeachForGivenName(@Param("name") name: String): Teacher

}