package com.smart.library.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.AnimRes
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import com.smart.library.R
import com.smart.library.STInitializer
import com.smart.library.util.STLogUtil

@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
open class STActivity : STBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            setContentView(FrameLayout(this))

            val bundle: Bundle? = intent.extras
            var fragment: Fragment? = null
            var fragmentClassName: String? = null

            when (val fragmentObject: Any? = bundle?.get(KEY_FRAGMENT_CLASS)) {
                is Class<*> -> {
                    fragmentClassName = fragmentObject.name
                    fragment = fragmentObject.newInstance() as Fragment
                }
                is String -> {
                    fragmentClassName = fragmentObject
                    try {
                        fragment = Class.forName(fragmentObject).newInstance() as Fragment
                    } catch (e: Exception) {
                        STLogUtil.e(TAG, "ClassNotFoundException:$fragmentClassName", e)
                    }
                }
                else -> {
                    STLogUtil.e(TAG, "KEY_FRAGMENT_CLASS must be either Fragment::javaClass or Fragment::javaClass.canonicalName !!!")
                }
            }
            if (fragment != null && !fragmentClassName.isNullOrBlank()) {
                fragment.arguments = bundle?.getBundle(KEY_FRAGMENT_ARGUMENTS)
                supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, fragmentClassName).commitAllowingStateLoss()
            }
        } catch (e: Exception) {
            STLogUtil.e(STActivity::javaClass.name, "Has error in new instance of fragment", e)
        }
    }

    companion object {
        private const val TAG = "[STActivity]"
        const val KEY_FRAGMENT_CLASS = "KEY_FRAGMENT_CLASS"
        const val KEY_FRAGMENT_ARGUMENTS = "KEY_FRAGMENT_ARGUMENTS"

        /**
         * 由 fragment 跳转, 由 fragment 接收结果
         */
        @JvmStatic
        @JvmOverloads
        fun startActivity(
            from: Fragment,
            fragmentClass: Any?,
            fragmentArguments: Bundle = Bundle(),
            requestCode: Int = 0,
            @StyleRes activityTheme: Int = STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT.id,
            enableSwipeBack: Boolean = true,
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
            val intent: Intent = createIntent(
                context = from.context,
                fragmentClass = fragmentClass,
                fragmentArguments = fragmentArguments,
                activityThem = activityTheme,
                enableSwipeBack = enableSwipeBack,
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
         * @param fragmentClass Fragment::javaClass or Fragment::javaClass.canonicalName
         */
        @JvmStatic
        @JvmOverloads
        fun startActivity(
            from: Context?,
            fragmentClass: Any?,
            fragmentArguments: Bundle = Bundle(),
            requestCode: Int = 0,
            @StyleRes activityTheme: Int = STActivityDelegate.Theme.APP_THEME_NORMAL.id,
            enableSwipeBack: Boolean = true,
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
                val intent: Intent = createIntent(
                    context = from,
                    fragmentClass = fragmentClass,
                    fragmentArguments = fragmentArguments,
                    activityThem = activityTheme,
                    enableSwipeBack = enableSwipeBack,
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

        @JvmStatic
        @JvmOverloads
        fun createIntent(
            context: Context?,
            fragmentClass: Any?,
            fragmentArguments: Bundle = Bundle(),
            @StyleRes activityThem: Int = STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT.id,
            enableSwipeBack: Boolean = true,
            enableImmersionStatusBar: Boolean = true,
            enableImmersionStatusBarWithDarkFont: Boolean = false,
            @FloatRange(from = 0.0, to = 1.0) statusBarAlphaForDarkFont: Float? = null,
            enableExitWithDoubleBackPressed: Boolean = false,
            enableFinishIfIsNotTaskRoot: Boolean? = null,
            enableActivityFullScreenAndExpandLayout: Boolean? = null,
            enableActivityFeatureNoTitle: Boolean? = null,
            activityDecorViewBackgroundResource: Int? = null,
            @AnimRes activityCloseEnterAnimation: Int = R.anim.st_anim_left_right_close_enter,
            @AnimRes activityCloseExitAnimation: Int = R.anim.st_anim_left_right_close_exit,
            adapterDesignWidth: Int? = null,
            adapterDesignHeight: Int? = null,
            enableAdapterDesign: Boolean? = null
        ): Intent {
            val intent = Intent(context, STActivity::class.java)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_THEME, activityThem)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_SWIPE_BACK, enableSwipeBack)
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

            if (fragmentClass is Class<*>) {
                intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClass)
            } else if (fragmentClass is String) {
                intent.putExtra(KEY_FRAGMENT_CLASS, fragmentClass)
            }
            intent.putExtra(KEY_FRAGMENT_ARGUMENTS, fragmentArguments)
            if (context !is Activity || context is Application) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            return intent
        }

        @UiThread
        @JvmStatic
        @JvmOverloads
        fun overrideWindowAnim(context: Context?, @AnimRes enterAnimation: Int = R.anim.st_anim_left_right_open_enter, @AnimRes exitAnimation: Int = R.anim.st_anim_left_right_close_exit, intent: Intent? = null) {
            //region home class 无需重写动画
            try {
                if (intent != null && intent.component != null) {
                    val className: String? = intent.component?.className
                    if (!className.isNullOrBlank() && className == STInitializer.configClass?.homeClass?.javaClass?.name) {
                        return
                    }
                }
            } catch (_: java.lang.Exception) {
            }
            //endregion
            if (context != null && context is Activity) {
                context.overridePendingTransition(enterAnimation, exitAnimation)
            }
        }
    }
}