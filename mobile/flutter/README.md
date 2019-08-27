# flutter
* [https://flutter.io/](https://flutter.io/)
* [https://flutterchina.club/setup-macos/](https://flutterchina.club/setup-macos/)
* [https://flutter-io.cn/](https://flutter-io.cn/)
* [✓] Flutter (Channel stable, v1.7.8+hotfix.4, on Mac OS X 10.14.6 18G87, locale zh-Hans-CN)

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

### 检查环境
```
flutter doctor
```

### flutter 升级
```
flutter upgrade
```

### 直接运行到已连接设备(注意同一时刻必须只有一台设备连接, ios 模拟器/android 真机 二选一)
```
flutter run --verbose
```

### [构建 android 发布包](https://flutterchina.club/android-release/)
```
# 检查下载工程依赖
flutter packages get --verbose

# 开始构建发布包
flutter build apk --release --verbose
 
# 安装到设备
flutter install --verbose
```

### 构建 android flutter-bundle aar
> 产出位于 flutter/build/flutter-bundle/outputs/aar/flutter-bundle-1.0.0-debug.aar\n
> 自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录

```
cd android # 以下任意指令均可生成 fat flutter-bundle-1.0.0-debug.aar
./gradlew clean flutter-bundle:assembleDebug --info         #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
./gradlew clean flutter-bundle:assembleRelease --info       #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
./gradlew clean assembleDebug --info                        #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
./gradlew clean assembleRelease --info                      #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
./gradlew clean installDebug --info                         #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
./gradlew clean installRelease --info                       #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
```

* flutter aar 路径
> flutter/build/flutter-bundle/outputs/aar/flutter-bundle-1.0.0-release.aar
* 最终发布包路径
> flutter/build/app/outputs/apk/release/app-release.apk

### [构建 ios 发布包](https://flutterchina.club/ios-release/)


### others
* [官方不支持热更新](https://github.com/flutter/flutter/issues/14330#issuecomment-485565194)