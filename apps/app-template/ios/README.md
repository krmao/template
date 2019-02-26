### ios app icon size

* 让设计师提供一个尺寸为 1024x1024 的 appIcon1024x1024.png 执行以下脚本
```
python autoExportAppIcon.py ~/Desktop/appIcon1024x1024.png
```

### [ios launch image size](https://developer.apple.com/design/human-interface-guidelines/ios/icons-and-images/launch-screen/)

* For iPhone:

| Nam     O     Orientatio    | Size in px (pixels    |
| :----     :----     :----     :----    
| iPhone      iOS 11    | Portrai     1125 x 243    |
| iPhone      iOS 11    | Landscap    | 2436 x 112    |
| Retina HD 5.5     iOS 8,    | Portrai     1242 x 220    |
| Retina HD 5.5     iOS 8,    | Landscap    | 2208 x 124    |
| Retina HD 4.7     iOS 8,    | Portrai     750 x 133     
| Retina      iOS 5-    | Portrai     640 x 113     
| 2     iOS 7-    | Portrai     640 x 96    | 
| 2     iOS 5,    | Portrai     640 x 96    | 
| 1     iOS 5,    | Portrai     320 x 48    | 

* For iPad:

| Nam     O     Orientatio    | Size in px (pixels    |
| :----     :----     :----     :----    
| 1     iOS 7-    | Portrai     768 x 102    
| 1     iOS 7-    | Landscap    | 1024 x 76    
| 2     iOS 7-    | Portrai     1536 x 204    |
| 2     iOS 7-    | Landscap    | 2048 x 153    |
| 1     iOS 5,    | Portrait (Without Status Bar) | 768 x 100    
| 1     iOS 5,    | Landscape(Without Status Bar) | 1024 x 74    
| 2     iOS 5,    | Portrait (Without Status Bar) | 1536 x 200    |
| 2     iOS 5,    | Landscape(Without Status Bar) | 2048 x 149    |


### [ios LaunchScreen.storyboard]

* launch image size

| Nam     Size in px (pixels    |
| :----     :----    
| 3     1125x x 200     
| 2     75    x 133     
| 1     37    x 66    | 

* Show the File Inspector
```
check 'Use as Launch Sceen'

```

### 发布

* Missing Purpose String in Info.plist File
```
    <key>NSAppleMusicUsageDescription</key>
    <string>App需要您的同意,才能访问媒体资料库</string>
    <key>NSBluetoothPeripheralUsageDescription</key>
    <string>App需要您的同意,才能访问蓝牙</string>
    <key>NSCalendarsUsageDescription</key>
    <string>App需要您的同意,才能访问日历</string>
    <key>NSCameraUsageDescription</key>
    <string>App需要您的同意,才能访问相机</string>
    <key>NSHealthShareUsageDescription</key>
    <string>App需要您的同意,才能访问健康分享</string>
    <key>NSHealthUpdateUsageDescription</key>
    <string>App需要您的同意,才能访问健康更新 </string>
    <key>NSLocationAlwaysUsageDescription</key>
    <string>App需要您的同意,才能始终访问位置</string>
    <key>NSLocationUsageDescription</key>
    <string>App需要您的同意,才能访问位置</string>
    <key>NSLocationWhenInUseUsageDescription</key>
    <string>App需要您的同意,才能在使用期间访问位置</string>
    <key>NSMicrophoneUsageDescription</key>
    <string>App需要您的同意,才能访问麦克风</string>
    <key>NSMotionUsageDescription</key>
    <string>App需要您的同意,才能访问运动与健身</string>
    <key>NSPhotoLibraryUsageDescription</key>
    <string>App需要您的同意,才能访问相册</string>
    <key>NSRemindersUsageDescription</key>
```
* Missing Push Notification Entitlement (禁用推送)
```
    Build Settings -> Apple LLVM 6.0 - Preprocessing -> Preprocessor Macros :
    DISABLE_PUSH_NOTIFICATIONS=1
```

### 适配








