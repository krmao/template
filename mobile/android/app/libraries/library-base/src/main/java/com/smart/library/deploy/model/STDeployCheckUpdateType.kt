package com.smart.library.deploy.model

import androidx.annotation.Keep

/**
 * 检查更新的时机
 */
@Suppress("unused")
@Keep
enum class STDeployCheckUpdateType {
    APP_START,
    APP_FOREGROUND_TO_BACKGROUND,
    APP_OPEN_FIRST_PAGE,
    APP_OPEN_EVERY_PAGE,
    APP_RECEIVE_PUSH
}