@file:Suppress("unused")

package com.smart.springcloud.appb.http.model

import com.smart.springcloud.appb.base.config.config.CXConfig

open class HKPagingData(var pageIndex: Int = 1, var pageSize: Int = 20)

data class HKTokenData(var accessToken: String?)

data class HKIdData(var id: Int = CXConfig.DEFAULT_SQL_ID)

data class HKRoleIdData(var roleId: Int = CXConfig.DEFAULT_SQL_ID)
class HKRoleIdDataPaging(var roleId: Int = CXConfig.DEFAULT_SQL_ID, pageIndex: Int, pageSize: Int) : HKPagingData(pageIndex, pageSize)

data class HKUserIdData(var userId: Int = CXConfig.DEFAULT_SQL_ID)
class HKUserIdDataPaging(var userId: Int = CXConfig.DEFAULT_SQL_ID, pageIndex: Int, pageSize: Int) : HKPagingData(pageIndex, pageSize)

/**
 * 一共有多少行被影响
 */
data class HKColumnData(var column: Int = 0)

data class HKMenuIdData(var menuId: Int = CXConfig.DEFAULT_SQL_ID)
class HKMenuIdDataPaging(var menuId: Int = CXConfig.DEFAULT_SQL_ID, pageIndex: Int, pageSize: Int) : HKPagingData(pageIndex, pageSize)

data class HKNamePwdData(var userName: String = "", var password: String = "")

data class HKUpdateData<NEW_DATA, CONDITION>(var newData: NEW_DATA, var condition: CONDITION)
