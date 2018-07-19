package com.smart.library.deploy.model

import com.smart.library.deploy.client.CXIDeployClient

/**
 * 动态部署支持的类型
 */
enum class CXDeployType(val supportCheckTypes: MutableSet<CheckType>) {
    //    ANDROID(mutableSetOf()),
//    HYBIRD(mutableSetOf()),
    REACT_NATIVE(mutableSetOf());

    var deployClient: CXIDeployClient? = null

    /**
     * 动态部署检查更新的时机
     */
    enum class CheckType {
        APP_START,
        APP_FORGROUND_TO_BACKGROUND,
        APP_BACKGROUND_TO_FORGROUND,
        APP_OPEN_FIRST_PAGE,
        APP_OPEN_EVERY_PAGE,
        APP_CLOSE_ALL_PAGES,
        APP_RECEIVE_PUSH,
    }
}
