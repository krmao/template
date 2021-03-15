package com.codesdancing.flutter.multiple

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.smart.library.R
import com.smart.library.util.STLogUtil
import com.smart.library.util.rx.RxBus
import com.smart.library.util.swipeback.STSwipeBackUtils
import io.flutter.embedding.android.FlutterActivityLaunchConfigs.BackgroundMode

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
open class STFlutterMultipleHomeActivity : STFlutterMultipleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack(false)
        enableImmersionStatusBar(true)
        enableExitWithDoubleBackPressed(true)
        super.onCreate(null)
    }

    override fun getBackgroundMode(): BackgroundMode {
        return BackgroundMode.transparent
    }

    override fun onFlutterUiDisplayed() {
        super.onFlutterUiDisplayed()
        STLogUtil.d(TAG, "onFlutterUiDisplayed needNotifyOnFlutterUiDisplayedEvent=$needNotifyOnFlutterUiDisplayedEvent, ${Thread.currentThread().name}")
        if (needNotifyOnFlutterUiDisplayedEvent) {
            Handler().post {
                STSwipeBackUtils.convertActivityFromTranslucent(this)
                Handler().post {
                    STLogUtil.d(TAG, "onFlutterUiDisplayed post OnFlutterUiDisplayedEvent")
                    RxBus.post(OnFlutterUiDisplayedEvent(initialRoute))
                }
            }
        }
    }

    class OnFlutterUiDisplayedEvent(val initialRoute: String?)

    companion object {

        private const val TAG = "[STFlutterMultipleHomeActivity]"

        @JvmStatic
        var needNotifyOnFlutterUiDisplayedEvent: Boolean = true

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
                @StyleRes activityTheme: Int = R.style.STAppTheme_Home_Transparent,
                enableSwipeBack: Boolean = false,
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
                adapterDesignWidth: Int? = null,
                adapterDesignHeight: Int? = null,
                enableAdapterDesign: Boolean? = null
        ) {
            val intent: Intent = STFlutterMultipleActivity.createIntent(
                    context = from.context,
                    activityClass = STFlutterMultipleHomeActivity::class.java,
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
                    adapterDesignWidth = adapterDesignWidth,
                    adapterDesignHeight = adapterDesignHeight,
                    enableAdapterDesign = enableAdapterDesign
            )
            from.startActivityForResult(intent, requestCode)
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
                @StyleRes activityTheme: Int = R.style.STAppTheme_Home_Transparent,
                enableSwipeBack: Boolean = false,
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
                adapterDesignWidth: Int? = null,
                adapterDesignHeight: Int? = null,
                enableAdapterDesign: Boolean? = null
        ) {
            if (from != null) {
                val intent: Intent = STFlutterMultipleActivity.createIntent(
                        context = from,
                        activityClass = STFlutterMultipleHomeActivity::class.java,
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
                        adapterDesignWidth = adapterDesignWidth,
                        adapterDesignHeight = adapterDesignHeight,
                        enableAdapterDesign = enableAdapterDesign
                )
                if (from is Activity) {
                    from.startActivityForResult(intent, requestCode)
                } else {
                    from.startActivity(intent)
                }
            } else {
                STLogUtil.e(TAG, "context is null !")
            }
        }
    }
}
