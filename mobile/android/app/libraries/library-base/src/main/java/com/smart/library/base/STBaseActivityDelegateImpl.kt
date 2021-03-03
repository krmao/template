package com.smart.library.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.Window
import androidx.annotation.FloatRange
import androidx.fragment.app.FragmentActivity
import com.gyf.immersionbar.ImmersionBar
import com.smart.library.STInitializer
import com.smart.library.util.STAdaptScreenUtils
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.swipeback.STSwipeBackLayout
import com.smart.library.util.swipeback.relate.STSwipeBackHelper
import com.smart.library.widget.debug.STDebugFragment
import com.smart.library.widget.debug.STDebugManager
import io.reactivex.disposables.CompositeDisposable


@Suppress("MemberVisibilityCanBePrivate")
open class STBaseActivityDelegateImpl(val activity: Activity) : STActivityDelegate {

    protected val disposables: CompositeDisposable by lazy { CompositeDisposable() }
    protected var statusBar: ImmersionBar? = null

    protected var exitTime: Long = 0
    protected var enableSwipeBack = true
    protected var enableSwipeBackRelate: Boolean = true
    protected var enableImmersionStatusBar = true
    protected var enableImmersionStatusBarWithDarkFont = false
    protected var statusBarAlphaForDarkFont: Float = 0.8f
    protected var enableExitWithDoubleBackPressed = false
    protected var enableFinishIfIsNotTaskRoot = false
    protected var enableActivityFullScreenAndExpandLayout = false
    protected var enableActivityFeatureNoTitle = false
    protected var activityDecorViewBackgroundResource = -1
    protected var activityTheme = -1
    protected var activityCloseEnterAnimation: Int = -1
    protected var activityCloseExitAnimation: Int = -1
    protected var enableAdapterDesign: Boolean = STInitializer.configAdapterDesign?.enableAdapterDesign ?: true
    protected var adapterDesignWidth: Int = -1
    protected var adapterDesignHeight: Int = -1
    protected var isActivityThemeTranslucent: Boolean = false
    protected var isActivityThemeTransparent: Boolean = false

