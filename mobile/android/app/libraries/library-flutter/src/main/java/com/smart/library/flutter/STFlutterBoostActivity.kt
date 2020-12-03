package com.smart.library.flutter

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Bundle
import android.view.KeyEvent
import android.widget.ImageView
import androidx.annotation.NonNull
import com.gyf.barlibrary.ImmersionBar
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import com.smart.library.base.STBaseActivityDelegate
import com.smart.library.base.STBaseActivityDelegateImpl
import io.flutter.embedding.android.DrawableSplashScreen
import io.flutter.embedding.android.SplashScreen
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.reactivex.disposables.CompositeDisposable

open class STFlutterBoostActivity : BoostFlutterActivity(), STBaseActivityDelegate {

    private var methodChannel: MethodChannel? = null

    /**
     * https://flutter.dev/docs/development/platform-integration/platform-channels
     * https://flutter.dev/docs/development/add-to-app/android/project-setup
     * https://flutter.dev/docs/development/platform-integration/platform-channels#step-3a-add-an-android-platform-specific-implementation-using-java
     * ------------------------------------------------------------------------------------------------------------------------
     * EventChannel 用于触摸等流事件, 持续通信，收到消息后无法回复此次消息, 通常用于Native向Dart的通信
     * MethodChannel 用于方法调用, 一次性通信, 通常用于Dart调用Native的方法
     * BasicMessageChannel 用于传递数据, 持续通信，收到信息后可以进行回复
     * ------------------------------------------------------------------------------------------------------------------------
     * Dart	            Java	            Kotlin	    Obj-C	                    Swift
     * null	            null	            null	    nil (NSNull when nested)	nil
     * bool	            java.lang.Boolean	Boolean	    NSNumber numberWithBool:	NSNumber(value: Bool)
     * int	            java.lang.Integer	Int	        NSNumber numberWithInt:	    NSNumber(value: Int32)
     * int, if 32bits   java.lang.Long	    Long	    NSNumber numberWithLong:	NSNumber(value: Int)
     * not enough
     * double           java.lang.Double	Double	    NSNumber numberWithDouble:	NSNumber(value: Double)
     * String	        java.lang.String	String	    NSString	String
     * Uint8List	    byte[]	            ByteArray	FlutterStandardTypedData	FlutterStandardTypedData(bytes: Data)
     *                                                  typedDataWithBytes
     * Int32List	    int[]	            IntArray	FlutterStandardTypedData	FlutterStandardTypedData(int32: Data)
     *                                                  typedDataWithInt32
     * Int64List	    long[]	            LongArray	FlutterStandardTypedData	FlutterStandardTypedData(int64: Data)
     *                                                  typedDataWithInt64
     * Float64List	    double[]	        DoubleArray	FlutterStandardTypedData	FlutterStandardTypedData(float64: Data)
     *                                                  typedDataWithFloat64
     * List	            java.util.ArrayList	List	    NSArray	                    Array
     * Map	            java.util.HashMap	HashMap	    NSDictionary	            Dictionary
     *------------------------------------------------------------------------------------------------------------------------
     */
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, STFlutterInitializer.CHANNEL_METHOD).apply {
            setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                STFlutterInitializer.onFlutterCallNativeMethod(activity, call, result)
            }
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

    protected open val delegate: STBaseActivityDelegate by lazy { STBaseActivityDelegateImpl(this) }

    override fun callback(): ((bundle: Bundle?) -> Unit?)? = delegate.callback()
    override fun disposables(): CompositeDisposable = delegate.disposables()
    override fun statusBar(): ImmersionBar? = delegate.statusBar()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        delegate.onPostCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        delegate.onResume()
        STFlutterInitializer.currentMethodChannel = methodChannel
    }

    override fun onDestroy() {
        super.onDestroy()
        delegate.onDestroy()
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
    override fun enableExitWithDoubleBackPressed(): Boolean = delegate.enableExitWithDoubleBackPressed()
    override fun enableExitWithDoubleBackPressed(enable: Boolean) = delegate.enableExitWithDoubleBackPressed(enable)
    override fun quitApplication() = delegate.quitApplication()

    companion object {
        fun withNewEngine(): NewEngineIntentBuilder {
            return NewEngineIntentBuilder(STFlutterBoostActivity::class.java)
        }
    }
}