package com.smart.library.flutter

import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.NonNull
import io.flutter.embedding.android.DrawableSplashScreen
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.android.SplashScreen
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel


/**
 * smart://template/flutter?page=home&params=jsonString
 */
@Suppress("unused", "PrivatePropertyName")
class STFlutterFragment : FlutterFragment() {

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
        methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, STFlutterManager.CHANNEL_METHOD).apply {
            setMethodCallHandler { call: MethodCall, result: MethodChannel.Result ->
                STFlutterManager.onFlutterCallNativeMethod(activity, call, result)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        STFlutterManager.currentMethodChannel = methodChannel
    }

    override fun provideSplashScreen(): SplashScreen? {
        val manifestSplashDrawable: Drawable? = getSplashScreenFromManifest()
        return manifestSplashDrawable?.let { DrawableSplashScreen(it) }
    }

    /**
     * Returns a [Drawable] to be used as a splash screen as requested by meta-data in the
     * `AndroidManifest.xml` file, or null if no such splash screen is requested.
     *
     *
     * See [FlutterActivityLaunchConfigs.SPLASH_SCREEN_META_DATA_KEY] for the meta-data key
     * to be used in a manifest file.
     */
    @Suppress("DEPRECATION")
    private fun getSplashScreenFromManifest(): Drawable? {
        val parentActivity = activity
        return if (parentActivity != null) {
            try {
                val activityInfo = parentActivity.packageManager.getActivityInfo(parentActivity.componentName, PackageManager.GET_META_DATA)
                val metadata = activityInfo.metaData
                val splashScreenId = metadata?.getInt("io.flutter.embedding.android.SplashScreenDrawable") ?: 0
                if (splashScreenId != 0) if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) resources.getDrawable(splashScreenId, parentActivity.theme) else resources.getDrawable(splashScreenId) else null
            } catch (e: PackageManager.NameNotFoundException) {
                // This is never expected to happen.
                null
            }
        } else {
            null
        }
    }
}
