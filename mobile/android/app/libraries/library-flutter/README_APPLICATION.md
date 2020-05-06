## 纯 FLUTTER 项目

### 创建一个 FLUTTER 应用程序
- 创建纯 Flutter 应用程序
```shell script
flutter create --help

flutter create -t app \
--pub \
--org com.smart.flutter \
--overwrite \
--ios-language objc \
--android-language kotlin \
--androidx \
--project-name flutter_app \
flutter_app
```
- 产出物
```shell script
tree -L 3
.
├── README.md
├── android
│   ├── app
│   │   ├── build.gradle
│   │   └── src
│   ├── build.gradle
│   ├── flutter_app_android.iml
│   ├── gradle
│   │   └── wrapper
│   ├── gradle.properties
│   ├── gradlew
│   ├── gradlew.bat
│   ├── local.properties
│   └── settings.gradle
├── flutter_app.iml
├── ios
│   ├── Flutter
│   │   ├── AppFrameworkInfo.plist
│   │   ├── Debug.xcconfig
│   │   ├── Generated.xcconfig
│   │   ├── Release.xcconfig
│   │   └── flutter_export_environment.sh
│   ├── Runner
│   │   ├── AppDelegate.h
│   │   ├── AppDelegate.m
│   │   ├── Assets.xcassets
│   │   ├── Base.lproj
│   │   ├── GeneratedPluginRegistrant.h
│   │   ├── GeneratedPluginRegistrant.m
│   │   ├── Info.plist
│   │   └── main.m
│   ├── Runner.xcodeproj
│   │   ├── project.pbxproj
│   │   ├── project.xcworkspace
│   │   └── xcshareddata
│   └── Runner.xcworkspace
│       └── contents.xcworkspacedata
├── lib
│   └── main.dart
├── pubspec.lock
├── pubspec.yaml
└── test
    └── widget_test.dart

16 directories, 27 files
flutter_app krmao$
```
### 运行
- 检测设备连接
```shell script
flutter devices                         # 查看当前连接设备, 真机使用USB连接
flutter emulators                       # 查看模拟器
flutter emulators --create [--name xyz] # 创建模拟器 
flutter emulators --launch [partial ID] # 打开模拟器

# android 模拟器详细指令
emulator -list-avds
emulator -avd avd_name
# ios 模拟器详细指令
open -a simulator
```
- 编译并运行到已连接设备
```shell script
flutter run --help
flutter run --build --debug --pub
```

- JIT 热更新
```shell script
# 因为上一步骤是 控制台执行的 flutter run
# 随便修改代码 控制台输入
r \ enter

# 推出热更新 控制台输入
q \ enter
```

- build
```shell script
flutter build --help
flutter build apk --help
flutter build apk --pub --debug
flutter build apk --pub --release

flutter build ios --help
flutter build ios --pub --debug
flutter build ios --pub --release

flutter build aar --help
flutter build aar --pub --debug --output-dir android/gradle/repo
```
