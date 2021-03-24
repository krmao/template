## flutter for ios pod repo 远程私有仓库

### 1. 将 flutter business 代码打包为 ios framework
```
flutter build ios-framework --cocoapods --pub --debug --no-profile --output=/Users/krmao/workspace/template/mobile/flutter/flutter_modules/flutter_module_template/build/host/outputs/repo-ios -v
```

### 2. 新建 flutter business cocoapods lib 仓库
> 在 github 上新建仓库 https://github.com/krmao/libsforios-flutter
```
git clone https://github.com/krmao/libsforios-flutter.git
pod lib create FlutterBusiness

### 配置 FlutterBusiness.podspec
### 代码详见 https://github.com/krmao/libsforios-flutter
```

### 3. 需要设置 git tag
```
cd ~/workspace/libsforios-flutter
git tag 0.1.0 # 版本详见 FlutterBusiness.podspec -> version
git push origin 0.1.0
```

### 4. 添加 flutter business 到私有仓库
```
pod ipc spec FlutterBusiness.podspec >> FlutterBusiness.podspec.json
pod repo push libsforios FlutterBusiness.podspec.json --allow-warnings

# 如果上一步报错, 比如报 app.framework 名字冲突错误
# 直接进入私有仓库, 新建文件夹并推送
cd /Users/krmao/.cocoapods/repos/krmao
mkdir FlutterBusiness
cd FlutterBusiness
mkdir 0.1.0
cp xxxxx/FlutterBusiness.podspec.json ./
git add .
git commit -m "force push"
git push
```

### 5. 添加 flutter engine 到私有仓库
```
cd ~/workspace/libsforios-flutter/FlutterEngine
cd FlutterEngineDebug/v1.22.400

pod ipc spec FlutterEngineDebug.podspec >> FlutterEngineDebug.podspec.json
pod repo push krmao FlutterEngineDebug.podspec.json --allow-warnings --skip-import-validation --skip-tests --verbose

cd ~/workspace/libsforios-flutter/FlutterEngine
cd FlutterEngineRelease/v1.22.400

pod ipc spec FlutterEngineRelease.podspec >> FlutterEngineRelease.podspec.json
pod repo push krmao FlutterEngineRelease.podspec.json --allow-warnings --skip-import-validation --skip-tests --verbose
```

### 6. 使用方式
```
source 'https://github.com/krmao/libsforios.git'
pod 'FlutterEngineDebug', '1.22.400'
pod 'FlutterBusiness',  '0.1.0', :subspecs => ["FlutterBusinessDebug"]
```

### 参考文档
* [https://www.jianshu.com/p/d6a592d6fced](https://www.jianshu.com/p/d6a592d6fced)