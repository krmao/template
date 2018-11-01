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