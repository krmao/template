package com.codesdancing.flutter.boost

import android.os.Bundle
import android.os.Handler
import com.smart.library.util.swipeback.STSwipeBackUtils

/*
<activity
    android:name="com.codesdancing.flutter.STFlutterBoostLaunchActivity"
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
open class STFlutterBoostHomeActivity : STFlutterBoostActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack(false)
        enableImmersionStatusBar(true)
        enableExitWithDoubleBackPressed(false)
        super.onCreate(null)

        // xml 中 theme 为透明, 则 flutter 加载期间可以看到 launch activity, 700ms 后转为不透明, 是因为如果不设置会看到 launch activity 1s 后的 finish 动画
        Handler().postDelayed({ STSwipeBackUtils.convertActivityFromTranslucent(this) }, 700)
    }

}
