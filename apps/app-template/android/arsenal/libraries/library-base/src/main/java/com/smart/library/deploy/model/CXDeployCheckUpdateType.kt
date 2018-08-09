package com.smart.library.deploy.model

/**
 * 检查更新的时机
 */
enum class CXDeployCheckUpdateType {
    APP_START,
    APP_FORGROUND_TO_BACKGROUND,
    APP_OPEN_FIRST_PAGE,
    APP_OPEN_EVERY_PAGE,
    APP_RECEIVE_PUSH
}