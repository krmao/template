package io.flutter.embedding.android

import android.app.Activity
import android.content.Context
import androidx.lifecycle.Lifecycle
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterShellArgs
import io.flutter.plugin.platform.PlatformPlugin

interface STFlutterHost : SplashScreenProvider, FlutterEngineProvider, FlutterEngineConfigurator {
    /** Returns the [Context] that backs the host [Activity] or `Fragment`.  */
    val context: Context

    /**
     * Returns the host [Activity] or the `Activity` that is currently attached to the
     * host `Fragment`.
     */
    val activity: Activity?

    /** Returns the [Lifecycle] that backs the host [Activity] or `Fragment`.  */
    val lifecycle: Lifecycle

    /** Returns the [FlutterShellArgs] that should be used when initializing Flutter.  */
    val flutterShellArgs: FlutterShellArgs

    /**
     * Returns the ID of a statically cached [FlutterEngine] to use within this delegate's
     * host, or `null` if this delegate's host does not want to use a cached [ ].
     */
    val cachedEngineId: String?

    /**
     * Returns true if the [FlutterEngine] used in this delegate should be destroyed when the
     * host/delegate are destroyed.
     *
     *
     * The default value is `true` in cases where `FlutterFragment` created its own
     * [FlutterEngine], and `false` in cases where a cached [FlutterEngine] was
     * provided.
     */
    fun shouldDestroyEngineWithHost(): Boolean

    /** Returns the Dart entrypoint that should run when a new [FlutterEngine] is created.  */
    val dartEntrypointFunctionName: String

    /** Returns the path to the app bundle where the Dart code exists.  */
    val appBundlePath: String

    /** Returns the initial route that Flutter renders.  */
    val initialRoute: String?

    /**
     * Returns the [RenderMode] used by the [FlutterView] that displays the [ ]'s content.
     */
    val renderMode: RenderMode

    /**
     * Returns the [TransparencyMode] used by the [FlutterView] that displays the [ ]'s content.
     */
    val transparencyMode: TransparencyMode

    override fun provideSplashScreen(): SplashScreen?

    /**
     * Returns the [FlutterEngine] that should be rendered to a [FlutterView].
     *
     *
     * If `null` is returned, a new [FlutterEngine] will be created automatically.
     */
    override fun provideFlutterEngine(context: Context): FlutterEngine?

    /**
     * Hook for the host to create/provide a [PlatformPlugin] if the associated Flutter
     * experience should control system chrome.
     */
    fun providePlatformPlugin(
        activity: Activity?, flutterEngine: FlutterEngine
    ): PlatformPlugin?

    /** Hook for the host to configure the [FlutterEngine] as desired.  */
    override fun configureFlutterEngine(flutterEngine: FlutterEngine)

    /**
     * Hook for the host to cleanup references that were established in [ ][.configureFlutterEngine] before the host is destroyed or detached.
     */
    override fun cleanUpFlutterEngine(flutterEngine: FlutterEngine)

    /**
     * Returns true if the [FlutterEngine]'s plugin system should be connected to the host
     * [Activity], allowing plugins to interact with it.
     */
    fun shouldAttachEngineToActivity(): Boolean

    /**
     * Invoked by this delegate when the [FlutterSurfaceView] that renders the Flutter UI is
     * initially instantiated.
     *
     *
     * This method is only invoked if the [ ] is set to [ ][io.flutter.embedding.android.FlutterView.RenderMode.surface]. Otherwise, [ ][.onFlutterTextureViewCreated] is invoked.
     *
     *
     * This method is invoked before the given [FlutterSurfaceView] is attached to the
     * `View` hierarchy. Implementers should not attempt to climb the `View` hierarchy
     * or make assumptions about relationships with other `View`s.
     */
    fun onFlutterSurfaceViewCreated(flutterSurfaceView: FlutterSurfaceView)

    /**
     * Invoked by this delegate when the [FlutterTextureView] that renders the Flutter UI is
     * initially instantiated.
     *
     *
     * This method is only invoked if the [ ] is set to [ ][io.flutter.embedding.android.FlutterView.RenderMode.texture]. Otherwise, [ ][.onFlutterSurfaceViewCreated] is invoked.
     *
     *
     * This method is invoked before the given [FlutterTextureView] is attached to the
     * `View` hierarchy. Implementers should not attempt to climb the `View` hierarchy
     * or make assumptions about relationships with other `View`s.
     */
    fun onFlutterTextureViewCreated(flutterTextureView: FlutterTextureView)

    /** Invoked by this delegate when its [FlutterView] starts painting pixels.  */
    fun onFlutterUiDisplayed()

    /** Invoked by this delegate when its [FlutterView] stops painting pixels.  */
    fun onFlutterUiNoLongerDisplayed()
}