### 参见 ~/workspace/template/mobile/flutter/README.md
```
cd ~/workspace/template/mobile/flutter
cat README.md
```

### 直接更新 flutter aar 指令
```
cd ~/workspace/template/mobile/flutter/android

./gradlew clean flutter-bundle:assembleDebug --info         #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
./gradlew clean flutter-bundle:assembleRelease --info       #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
./gradlew clean assembleDebug --info                        #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
./gradlew clean assembleRelease --info                      #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
./gradlew clean installDebug --info                         #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
./gradlew clean installRelease --info                       #自动 copy 到 template/mobile/android/app/libraries/repo/aars 目录
```

---

### 杀死 flutter 进程
```shell script
ps -ef | grep flutter
kill -9 progressId
```

### 检测 flutter 环境
```shell script
flutter doctor

flutter_smart smart$ flutter doctor
Doctor summary (to see all details, run flutter doctor -v):
[✓] Flutter (Channel stable, v1.12.13+hotfix.9, on Mac OS X 10.14.6 18G4032, locale zh-Hans-CN)

[!] Android toolchain - develop for Android devices (Android SDK version 29.0.3)
    ! Some Android licenses not accepted.  To resolve this, run: flutter doctor --android-licenses
[✓] Xcode - develop for iOS and macOS (Xcode 11.3.1)
[✓] Android Studio (version 3.6)
[✓] IntelliJ IDEA Ultimate Edition (version 2019.3.3)
[✓] VS Code (version 1.44.2)
[✓] Connected device (1 available)

! Doctor found issues in 1 category.
flutter_smart smart$ flutter doctor --android-licenses

flutter_smart smart$ flutter doctor
Doctor summary (to see all details, run flutter doctor -v):
[✓] Flutter (Channel stable, v1.12.13+hotfix.9, on Mac OS X 10.14.6 18G4032, locale zh-Hans-CN)

[✓] Android toolchain - develop for Android devices (Android SDK version 29.0.3)
[✓] Xcode - develop for iOS and macOS (Xcode 11.3.1)
[✓] Android Studio (version 3.6)
[✓] IntelliJ IDEA Ultimate Edition (version 2019.3.3)
[✓] VS Code (version 1.44.2)
[✓] Connected device (1 available)

• No issues found!
flutter_smart smart$
```

### 新建一个 flutter 模块
```shell script
flutter create -t module --org com.smart.flutter flutter_smart

flutter_smart smart$ ls -al
total 72
drwxr-xr-x  16 smart  staff   512  4 29 17:14 .
drwxr-xr-x  10 smart  staff   320  4 29 17:06 ..
drwxr-xr-x  12 smart  staff   384  4 29 17:14 .android
drwxr-xr-x   3 smart  staff    96  4 29 17:14 .dart_tool
-rw-r--r--   1 smart  staff   369  4 29 17:06 .gitignore
drwxr-xr-x   5 smart  staff   160  4 29 17:06 .idea
drwxr-xr-x   7 smart  staff   224  4 29 17:14 .ios
-rw-r--r--   1 smart  staff   308  4 29 17:06 .metadata
-rw-r--r--   1 smart  staff  2431  4 29 17:14 .packages
-rw-r--r--   1 smart  staff   162  4 29 17:06 README.md
-rw-r--r--   1 smart  staff   896  4 29 17:06 flutter_smart.iml
-rw-r--r--   1 smart  staff  1465  4 29 17:06 flutter_smart_android.iml
drwxr-xr-x   3 smart  staff    96  4 29 17:06 lib
-rw-r--r--   1 smart  staff  4340  4 29 17:14 pubspec.lock
-rw-r--r--   1 smart  staff  3474  4 29 17:06 pubspec.yaml
drwxr-xr-x   3 smart  staff    96  4 29 17:06 test
flutter_smart smart$
```

### 下载依赖的插件
```shell script
flutter pub get --verbose
```