    override fun onCreateBefore(savedInstanceState: Bundle?) {
        //region read params
        val bundle: Bundle? = activity.intent?.extras
        if (bundle != null) {
            activityTheme = bundle.getInt(STActivityDelegate.KEY_ACTIVITY_THEME, activityTheme)
            isActivityThemeTransparent = activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT.id ||
                    activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT_FADE.id ||
                    activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_ACTIONBAR_TRANSPARENT.id ||
                    activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_ACTIONBAR_TRANSPARENT_FADE.id
            isActivityThemeTranslucent = activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSLUCENT.id ||
                    activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSLUCENT_FADE.id ||
                    activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT.id ||
                    activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_TRANSPARENT_FADE.id ||
                    activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_ACTIONBAR_TRANSLUCENT.id ||
                    activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_ACTIONBAR_TRANSLUCENT_FADE.id ||
                    activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_ACTIONBAR_TRANSPARENT.id ||
                    activityTheme == STActivityDelegate.Theme.APP_THEME_NORMAL_ACTIONBAR_TRANSPARENT_FADE.id

            enableImmersionStatusBar = bundle.getBoolean(STActivityDelegate.KEY_ACTIVITY_ENABLE_IMMERSION_STATUS_BAR, enableImmersionStatusBar)
            enableImmersionStatusBarWithDarkFont = bundle.getBoolean(STActivityDelegate.KEY_ACTIVITY_ENABLE_IMMERSION_STATUS_BAR_WITH_DARK_FONT, enableImmersionStatusBarWithDarkFont)
            statusBarAlphaForDarkFont = bundle.getFloat(STActivityDelegate.KEY_ACTIVITY_STATUS_BAR_ALPHA_FOR_DARK_FONT, statusBarAlphaForDarkFont)

            //region can't enableSwipeBack if enableExitWithDoubleBackPressed == true
            enableSwipeBackRelate = bundle.getBoolean(STActivityDelegate.KEY_ACTIVITY_ENABLE_SWIPE_BACK_RELATE, !isActivityThemeTransparent)
            enableSwipeBack = bundle.getBoolean(STActivityDelegate.KEY_ACTIVITY_ENABLE_SWIPE_BACK, enableSwipeBack)
            enableExitWithDoubleBackPressed = bundle.getBoolean(STActivityDelegate.KEY_ACTIVITY_ENABLE_EXIT_WITH_DOUBLE_BACK_PRESSED, enableExitWithDoubleBackPressed)
            if (enableExitWithDoubleBackPressed) {
                enableSwipeBack = false
            }
            //endregion

            enableFinishIfIsNotTaskRoot = bundle.getBoolean(STActivityDelegate.KEY_ACTIVITY_ENABLE_FINISH_IF_IS_NOT_TASK_ROOT, enableFinishIfIsNotTaskRoot)
            enableActivityFullScreenAndExpandLayout = bundle.getBoolean(STActivityDelegate.KEY_ACTIVITY_ENABLE_FULLSCREEN_AND_EXPAND_LAYOUT, enableActivityFullScreenAndExpandLayout)
            enableActivityFeatureNoTitle = bundle.getBoolean(STActivityDelegate.KEY_ACTIVITY_ENABLE_FEATURE_NO_TITLE, enableActivityFeatureNoTitle)
            activityDecorViewBackgroundResource = bundle.getInt(STActivityDelegate.KEY_ACTIVITY_DECOR_VIEW_BACKGROUND_RESOURCE, activityDecorViewBackgroundResource)
            activityCloseEnterAnimation = bundle.getInt(STActivityDelegate.KEY_ACTIVITY_CLOSE_ENTER_ANIMATION, activityCloseEnterAnimation)
            activityCloseExitAnimation = bundle.getInt(STActivityDelegate.KEY_ACTIVITY_CLOSE_EXIT_ANIMATION, activityCloseExitAnimation)
            adapterDesignWidth = bundle.getInt(STActivityDelegate.KEY_ACTIVITY_ADAPTER_DESIGN_WIDTH, adapterDesignWidth)
            adapterDesignHeight = bundle.getInt(STActivityDelegate.KEY_ACTIVITY_ADAPTER_DESIGN_HEIGHT, adapterDesignHeight)
            enableAdapterDesign = bundle.getBoolean(STActivityDelegate.KEY_ACTIVITY_ENABLE_ADAPTER_DESIGN, enableAdapterDesign)
        }
        //endregion

        if (activityTheme != -1) {
            // setTheme 设置透明主题无效
            // https://blog.csdn.net/qq_43278826/article/details/107557784
            // https://medium.com/pxhouse/runtime-theming-of-translucent-activities-640013ad40d0
            activity.setTheme(activityTheme)
        }
        // 代码设置可以看到状态栏动画, theme.xml 中设置全屏比较突兀
        if (enableActivityFeatureNoTitle) {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        /*if (activityDecorViewBackgroundResource > 0) {
            activity.window.decorView.setBackgroundResource(activityDecorViewBackgroundResource)
        }*/
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreateAfter(savedInstanceState: Bundle?) {
        if (enableActivityFullScreenAndExpandLayout) {
            STSystemUtil.setActivityFullScreenAndExpandLayout(activity)
        }

        // 避免通过其他方式启动程序后，再通过程序列表中的launcher启动，重新走启动流程 todo
        if (enableFinishIfIsNotTaskRoot && !activity.isTaskRoot) {
            val intent: Intent? = activity.intent
            val action: String? = intent?.action
            if (intent?.hasCategory(Intent.CATEGORY_LAUNCHER) == true && action == Intent.ACTION_MAIN) {
                activity.finish()
                return
            }
        }

        if (enableImmersionStatusBar) {
            //navigationBarEnable=true 华为荣耀6 4.4.2 手机会出现导航栏错乱问题
            statusBar = ImmersionBar.with(activity)
                .navigationBarEnable(false)
                .statusBarColorInt(Color.TRANSPARENT)
                .statusBarAlpha(0f)
                .transparentStatusBar()
                .statusBarDarkFont(enableImmersionStatusBarWithDarkFont, if (enableImmersionStatusBarWithDarkFont) statusBarAlphaForDarkFont else 0f)
            statusBar?.init()
        }

        STSwipeBackHelper.onCreate(activity)
        STSwipeBackHelper.getCurrentPage(activity)                      // get current instance
            .setSwipeBackEnable(enableSwipeBack, isActivityThemeTranslucent)    // on-off
            .setSwipeRelateEnable(enableSwipeBackRelate)                  // 如果是透明主题背景, 当位移时, 前一个页面的前一个页面是黑色背景, 因此透明背景关闭位移功能 if should move together with the following Activity
            .setSwipeRelateOffset((STSystemUtil.screenWidth() * 0.4).toInt())   // the Offset of following Activity when setSwipeRelateEnable(true)
            // .setSwipeEdge(200)                                       // set the touch area。200 mean only the left 200px of screen can touch to begin swipe.
            .setSwipeEdgePercent(0.1f)                                  // 0.2 mean left 20% of screen can touch to begin swipe.
            .setSwipeSensitivity(0.7f)                                  // sensitiveness of the gesture。0:slow  1:sensitive
            .setScrimColor(Color.parseColor("#99000000"))    // 50%==7f 60%=99 color of Scrim below the activity
            .setClosePercent(0.7f)                                      // close activity when swipe over activity
            .setDisallowInterceptTouchEvent(false)                      // your view can hand the events first.default false;
            .addListener(object : STSwipeBackLayout.SwipeListener {
                override fun onScrollStateChange(state: Int, scrollPercent: Float) {
                }

                override fun onEdgeTouch(edgeFlag: Int) {
                }

                override fun onScrollOverThreshold() {
                }
            })
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        STSwipeBackHelper.onPostCreate(activity)
    }

    override fun onResume() {
        STDebugManager.showActivityInfo(activity)
    }

    override fun onDestroy() {
        disposables.dispose()
        STSwipeBackHelper.onDestroy(activity)
    }

    override fun finishAfter() {
        if (activityCloseEnterAnimation != -1 && activityCloseExitAnimation != -1) {
            STActivity.overrideWindowAnim(activity, activityCloseEnterAnimation, activityCloseExitAnimation)
        }
    }

    override fun onBackPressedIntercept(): Boolean = false
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (onBackPressedIntercept()) return true

            activity.window.decorView.clearAnimation()
            if (activity is FragmentActivity) {
                activity.supportFragmentManager.fragments.firstOrNull { it is STBaseFragment.OnBackPressedListener }?.let {
                    if ((it as? STBaseFragment.OnBackPressedListener)?.onBackPressed() == true) {
                        // intercept
                        return true
                    }
                }

                if (activity.supportFragmentManager.backStackEntryCount == 0) {
                    if (enableExitWithDoubleBackPressed) quitApplication() else activity.finish()
                } else {
                    activity.supportFragmentManager.popBackStackImmediate()
                }
            } else {
                if (enableExitWithDoubleBackPressed) quitApplication() else activity.finish()
            }
            return true
        } else {
            return false
        }
    }

