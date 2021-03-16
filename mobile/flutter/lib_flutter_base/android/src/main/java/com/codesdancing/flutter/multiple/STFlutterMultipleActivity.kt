package com.codesdancing.flutter.multiple

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import androidx.annotation.AnimRes
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.codesdancing.flutter.LibFlutterBaseMultiplePlugin
import com.gyf.immersionbar.ImmersionBar
import com.smart.library.R
import com.smart.library.base.STActivity
import com.smart.library.base.STActivityDelegate
import com.smart.library.base.STBaseActivityDelegateImpl
import com.smart.library.util.STLogUtil
import io.flutter.FlutterInjector
import io.flutter.embedding.android.DrawableSplashScreen
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.SplashScreen
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.reactivex.disposables.CompositeDisposable
import org.json.JSONObject

open class STFlutterMultipleActivity : FlutterActivity(), STActivityDelegate {

    protected open val activityDelegate: STActivityDelegate by lazy { STBaseActivityDelegateImpl(this) }

    override fun disposables(): CompositeDisposable = activityDelegate.disposables()
    override fun statusBar(): ImmersionBar? = activityDelegate.statusBar()
    override fun onCreate(savedInstanceState: Bundle?) {
        STLogUtil.d("[page]", "$TAG onCreate")
        onCreateBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
        onCreateAfter(savedInstanceState)
    }

    override fun onCreateBefore(savedInstanceState: Bundle?) {
        activityDelegate.onCreateBefore(savedInstanceState)
    }