### 测试 android build aar
```shell script
flutter build aar --verbose

# 命令执行的目录
/Users/smart/workspace/template/mobile/flutter_smart/.android/
# 详细命令
/Users/smart/workspace/template/mobile/flutter_smart/.android/gradlew \
-I=/Users/smart/workspace/flutter/packages/flutter_tools/gradle/aar_init_script.gradle \
-Pflutter-root=/Users/smart/workspace/flutter \
-Poutput-dir=/Users/smart/workspace/template/mobile/flutter_smart/build/host \
-Pis-plugin=false \
-Ptarget-platform=android-arm,android-arm64,android-x64 \
assembleAarDebug
# 下载 gradle
Downloading https://services.gradle.org/distributions/gradle-5.6.2-all.zip


[/Users/krmao/workspace/template/mobile/flutter_smart/.android/] 
/Users/krmao/workspace/template/mobile/flutter_smart/.android/gradlew \
-I=/Users/krmao/workspace/flutter/packages/flutter_tools/gradle/aar_init_script.gradle \
-Pflutter-root=/Users/krmao/workspace/flutter
-Poutput-dir=/Users/krmao/workspace/template/mobile/flutter_smart/build/host \
-Pis-plugin=false \
-Ptarget-platform=android-arm,android-arm64,android-x64 \
assembleAarRelease
...
                     BUILD SUCCESSFUL in 1m 24s
                     28 actionable tasks: 26 executed, 2 up-to-date
[   +1 ms] Running Gradle task 'assembleAarRelease'... (completed in 84.7s)
[   +1 ms] ✓ Built build/host/outputs/repo.
[   +2 ms]
           Consuming the Module
             1. Open <host>/app/build.gradle
             2. Ensure you have the repositories configured, otherwise add them:

                 repositories {
                   maven {
                       url '/Users/krmao/workspace/template/mobile/flutter_smart/build/host/outputs/repo'
                   }
                   maven {
                       url 'http://download.flutter.io'
                   }
                 }

             3. Make the host app depend on the Flutter module:

               dependencies {
[        ]       debugImplementation 'com.smart.flutter.flutter_smart:flutter_debug:1.0
[        ]       profileImplementation 'com.smart.flutter.flutter_smart:flutter_profile:1.0
[        ]       releaseImplementation 'com.smart.flutter.flutter_smart:flutter_release:1.0
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

[        ] To learn more, visit https://flutter.dev/go/build-aar
[   +9 ms] "flutter aar" took 814,444ms.



build krmao$ tree -L 15
.
└── host
    └── outputs
        └── repo
            └── com
                └── smart
                    └── flutter
                        └── flutter_smart
                            ├── flutter_debug
                            │   ├── 1.0
                            │   │   ├── flutter_debug-1.0.aar
                            │   │   ├── flutter_debug-1.0.aar.md5
                            │   │   ├── flutter_debug-1.0.aar.sha1
                            │   │   ├── flutter_debug-1.0.pom
                            │   │   ├── flutter_debug-1.0.pom.md5
                            │   │   └── flutter_debug-1.0.pom.sha1
                            │   ├── maven-metadata.xml
                            │   ├── maven-metadata.xml.md5
                            │   └── maven-metadata.xml.sha1
                            ├── flutter_profile
                            │   ├── 1.0
                            │   │   ├── flutter_profile-1.0.aar
                            │   │   ├── flutter_profile-1.0.aar.md5
                            │   │   ├── flutter_profile-1.0.aar.sha1
                            │   │   ├── flutter_profile-1.0.pom
                            │   │   ├── flutter_profile-1.0.pom.md5
                            │   │   └── flutter_profile-1.0.pom.sha1
                            │   ├── maven-metadata.xml
                            │   ├── maven-metadata.xml.md5
                            │   └── maven-metadata.xml.sha1
                            └── flutter_release
                                ├── 1.0
                                │   ├── flutter_release-1.0.aar
                                │   ├── flutter_release-1.0.aar.md5
                                │   ├── flutter_release-1.0.aar.sha1
                                │   ├── flutter_release-1.0.pom
                                │   ├── flutter_release-1.0.pom.md5
                                │   └── flutter_release-1.0.pom.sha1
                                ├── maven-metadata.xml
                                ├── maven-metadata.xml.md5
                                └── maven-metadata.xml.sha1

13 directories, 27 files
build krmao$
```

### 热更新
- https://juejin.im/post/5db98b6a51882564661608b9
- https://mp.weixin.qq.com/s/R6IxJqawwbmlWvlwb3ZXww
- http://gityuan.com/2017/03/26/load_library/