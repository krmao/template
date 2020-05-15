package com.smart.library.flutter

import android.content.Context
import android.content.Intent
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import io.flutter.embedding.android.STFlutterActivityLaunchConfigs

/**
 * Flutter 启动页
 * https://flutter.cn/docs/development/ui/splash-screen/android-splash-screen
 */
@Suppress("unused", "PrivatePropertyName")
class STFlutterFragmentLaunchActivity : STFlutterFragmentActivity() {

    companion object {
        @JvmStatic
        fun goToFlutterFragment(@NonNull from: Context, @Nullable initialRoute: String? = null, @Nullable cachedEngineId: String? = null) {
            val intent = Intent(from, STFlutterFragmentLaunchActivity::class.java)
            if (!initialRoute.isNullOrBlank()) {
                intent.putExtra(STFlutterActivityLaunchConfigs.EXTRA_INITIAL_ROUTE, initialRoute)
            }
            if (!cachedEngineId.isNullOrBlank()) {
                intent.putExtra(STFlutterActivityLaunchConfigs.EXTRA_CACHED_ENGINE_ID, cachedEngineId)
            }
            from.startActivity(intent)
        }
    }

}
