package com.smart.housekeeper.repository.remote

import com.smart.housekeeper.repository.remote.HKURLManager.Environments.PRD

/**
 * [ DEV --> SIT --> UAT --> PRD ]
 *
 * DEV: Development System          开发系统环境
 * SIT: System Integrate Test       系统集成测试(公司测试部门执行)
 * UAT: User Acceptance Testing     用户验收测试(用户执行)
 * PRD: Production System           生产系统环境
 */
internal object HKURLManager {
    val KEY_HOST = "KEY_URL1_HOST"

    enum class Environments(val map: Map<String, String>) {
        DEV(mapOf(
            KEY_HOST to "http://10.47.18.39:7777")
        ),
        SIT(mapOf(
            KEY_HOST to "http://10.47HK.18.39:7777")
        ),
        UAT(mapOf(
            KEY_HOST to "http://10.47.18.39:7777")
        ),
        PRD(mapOf(
            KEY_HOST to "http://10.47.18.39:7777")
        )
    }

    var curEnvironment: Environments = PRD
        get() = PRD

    val curHost get() = curEnvironment.map[KEY_HOST] ?: ""
}