    override fun onCreateAfter(savedInstanceState: Bundle?) {
        activityDelegate.onCreateAfter(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        STLogUtil.d("[page]", "$TAG onPostCreate")
        super.onPostCreate(savedInstanceState)
        activityDelegate.onPostCreate(savedInstanceState)
    }

    override fun onResume() {
        STLogUtil.d("[page]", "$TAG onResume")
        super.onResume()
        activityDelegate.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        STLogUtil.d("[page]", "$TAG onActivityResult requestCode=$requestCode, resultCode=$resultCode, data=$data")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                val argumentsJsonString = data?.getStringExtra(KEY_ARGUMENTS_JSON_STRING)
                STLogUtil.w("[page]", "$TAG onActivityResult requestCode=$requestCode, resultCode=$resultCode, argumentsJsonString=$argumentsJsonString")
                val eventKey: String = KEY_ARGUMENTS_JSON_STRING
                val eventInfo: JSONObject? = if (!argumentsJsonString.isNullOrBlank() && argumentsJsonString.startsWith("{") && argumentsJsonString.endsWith("}")) JSONObject(argumentsJsonString) else null
                LibFlutterBaseMultiplePlugin.getFlutterBaseMultiplePlugin(flutterEngine)?.sendEventToDart(eventKey, eventInfo)
            }
        }
    }

    override fun onDestroy() {
        STLogUtil.d("[page]", "$TAG onDestroy")
        super.onDestroy()
        activityDelegate.onDestroy()
    }

    override fun finish() {
        STLogUtil.d("[page]", "$TAG finish")
        super.finish()
        finishAfter()
    }

    override fun finishAfter() {
        STLogUtil.d("[page]", "$TAG finishAfter")
        activityDelegate.finishAfter()
    }

    override fun provideFlutterEngine(context: Context): FlutterEngine? {
        STLogUtil.d("[page]", "$TAG provideFlutterEngine")
        val dartEntrypointFunctionName = intent.extras?.getString(KEY_DART_ENTRYPOINT_FUNCTION_NAME) ?: "main"
        val initialRoute = intent.extras?.getString(KEY_INITIAL_ROUTE) ?: "/"
        val dartEntryPoint = DartExecutor.DartEntrypoint(FlutterInjector.instance().flutterLoader().findAppBundlePath(), dartEntrypointFunctionName)
        return STFlutterMultipleInitializer.flutterEngineGroup?.createAndRunEngine(activity, dartEntryPoint, initialRoute)
    }

    override fun getInitialRoute(): String? {
        STLogUtil.d("[page]", "$TAG getInitialRoute")
        return intent.extras?.getString(KEY_INITIAL_ROUTE) ?: "/" // no need
    }

    override fun provideSplashScreen(): SplashScreen? {
        val manifestSplashDrawable = getSplashScreenFromManifest()
        return if (manifestSplashDrawable != null) DrawableSplashScreen(manifestSplashDrawable, ImageView.ScaleType.CENTER, 500L) else null
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getSplashScreenFromManifest(): Drawable? {
        return try {
            val activityInfo = this.packageManager.getActivityInfo(this.componentName, PackageManager.GET_META_DATA)
            val metadata = activityInfo.metaData
            val splashScreenId = metadata?.getInt("io.flutter.embedding.android.SplashScreenDrawable")
            val splashUntilFirstFrame = metadata?.getBoolean("io.flutter.app.android.SplashScreenUntilFirstFrame")
            @Suppress("DEPRECATION")
            if (splashUntilFirstFrame == true && splashScreenId != null) (if (VERSION.SDK_INT > 21) this.resources.getDrawable(splashScreenId, this.theme) else this.resources.getDrawable(splashScreenId)) else null
        } catch (var4: PackageManager.NameNotFoundException) {
            null
        }
    }

    override fun onBackPressedIntercept(): Boolean = activityDelegate.onBackPressedIntercept()

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        STLogUtil.d("[page]", "$TAG onKeyDown")
        return if (activityDelegate.onKeyDown(keyCode, event)) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun enableSwipeBack(): Boolean = activityDelegate.enableSwipeBack()
    override fun enableSwipeBack(enable: Boolean) = activityDelegate.enableSwipeBack(enable)
    override fun enableSwipeBackRelate(): Boolean = activityDelegate.enableSwipeBackRelate()
    override fun enableSwipeBackRelate(enable: Boolean) = activityDelegate.enableSwipeBackRelate(enable)
    override fun enableSwipeBackShadow(): Boolean = activityDelegate.enableSwipeBackShadow()
    override fun enableSwipeBackShadow(enable: Boolean) = activityDelegate.enableSwipeBackShadow(enable)
    override fun enableImmersionStatusBar(): Boolean = activityDelegate.enableImmersionStatusBar()
    override fun enableImmersionStatusBar(enable: Boolean) = activityDelegate.enableImmersionStatusBar(enable)
    override fun enableImmersionStatusBarWithDarkFont(): Boolean = activityDelegate.enableImmersionStatusBarWithDarkFont()
    override fun enableImmersionStatusBarWithDarkFont(enable: Boolean) = activityDelegate.enableImmersionStatusBarWithDarkFont(enable)
    override fun statusBarAlphaForDarkFont(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = activityDelegate.statusBarAlphaForDarkFont(alpha)
    override fun enableExitWithDoubleBackPressed(): Boolean = activityDelegate.enableExitWithDoubleBackPressed()
    override fun enableExitWithDoubleBackPressed(enable: Boolean) = activityDelegate.enableExitWithDoubleBackPressed(enable)
    override fun enableFinishIfIsNotTaskRoot(): Boolean = activityDelegate.enableFinishIfIsNotTaskRoot()
    override fun enableFinishIfIsNotTaskRoot(enable: Boolean) = activityDelegate.enableFinishIfIsNotTaskRoot(enable)
    override fun enableActivityFullScreenAndExpandLayout(): Boolean = activityDelegate.enableActivityFullScreenAndExpandLayout()
    override fun enableActivityFullScreenAndExpandLayout(enable: Boolean) = activityDelegate.enableActivityFullScreenAndExpandLayout(enable)
    override fun enableActivityFeatureNoTitle(): Boolean = activityDelegate.enableActivityFeatureNoTitle()
    override fun enableActivityFeatureNoTitle(enable: Boolean) = activityDelegate.enableActivityFeatureNoTitle(enable)
    override fun activityDecorViewBackgroundResource(): Int = activityDelegate.activityDecorViewBackgroundResource()
    override fun activityDecorViewBackgroundResource(drawableResource: Int) = activityDelegate.activityDecorViewBackgroundResource(drawableResource)
    override fun activityTheme(): Int = activityDelegate.activityTheme()
    override fun activityTheme(activityTheme: Int) = activityDelegate.activityTheme(activityTheme)
    override fun activityCloseEnterAnimation(): Int = activityDelegate.activityCloseEnterAnimation()
    override fun activityCloseEnterAnimation(animation: Int) = activityDelegate.activityCloseEnterAnimation(animation)
    override fun activityCloseExitAnimation(): Int = activityDelegate.activityCloseExitAnimation()
    override fun activityCloseExitAnimation(animation: Int) = activityDelegate.activityCloseExitAnimation(animation)
    override fun adapterDesignWidth(designWidth: Int) = activityDelegate.adapterDesignWidth(designWidth)
    override fun adapterDesignHeight(designHeight: Int) = activityDelegate.adapterDesignHeight(designHeight)
    override fun getResources(resources: Resources): Resources = activityDelegate.getResources(resources)
    override fun enableAdapterDesign(enable: Boolean) = activityDelegate.enableAdapterDesign(enable)
    override fun quitApplication() = activityDelegate.quitApplication()


    companion object {
        private const val TAG = "[STFlutterMultipleActivity]"
        const val REQUEST_CODE = 20210316
        const val KEY_DART_ENTRYPOINT_FUNCTION_NAME = "KEY_DART_ENTRYPOINT_FUNCTION_NAME"
        const val KEY_INITIAL_ROUTE = "KEY_INITIAL_ROUTE"
        const val KEY_ARGUMENTS_JSON_STRING = "KEY_ARGUMENTS_JSON_STRING"

        /**
         * 由 fragment 跳转, 由 fragment 接收结果
         */
        @JvmStatic
        @JvmOverloads
        fun startActivity(
                from: Fragment,
                dartEntrypointFunctionName: String = "main",
                initialRoute: String = "/",
                argumentsJsonString: String? = null,
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
                @AnimRes activityOpenEnterAnimation: Int? = com.smart.library.R.anim.st_anim_left_right_open_enter,
                @AnimRes activityOpenExitAnimation: Int? = com.smart.library.R.anim.st_anim_left_right_open_exit,
                @AnimRes activityCloseEnterAnimation: Int? = com.smart.library.R.anim.st_anim_left_right_close_enter,
                @AnimRes activityCloseExitAnimation: Int? = com.smart.library.R.anim.st_anim_left_right_close_exit,
                adapterDesignWidth: Int? = null,
                adapterDesignHeight: Int? = null,
                enableAdapterDesign: Boolean? = null
        ) {
            val intent: Intent = createIntent(
                    context = from.context,
                    activityClass = STFlutterMultipleActivity::class.java,
                    dartEntrypointFunctionName = dartEntrypointFunctionName,
                    initialRoute = initialRoute,
                    argumentsJsonString = argumentsJsonString,
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
            from.startActivityForResult(intent, REQUEST_CODE)
            STActivity.overrideWindowAnim(from.activity, activityOpenEnterAnimation, activityOpenExitAnimation, intent)
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
                argumentsJsonString: String? = null,
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
                @AnimRes activityOpenEnterAnimation: Int? = com.smart.library.R.anim.st_anim_left_right_open_enter,
                @AnimRes activityOpenExitAnimation: Int? = com.smart.library.R.anim.st_anim_left_right_open_exit,
                @AnimRes activityCloseEnterAnimation: Int? = com.smart.library.R.anim.st_anim_left_right_close_enter,
                @AnimRes activityCloseExitAnimation: Int? = com.smart.library.R.anim.st_anim_left_right_close_exit,
                adapterDesignWidth: Int? = null,
                adapterDesignHeight: Int? = null,
                enableAdapterDesign: Boolean? = null
        ) {
            if (from != null) {
                val intent: Intent = createIntent(
                        context = from,
                        activityClass = STFlutterMultipleActivity::class.java,
                        dartEntrypointFunctionName = dartEntrypointFunctionName,
                        initialRoute = initialRoute,
                        argumentsJsonString = argumentsJsonString,
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
                    from.startActivityForResult(intent, REQUEST_CODE)
                } else {
                    from.startActivity(intent)
                }
                STActivity.overrideWindowAnim(from, activityOpenEnterAnimation, activityOpenExitAnimation, intent)
            } else {
                STLogUtil.e(TAG, "context is null !")
            }
        }

        @JvmStatic
        @JvmOverloads
        fun createIntent(
                context: Context?,
                activityClass: Class<*>?,
                dartEntrypointFunctionName: String = "main",
                initialRoute: String = "/",
                argumentsJsonString: String? = null,
                @StyleRes activityThem: Int = STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT.id,
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
                @AnimRes activityCloseEnterAnimation: Int? = R.anim.st_anim_left_right_close_enter,
                @AnimRes activityCloseExitAnimation: Int? = R.anim.st_anim_left_right_close_exit,
                adapterDesignWidth: Int? = null,
                adapterDesignHeight: Int? = null,
                enableAdapterDesign: Boolean? = null
        ): Intent {
            val intent = Intent(context, activityClass)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_THEME, activityThem)
            // intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_SWIPE_BACK, enableSwipeBack) // TODO
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_SWIPE_BACK, false)
            if (enableSwipeBackRelate != null) intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_SWIPE_BACK_RELATE, enableSwipeBackRelate)
            if (enableSwipeBackShadow != null) intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_SWIPE_BACK_SHADOW, enableSwipeBackShadow)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_IMMERSION_STATUS_BAR, enableImmersionStatusBar)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_IMMERSION_STATUS_BAR_WITH_DARK_FONT, enableImmersionStatusBarWithDarkFont)
            if (statusBarAlphaForDarkFont != null) intent.putExtra(STActivityDelegate.KEY_ACTIVITY_STATUS_BAR_ALPHA_FOR_DARK_FONT, statusBarAlphaForDarkFont)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_EXIT_WITH_DOUBLE_BACK_PRESSED, enableExitWithDoubleBackPressed)
            if (enableFinishIfIsNotTaskRoot != null) intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_FINISH_IF_IS_NOT_TASK_ROOT, enableFinishIfIsNotTaskRoot)
            if (enableActivityFullScreenAndExpandLayout != null) intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_FULLSCREEN_AND_EXPAND_LAYOUT, enableActivityFullScreenAndExpandLayout)
            if (enableActivityFeatureNoTitle != null) intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_FEATURE_NO_TITLE, enableActivityFeatureNoTitle)
            if (activityDecorViewBackgroundResource != null) intent.putExtra(STActivityDelegate.KEY_ACTIVITY_DECOR_VIEW_BACKGROUND_RESOURCE, activityDecorViewBackgroundResource)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_CLOSE_ENTER_ANIMATION, activityCloseEnterAnimation)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_CLOSE_EXIT_ANIMATION, activityCloseExitAnimation)

            if (adapterDesignWidth != null) intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ADAPTER_DESIGN_WIDTH, adapterDesignWidth)
            if (adapterDesignHeight != null) intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ADAPTER_DESIGN_HEIGHT, adapterDesignHeight)
            if (enableAdapterDesign != null) intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_ADAPTER_DESIGN, enableAdapterDesign)

            intent.putExtra(KEY_DART_ENTRYPOINT_FUNCTION_NAME, dartEntrypointFunctionName)
            intent.putExtra(KEY_INITIAL_ROUTE, initialRoute)
            if (argumentsJsonString != null) intent.putExtra(KEY_ARGUMENTS_JSON_STRING, argumentsJsonString)
            if (context !is Activity || context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            return intent
        }
    }
}