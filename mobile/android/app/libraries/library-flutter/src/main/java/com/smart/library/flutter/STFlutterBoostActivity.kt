package com.smart.library.flutter

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.widget.ImageView
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import io.flutter.embedding.android.DrawableSplashScreen
import io.flutter.embedding.android.SplashScreen

open class STFlutterBoostActivity : BoostFlutterActivity() {

    companion object {
        fun withNewEngine(): NewEngineIntentBuilder {
            return NewEngineIntentBuilder(STFlutterBoostActivity::class.java)
        }
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

}