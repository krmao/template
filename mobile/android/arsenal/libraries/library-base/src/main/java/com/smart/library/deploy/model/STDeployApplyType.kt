package com.smart.library.deploy.model

/**
 * 尝试应用热部署的时机
 */
enum class STDeployApplyType {
    APP_START,
    APP_FORGROUND_TO_BACKGROUND,
    APP_OPEN_FIRST_PAGE,
    APP_CLOSE_ALL_PAGES,
}