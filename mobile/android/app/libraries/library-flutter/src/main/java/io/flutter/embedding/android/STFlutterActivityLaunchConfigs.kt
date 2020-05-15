package io.flutter.embedding.android

/** Collection of Flutter launch configuration options.  */ // This class is public so that Flutter app developers can reference
// BackgroundMode
@Suppress("HasPlatformType", "unused")
object STFlutterActivityLaunchConfigs {
    // Meta-data arguments, processed from manifest XML.
    const val DART_ENTRYPOINT_META_DATA_KEY = FlutterActivityLaunchConfigs.DART_ENTRYPOINT_META_DATA_KEY

    const val INITIAL_ROUTE_META_DATA_KEY = FlutterActivityLaunchConfigs.INITIAL_ROUTE_META_DATA_KEY

    const val SPLASH_SCREEN_META_DATA_KEY = FlutterActivityLaunchConfigs.SPLASH_SCREEN_META_DATA_KEY

    const val NORMAL_THEME_META_DATA_KEY = FlutterActivityLaunchConfigs.NORMAL_THEME_META_DATA_KEY

    // Intent extra arguments.
    const val EXTRA_INITIAL_ROUTE = FlutterActivityLaunchConfigs.EXTRA_INITIAL_ROUTE

    const val EXTRA_BACKGROUND_MODE = FlutterActivityLaunchConfigs.EXTRA_BACKGROUND_MODE

    const val EXTRA_CACHED_ENGINE_ID = FlutterActivityLaunchConfigs.EXTRA_CACHED_ENGINE_ID

    const val EXTRA_DESTROY_ENGINE_WITH_ACTIVITY = FlutterActivityLaunchConfigs.EXTRA_DESTROY_ENGINE_WITH_ACTIVITY

    // Default configuration.
    const val DEFAULT_DART_ENTRYPOINT = FlutterActivityLaunchConfigs.DEFAULT_DART_ENTRYPOINT

    const val DEFAULT_INITIAL_ROUTE = FlutterActivityLaunchConfigs.DEFAULT_INITIAL_ROUTE

    val DEFAULT_BACKGROUND_MODE = FlutterActivityLaunchConfigs.DEFAULT_BACKGROUND_MODE
}
