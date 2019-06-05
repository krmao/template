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