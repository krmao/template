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
