### ios app icon size

* 让设计师提供一个尺寸为 1024x1024 的 appIcon1024x1024.png 执行以下脚本
```
python autoExportAppIcon.py ~/Desktop/appIcon1024x1024.png
```

### [ios launch image size](https://developer.apple.com/design/human-interface-guidelines/ios/icons-and-images/launch-screen/)

* For iPhone:

| Name              | OS        | Orientation   | Size in px (pixels)   |
| :----:            | :----:    | :----:        | :----:                |
| iPhone X          | iOS 11+   | Portrait      | 1125 x 2436           |
| iPhone X          | iOS 11+   | Landscape     | 2436 x 1125           |
| Retina HD 5.5"    | iOS 8,9   | Portrait      | 1242 x 2208           |
| Retina HD 5.5"    | iOS 8,9   | Landscape     | 2208 x 1242           |
| Retina HD 4.7"    | iOS 8,9   | Portrait      | 750 x 1334            | 
| Retina 4          | iOS 5-9   | Portrait      | 640 x 1136            | 
| 2x                | iOS 7-8   | Portrait      | 640 x 960             | 
| 2x                | iOS 5,6   | Portrait      | 640 x 960             | 
| 1x                | iOS 5,6   | Portrait      | 320 x 480             | 

* For iPad:

| Name              | OS        | Orientation                   | Size in px (pixels)   |
| :----:            | :----:    | :----:                        | :----:                |
| 1x                | iOS 7-9   | Portrait                      | 768 x 1024            |
| 1x                | iOS 7-9   | Landscape                     | 1024 x 768            |
| 2x                | iOS 7-9   | Portrait                      | 1536 x 2048           |
| 2x                | iOS 7-9   | Landscape                     | 2048 x 1536           |
| 1x                | iOS 5,6   | Portrait (Without Status Bar) | 768 x 1004            |
| 1x                | iOS 5,6   | Landscape(Without Status Bar) | 1024 x 748            |
| 2x                | iOS 5,6   | Portrait (Without Status Bar) | 1536 x 2008           |
| 2x                | iOS 5,6   | Landscape(Without Status Bar) | 2048 x 1496           |


### [ios LaunchScreen.storyboard]

* launch image size

| Name              | Size in px (pixels)   |
| :----:            | :----:                |
| 3x                | 1125x x 2001          | 
| 2x                | 750   x 1334          | 
| 1x                | 375   x 667           | 

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








