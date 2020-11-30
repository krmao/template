# flutter_module

A new flutter module project.

## create flutter module with command
```shell script
flutter create -t module --ios-language objc --android-language kotlin --org com.codesdancing.flutter --pub --with-driver-test flutter_module 
```

## build aar
* local repo
    ```shell script
    flutter build aar --debug --build-number 0.0.1 --pub --verbose
    # /Users/krmao/Desktop/flutter_module/flutter_module/.android/gradlew -I=/Users/krmao/fvm/versions/1.22.4/packages/flutter_tools/gradle/aar_init_script.gradle -Pflutter-root=/Users/krmao/fvm/versions/1.22.4 -Poutput-dir=/Users/krmao/Desktop/flutter_module/flutter_module/build/host -Pis-plugin=false -PbuildNumber=0.0.1 -Pverbose=true -Pfont-subset=true -Ptarget-platform=android-arm,android-arm64,android-x64 assembleAarRelease --info --stacktrace
    ```
* custom repo
    ```shell script
    cd .. && fvm install && fvm flutter packages get
    cd .android && ./gradlew -I=../tools/aar_init_script.gradle -Pflutter-root=/Users/krmao/fvm/versions/1.22.4 -Poutput-dir=/Users/krmao/Desktop/new/flutter_module/build/host  -Pis-plugin=false -PbuildNumber=0.0.1 -Pverbose=true -Pfont-subset=true -Ptarget-platform=android-arm,android-arm64,android-x64 assembleAarRelease --info --stacktrace
    ```

### 通过 clone 方式下载 flutter sdk
```shell script
git clone https://github.com/flutter/flutter.git
git pull && git checkout stable # 升级到最新稳定版
cd bin && ./flutter doctor  # 检查并安装所有依赖, 包含 dart sdk

# 配置环境变量
vi ~/.bash_profile
```

```shell script
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
# 应用环境变量
source ~/.bash_profile
```

### 通过 [fvm](https://github.com/leoafarias/fvm) 方式下载 flutter sdk
```shell script
brew tap dart-lang/dart
brew install dart
# brew upgrade dart
brew switch dart 2.10.4
brew info dart
pub global activate fvm

export PATH="$PATH":"$HOME/.pub-cache/bin":"~/fvm/default/bin"

fvm list
fvm install 1.22.4
fvm use 1.22.4
fvm use 1.22.4 --global # --force

fvm flutter --version
fvm flutter doctor
fvm list

fvm flutter create flutter
fvm flutter pub get

# fvm remove <version>
# fvm flutter upgrade
# fvm flutter run
# .fvm/flutter/bin run

# 如果 flutter 工程有 .fvm/fvm_config.json
{
  "flutterSdkVersion": "1.22.4"
}

#直接 fvm install
./.fvm/flutter_sdk/bin/flutter --version
```

### 修改 Android Studio flutter path
```shell script
Android Studio
Copy the absolute path of fvm symbolic link in your root project directory. Example: /absolute/path-to-your-project/.fvm/flutter_sdk

In the Android Studio menu open Languages & Frameworks -> Flutter or search for Flutter and change Flutter SDK path. Apply the changes. You now can Run and Debug with the selected versions of Flutter. Restart Android Studio to see the new settings applied.
```

```

# 检查当前环境
flutter doctor
# 查看当前分支
flutter channel
# 切换当前分支
flutter channel master
cd ~/workspace/flutter
git branch
git pull
flutter upgrade
flutter doctor

# flutter 升级
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
flutter packages get --verbose

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

### flutter boost v1.22.4-hotfixes

### 开发规范
* 文件名 小写+下划线
* 类名 大驼峰命名法
* 页面名称以 FlutterXXXWidget 命名法 并与 Native FlutterXXXActivity/FlutterXXXFragment 相对应

### others
* [官方不支持热更新](https://github.com/flutter/flutter/issues/14330#issuecomment-485565194)
* [集成到 ios 现有项目](https://github.com/flutter/flutter/wiki/Upgrading-Flutter-added-to-existing-iOS-Xcode-project) [新版集成方式代码变动](https://github.com/flutter/flutter/pull/36793)
* [集成到现有项目](https://github.com/flutter/flutter/wiki/Add-Flutter-to-existing-apps)
* [老板 ios 集成方式 升级到 新版](https://github.com/flutter/flutter/wiki/Upgrading-Flutter-added-to-existing-iOS-Xcode-project)

### 参考
* [https://flutter.io/](https://flutter.io/)
* [https://flutterchina.club/setup-macos/](https://flutterchina.club/setup-macos/)
* [https://flutter-io.cn/](https://flutter-io.cn/)
* [✓] Flutter (Channel stable, v1.7.8+hotfix.4, on Mac OS X 10.14.6 18G87, locale zh-Hans-CN)

### 依赖
```shell script
[   +3 ms] 
           Consuming the Module
[        ]   1. Open <host>/app/build.gradle
             2. Ensure you have the repositories configured, otherwise add them:
           
                 String storageUrl = System.env.FLUTTER_STORAGE_BASE_URL ?: "https://storage.googleapis.com"
                 repositories {
                   maven {
                       url '~/workspace/template/mobile/flutter_module/build/host/outputs/repo'
                   }
                   maven {
                       url '$storageUrl/download.flutter.io'
                   }
                 }
           
             3. Make the host app depend on the Flutter module:
           
               dependencies {
[        ]       debugImplementation 'com.codesdancing.flutter_module:flutter_debug:0.0.1'
[        ]       profileImplementation 'com.codesdancing.flutter_module:flutter_profile:0.0.1'
[        ]       releaseImplementation 'com.codesdancing.flutter_module:flutter_release:0.0.1'
[        ]     }
           
[        ] 
             4. Add the `profile` build type:
           
               android {
                 buildTypes {
                   profile {
                     initWith debug
                   }
                 }
               }
           

```