    override fun disposables(): CompositeDisposable = disposables
    override fun statusBar(): ImmersionBar? = statusBar
    override fun enableSwipeBack(): Boolean = enableSwipeBack

    @SuppressLint("ObsoleteSdkInt")
    override fun enableSwipeBack(enable: Boolean) {
        //region can't enableSwipeBack if enableExitWithDoubleBackPressed == true
        if (!enableExitWithDoubleBackPressed || (enableExitWithDoubleBackPressed && !enable)) {
            //endregion
            try {
                this.enableSwipeBack = enable
                STSwipeBackHelper.getCurrentPage(activity).setSwipeBackEnable(enable, isActivityThemeTranslucent)
            } catch (_: RuntimeException) {
            }
        }
    }

    override fun enableSwipeBackRelate(): Boolean = enableSwipeBackRelate

    @SuppressLint("ObsoleteSdkInt")
    override fun enableSwipeBackRelate(enable: Boolean) {
        try {
            this.enableSwipeBackRelate = enable
            STSwipeBackHelper.getCurrentPage(activity).setSwipeRelateEnable(enable)
        } catch (_: RuntimeException) {
        }
    }

    override fun enableImmersionStatusBar(): Boolean = enableImmersionStatusBar
    override fun enableImmersionStatusBar(enable: Boolean) {
        this.enableImmersionStatusBar = enable
    }

