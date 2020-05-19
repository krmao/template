## FLUTTER 模块
> android studio 打开 .android 的时候在选择框使用快捷键 option + command + . 切换 隐藏文件夹显示

- 直接测试 flutter module
    - 连接设备
    - flutter run
- 集成测试 flutter module 到 native app 测试
    - 编译 flutter build aar --build-number 1.0 --pub --debug --no-profile --no-release --output-dir ~/workspace/template/mobile/android/app/libraries/repo
    - 集成 在 template/mobile/android 确保 project_config.gradle 中 flutter enable = true
    - 安装 installDebug template/mobile/android
    - 或者直接一条指令搞定 cd ~/workspace/template/mobile/flutter_module && flutter build aar --build-number 1.0 --pub --debug --no-profile --no-release --output-dir ~/workspace/template/mobile/android/app/libraries/repo && cd ~/workspace/template/mobile/android && ./gradlew installDebug --stacktrace --info

## 查看并升级到最新的 Flutter 稳定版本
- https://github.com/flutter/flutter/tags
```
git checkout master
git pull
git checkout 1.17.1
flutter doctor
```

### 集成到现有项目
- https://flutter.dev/docs/development/add-to-app
- https://flutter.dev/docs/development/add-to-app/android/project-setup
- https://flutter.dev/docs/development/add-to-app/android/add-flutter-screen?tab=custom-activity-launch-kotlin-tab


### 创建一个 FLUTTER 应用程序
- 创建纯 Flutter 应用程序
```shell script
flutter create --help

flutter create -t module \
--pub \
--org com.smart.flutter \
--overwrite \
--ios-language objc \
--android-language kotlin \
--androidx \
--project-name flutter_module \
flutter_module
```
- 产出物
```shell script
tree -L 3
.
├── README.md
├── flutter_module.iml
├── .android
│   ├── Flutter
│   │   ├── build
│   │   ├── build.gradle
│   │   ├── flutter.iml
│   │   └── src
│   ├── app
│   │   ├── build.gradle
│   │   └── src
│   ├── build.gradle
│   ├── gradle
│   │   └── wrapper
│   ├── gradle.properties
│   ├── gradlew
│   ├── gradlew.bat
│   ├── include_flutter.groovy
│   ├── local.properties
│   └── settings.gradle
├── .ios      
│   ├── Config
│   │   ├── Debug.xcconfig
│   │   ├── Flutter.xcconfig
│   │   └── Release.xcconfig
│   ├── Flutter
│   │   ├── AppFrameworkInfo.plist
│   │   ├── FlutterPluginRegistrant
│   │   ├── Generated.xcconfig
│   │   ├── README.md
│   │   ├── flutter_export_environment.sh
│   │   ├── flutter_module.podspec
│   │   └── podhelper.rb
│   ├── Runner
│   │   ├── AppDelegate.h
│   │   ├── AppDelegate.m
│   │   ├── Assets.xcassets
│   │   ├── Base.lproj
│   │   ├── Info.plist
│   │   └── main.m
│   ├── Runner.xcodeproj
│   │   ├── project.pbxproj
│   │   ├── project.xcworkspace
│   │   └── xcshareddata
│   └── Runner.xcworkspace
│       └── contents.xcworkspacedata 
├── lib
│   └── main.dart
├── pubspec.lock
├── pubspec.yaml
└── test
    └── widget_test.dart
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
flutter build apk --pub --debug --no-profile 
flutter build apk --pub --release --no-profile 

flutter build ios --help
flutter build ios --pub --debug --no-profile 
flutter build ios --pub --release --no-profile 

flutter build aar --help
flutter build aar --build-number 1.0 --pub --debug --no-profile --no-release --output-dir ~/workspace/template/mobile/android/app/libraries/repo
flutter build aar --build-number 1.0 --pub --release --no-profile --no-debug --output-dir ~/workspace/template/mobile/android/app/libraries/repo
```

## API 文档 在线演示
- https://api.flutter.dev/flutter/material/InkWell-class.html
