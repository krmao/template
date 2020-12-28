package com.smart.library.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.AnimRes
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
            enableExitWithDoubleBackPressed: Boolean = false,
            enableFinishIfIsNotTaskRoot: Boolean = false,
            enableActivityFullScreenAndExpandLayout: Boolean = false,
            enableActivityFeatureNoTitle: Boolean = false,
            activityDecorViewBackgroundResource: Int = -1,
            @AnimRes activityOpenEnterAnimation: Int = R.anim.st_anim_left_right_open_enter,
            @AnimRes activityOpenExitAnimation: Int = R.anim.st_anim_left_right_open_exit,
            @AnimRes activityCloseEnterAnimation: Int = R.anim.st_anim_left_right_close_enter,
            @AnimRes activityCloseExitAnimation: Int = R.anim.st_anim_left_right_close_exit,
            adapterDesignWidth: Int = -1,
            adapterDesignHeight: Int = -1,
        ) {
            val intent: Intent = createIntent(from.context, fragmentClass, fragmentArguments, activityTheme, enableSwipeBack, enableImmersionStatusBar, enableImmersionStatusBarWithDarkFont, enableExitWithDoubleBackPressed, enableFinishIfIsNotTaskRoot, enableActivityFullScreenAndExpandLayout, enableActivityFeatureNoTitle, activityDecorViewBackgroundResource, activityCloseEnterAnimation, activityCloseExitAnimation, adapterDesignWidth, adapterDesignHeight)
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
            @StyleRes activityTheme: Int = STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT.id,
            enableSwipeBack: Boolean = true,
            enableImmersionStatusBar: Boolean = true,
            enableImmersionStatusBarWithDarkFont: Boolean = false,
            enableExitWithDoubleBackPressed: Boolean = false,
            enableFinishIfIsNotTaskRoot: Boolean = false,
            enableActivityFullScreenAndExpandLayout: Boolean = false,
            enableActivityFeatureNoTitle: Boolean = false,
            activityDecorViewBackgroundResource: Int = -1,
            @AnimRes activityOpenEnterAnimation: Int = R.anim.st_anim_left_right_open_enter,
            @AnimRes activityOpenExitAnimation: Int = R.anim.st_anim_left_right_open_exit,
            @AnimRes activityCloseEnterAnimation: Int = R.anim.st_anim_left_right_close_enter,
            @AnimRes activityCloseExitAnimation: Int = R.anim.st_anim_left_right_close_exit,
            adapterDesignWidth: Int = -1,
            adapterDesignHeight: Int = -1,
        ) {
            if (from != null) {
                val intent: Intent = createIntent(from, fragmentClass, fragmentArguments, activityTheme, enableSwipeBack, enableImmersionStatusBar, enableImmersionStatusBarWithDarkFont, enableExitWithDoubleBackPressed, enableFinishIfIsNotTaskRoot, enableActivityFullScreenAndExpandLayout, enableActivityFeatureNoTitle, activityDecorViewBackgroundResource, activityCloseEnterAnimation, activityCloseExitAnimation, adapterDesignWidth, adapterDesignHeight)
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
            enableExitWithDoubleBackPressed: Boolean = false,
            enableFinishIfIsNotTaskRoot: Boolean = false,
            enableActivityFullScreenAndExpandLayout: Boolean = false,
            enableActivityFeatureNoTitle: Boolean = false,
            activityDecorViewBackgroundResource: Int = -1,
            @AnimRes activityCloseEnterAnimation: Int = R.anim.st_anim_left_right_close_enter,
            @AnimRes activityCloseExitAnimation: Int = R.anim.st_anim_left_right_close_exit,
            adapterDesignWidth: Int = -1,
            adapterDesignHeight: Int = -1,
        ): Intent {
            val intent = Intent(context, STActivity::class.java)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_THEME, activityThem)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_SWIPE_BACK, enableSwipeBack)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_IMMERSION_STATUS_BAR, enableImmersionStatusBar)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_IMMERSION_STATUS_BAR_WITH_DARK_FONT, enableImmersionStatusBarWithDarkFont)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_EXIT_WITH_DOUBLE_BACK_PRESSED, enableExitWithDoubleBackPressed)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_FINISH_IF_IS_NOT_TASK_ROOT, enableFinishIfIsNotTaskRoot)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_FULLSCREEN_AND_EXPAND_LAYOUT, enableActivityFullScreenAndExpandLayout)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_FEATURE_NO_TITLE, enableActivityFeatureNoTitle)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_DECOR_VIEW_BACKGROUND_RESOURCE, activityDecorViewBackgroundResource)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_CLOSE_ENTER_ANIMATION, activityCloseEnterAnimation)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_CLOSE_EXIT_ANIMATION, activityCloseExitAnimation)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ADAPTER_DESIGN_WIDTH, adapterDesignWidth)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ADAPTER_DESIGN_HEIGHT, adapterDesignHeight)
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

