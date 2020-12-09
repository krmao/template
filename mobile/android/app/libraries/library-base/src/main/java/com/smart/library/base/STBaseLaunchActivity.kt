package com.smart.library.base

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.smart.library.R
import com.smart.library.util.STSystemUtil

/**
 * 检测 App 冷启动到 LaunchActivity 成功渲染耗时:
 *  $ adb shell am start -S -W com.smart.template/com.smart.template.FinalLaunchActivity
 *
 * 一些测试结果:
 *  华为 P20 初始化代码无                    950ms
 *  华为 P20 初始化代码在 Application        1065ms, 延时 1200ms 后初始化, 977-999ms
 *  华为 P20 初始化代码在 LaunchActivity     980ms, 但需要注意 activityLifecycle 注册时机(LaunchActivity 已启动了再注册会出现问题)
 */
open class STBaseLaunchActivity : AppCompatActivity() {

    companion object {
        const val TAG = "[splash]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 代码设置可以看到状态栏动画, theme.xml 中设置全屏比较突兀
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.decorView.setBackgroundResource(R.drawable.st_launch)
        super.onCreate(null)
        STSystemUtil.setActivityFullScreenAndExpandLayout(this)

        // 避免通过其他方式启动程序后，再通过程序列表中的launcher启动，重新走启动流程
        if (!isTaskRoot) {
            val intent: Intent? = intent
            val action: String? = intent?.action
            if (intent?.hasCategory(Intent.CATEGORY_LAUNCHER) == true && action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }

    }
}
