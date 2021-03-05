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
    flutter pub publish``
    ```
* [Search packages](https://pub.dartlang.org) will find [uploaded flutter plugin](https://pub.dev/packages/lib_flutter_base)
* How to use
    ```shell script
    # depend on
    dependencies:
      lib_flutter_base: ^0.0.6
  
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
    lib_flutter_base: ^0.0.6
    ```