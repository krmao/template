package com.smart.library.base

import android.os.Bundle

/**
 * 检测 App 冷启动到 LaunchActivity 成功渲染耗时:
 *  $ adb shell am start -S -W com.smart.template/com.smart.template.FinalLaunchActivity
 *
 * 一些测试结果:
 *  华为 P20 初始化代码无                    950ms
 *  华为 P20 初始化代码在 Application        1065ms, 延时 1200ms 后初始化, 977-999ms
 *  华为 P20 初始化代码在 LaunchActivity     980ms, 但需要注意 activityLifecycle 注册时机(LaunchActivity 已启动了再注册会出现问题)
 */
@Suppress("LeakingThis")
open class STBaseLaunchActivity : STBaseActivity() {

    companion object {
        const val TAG = "[splash]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        if (isFinishing) return

        // do something
    }

    init {
        enableActivityFeatureNoTitle(true)
        // activityDecorViewBackgroundResource(R.drawable.st_launch)
        enableActivityFullScreenAndExpandLayout(true)
        enableSwipeBack(false)
        enableImmersionStatusBar(false)
        enableExitWithDoubleBackPressed(false)
        enableFinishIfIsNotTaskRoot(true)
    }
}
