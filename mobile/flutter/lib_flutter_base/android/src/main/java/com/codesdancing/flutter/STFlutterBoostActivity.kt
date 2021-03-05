package com.codesdancing.flutter

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import androidx.annotation.FloatRange
import com.gyf.immersionbar.ImmersionBar
import com.idlefish.flutterboost.containers.FlutterBoostActivity
import com.smart.library.base.STActivityDelegate
import com.smart.library.base.STBaseActivityDelegateImpl
import io.flutter.embedding.android.DrawableSplashScreen
import io.flutter.embedding.android.SplashScreen
import io.reactivex.disposables.CompositeDisposable

open class STFlutterBoostActivity : FlutterBoostActivity(), STActivityDelegate {

    protected open val activityDelegate: STActivityDelegate by lazy { STBaseActivityDelegateImpl(this) }

    override fun disposables(): CompositeDisposable = activityDelegate.disposables()
    override fun statusBar(): ImmersionBar? = activityDelegate.statusBar()
    override fun onCreate(savedInstanceState: Bundle?) {
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
        super.onPostCreate(savedInstanceState)
        activityDelegate.onPostCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        activityDelegate.onResume()
    }

    override fun onDestroy() {
        try {
            super.onDestroy()
        }catch (e:Exception){
            e.printStackTrace()
        }
        activityDelegate.onDestroy()
    }

    override fun finish() {
        super.finish()
        finishAfter()
    }
    override fun finishAfter() {
        activityDelegate.finishAfter()
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
    
}