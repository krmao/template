package com.smart.library.flutter

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import com.smart.library.util.STLogUtil
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.android.STFlutterActivityLaunchConfigs
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache

@Suppress("MemberVisibilityCanBePrivate", "KDocUnresolvedReference", "unused", "RemoveRedundantQualifierName")
class STFlutterActivity : FlutterActivity() {

    companion object {

        @JvmStatic
        fun goToFlutterActivityWithNewEngine(@NonNull context: Context, @NonNull initialRoute: String) {
            val intent: Intent = STFlutterActivity.withNewEngine().backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.opaque).build(context)
            intent.putExtra(STFlutterActivityLaunchConfigs.EXTRA_INITIAL_ROUTE, initialRoute)
            context.startActivity(intent)
        }

        @JvmStatic
        fun goToFlutterActivityWithCachedEngine(@NonNull context: Context, @NonNull cachedEngineId: String) {
            if (FlutterEngineCache.getInstance().contains(cachedEngineId)) {
                STLogUtil.d(STFlutterManager.TAG, "cachedEngineId is valid!")
                val intent: Intent = STFlutterActivity.withCachedEngine(cachedEngineId).backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.opaque).build(context)
                context.startActivity(intent)
            } else {
                STLogUtil.e(STFlutterManager.TAG, "cachedEngineId is invalid!")
            }
        }

        /**
         * Creates an [Intent] that launches a `FlutterActivity`, which creates a [ ] that executes a `main()` Dart entrypoint, and displays the "/" route as
         * Flutter's initial route.
         *
         *
         * Consider using the [.withCachedEngine] [Intent] builder to control when
         * the [FlutterEngine] should be created in your application.
         */
        fun createDefaultIntent(launchContext: Context): Intent {
            return withNewEngine().build(launchContext)
        }

        /**
         * Creates an [NewEngineIntentBuilder], which can be used to configure an [Intent] to
         * launch a `FlutterActivity` that internally creates a new [FlutterEngine] using the
         * desired Dart entrypoint, initial route, etc.
         */
        fun withNewEngine(): NewEngineIntentBuilder {
            return STNewEngineIntentBuilder(STFlutterActivity::class.java)
        }

        /**
         * Creates a [CachedEngineIntentBuilder], which can be used to configure an [Intent]
         * to launch a `FlutterActivity` that internally uses an existing [FlutterEngine] that
         * is cached in [io.flutter.embedding.engine.FlutterEngineCache].
         */
        fun withCachedEngine(cachedEngineId: String): CachedEngineIntentBuilder {
            return STCachedEngineIntentBuilder(STFlutterActivity::class.java, cachedEngineId)
        }
    }

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
        STFlutterManager.configureFlutterEngine(this, flutterEngine)
    }

    class STNewEngineIntentBuilder(activityClass: Class<out FlutterActivity?>) : NewEngineIntentBuilder(activityClass)
    class STCachedEngineIntentBuilder(activityClass: Class<out FlutterActivity?>, @NonNull engineId: String) : CachedEngineIntentBuilder(activityClass, engineId)

}