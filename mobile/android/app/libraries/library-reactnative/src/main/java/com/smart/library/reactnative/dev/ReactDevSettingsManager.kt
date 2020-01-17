package com.smart.library.reactnative.dev

import android.annotation.SuppressLint
import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.facebook.react.modules.debug.interfaces.DeveloperSettings
import com.smart.library.reactnative.ReactManager
import com.smart.library.util.STRegexManager

@Suppress("MemberVisibilityCanBePrivate", "unused")
@SuppressLint("StaticFieldLeak")
class ReactDevSettingsManager internal constructor(val application: Application, val debug: Boolean) {

    val devSettings: DeveloperSettings?
        get() = ReactManager.instanceManager?.devSupportManager?.devSettings

    val preferences: SharedPreferences by lazy { PreferenceManager.getDefaultSharedPreferences(application) }

    /**
     * JS Dev Mode
     * Load JavaScript bundle with __DEV__ = true for easier debugging.  Disable for performance testing. Reload for the change to take effect.
     * defaultValue="true"
     */

    fun setJSDevModeDebug(enable: Boolean) {
        if (debug) {
            preferences.edit().putBoolean("js_dev_mode_debug", enable).apply()
        }
    }

    /**
     * JS Minify
     * Load JavaScript bundle with minify=true for debugging minification issues.
     * defaultValue="false"
     */

    fun setJSMinifyDebug(enable: Boolean) {
        if (debug) {
            preferences.edit().putBoolean("js_minify_debug", enable).apply()
        }
    }

    /**
     * Use JS Deltas
     * Request delta bundles from metro to get faster reloads (Experimental)
     * defaultValue="true"
     */
    fun setJSBundleDeltas(enable: Boolean) {
        if (debug) {
            preferences.edit().putBoolean("js_bundle_deltas", enable).apply()
        }
    }

    fun getJSBundleDeltas(): Boolean {
        try {
            return preferences.getBoolean("js_bundle_deltas", true)
        } catch (e: ClassCastException) {
        }
        return true
    }

    /**
     * Animations FPS Summaries
     * At the end of animations, Toasts and logs to logcat enable information about the FPS during that transition. Currently only supported for transitions (animated navigations).
     * defaultValue="false"
     */
    fun setAnimationsDebug(enable: Boolean) {
        if (debug) {
            preferences.edit().putBoolean("animations_debug", enable).apply()
        }
    }

    fun getAnimationsDebug(): Boolean {
        try {
            return preferences.getBoolean("animations_debug", false)
        } catch (e: ClassCastException) {
        }
        return false
    }

    /**
     * Debug server host &amp; port for device
     * Debug server host &amp; port for downloading JS bundle or communicating with JS debugger. With this setting empty launcher should work fine when running on emulator (or genymotion) and connection to debug server running on emulator's host.
     * defaultValue=""
     * example: 127.0.0.1:8081
     */
    fun setDebugHttpHost(host: String?): Boolean {
        if (debug) {
            if (host.isNullOrBlank() || STRegexManager.isValidIPPort(host)) {
                preferences.edit().putString("debug_http_host", host).apply()
                return true
            }
        }
        return false
    }

    fun getDebugHttpHost(): String {
        return preferences.getString("debug_http_host", "") ?: ""
    }

    /**
     * Start Sampling Profiler on init
     * If true the Sampling Profiler will start on initialization of JS. Useful for profiling startup of the app. Reload JS after setting.
     * defaultValue="false"
     */
    fun setStartSamplingProfilerOnInit(enable: Boolean) {
        if (debug) {
            preferences.edit().putBoolean("start_sampling_profiler_on_init", enable).apply()
        }
    }

    fun getStartSamplingProfilerOnInit(): Boolean {
        try {
            return preferences.getBoolean("start_sampling_profiler_on_init", false)
        } catch (e: ClassCastException) {
        }
        return false
    }

    /**
     * Sample interval for Sampling Profiler
     * Sample interval in microseconds for the Sampling Profiler (default: 1000). Reload JS after setting.
     * defaultValue="1000"
     */
    fun setSamplingProfilerSampleInterval(interval: Int) {
        if (debug) {
            preferences.edit().putInt("sampling_profiler_sample_interval", interval).apply()
        }
    }

    fun getSamplingProfilerSampleInterval(): Int {
        try {
            return preferences.getInt("sampling_profiler_sample_interval", 1000)
        } catch (e: ClassCastException) {
        }
        return 1000
    }

    fun setDefaultStartComponent(component: String) {
        preferences.edit().putString(KEY_DEFAULT_START_COMPONENT, component).apply()
    }

    fun getDefaultStartComponent(): String {
        return preferences.getString(KEY_DEFAULT_START_COMPONENT, "cc-rn") ?: "cc-rn"
    }

    fun setDefaultStartComponentPage(component: String) {
        preferences.edit().putString(KEY_DEFAULT_START_COMPONENT_PAGE, component).apply()
    }

    fun getDefaultStartComponentPage(): String {
        return preferences.getString(KEY_DEFAULT_START_COMPONENT_PAGE, "home") ?: "home"
    }

    fun showDevOptionsDialog() {
        ReactManager.instanceManager?.devSupportManager?.showDevOptionsDialog()
    }

    companion object {
        private const val KEY_DEFAULT_START_COMPONENT: String = "rn_default_start_component"
        private const val KEY_DEFAULT_START_COMPONENT_PAGE: String = "rn_default_component_page"
    }
}