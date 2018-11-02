# flutter_mixed

A new flutter module project.

## Getting Started

For help getting started with Flutter, view our online
[documentation](https://flutter.io/).

```
/**
 * https://developer.android.com/ndk/guides/abis?hl=zh-cn
 * Android 系统运行时 会优先寻找 主要ABI(主要ABI可以实现系统最佳性能), 没有的话会选择 辅助ABI, 如果找都找不到, 应用可以安装但会在运行时奔溃
 *
 * https://android.stackexchange.com/questions/34958/what-are-the-minimum-hardware-specifications-for-android
 * https://stackoverflow.com/questions/28926101/is-it-safe-to-support-only-armeabi-v7a-for-android-4-and-above
 * 自r16以来NDK已弃用armeabi和mips。因此默认情况下不会生成这些体系结构的文件。虽然在abifilters可以通过修改得到这样的文件,但实际上意味着 armeabi 和 mips 现在绝对不需要
 *
 * Android 4+ 仅支持 armeabi-v7a 安全吗？
 * 1: 原则上，armeabi 如果您的应用需要Android 4.0，您可以放弃，但不确定是否有任何此类官方保证。如果是 Android 4.4+，应该绝对没问题。
 * 2: 没有支持 armeabi 但不支持的 Android 4+ 设备 armeabi-v7a，因此可以安全地放弃armeabi。
 *
 * 总结: android 4.0+ 可以只用 armeabi-v7a 不用 armeabi 但可能有风险, android 4.4+ 绝对没问题
 *
 * --------------------------------------------------------------------------------
 * cpu arch                 primary-abi             secondary-abi
 * --------------------------------------------------------------------------------
 * armeabi                  armeabi                 none
 * armeabi-v7a              armeabi-v7a             armeabi
 * arm64-v8a                arm64-v8a               armeabi-v7a / armeabi
 * --------------------------------------------------------------------------------
 * x86                      x86                     armeabi-v7a / armeabi
 * x86_64                   x86_64                  x86 / armeabi-v7a / armeabi
 * --------------------------------------------------------------------------------
 * mips                     mips                    none
 * mips64                   mips64                  mips
 * --------------------------------------------------------------------------------
 *
 */
```

```
// android ------------------------

// flutter build apk --release --verbose
./gradlew -Pverbose=true -Ptarget=../lib/main.dart -Ptrack-widget-creation=false -Ptarget-platform=android-arm clean assembleRelease --info --stacktrace

// flutter build apk --debug --verbose
./gradlew -Pverbose=true -Ptarget=lib/main.dart -Ptrack-widget-creation=false -Ptarget-platform=android-arm assembleDebug


// flutter run --debug --verbose
./gradlew -Pverbose=true -Ptarget=lib/main.dart -Ptrack-widget-creation=false -Ptarget-platform=android-arm64 assembleDebug

// flutter run --release --verbose
./gradlew -Pverbose=true -Ptarget=lib/main.dart -Ptrack-widget-creation=false -Ptarget-platform=android-arm64 assembleRelease


// android ------------------------
```


```

// release
---------------------
./gradlew -Pverbose=true -Ptarget=lib/main.dart -Ptrack-widget-creation=false -Ptarget-platform=android-arm clean assembleRelease --info --stacktrace
---------------------
flutter build aot --suppress-analytics --quiet --target lib/main.dart --target-platform android-arm --output-dir output/release --target-platform android-arm --release
flutter build bundle --suppress-analytics --target lib/main.dart --verbose --target-platform android-arm --precompiled --asset-dir output/release/flutter_assets --release
---------------------





---

// debug
---------------------
./gradlew -Pverbose=true -Ptarget=lib/main.dart -Ptrack-widget-creation=false -Ptarget-platform=android-arm assembleDebug --info --stacktrace
flutter build aot --suppress-analytics --quiet --target lib/main.dart --target-platform android-arm --output-dir output/debug --target-platform android-arm --debug
flutter build bundle --suppress-analytics --target lib/main.dart --verbose --target-platform android-arm --precompiled --asset-dir output/debug/flutter_assets --debug

```