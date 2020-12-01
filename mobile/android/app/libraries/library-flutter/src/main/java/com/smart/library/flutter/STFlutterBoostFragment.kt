package com.smart.library.flutter

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.idlefish.flutterboost.containers.BoostFlutterActivity
import io.flutter.embedding.android.DrawableSplashScreen
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.android.SplashScreen
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

@Suppress("UNCHECKED_CAST", "unused")
class STFlutterBoostFragment : com.idlefish.flutterboost.containers.FlutterFragment() {

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

    override fun onResume() {
        super.onResume()
        STFlutterInitializer.currentMethodChannel = methodChannel
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
                val splashUntilFirstFrame = metadata?.getBoolean("io.flutter.app.android.SplashScreenUntilFirstFrame")
                if (splashUntilFirstFrame == true && splashScreenId != 0) (if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) resources.getDrawable(splashScreenId, parentActivity.theme) else resources.getDrawable(splashScreenId)) else null
            } catch (e: PackageManager.NameNotFoundException) {
                // This is never expected to happen.
                null
            }
        } else {
            null
        }
    }

    companion object {

        /**
         * @param containerUrl       当前Activity在Flutter层对应的name，
         *                            混合栈将会在flutter层根据这个名字，在注册的Route表中查找对应的Widget
         *                            在flutter层有注册函数：
         *                            <p>
         *                            FlutterBoost.singleton.registerPageBuilders({
         *                            'first': (pageName, params, _) => FirstRouteWidget(),
         *                            'second': (pageName, params, _) => SecondRouteWidget(),
         *                            ...
         *                            });
         *                            <p>
         *                            该方法中返回的就是注册的key：first , second
         * @param containerParams     该方法返回的参数将会传递给上层的flutter对应的Widget
         *                            <p>
         *                            在flutter层有注册函数：
         *                            FlutterBoost.singleton.registerPageBuilders({
         *                            'first': (pageName, params, _) => FirstRouteWidget(),
         *                            'second': (pageName, params, _) => SecondRouteWidget(),
         *                            ...
         *                            });
         *                            <p>
         *                            该方法返回的参数就会封装成上面的params
         * @return
         */
        @JvmStatic
        @JvmOverloads
        fun startForResult(activity: Activity?, containerUrl: String, containerParams: MutableMap<String, Any>? = hashMapOf(), requestCode: Int = 0) {
            STFlutterBoostFragmentContainer.startForResult(activity, requestCode, STFlutterBoostFragment::class.java, createArguments(containerUrl, containerParams))
        }

        @JvmStatic
        @JvmOverloads
        fun startForResult(fragment: Fragment?, containerUrl: String, containerParams: MutableMap<String, Any>? = hashMapOf(), requestCode: Int = 0) {
            STFlutterBoostFragmentContainer.startForResult(fragment, requestCode, STFlutterBoostFragment::class.java, createArguments(containerUrl, containerParams))
        }

        @JvmStatic
        @JvmOverloads
        fun createFragment(containerUrl: String, containerParams: MutableMap<String, Any>? = hashMapOf()): STFlutterBoostFragment {
            val fragment = STFlutterBoostFragment()
            fragment.arguments = createArguments(containerUrl, containerParams)
            return fragment
        }

        @JvmStatic
        @JvmOverloads
        fun createArguments(containerUrl: String, containerParams: MutableMap<String, Any>? = hashMapOf()): Bundle {
            val arguments = Bundle()
            arguments.putString(EXTRA_URL, containerUrl)
            if (containerParams != null) {
                val serializableMap = BoostFlutterActivity.SerializableMap()
                serializableMap.map = containerParams
                arguments.putSerializable(EXTRA_PARAMS, serializableMap)
            }
            return arguments
        }
    }
}
