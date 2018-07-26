package com.smart.library.deploy

import com.smart.library.deploy.client.CXIDeployClient

/**
 * 动态部署支持的类型
 */
enum class CXDeployManager(val supportCheckTypes: MutableSet<CheckType>) : CXIDeployClient {

    ANDROID(mutableSetOf()),
    HYBIRD(mutableSetOf()),
    REACT_NATIVE(mutableSetOf());

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

    companion object {
        const val TAG: String = "[rn-deploy]"
    }

    var client: CXIDeployClient? = null

    override fun check() {
        client?.check()
    }
}
