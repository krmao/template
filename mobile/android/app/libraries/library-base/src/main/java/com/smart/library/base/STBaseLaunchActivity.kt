package com.smart.library.base

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.smart.library.R
import com.smart.library.util.STLogUtil

/**
 * 务必继承 FragmentActivity
 */
open class STBaseLaunchActivity : FragmentActivity() {

    companion object {
        const val TAG = "[splash]"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // 代码设置可以看到状态栏动画, theme.xml 中设置全屏比较突兀
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // 设置页面全屏显示 务必继承 FragmentActivity
        if (Build.VERSION.SDK_INT >= 28) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val layoutParams = window.attributes
            val layoutParamsClass: Class<*> = layoutParams.javaClass
            try {
                val field = layoutParamsClass.getDeclaredField("layoutInDisplayCutoutMode")
                field.isAccessible = true
                field[layoutParams] = 1
                // 设置页面延伸到刘海区显示
                window.attributes = layoutParams
            } catch (e: Exception) {
                STLogUtil.e("splash", e)
            }
        }

        // 避免通过其他方式启动程序后，再通过程序列表中的launcher启动，重新走启动流程
        if (!isTaskRoot) {
            val intent: Intent? = intent
            val action: String? = intent?.action
            if (intent?.hasCategory(Intent.CATEGORY_LAUNCHER) == true && action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }

        setContentView(R.layout.st_launch)
    }
}
