package com.smart.springcloud.appa.base.config.security.user

import com.smart.springcloud.appa.database.model.UserModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

@Suppress("MemberVisibilityCanPrivate", "CanBeParameter")
class CXUserDetails @JvmOverloads constructor(
    var userModel: UserModel,
    enabled: Boolean = true,
    accountNonExpired: Boolean = true,
    credentialsNonExpired: Boolean = true,
    accountNonLocked: Boolean = true,
    authorities: MutableCollection<out GrantedAuthority>?
) : User(userModel.userName ?: "", userModel.password
    ?: "", enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities)
