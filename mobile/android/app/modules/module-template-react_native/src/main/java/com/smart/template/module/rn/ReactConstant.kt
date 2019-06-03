package com.smart.template.module.rn

import com.smart.library.util.STSystemUtil

object ReactConstant {

    const val PATH_ASSETS_BUNDLE = "assets://index.android.bundle"
    const val PATH_ASSETS_BASE_BUNDLE = "assets://base.android.bundle"
    const val PATH_ASSETS_BUSINESS_BUNDLE = "assets://business.android.bundle"
    const val COMPONENT_NAME = "smart-travel"

    /**
     * 跟随 apk 一起打包的 react native 离线包版本号, 0代表当前无可使用的离线包
     */
    @JvmStatic
    val VERSION_RN_BASE: Int by lazy { (STSystemUtil.getAppMetaData("VERSION_RN") ?: 0) as Int }

    /**
     * 当前正在使用的 react native 离线包版本号, -1 代表是在线版本, 0代表当前无可使用的离线包
     */
    @JvmStatic
    var VERSION_RN_CURRENT: Int = 0
        internal set

}
