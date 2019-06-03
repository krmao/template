# flutter
* [https://flutter.io/](https://flutter.io/)
* [https://flutterchina.club/setup-macos/](https://flutterchina.club/setup-macos/)
* [https://flutter-io.cn/](https://flutter-io.cn/)

### 克隆 flutter 代码
```
git clone https://github.com/flutter/flutter.git
git pull && git checkout stable # 升级到最新稳定版
cd bin && ./flutter doctor  # 检查并安装所有依赖, 包含 dart sdk
```

### 环境变量
```
vi ~/.bash_profile
```

```
ANDROID_HOME=~/sdks/android/sdk
ANDROID_TOOLS=$ANDROID_HOME/tools
ANDROID_PLATFORM_TOOLS=$ANDROID_HOME/platform-tools

JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_192.jdk/Contents/Home

FLUTTER_HOME=~/workspace/flutter

PATH=$PATH:$FLUTTER_HOME:$FLUTTER_HOME/bin:

export FLUTTER_HOME
export ANDROID_HOME
export ANDROID_TOOLS
export ANDROID_PLATFORM_TOOLS

export ANDROID_SDK=$ANDROID_HOME
export ANDROID_NDK=/Users/krmao/sdks/android/ndk-old/android-ndk-r10e

export JAVA_HOME
export PATH
```

```
source ~/.bash_profile
```

### 检查依赖
```
flutter doctor
```

### [构建 android 发布包](https://flutterchina.club/android-release/)
```
flutter build apk --release --verbose
 
# 安装到设备
flutter install
```

* flutter aar 路径
> flutter/build/flutter-bundle/outputs/aar/flutter-bundle-1.0.0-release.aar
* 最终发布包路径
> flutter/build/app/outputs/apk/release/app-release.apk

### others
* [官方不支持热更新](https://github.com/flutter/flutter/issues/14330#issuecomment-485565194)