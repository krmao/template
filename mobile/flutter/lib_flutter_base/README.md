# lib_flutter_base

A new flutter plugin project.

## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.

## Depend On Versions
* flutter_boost origin/v1.22.4-hotfixes

## Create Flutter Plugin [Steps](https://flutter.dev/docs/development/packages-and-plugins/developing-packages#plugin)
* Create flutter plugin
    ```shell script
    flutter create --org com.codesdancing.flutter  --template=plugin --platforms=android,ios -i objc -a kotlin lib_flutter_base
    ```
* Implement
    * Define API lib_flutter_base/lib/lib_flutter_base.dart # file name must be same as plugin name
    * Add android/IOS platform code
    * Add document and licenses file
* Publish
    ```shell script
    flutter pub publish
    ```
* [Search packages](https://pub.dartlang.org) will find [uploaded flutter plugin](https://pub.dev/packages/lib_flutter_base)
* How to use
    ```shell script
    # depend on
    dependencies:
      lib_flutter_base: ^0.0.1
  
    # install
    flutter pub get
  
    # import  
    import 'package:lib_flutter_base/lib_flutter_base.dart';
   ```
* Example Test
    ```shell script
    # lib_flutter_base:
    #   path: ../
    
    # change local dependency to remote
    lib_flutter_base: ^0.0.1
    ```

* Example Test Release (only can android, not ios)
> flutter run --release --verbose

* Flutter upgrade
0. remove example/.fvm/flutter_sdk/
1. change example/.fvm/fvm_config.json flutter version and then execute the command **fvm install** and **fvm use 2.0.2 --global --force**
2. change Android Studio -> Preference -> Language && Frameworks -> Flutter sdk path
3. delete pubspec.lock 
4. delete example/pubspec.lock 
5. delete example/ios/Podfile.lock
6. delete example/ios/Pods
7. delete example/ios/Pods/.symlinks/
8. flutter --no-color pub get && flutter run --verbose

* Switch boost/multiple
1. ./lib_flutter_base/pubspec.yaml
```shell script
flutter:
  # This section identifies this Flutter project as a plugin project.
  # The 'pluginClass' and Android 'package' identifiers should not ordinarily
  # be modified. They are used by the tooling to maintain consistency when
  # adding or updating assets for this project.
  plugin:
    platforms:
      android:
        package: com.codesdancing.flutter
        # pluginClass: LibFlutterBaseBoostPlugin
        pluginClass: LibFlutterBaseMultiplePlugin
      ios:
        # pluginClass: LibFlutterBaseBoostPlugin
        pluginClass: LibFlutterBaseMultiplePlugin
```

2. ./lib_flutter_base/android
```kotlin
// STFlutterInitializer.kt
enableMultiple = false/true
```

```kotlin
// STFlutterMultipleUtils.kt
enableMultiEnginesWithSingleRoute = false/true
```

3. ./lib_flutter_base/ios
```objectivec
_enableMultiple = YES/NO
_enableMultiEnginesWithSingleRoute = YES/NO
```
4. ./lib_flutter_base/lib
```dart
// base_bridge_page.dart
enableMultiple = false/true
```