    override fun enableImmersionStatusBarWithDarkFont(): Boolean = enableImmersionStatusBarWithDarkFont
    override fun enableImmersionStatusBarWithDarkFont(enable: Boolean) {
        this.enableImmersionStatusBarWithDarkFont = enable
    }

    override fun statusBarAlphaForDarkFont(@FloatRange(from = 0.0, to = 1.0) alpha: Float) {
        this.statusBarAlphaForDarkFont = alpha
    }

    override fun enableExitWithDoubleBackPressed(): Boolean = enableExitWithDoubleBackPressed
    override fun enableExitWithDoubleBackPressed(enable: Boolean) {
        this.enableExitWithDoubleBackPressed = enable
    }

    override fun enableFinishIfIsNotTaskRoot(): Boolean = enableFinishIfIsNotTaskRoot
    override fun enableFinishIfIsNotTaskRoot(enable: Boolean) {
        this.enableFinishIfIsNotTaskRoot = enable
    }

    override fun enableActivityFullScreenAndExpandLayout(): Boolean = enableActivityFullScreenAndExpandLayout
    override fun enableActivityFullScreenAndExpandLayout(enable: Boolean) {
        this.enableActivityFullScreenAndExpandLayout = enable
    }

    override fun enableActivityFeatureNoTitle(): Boolean = enableActivityFeatureNoTitle
    override fun enableActivityFeatureNoTitle(enable: Boolean) {
        this.enableActivityFeatureNoTitle = enable
    }

    override fun activityDecorViewBackgroundResource(): Int = activityDecorViewBackgroundResource
    override fun activityDecorViewBackgroundResource(drawableResource: Int) {
        this.activityDecorViewBackgroundResource = drawableResource
    }

    override fun activityCloseEnterAnimation(): Int = activityCloseEnterAnimation
    override fun activityCloseEnterAnimation(animation: Int) {
        this.activityCloseEnterAnimation = animation
    }

    override fun activityCloseExitAnimation(): Int = activityCloseExitAnimation
    override fun activityCloseExitAnimation(animation: Int) {
        this.activityCloseExitAnimation = animation
    }

    override fun activityTheme(): Int = activityTheme
    override fun activityTheme(activityTheme: Int) {
        this.activityTheme = activityTheme
    }

    override fun adapterDesignWidth(designWidth: Int) {
        this.adapterDesignWidth = designWidth
    }

    override fun adapterDesignHeight(designHeight: Int) {
        this.adapterDesignHeight = designHeight
    }

    override fun enableAdapterDesign(enable: Boolean) {
        this.enableAdapterDesign = enable
    }

    override fun getResources(resources: Resources): Resources {
        val globalAdapterDesignWidth = STInitializer.configAdapterDesign?.adapterDesignWidth ?: -1
        val globalAdapterDesignHeight = STInitializer.configAdapterDesign?.adapterDesignHeight ?: -1
        val enableGlobalAdapterDesign = STInitializer.configAdapterDesign?.enableAdapterDesign ?: false
        return when {
            enableAdapterDesign && adapterDesignWidth != -1 -> STAdaptScreenUtils.adaptWidth(resources, adapterDesignWidth)
            enableAdapterDesign && adapterDesignHeight != -1 -> STAdaptScreenUtils.adaptHeight(resources, adapterDesignHeight)
            enableAdapterDesign && enableGlobalAdapterDesign && globalAdapterDesignWidth != -1 -> STAdaptScreenUtils.adaptWidth(resources, globalAdapterDesignWidth)
            enableAdapterDesign && enableGlobalAdapterDesign && globalAdapterDesignHeight != -1 -> STAdaptScreenUtils.adaptHeight(resources, globalAdapterDesignHeight)
            else -> STAdaptScreenUtils.closeAdapt(resources)
        }
    }

    override fun quitApplication() = if (System.currentTimeMillis() - exitTime > 2000) {
        STToastUtil.show("再按一次退出程序")
        exitTime = System.currentTimeMillis()
    } else {
        if (STInitializer.debug()) {
            STDebugFragment.cancelDebugNotification()
        }
        STInitializer.quitApplication()
    }
}
