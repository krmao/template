package com.smart.library.util.rx.permission

import androidx.annotation.Keep

/**
 * @param shouldShowRequestPermissionRationale false==勾选 true==未勾选, 如果返回false, 说明 <开启权限> 或 <未开启权限 并且勾选了 "不再提示"> , 如果是true 说明 <未开启权限 且未勾选 "不在提示">
 */
//@Keep
@Suppress("MemberVisibilityCanBePrivate")
data class Permission(val name: String, val granted: Boolean, val shouldShowRequestPermissionRationale: Boolean = false) {
    constructor(permissions: List<Permission>) : this(permissions.joinToString { it.name }, permissions.all { it.granted }, permissions.any { it.shouldShowRequestPermissionRationale })
}