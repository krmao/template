package com.smart.library.flutter

import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.ImageView
import androidx.annotation.FloatRange
import com.gyf.immersionbar.ImmersionBar
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import com.smart.library.base.STActivityDelegate
import com.smart.library.base.STBaseActivityDelegateImpl
import io.flutter.embedding.android.DrawableSplashScreen
import io.flutter.embedding.android.SplashScreen
import io.reactivex.disposables.CompositeDisposable

open class STFlutterBoostActivity : BoostFlutterActivity(), STActivityDelegate {

    protected open val delegate: STActivityDelegate by lazy { STBaseActivityDelegateImpl(this) }

    override fun disposables(): CompositeDisposable = delegate.disposables()
    override fun statusBar(): ImmersionBar? = delegate.statusBar()
    override fun onCreate(savedInstanceState: Bundle?) {
        onCreateBefore(savedInstanceState)
        super.onCreate(savedInstanceState)
        onCreateAfter(savedInstanceState)
    }

    override fun onCreateBefore(savedInstanceState: Bundle?) {
        delegate.onCreateBefore(savedInstanceState)
    }

    override fun onCreateAfter(savedInstanceState: Bundle?) {
        delegate.onCreateAfter(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delegate.onPostCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        delegate.onResume()
    }

//    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
//        return if(delegate.dispatchTouchEvent(event)){
//            true
//        }else {
//            super.dispatchTouchEvent(event)
//        }
//    }
//
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        delegate.onTouchEvent(event)
//        return super.onTouchEvent(event)
//    }

    override fun onDestroy() {
        try {
            super.onDestroy()
        }catch (e:Exception){
            e.printStackTrace()
        }
        delegate.onDestroy()
    }

    override fun finish() {
        super.finish()
        finishAfter()
    }
    override fun finishAfter() {
        delegate.finishAfter()
    }

    override fun provideSplashScreen(): SplashScreen? {
        val manifestSplashDrawable = getSplashScreenFromManifest()
        return if (manifestSplashDrawable != null) DrawableSplashScreen(manifestSplashDrawable, ImageView.ScaleType.CENTER, 500L) else null
    }

    private fun getSplashScreenFromManifest(): Drawable? {
        return try {
            val activityInfo = this.packageManager.getActivityInfo(this.componentName, PackageManager.GET_META_DATA)
            val metadata = activityInfo.metaData
            val splashScreenId = metadata?.getInt("io.flutter.embedding.android.SplashScreenDrawable")
            val splashUntilFirstFrame = metadata?.getBoolean("io.flutter.app.android.SplashScreenUntilFirstFrame")
            if (splashUntilFirstFrame == true && splashScreenId != null) (if (VERSION.SDK_INT > 21) this.resources.getDrawable(splashScreenId, this.theme) else this.resources.getDrawable(splashScreenId, null)) else null
        } catch (var4: PackageManager.NameNotFoundException) {
            null
        }
    }

    override fun onBackPressedIntercept(): Boolean = delegate.onBackPressedIntercept()

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (delegate.onKeyDown(keyCode, event)) {
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    override fun enableSwipeBack(): Boolean = delegate.enableSwipeBack()
    override fun enableSwipeBack(enable: Boolean) = delegate.enableSwipeBack(enable)
    override fun enableImmersionStatusBar(): Boolean = delegate.enableImmersionStatusBar()
    override fun enableImmersionStatusBar(enable: Boolean) = delegate.enableImmersionStatusBar(enable)
    override fun enableImmersionStatusBarWithDarkFont(): Boolean = delegate.enableImmersionStatusBarWithDarkFont()
    override fun enableImmersionStatusBarWithDarkFont(enable: Boolean) = delegate.enableImmersionStatusBarWithDarkFont(enable)
    override fun statusBarAlphaForDarkFont(@FloatRange(from = 0.0, to = 1.0) alpha: Float) = delegate.statusBarAlphaForDarkFont(alpha)
    override fun enableExitWithDoubleBackPressed(): Boolean = delegate.enableExitWithDoubleBackPressed()
    override fun enableExitWithDoubleBackPressed(enable: Boolean) = delegate.enableExitWithDoubleBackPressed(enable)
    override fun enableFinishIfIsNotTaskRoot(): Boolean = delegate.enableFinishIfIsNotTaskRoot()
    override fun enableFinishIfIsNotTaskRoot(enable: Boolean) = delegate.enableFinishIfIsNotTaskRoot(enable)
    override fun enableActivityFullScreenAndExpandLayout(): Boolean = delegate.enableActivityFullScreenAndExpandLayout()
    override fun enableActivityFullScreenAndExpandLayout(enable: Boolean) = delegate.enableActivityFullScreenAndExpandLayout(enable)
    override fun enableActivityFeatureNoTitle(): Boolean = delegate.enableActivityFeatureNoTitle()
    override fun enableActivityFeatureNoTitle(enable: Boolean) = delegate.enableActivityFeatureNoTitle(enable)
    override fun activityDecorViewBackgroundResource(): Int = delegate.activityDecorViewBackgroundResource()
    override fun activityDecorViewBackgroundResource(drawableResource: Int) = delegate.activityDecorViewBackgroundResource(drawableResource)
    override fun activityTheme(): Int = delegate.activityTheme()
    override fun activityTheme(activityTheme: Int) = delegate.activityTheme(activityTheme)
    override fun activityCloseEnterAnimation(): Int = delegate.activityCloseEnterAnimation()
    override fun activityCloseEnterAnimation(animation: Int) = delegate.activityCloseEnterAnimation(animation)
    override fun activityCloseExitAnimation(): Int = delegate.activityCloseExitAnimation()
    override fun activityCloseExitAnimation(animation: Int) = delegate.activityCloseExitAnimation(animation)
    override fun adapterDesignWidth(designWidth: Int) = delegate.adapterDesignWidth(designWidth)
    override fun adapterDesignHeight(designHeight: Int) = delegate.adapterDesignHeight(designHeight)
    override fun getResources(resources: Resources): Resources = delegate.getResources(resources)
    override fun enableAdapterDesign(enable: Boolean) = delegate.enableAdapterDesign(enable)
    override fun quitApplication() = delegate.quitApplication()

    companion object {
        fun withNewEngine(): NewEngineIntentBuilder {
            return NewEngineIntentBuilder(STFlutterBoostActivity::class.java)
        }
    }
}