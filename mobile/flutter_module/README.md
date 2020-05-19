# flutter_module

- 直接测试 flutter module
    - 连接设备
    - flutter run
- 集成测试 flutter module 到 native app 测试
    - 编译 flutter build aar --build-number 1.0 --pub --debug --no-profile --no-release --output-dir ~/workspace/template/mobile/android/app/libraries/repo
    - 编译 flutter build aar --build-number 1.0 --pub --debug --no-profile --output-dir ~/workspace/template/mobile/android/app/libraries/repo
    - 集成 在 template/mobile/android 确保 project_config.gradle 中 flutter enable = true
    - 安装 installDebug template/mobile/android


## Getting Started

For help getting started with Flutter, view our online
[documentation](https://flutter.dev/).

## 查看并升级到最新的 Flutter 稳定版本
- https://github.com/flutter/flutter/tags
```
git checkout master
git pull
git checkout 1.17.1
flutter doctor
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
flutter build aar --build-number 1.0 --pub --debug --no-profile --no-release --output-dir ~/workspace/template/mobile/android/app/libraries/repo
flutter build aar --build-number 1.0 --pub --release --no-profile --no-debug --output-dir ~/workspace/template/mobile/android/app/libraries/repo
```

## API 文档 在线演示
- https://api.flutter.dev/flutter/material/InkWell-class.html