package com.codesdancing.flutter.multiple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import com.smart.library.base.STActivityDelegate
import com.smart.library.util.STLogUtil
import com.smart.library.util.rx.RxBus
import com.smart.library.util.swipeback.STSwipeBackUtils
import io.flutter.embedding.android.FlutterActivityLaunchConfigs.BackgroundMode

/*  @example
    class FinalLaunchActivity : STBaseLaunchActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(null)
            STInitializer.state.ensureBusInitialized {
                STLogUtil.w(TAG, "ensureBusInitialized isFinishing=$isFinishing, thread=${Thread.currentThread().name}")

                if (!isFinishing) {
                    //region schema
                    val url: String? = intent.data?.toString()
                    STLogUtil.w(TAG, "schema url=$url")
                    if (url?.startsWith("smart://template") == true) {
                        STInitializer.openSchema(this@FinalLaunchActivity, url)
                        finish()
                        return@ensureBusInitialized
                    } else {
                        goToFlutterHome()
                    }
                    //endregion
                }
            }
        }

        private var disposable: Disposable? = null

        @SuppressLint("CheckResult")
        private fun goToFlutterHome() {
            disposable = RxBus.toObservable(STFlutterMultipleHomeActivity.OnFlutterUiDisplayedEvent::class.java).subscribe {
                STLogUtil.d(TAG, "goToFlutterHome receive OnFlutterUiDisplayedEvent, ${Thread.currentThread().name}")
                if (!isFinishing) finish()
                STFlutterMultipleHomeActivity.needNotifyOnFlutterUiDisplayedEvent = false
                disposable?.dispose() // unsubscribe
            }
            STFlutterMultipleHomeActivity.needNotifyOnFlutterUiDisplayedEvent = true
            STFlutterUtils.openNewFlutterHomeActivityByName(this, "FlutterBridge")
        }
    }
 */
@Suppress("unused", "PrivatePropertyName")
open class STFlutterMultipleHomeActivity : STFlutterMultipleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack(false)
        enableImmersionStatusBar(true)
        enableExitWithDoubleBackPressed(true)
        super.onCreate(null)
    }

    override fun onStop() {
        super.onStop()
        STLogUtil.d(TAG, "onStop")
    }

    /**
     * Sets this `Activity`'s `Window` background to be transparent, and hides the status
     * bar, if this `Activity`'s desired [BackgroundMode] is [BackgroundMode.transparent].
     *
     * For `Activity` transparency to work as expected, the theme applied to this `Activity` must include `<item name="android:windowIsTranslucent">true</item>`.
     *
     * @see Companion.startActivity#activityTheme
     *      activity transparent is relate to the argument activityTheme, must set the argument `activityTheme = R.style.STAppTheme_Home_Transparent` or `activityTheme = STActivityDelegate.Theme.APP_THEME_HOME_TRANSLUCENT.id`
     *      **no need** to set AndroidManifest.xml STFlutterMultipleHomeActivity theme to `android:theme="@style/STAppTheme.Home.Transparent"`
     *      **only need** to set AndroidManifest.xml STFlutterMultipleHomeActivity theme to `android:theme="@style/STAppTheme.Home"`
     *      with these can success call {@link #onStop} after activity is hide.
     *
     * @see configureWindowForTransparency
     */
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
         * 由 context 跳转, 如果是 activity, 由 activity 接收结果
         *
         * @param activityTheme
         *        activity transparent is relate to the argument activityTheme, must set the argument `activityTheme = R.style.STAppTheme_Home_Transparent` or `activityTheme = STActivityDelegate.Theme.APP_THEME_HOME_TRANSLUCENT.id`
         *        **no need** to set AndroidManifest.xml STFlutterMultipleHomeActivity theme to `android:theme="@style/STAppTheme.Home.Transparent"`
         *        **only need** to set AndroidManifest.xml STFlutterMultipleHomeActivity theme to `android:theme="@style/STAppTheme.Home"`
         *        with these can success call {@link #onStop} after activity is hide.
         */
        @JvmStatic
        @JvmOverloads
        fun startActivity(
                from: Context?,
                dartEntrypointFunctionName: String = "main",
                initialRoute: String = "/",
                argumentsJsonString: String? = null,
                @StyleRes activityTheme: Int = STActivityDelegate.Theme.APP_THEME_HOME_TRANSLUCENT.id,
                enableSwipeBackRelate: Boolean? = null,
                enableSwipeBackShadow: Boolean? = null,
                enableImmersionStatusBar: Boolean = true,
                enableImmersionStatusBarWithDarkFont: Boolean = false,
                @FloatRange(from = 0.0, to = 1.0) statusBarAlphaForDarkFont: Float? = null,
                enableFinishIfIsNotTaskRoot: Boolean? = null,
                enableActivityFullScreenAndExpandLayout: Boolean? = null,
                enableActivityFeatureNoTitle: Boolean? = null,
                activityDecorViewBackgroundResource: Int? = null,
                adapterDesignWidth: Int? = null,
                adapterDesignHeight: Int? = null,
                enableAdapterDesign: Boolean? = null
        ) {
            if (from != null) {
                val intent: Intent = createIntent(
                        context = from,
                        activityClass = STFlutterMultipleHomeActivity::class.java,
                        dartEntrypointFunctionName = dartEntrypointFunctionName,
                        initialRoute = initialRoute,
                        argumentsJsonString = argumentsJsonString,
                        activityThem = activityTheme,
                        enableSwipeBack = false,
                        enableSwipeBackRelate = enableSwipeBackRelate,
                        enableSwipeBackShadow = enableSwipeBackShadow,
                        enableImmersionStatusBar = enableImmersionStatusBar,
                        enableImmersionStatusBarWithDarkFont = enableImmersionStatusBarWithDarkFont,
                        statusBarAlphaForDarkFont = statusBarAlphaForDarkFont,
                        enableExitWithDoubleBackPressed = false,
                        enableFinishIfIsNotTaskRoot = enableFinishIfIsNotTaskRoot,
                        enableActivityFullScreenAndExpandLayout = enableActivityFullScreenAndExpandLayout,
                        enableActivityFeatureNoTitle = enableActivityFeatureNoTitle,
                        activityDecorViewBackgroundResource = activityDecorViewBackgroundResource,
                        activityCloseEnterAnimation = null,
                        activityCloseExitAnimation = null,
                        adapterDesignWidth = adapterDesignWidth,
                        adapterDesignHeight = adapterDesignHeight,
                        enableAdapterDesign = enableAdapterDesign
                )
                from.startActivity(intent)
            } else {
                STLogUtil.e(TAG, "context is null !")
            }
        }
    }
}
