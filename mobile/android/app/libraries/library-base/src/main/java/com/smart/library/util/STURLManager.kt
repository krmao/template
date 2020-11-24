package com.smart.library.util

import com.smart.library.util.STURLManager.Environments.PRD

/**
 * [ DEV --> SIT --> UAT --> PRD ]
 *
 * DEV: Development System          开发系统环境
 * SIT: System Integrate Test       系统集成测试(公司测试部门执行)
 * UAT: User Acceptance Testing     用户验收测试(用户执行)
 * PRD: Production System           生产系统环境
 */
@Suppress("SuspiciousVarProperty", "unused")
object STURLManager {
    const val KEY_HOST = "KEY_URL_HOST"

    enum class Environments(val map: MutableMap<String, String?>) {
        DEV(
            mutableMapOf(
                KEY_HOST to "http://127.0.0.1:1234"
            )
        ),
        SIT(
            mutableMapOf(
                KEY_HOST to "http://127.0.0.1:1234"
            )
        ),
        UAT(
            mutableMapOf(
                KEY_HOST to "http://127.0.0.1:1234"
            )
        ),
        PRD(
            mutableMapOf(
                KEY_HOST to "http://127.0.0.1:1234"
            )
        );

        fun setURL(url: String?) {
            map[KEY_HOST] = url
        }

        fun getURL(): String? = map[KEY_HOST]
    }

    var curEnvironment: Environments = PRD
        get() = PRD

    val curHost get() = curEnvironment.map[KEY_HOST] ?: ""
}