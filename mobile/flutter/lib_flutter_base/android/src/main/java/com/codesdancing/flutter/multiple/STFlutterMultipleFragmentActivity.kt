package com.codesdancing.flutter.multiple

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.widget.FrameLayout
import androidx.annotation.AnimRes
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.codesdancing.flutter.multiple.STFlutterMultipleActivity.Companion.KEY_DART_ENTRYPOINT_FUNCTION_NAME
import com.codesdancing.flutter.multiple.STFlutterMultipleActivity.Companion.KEY_INITIAL_ROUTE
import com.smart.library.R
import com.smart.library.base.STActivity
import com.smart.library.base.STActivityDelegate
import com.smart.library.util.STLogUtil
import io.flutter.FlutterInjector
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor

open class STFlutterMultipleFragmentActivity : STActivity() {

    protected open fun engineId(): String = "$this-${SystemClock.elapsedRealtime()}"

    // This has to be lazy to avoid creation before the FlutterEngineGroup.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))

        val dartEntrypointFunctionName = intent.extras?.getString(KEY_DART_ENTRYPOINT_FUNCTION_NAME) ?: "main"
        val initialRoute = intent.extras?.getString(KEY_INITIAL_ROUTE) ?: "/"
        // This has to be lazy to avoid creation before the FlutterEngineGroup.
        val dartEntrypoint = DartExecutor.DartEntrypoint(FlutterInjector.instance().flutterLoader().findAppBundlePath(), dartEntrypointFunctionName)

        // Warning: "setInitialRoute" on the navigationChannel must be called before running your FlutterEngine in order for Flutter’s first frame to use the desired route.
        // Specifically, this must be called before running the Dart entrypoint. The entrypoint may lead to a series of events where runApp builds a Material/Cupertino/WidgetsApp that implicitly creates a Navigator that might window.defaultRouteName when the NavigatorState is first initialized.
        // Setting the initial route after running the engine doesn’t have an effect.
        val flutterEngine = STFlutterMultipleInitializer.flutterEngineGroup?.createAndRunEngine(this, dartEntrypoint, initialRoute)
        FlutterEngineCache.getInstance().put(engineId(), flutterEngine)
        supportFragmentManager.beginTransaction().add(android.R.id.content, FlutterFragment
                .withCachedEngine(engineId())
                .build<FlutterFragment>()).commit()
    }

    override fun onDestroy() {
        FlutterEngineCache.getInstance().remove(engineId())
        super.onDestroy()
    }

    companion object {
        private const val TAG = "[STFlutterMultipleFragmentActivity]"

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
                    activityClass = STFlutterMultipleFragmentActivity::class.java,
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
                        activityClass = STFlutterMultipleFragmentActivity::class.java,
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