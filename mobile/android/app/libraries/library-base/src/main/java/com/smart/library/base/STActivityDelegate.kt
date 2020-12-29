package com.smart.library.base

import android.content.res.Resources
import android.os.Bundle
import android.view.KeyEvent
import androidx.annotation.FloatRange
import androidx.annotation.StyleRes
import com.gyf.immersionbar.ImmersionBar
import com.smart.library.R
import io.reactivex.disposables.CompositeDisposable

interface STActivityDelegate {

    fun disposables(): CompositeDisposable

    fun statusBar(): ImmersionBar?

    fun onCreateBefore(savedInstanceState: Bundle?)

    fun onCreateAfter(savedInstanceState: Bundle?)

    fun onPostCreate(savedInstanceState: Bundle?)

    fun onResume()

    fun onDestroy()

    fun finishAfter()

    fun onBackPressedIntercept(): Boolean = false

    fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean

    fun enableSwipeBack(): Boolean
    fun enableSwipeBack(enable: Boolean)
    fun enableImmersionStatusBar(): Boolean
    fun enableImmersionStatusBar(enable: Boolean)
    fun enableImmersionStatusBarWithDarkFont(): Boolean
    fun statusBarAlphaForDarkFont(@FloatRange(from = 0.0, to = 1.0) alpha: Float)
    fun enableImmersionStatusBarWithDarkFont(enable: Boolean)
    fun enableExitWithDoubleBackPressed(): Boolean
    fun enableExitWithDoubleBackPressed(enable: Boolean)
    fun enableFinishIfIsNotTaskRoot(): Boolean
    fun enableFinishIfIsNotTaskRoot(enable: Boolean)
    fun enableActivityFullScreenAndExpandLayout(): Boolean
    fun enableActivityFullScreenAndExpandLayout(enable: Boolean)
    fun enableActivityFeatureNoTitle(): Boolean
    fun enableActivityFeatureNoTitle(enable: Boolean)
    fun activityDecorViewBackgroundResource(): Int
    fun activityDecorViewBackgroundResource(drawableResource: Int)
    fun activityCloseEnterAnimation(): Int
    fun activityCloseEnterAnimation(animation: Int)
    fun activityCloseExitAnimation(): Int
    fun activityCloseExitAnimation(animation: Int)
    fun activityTheme(): Int
    fun activityTheme(activityTheme: Int)
    fun adapterDesignWidth(designWidth: Int)
    fun adapterDesignHeight(designHeight: Int)
    fun enableAdapterDesign(enable: Boolean)
    fun getResources(resources:Resources): Resources
    fun quitApplication()

    @Suppress("unused")
    enum class Theme(@StyleRes val id: Int) {
        APP_THEME(R.style.STAppTheme),
        APP_THEME_LAUNCH(R.style.STAppTheme_Launch),
        APP_THEME_HOME(R.style.STAppTheme_Home),
        APP_THEME_LOGIN(R.style.STAppTheme_Login),
        APP_THEME_NORMAL(R.style.STAppTheme_Normal),
        APP_THEME_NORMAL_FADE(R.style.STAppTheme_Normal_Fade),
        APP_THEME_NORMAL_FULLSCREEN(R.style.STAppTheme_Normal_FullScreen),
        APP_THEME_NORMAL_ACTIONBAR(R.style.STAppTheme_Normal_ActionBar),
        APP_THEME_NORMAL_ACTIONBAR_TRANSLUCENT(R.style.STAppTheme_Normal_ActionBar_Translucent),
        APP_THEME_NORMAL_ACTIONBAR_TRANSLUCENT_FADE(R.style.STAppTheme_Normal_ActionBar_Translucent_Fade),
        APP_THEME_NORMAL_ACTIONBAR_TRANSPARENT(R.style.STAppTheme_Normal_ActionBar_Transparent),
        APP_THEME_NORMAL_ACTIONBAR_TRANSPARENT_FADE(R.style.STAppTheme_Normal_ActionBar_Transparent_Fade),
        APP_THEME_NORMAL_TRANSLUCENT(R.style.STAppTheme_Normal_Translucent),
        APP_THEME_NORMAL_TRANSPARENT(R.style.STAppTheme_Normal_Transparent),
        APP_THEME_NORMAL_TRANSPARENT_FADE(R.style.STAppTheme_Normal_Transparent_Fade),
        APP_THEME_NORMAL_TRANSLUCENT_FADE(R.style.STAppTheme_Normal_Translucent_Fade),
    }

    @Suppress("unused")
    companion object {
        const val KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME"
        const val KEY_ACTIVITY_ENABLE_SWIPE_BACK = "KEY_ACTIVITY_ENABLE_SWIPE_BACK"
        const val KEY_ACTIVITY_ENABLE_IMMERSION_STATUS_BAR = "KEY_ACTIVITY_ENABLE_IMMERSION_STATUS_BAR"
        const val KEY_ACTIVITY_ENABLE_IMMERSION_STATUS_BAR_WITH_DARK_FONT = "KEY_ACTIVITY_ENABLE_IMMERSION_STATUS_BAR_WITH_DARK_FONT"
        const val KEY_ACTIVITY_STATUS_BAR_ALPHA_FOR_DARK_FONT = "KEY_ACTIVITY_STATUS_BAR_ALPHA_FOR_DARK_FONT"
        const val KEY_ACTIVITY_ENABLE_EXIT_WITH_DOUBLE_BACK_PRESSED = "KEY_ACTIVITY_ENABLE_EXIT_WITH_DOUBLE_BACK_PRESSED"
        const val KEY_ACTIVITY_ENABLE_FINISH_IF_IS_NOT_TASK_ROOT = "KEY_ACTIVITY_ENABLE_FINISH_IF_IS_NOT_TASK_ROOT"
        const val KEY_ACTIVITY_ENABLE_FULLSCREEN_AND_EXPAND_LAYOUT = "KEY_ACTIVITY_ENABLE_FULLSCREEN_AND_EXPAND_LAYOUT"
        const val KEY_ACTIVITY_ENABLE_FEATURE_NO_TITLE = "KEY_ACTIVITY_ENABLE_FEATURE_NO_TITLE"
        const val KEY_ACTIVITY_DECOR_VIEW_BACKGROUND_RESOURCE = "KEY_ACTIVITY_DECOR_VIEW_BACKGROUND_RESOURCE"
        const val KEY_ACTIVITY_CLOSE_ENTER_ANIMATION = "KEY_ACTIVITY_CLOSE_ENTER_ANIMATION"
        const val KEY_ACTIVITY_CLOSE_EXIT_ANIMATION = "KEY_ACTIVITY_CLOSE_EXIT_ANIMATION"
        const val KEY_ACTIVITY_ADAPTER_DESIGN_WIDTH = "KEY_ACTIVITY_ADAPTER_DESIGN_WIDTH"
        const val KEY_ACTIVITY_ADAPTER_DESIGN_HEIGHT = "KEY_ACTIVITY_ADAPTER_DESIGN_HEIGHT"
        const val KEY_ACTIVITY_ENABLE_ADAPTER_DESIGN = "KEY_ACTIVITY_ENABLE_ADAPTER_DESIGN"
    }
}
