package com.codesdancing.flutter.multiple

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.annotation.AnimRes
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.smart.library.R
import com.smart.library.base.STActivityDelegate
import com.smart.library.util.STLogUtil
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
open class STFlutterMultipleFragmentHomeActivity : STFlutterMultipleFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack(false)
        enableImmersionStatusBar(true)
        enableExitWithDoubleBackPressed(true)
        super.onCreate(null)

        // xml 中 theme 为透明, 则 flutter 加载期间可以看到 launch activity, 700ms 后转为不透明, 是因为如果不设置会看到 launch activity 1s 后的 finish 动画
        Handler().postDelayed({ STSwipeBackUtils.convertActivityFromTranslucent(this) }, 700)
    }

    companion object {
        private const val TAG = "[STFlutterMultipleFragmentHomeActivity]"

        /**
         * 由 fragment 跳转, 由 fragment 接收结果
         */
        @JvmStatic
        @JvmOverloads
        fun startActivity(
                from: Fragment,
                dartEntrypointFunctionName: String = "main",
                initialRoute: String = "/",
                requestCode: Int = 0,
                @StyleRes activityTheme: Int = STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT.id,
                enableSwipeBack: Boolean = true,
                enableSwipeBackRelate: Boolean? = null,
                enableSwipeBackShadow: Boolean? = null,
                enableImmersionStatusBar: Boolean = true,
                enableImmersionStatusBarWithDarkFont: Boolean = false,
                @FloatRange(from = 0.0, to = 1.0) statusBarAlphaForDarkFont: Float? = null,
                enableExitWithDoubleBackPressed: Boolean = false,
                enableFinishIfIsNotTaskRoot: Boolean? = null,
                enableActivityFullScreenAndExpandLayout: Boolean? = null,
                enableActivityFeatureNoTitle: Boolean? = null,
                activityDecorViewBackgroundResource: Int? = null,
                @AnimRes activityOpenEnterAnimation: Int = R.anim.st_anim_left_right_open_enter,
                @AnimRes activityOpenExitAnimation: Int = R.anim.st_anim_left_right_open_exit,
                @AnimRes activityCloseEnterAnimation: Int = R.anim.st_anim_left_right_close_enter,
                @AnimRes activityCloseExitAnimation: Int = R.anim.st_anim_left_right_close_exit,
                adapterDesignWidth: Int? = null,
                adapterDesignHeight: Int? = null,
                enableAdapterDesign: Boolean? = null
        ) {
            val intent: Intent = STFlutterMultipleActivity.createIntent(
                    context = from.context,
                    activityClass = STFlutterMultipleFragmentHomeActivity::class.java,
                    dartEntrypointFunctionName = dartEntrypointFunctionName,
                    initialRoute = initialRoute,
                    activityThem = activityTheme,
                    enableSwipeBack = enableSwipeBack,
                    enableSwipeBackRelate = enableSwipeBackRelate,
                    enableSwipeBackShadow = enableSwipeBackShadow,
                    enableImmersionStatusBar = enableImmersionStatusBar,
                    enableImmersionStatusBarWithDarkFont = enableImmersionStatusBarWithDarkFont,
                    statusBarAlphaForDarkFont = statusBarAlphaForDarkFont,
                    enableExitWithDoubleBackPressed = enableExitWithDoubleBackPressed,
                    enableFinishIfIsNotTaskRoot = enableFinishIfIsNotTaskRoot,
                    enableActivityFullScreenAndExpandLayout = enableActivityFullScreenAndExpandLayout,
                    enableActivityFeatureNoTitle = enableActivityFeatureNoTitle,
                    activityDecorViewBackgroundResource = activityDecorViewBackgroundResource,
                    activityCloseEnterAnimation = activityCloseEnterAnimation,
                    activityCloseExitAnimation = activityCloseExitAnimation,
                    adapterDesignWidth = adapterDesignWidth,
                    adapterDesignHeight = adapterDesignHeight,
                    enableAdapterDesign = enableAdapterDesign
            )
            from.startActivityForResult(intent, requestCode)
            overrideWindowAnim(from.activity, activityOpenEnterAnimation, activityOpenExitAnimation, intent)
        }

        /**
         * 由 context 跳转, 如果是 activity, 由 activity 接收结果
         */
        @JvmStatic
        @JvmOverloads
        fun startActivity(
                from: Context?,
                dartEntrypointFunctionName: String = "main",
                initialRoute: String = "/",
                requestCode: Int = 0,
                @StyleRes activityTheme: Int = STActivityDelegate.Theme.APP_THEME_NORMAL.id,
                enableSwipeBack: Boolean = true,
                enableSwipeBackRelate: Boolean? = null,
                enableSwipeBackShadow: Boolean? = null,
                enableImmersionStatusBar: Boolean = true,
                enableImmersionStatusBarWithDarkFont: Boolean = false,
                @FloatRange(from = 0.0, to = 1.0) statusBarAlphaForDarkFont: Float? = null,
                enableExitWithDoubleBackPressed: Boolean = false,
                enableFinishIfIsNotTaskRoot: Boolean? = null,
                enableActivityFullScreenAndExpandLayout: Boolean? = null,
                enableActivityFeatureNoTitle: Boolean? = null,
                activityDecorViewBackgroundResource: Int? = null,
                @AnimRes activityOpenEnterAnimation: Int = R.anim.st_anim_left_right_open_enter,
                @AnimRes activityOpenExitAnimation: Int = R.anim.st_anim_left_right_open_exit,
                @AnimRes activityCloseEnterAnimation: Int = R.anim.st_anim_left_right_close_enter,
                @AnimRes activityCloseExitAnimation: Int = R.anim.st_anim_left_right_close_exit,
                adapterDesignWidth: Int? = null,
                adapterDesignHeight: Int? = null,
                enableAdapterDesign: Boolean? = null
        ) {
            if (from != null) {
                val intent: Intent = STFlutterMultipleActivity.createIntent(
                        context = from,
                        activityClass = STFlutterMultipleFragmentHomeActivity::class.java,
                        dartEntrypointFunctionName = dartEntrypointFunctionName,
                        initialRoute = initialRoute,
                        activityThem = activityTheme,
                        enableSwipeBack = enableSwipeBack,
                        enableSwipeBackRelate = enableSwipeBackRelate,
                        enableSwipeBackShadow = enableSwipeBackShadow,
                        enableImmersionStatusBar = enableImmersionStatusBar,
                        enableImmersionStatusBarWithDarkFont = enableImmersionStatusBarWithDarkFont,
                        statusBarAlphaForDarkFont = statusBarAlphaForDarkFont,
                        enableExitWithDoubleBackPressed = enableExitWithDoubleBackPressed,
                        enableFinishIfIsNotTaskRoot = enableFinishIfIsNotTaskRoot,
                        enableActivityFullScreenAndExpandLayout = enableActivityFullScreenAndExpandLayout,
                        enableActivityFeatureNoTitle = enableActivityFeatureNoTitle,
                        activityDecorViewBackgroundResource = activityDecorViewBackgroundResource,
                        activityCloseEnterAnimation = activityCloseEnterAnimation,
                        activityCloseExitAnimation = activityCloseExitAnimation,
                        adapterDesignWidth = adapterDesignWidth,
                        adapterDesignHeight = adapterDesignHeight,
                        enableAdapterDesign = enableAdapterDesign
                )
                if (from is Activity) {
                    from.startActivityForResult(intent, requestCode)
                } else {
                    from.startActivity(intent)
                }
                overrideWindowAnim(from, activityOpenEnterAnimation, activityOpenExitAnimation, intent)
            } else {
                STLogUtil.e(TAG, "context is null !")
            }
        }
    }

}
