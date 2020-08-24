package com.smart.springcloud.appb.base.config.security.user

import com.smart.springcloud.appb.database.mapper.UserMapper
import com.smart.springcloud.appb.database.model.UserModel
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


/**
 * @Service
 * http://blog.csdn.net/u012581453/article/details/53709623
 */
//@Service
//class CXUserDetailService(val userMapper: UserMapper, val roleMenuMapper: RoleMenuMapper, val roleMapper: RoleMapper) : UserDetailsService {
//    private val logger: Logger = LogManager.getLogger(CXUserDetailService::class.java.name)
//
//    @Throws(UsernameNotFoundException::class)
//    override fun loadUserByUsername(username: String?): UserDetails {
//        logger.warn("username:$username")
//        val userModel = userMapper.findByUserName(username) ?: throw  UsernameNotFoundException(username + " not found")
//
//        //val authorityList: MutableList<SimpleGrantedAuthority> = roleMenuMapper.findAllMenusByUserId(userModel.roleId).map {
//        //    SimpleGrantedAuthority(it.roleId.toString())
//        //}.toMutableList()
//
//        //添加所有权限小于本角色的角色权限
//        //((user.roleId ?: 7)..7).mapTo(authorityList) { SimpleGrantedAuthority("ROLE_$it") }
//
//        //只添加本角色的的权限
//        val authorityList: MutableList<SimpleGrantedAuthority> = arrayListOf(SimpleGrantedAuthority("ROLE_${userModel.roleId}"))
//        return CXUserDetails(userModel, authorities = authorityList)
//    }
//}

@Service
class CXUserDetailService : UserDetailsService {
    private val logger: Logger = LogManager.getLogger(CXUserDetailService::class.java.name)

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        logger.warn("username:$username")
        throw UsernameNotFoundException("please connect database")
    }
}

