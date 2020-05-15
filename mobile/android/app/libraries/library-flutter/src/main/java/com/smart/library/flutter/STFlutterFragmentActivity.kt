package com.smart.library.flutter

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import com.smart.library.util.STLogUtil
import io.flutter.Log
import io.flutter.embedding.android.*
import io.flutter.embedding.android.FlutterActivityLaunchConfigs.BackgroundMode
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.FlutterShellArgs

@Suppress("unused", "PrivatePropertyName")
open class STFlutterFragmentActivity : FlutterFragmentActivity() {

    companion object {
        // Define a tag String to represent the FlutterFragment within this
        // Activity's FragmentManager. This value can be whatever you'd like.
        private const val TAG_FLUTTER_FRAGMENT = "flutter_fragment"
        private const val KEY_INITIAL_ROUTE = "key_initial_route"
        const val TAG = "[FLUTTER FRAGMENT ACTIVITY]"

        @JvmStatic
        fun goToFlutterFragmentWithNewEngine(@NonNull from: Context, @NonNull initialRoute: String) {
            val intent = Intent(from, STFlutterFragmentActivity::class.java)
            intent.putExtra(STFlutterActivityLaunchConfigs.EXTRA_INITIAL_ROUTE, initialRoute)
            from.startActivity(intent)
        }

        @JvmStatic
        fun goToFlutterFragmentWithCachedEngine(@NonNull from: Context, cachedEngineId: String?) {
            if (!cachedEngineId.isNullOrBlank() && FlutterEngineCache.getInstance().contains(cachedEngineId)) {
                STLogUtil.d(STFlutterManager.TAG, "cachedEngineId is valid!")
                val intent = Intent(from, STFlutterFragmentActivity::class.java)
                intent.putExtra(STFlutterActivityLaunchConfigs.EXTRA_CACHED_ENGINE_ID, cachedEngineId)
                from.startActivity(intent)
            } else {
                STLogUtil.e(STFlutterManager.TAG, "cachedEngineId is invalid!")
            }
        }
    }

    /**
     * Creates the instance of the [FlutterFragment] that this `FlutterFragmentActivity`
     * displays.
     *
     *
     * Subclasses may override this method to return a specialization of [FlutterFragment].
     */
    override fun createFlutterFragment(): FlutterFragment {
        val backgroundMode = backgroundMode
        val renderMode = if (backgroundMode == BackgroundMode.opaque) RenderMode.surface else RenderMode.texture
        val transparencyMode = if (backgroundMode == BackgroundMode.opaque) TransparencyMode.opaque else TransparencyMode.transparent
        return if (cachedEngineId != null) {
            Log.v(
                TAG,
                """
                    Creating FlutterFragment with cached engine:
                    Cached engine ID: $cachedEngineId
                    Will destroy engine when Activity is destroyed: ${shouldDestroyEngineWithHost()}
                    Background transparency mode: $backgroundMode
                    Will attach FlutterEngine to Activity: ${shouldAttachEngineToActivity()}
                    """.trimIndent()
            )
            STCachedEngineFragmentBuilder(STFlutterFragment::class.java, cachedEngineId!!)
                .renderMode(renderMode)
                .transparencyMode(transparencyMode)
                .shouldAttachEngineToActivity(shouldAttachEngineToActivity())
                .destroyEngineWithFragment(shouldDestroyEngineWithHost())
                .build()
        } else {
            Log.v(
                TAG,
                """
                    Creating FlutterFragment with new engine:
                    Background transparency mode: $backgroundMode
                    Dart entrypoint: $dartEntrypointFunctionName
                    Initial route: $initialRoute
                    App bundle path: $appBundlePath
                    Will attach FlutterEngine to Activity: ${shouldAttachEngineToActivity()}
                    """.trimIndent()
            )
            FlutterFragment.NewEngineFragmentBuilder(STFlutterFragment::class.java)
                .dartEntrypoint(dartEntrypointFunctionName)
                .initialRoute(initialRoute)
                .appBundlePath(appBundlePath)
                .flutterShellArgs(FlutterShellArgs.fromIntent(intent))
                .renderMode(renderMode)
                .transparencyMode(transparencyMode)
                .shouldAttachEngineToActivity(shouldAttachEngineToActivity())
                .build()
        }
    }

    class STCachedEngineFragmentBuilder(subclass: Class<out FlutterFragment?>, engineId: String) : FlutterFragment.CachedEngineFragmentBuilder(subclass, engineId)
}
