# flutter_mixed

A new flutter module project.

## Getting Started

For help getting started with Flutter, view our online
[documentation](https://flutter.io/).


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
flutter build aot --suppress-analytics --quiet --target lib/main.dart --target-platform android-arm --output-dir output/debug --target-platform android-arm --debug
flutter build bundle --suppress-analytics --target lib/main.dart --verbose --target-platform android-arm --precompiled --asset-dir output/debug/flutter_assets --debug

```