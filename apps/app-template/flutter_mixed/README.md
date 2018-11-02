# flutter_mixed

A new flutter module project.

## Getting Started

For help getting started with Flutter, view our online
[documentation](https://flutter.io/).

```
/**
 * https://developer.android.com/ndk/guides/abis?hl=zh-cn
 * Android 系统运行时 会优先寻找 主要ABI(主要ABI可以实现系统最佳性能), 没有的话会选择 辅助ABI, 如果找都找不到, 应用可以安装但会在运行时奔溃
 * --------------------------------------------------------------------------------
 * cpu arch                 primary-abi             secondary-abi
 * --------------------------------------------------------------------------------
 * armeabi                  armeabi                 none
 * armeabi-v7a              armeabi-v7a             armeabi
 * arm64-v8a                arm64-v8a               armeabi-v7a / armeabi
 * --------------------------------------------------------------------------------
 * x86                      x86                     armeabi-v7a / armeabi
 * x86_64                   x86_64                  x86_64 / x86 / armeabi
 * --------------------------------------------------------------------------------
 * mips                     mips                    none
 * mips64                   mips64                  mips
 * --------------------------------------------------------------------------------
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