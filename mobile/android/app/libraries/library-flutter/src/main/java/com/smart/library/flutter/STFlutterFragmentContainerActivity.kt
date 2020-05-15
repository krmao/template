package com.smart.library.flutter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentManager
import com.smart.library.base.STBaseActivity
import com.smart.library.util.STLogUtil
import io.flutter.Log
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.android.FlutterActivityLaunchConfigs
import io.flutter.embedding.android.FlutterActivityLaunchConfigs.BackgroundMode
import io.flutter.embedding.android.FlutterFragment
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.plugin.platform.PlatformPlugin

@Suppress("unused", "PrivatePropertyName")
open class STFlutterFragmentContainerActivity : STBaseActivity() {

    companion object {
        // Define a tag String to represent the FlutterFragment within this
        // Activity's FragmentManager. This value can be whatever you'd like.
        private const val TAG_FLUTTER_FRAGMENT = "flutter_fragment"
        private const val KEY_INITIAL_ROUTE = "key_initial_route"

        @JvmStatic
        fun goToWithFlutterFragment(from: Context?, initialRoute: String) {
            val intent = Intent(from, STFlutterFragmentContainerActivity::class.java)
            intent.putExtra(KEY_INITIAL_ROUTE, initialRoute)
            from?.startActivity(intent)
        }

        @JvmStatic
        fun goToOriginFlutterActivity(context: Context?, initialRoute: String) {
            context?.startActivity(FlutterActivity.withNewEngine().initialRoute(initialRoute).backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.opaque).build(context).apply { putExtra("url", initialRoute) })
        }

        @JvmStatic
        fun goToOriginFlutterActivityWithCachedEngine(context: Context?, @NonNull cachedEngineId: String) {
            STLogUtil.w(FlutterBusHandler.TAG, " cacheEngine with $cachedEngineId exists? == ${FlutterEngineCache.getInstance().contains(cachedEngineId)}")
            context?.startActivity(FlutterActivity.withCachedEngine(cachedEngineId).backgroundMode(FlutterActivityLaunchConfigs.BackgroundMode.opaque).build(context))
        }
    }

    // Declare a local variable to reference the FlutterFragment so that you
    // can forward calls to it later.
    private lateinit var flutterFragment: FlutterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        switchLaunchThemeForNormalTheme()

        super.onCreate(savedInstanceState)

        configureWindowForTransparency()

        // Throws an [IllegalArgumentException] if the initialRoute is null
        val initialRoute: String = requireNotNull(intent?.getStringExtra(KEY_INITIAL_ROUTE))

        // Inflate a layout that has a container for your FlutterFragment. For
        // this example, assume that a FrameLayout exists with an ID of
        // R.id.fragment_container.
        setContentView(FrameLayout(this))

        configureStatusBarForFullscreenFlutterExperience()

        // Get a reference to the Activity's FragmentManager to add a new
        // FlutterFragment, or find an existing one.
        val fragmentManager: FragmentManager = supportFragmentManager

        // Attempt to find an existing FlutterFragment, in case this is not the
        // first time that onCreate() was run.
        var newFlutterFragment: FlutterFragment? = fragmentManager.findFragmentByTag(TAG_FLUTTER_FRAGMENT) as? FlutterFragment

        // Create and attach a FlutterFragment if one does not exist.
        if (newFlutterFragment == null) {
            newFlutterFragment = FlutterFragment
                .NewEngineFragmentBuilder(STFlutterFragment::class.java)
                .initialRoute(initialRoute)
                .shouldAttachEngineToActivity(false)
                .build()

            flutterFragment = newFlutterFragment
            fragmentManager
                .beginTransaction()
                .add(android.R.id.content, newFlutterFragment, TAG_FLUTTER_FRAGMENT)
                .commit()
        } else {
            flutterFragment = newFlutterFragment
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        flutterFragment.onPostResume()
    }

    override fun onNewIntent(@NonNull intent: Intent) {
        super.onNewIntent(intent)
        flutterFragment.onNewIntent(intent)
    }

    override fun onBackPressed() {
        flutterFragment.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        flutterFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onUserLeaveHint() {
        flutterFragment.onUserLeaveHint()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        flutterFragment.onTrimMemory(level)
    }

    /**
     * Switches themes for this `Activity` from the theme used to launch this `Activity`
     * to a "normal theme" that is intended for regular `Activity` operation.
     *
     *
     * This behavior is offered so that a "launch screen" can be displayed while the application
     * initially loads. To utilize this behavior in an app, do the following:
     *
     *
     *  1. Create 2 different themes in style.xml: one theme for the launch screen and one theme for
     * normal display.
     *  1. In the launch screen theme, set the "windowBackground" property to a `Drawable` of
     * your choice.
     *  1. In the normal theme, customize however you'd like.
     *  1. In the AndroidManifest.xml, set the theme of your `FlutterActivity` to your launch
     * theme.
     *  1. Add a `<meta-data>` property to your `FlutterActivity` with a name of
     * "io.flutter.embedding.android.NormalTheme" and set the resource to your normal theme,
     * e.g., `android:resource="@style/MyNormalTheme`.
     *
     *
     * With the above settings, your launch theme will be used when loading the app, and then the
     * theme will be switched to your normal theme once the app has initialized.
     *
     *
     * Do not change aspects of system chrome between a launch theme and normal theme. Either
     * define both themes to be fullscreen or not, and define both themes to display the same status
     * bar and navigation bar settings. If you wish to adjust system chrome once your Flutter app
     * renders, use platform channels to instruct Android to do so at the appropriate time. This will
     * avoid any jarring visual changes during app startup.
     */
    private fun switchLaunchThemeForNormalTheme() {
        try {
            val activityInfo = packageManager.getActivityInfo(componentName, PackageManager.GET_META_DATA)
            if (activityInfo.metaData != null) {
                val normalThemeRID = activityInfo.metaData.getInt("io.flutter.embedding.android.NormalTheme", -1)
                if (normalThemeRID != -1) {
                    setTheme(normalThemeRID)
                }
            } else {
                Log.v(FlutterBusHandler.TAG, "Using the launch theme as normal theme.")
            }
        } catch (exception: PackageManager.NameNotFoundException) {
            Log.e(FlutterBusHandler.TAG, "Could not read meta-data for FlutterActivity. Using the launch theme as normal theme.")
        }
    }

    /**
     * Sets this `Activity`'s `Window` background to be transparent, and hides the status
     * bar, if this `Activity`'s desired [BackgroundMode] is [ ][BackgroundMode.transparent].
     *
     *
     * For `Activity` transparency to work as expected, the theme applied to this `Activity` must include `<item name="android:windowIsTranslucent">true</item>`.
     */
    private fun configureWindowForTransparency() {
        val backgroundMode: BackgroundMode = getBackgroundMode()
        if (backgroundMode == BackgroundMode.transparent) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    /**
     * The desired window background mode of this `Activity`, which defaults to [ ][BackgroundMode.opaque].
     */
    private fun getBackgroundMode(): BackgroundMode {
        val backgroundMode: String? = if (intent.hasExtra("background_mode")) intent.getStringExtra("background_mode") else null
        return if (!backgroundMode.isNullOrBlank()) BackgroundMode.valueOf(backgroundMode) else BackgroundMode.opaque
    }

    private fun configureStatusBarForFullscreenFlutterExperience() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = 0x40000000
            window.decorView.systemUiVisibility = PlatformPlugin.DEFAULT_SYSTEM_UI
        }
    }

}
