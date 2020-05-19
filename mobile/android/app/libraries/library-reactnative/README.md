## 开发步骤
1. 在 template/web/react_native 项目中开发, 使用 npm run start 进行在线调试
2. 执行 npm run bundle-android 打包并将产物 /build/bundle/android 重命名为 bundle-rn 并压缩为 bundle-rn.zip  copy并替换 到 template/app/libraries/library-reactnative/src/main/assets/bundle-rn.zip
4. 运行 template app 在 react native tab 页面 点击 'REACT NATIVE PAGE BRIDGE' 预览效果

### 1: 在线调试
* 在纯的 react-native 项目开启服务 **npm run start** 查看本地 IP 为 *10.10.10.111*
* 设置 **IP:PORT** *10.10.10.111:8081* (Dev Settings -> Debug server host & port for device)

### 2: 发布离线包
```
react-native bundle  --platform android  --dev false  --entry-file ./index.js  --bundle-output ./bundle/assets/index.android.bundle  --assets-dest ./bundle/res/
```

### 3: adb 模拟按键 KEYCODE_MENU
```
adb shell input keyevent 82
```

### 4: 首次加载白屏优化 [https://www.jianshu.com/p/78571e5435ec](https://www.jianshu.com/p/78571e5435ec)

### 5: debug chrome host [https://github.com/facebook/react-native/blob/0.52-stable/local-cli/server/middleware/getDevToolsMiddleware.js](https://github.com/facebook/react-native/blob/0.52-stable/local-cli/server/middleware/getDevToolsMiddleware.js)
