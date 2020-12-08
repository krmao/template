package com.smart.library.flutter

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.smart.library.R
import com.smart.library.util.STLogUtil

/*
<activity
    android:name="com.smart.library.flutter.STFlutterBoostLaunchActivity"
    android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|layoutDirection|fontScale|screenLayout|density|uiMode"
    android:hardwareAccelerated="true"
    android:launchMode="standard"
    android:theme="@style/STAppTheme.Launch"
    android:windowSoftInputMode="adjustResize">

    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>

    <meta-data
        android:name="io.flutter.embedding.android.NormalTheme"
        android:resource="@style/STAppTheme.Launch" />
    <!--This keeps the window background of the activity showing
    until Flutter renders its first frame. It can be removed if
    there is no splash screen (such as the default splash screen
    defined in @style/LaunchTheme). -->
    <!-- must be false -->
    <meta-data
        android:name="io.flutter.app.android.SplashScreenUntilFirstFrame"
        android:value="false" />
    <!--Flutter 闪屏页
    https://flutter.cn/docs/development/ui/splash-screen/android-splash-screen#showing-a-drawable-splash-screen -->
    <meta-data
        android:name="io.flutter.embedding.android.SplashScreenDrawable"
        android:resource="@drawable/flutter_splash_screen_drawable" />

</activity>
 */
/**
 * Flutter 启动页
 * https://flutter.cn/docs/development/ui/splash-screen/android-splash-screen
 */
@Suppress("unused", "PrivatePropertyName")
open class STFlutterBoostLaunchActivity : STFlutterBoostActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        intent = launchIntent()

        // 代码设置可以看到状态栏动画, theme.xml 中设置全屏比较突兀
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setWindowsDecorViewBackground()
        enableSwipeBack(false)
        enableImmersionStatusBar(false)
        enableExitWithDoubleBackPressed(false)
        super.onCreate(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

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
    }

    protected open fun launchIntent(): Intent {
        return withNewEngine()
            .url("flutter_bridge")
            .params(hashMapOf<String, Any?>())
            .backgroundMode(BackgroundMode.opaque)
            .build(context)
    }

    protected open fun setWindowsDecorViewBackground() {
        window.decorView.setBackgroundResource(R.drawable.st_launch)
    }

}
