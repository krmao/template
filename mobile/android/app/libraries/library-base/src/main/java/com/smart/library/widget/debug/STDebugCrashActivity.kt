package com.smart.library.widget.debug

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.FloatRange
import androidx.annotation.Keep
import androidx.annotation.StyleRes
import com.smart.library.R
import com.smart.library.base.STActivity
import com.smart.library.base.STActivityDelegate

@Keep
@Suppress("unused", "MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
open class STDebugCrashActivity : STActivity() {

    companion object {

        @JvmStatic
        @JvmOverloads
        fun createIntent(
            context: Context?,
            fragmentClass: Any?,
            fragmentArguments: Bundle = Bundle(),
            @StyleRes activityThem: Int = STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT.id,
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
            @AnimRes activityCloseEnterAnimation: Int? = R.anim.st_anim_left_right_close_enter,
            @AnimRes activityCloseExitAnimation: Int? = R.anim.st_anim_left_right_close_exit,
            adapterDesignWidth: Int? = null,
            adapterDesignHeight: Int? = null,
            enableAdapterDesign: Boolean? = null
        ): Intent {
            val intent = Intent(context, STDebugCrashActivity::class.java)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_THEME, activityThem)
            intent.putExtra(STActivityDelegate.KEY_ACTIVITY_ENABLE_SWIPE_BACK, enableSwipeBack)
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
    }
}