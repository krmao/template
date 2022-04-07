package com.smart.library.deploy.model

import androidx.annotation.Keep

/**
 * 尝试应用热部署的时机
 */
@Suppress("unused")
//@Keep
enum class STDeployApplyType {
    APP_START,
    APP_FOREGROUND_TO_BACKGROUND,
    APP_OPEN_FIRST_PAGE,
    APP_CLOSE_ALL_PAGES,
}