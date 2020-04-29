package com.smart.library.util

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

/*
    设置引导页面全屏, 状态栏消失并整体页面顶上去, 不是仅仅的隐藏但是占位

    <style name="AppStartTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="android:windowDisablePreview">false</item>
        <item name="android:windowNoTitle">true</item>
        <item name="android:windowFullscreen">true</item>
        <item name="android:windowContentOverlay">@null</item>
        <item name="android:windowAnimationStyle">@null</item>
        <item name="android:windowBackground">@drawable/launch_jiduo</item>
    </style>

    <activity
        android:name="com.app.AppLaunchActivity"
        android:configChanges="locale|fontScale|orientation|keyboardHidden|screenSize|smallestScreenSize|screenLayout"
        android:launchMode="standard"
        android:screenOrientation="portrait"
        android:theme="@style/AppStartTheme">
        <meta-data
            android:name="android.notch_support"
            android:value="true" />
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />

            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>

*/
object STFullScreenUtil {

    @JvmStatic
    fun initBeforeSuperOnCreateInOnCreate(activity: Activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    @JvmStatic
    fun initBeforeSetContentViewAfterSuperOnCreateInOnCreate(activity: Activity) {
        // 设置全屏
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        if (Build.VERSION.SDK_INT >= 28) {
            // 设置状态栏全屏且透明
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            /*    val lp: WindowManager.LayoutParams = activity.window.attributes
                val c: Class<*> = lp.javaClass
                try {
                    val f = c.getDeclaredField("layoutInDisplayCutoutMode")
                    f.isAccessible = true
                    f[lp] = 1
                    activity.window.attributes = lp
                } catch (e: Exception) {
                    Log.e("splash", "splash", e)
                }
                */
        }
        if (!activity.isTaskRoot) {
            val intent: Intent = activity.intent
            val action = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action != null && action == Intent.ACTION_MAIN) {
                activity.finish()
                return
            }
        }
    }
}