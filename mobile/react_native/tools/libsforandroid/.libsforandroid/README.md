### upload to maven repository
* ./gradlew :library-reactnative:clean :library-reactnative:uploadArchives --info --stacktrace

### 使用方式
```
if (gradle.ext.modules.reactNative.repository.enable == true) {
    // 使用本库
    api gradle.ext.modules.reactNative.repository.dependency // api 'com.smart.library:libs-reactnative:0.62.2.1-SNAPSHOT'
} else {
    // 使用 react native 原生集成
    api project(':react-native-gesture-handler')
    api project(':react-native-safe-area-context')
    api project(':react-native-reanimated')
    api project(':react-native-vector-icons')
    api "com.facebook.react:react-native:${rootProject.ext.reactNativeVersion}" // From node_modules. cd react && npm install
}
```

### 参考文档
* https://www.jianshu.com/p/08ea37a0da03
* https://github.com/kezong/fat-aar-android
