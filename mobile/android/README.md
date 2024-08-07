### [配色工具](https://material.io/resources/color/#!/?view.left=0&view.right=0&secondary.color=F4511E&primary.color=FF9800)

### query granted permissions
```
adb shell dumpsys package packagename

// and check grantedPermissions section at the bottom of the output
// android 6.0 为分界线, 6.0 以下都是不用询问直接赋值
```

### [Pixel Densities](http://iconhandbook.co.uk/reference/chart/android/)
```
mdpi (Baseline):	160 dpi	1×
hdpi:	            240 dpi	1.5×
xhdpi:	            320 dpi	2×
xxhdpi:	            480 dpi	3×
xxxhdpi:	        640 dpi	4×
Sizes (px)	        Format and naming	Notes
```

### [Launcher icons](https://developer.android.com/guide/practices/ui_guidelines/icon_design_adaptive)
* Adaptive icons
    * 在Android 7.1（API级别25）及更早版本中，启动器图标大小为48 x 48 dp。您现在必须使用以下准则来调整图标图层的大小：
        * 两层的尺寸必须为108 x 108 dp。
        * 图标的内部72 x 72 dp出现在蒙版视口内。
        * 系统在四面各留出18dp，以产生有趣的视觉效果，如视差或脉冲。

* Image Asset 自动生成应用图标
    * 如果选择 **自动生成兼容 7.1** 以下的图片, 生成的图标**四周会有留白**, 在 **5.0** 等机器上**图标变小**的问题, 如果不合适, 则需要**UI提供适配7.1以下**的**五套图**

* **Warning** 如果适配Android 8.0 (targetSdkVersion = 26) 应用图标会出现适配问题, 如果已经做了适配操作,在谷歌 Pixel 2 图标显示正常, 但是在华为 Mate10 显示的却是圆形; 如果不适配则谷歌 Pixel 2 图标显示被一个框框包裹进去, 
所以目前设置为 targetSdkVersion = 25, 直接让 UI 给 5套图即可, 不做 android 8.0  Adaptive icons 适配

* [Pixel Densities](http://iconhandbook.co.uk/reference/chart/android/)
    * Android icons require five separate sizes for different screen pixel densities. Icons for lower resolution are created automatically from the baseline.


![android 8.0 adaptive icons](gradle/readme/adaptive_icons.gif)

|  target  |    layer      |   icon(6:4)      |   padding(6:1)  |    final   |   dpi(px)   |
|   :-:    |      :-:      |        :-:       |       :-:       |     :-:    |     :-:     |
|anydpi-v26|               |                  |                 |            |             |
|xxxhdpi   |  432 x 432    | 288 x 288(6:4)   |  72 x 72(6:1)   |  192 × 192 |   640(x4  ) |
| xxhdpi   |  324 x 324    |                  |                 |  144 × 144 |   480(x3  ) |
|  xhdpi   |  216 x 216    |                  |                 |   96 × 96  |   320(x2  ) |
|   hdpi   |  162 x 162    |                  |                 |   72 × 72  |   240(x1.5) |
|   mdpi   |  108 x 108    |  72 x 72 (6:4)   |  18 x 18(6:1)   |   48 × 48  |   160(x1  ) |
|  store   |               |                  |                 |  512 × 512 |             |

* Config in AndroidManifest.xml
    ```
    <application
        android:icon="@mipmap/ic_launcher"
        android:roundIcon="@mipmap/ic_launcher_round">
    ```

### Action bar, Dialog & Tab icons
* These icons are used in the action bar menu. The first number is the size of the icon area, and the second is file size.
```
24 × 24 area in 32 × 32 (mdpi)
36 × 36 area in 48 × 48 (hdpi)
48 × 48 area in 64 × 64 (xhdpi)
72 × 72 area in 96 × 96 (xxhdpi)
96 × 96 area in 128 × 128 (xxxhdpi)
```

### Small Contextual Icons
* Small icons are used to surface actions and/or provide status for specific items. For example, in the Gmail app, each message has a star icon that marks the message as important.
```
16 × 16 (mdpi)
24 × 24 (hdpi)
32 × 32 (xhdpi)
48 × 48 (xxhdpi)
64 × 64 (xxxhdpi)
```

### Notification icons
* 通知栏小图标必须为纯透明背景,白色 icon 内容
    * https://blog.csdn.net/u013706904/article/details/51912634
    * https://stackoverflow.com/questions/30795431/icon-not-displaying-in-notification-white-square-shown-instead
* These are used to represent application notifications in the status bar. They should be flat (no gradients), white and face-on perspective
```
22 × 22 area in 24 × 24 (mdpi)
33 × 33 area in 36 × 36 (hdpi)
44 × 44 area in 48 × 48 (xhdpi)
66 × 66 area in 72 × 72 (xxhdpi)
88 × 88 area in 96 × 96 (xxxhdpi)
```

### ABI
```
/**
 * https://developer.android.com/ndk/guides/abis?hl=zh-cn
 * Android 系统运行时 会优先寻找 主要ABI(主要ABI可以实现系统最佳性能), 没有的话会选择 辅助ABI, 如果找都找不到, 应用可以安装但会在运行时奔溃
 *
 * https://android.stackexchange.com/questions/34958/what-are-the-minimum-hardware-specifications-for-android
 * https://stackoverflow.com/questions/28926101/is-it-safe-to-support-only-armeabi-v7a-for-android-4-and-above
 * 自r16以来NDK已弃用armeabi和mips。因此默认情况下不会生成这些体系结构的文件。虽然在abifilters可以通过修改得到这样的文件,但实际上意味着 armeabi 和 mips 现在绝对不需要
 *
 * Android 4+ 仅支持 armeabi-v7a 安全吗？
 * 1: 原则上，armeabi 如果您的应用需要Android 4.0，您可以放弃，但不确定是否有任何此类官方保证。如果是 Android 4.4+，应该绝对没问题。
 * 2: 没有支持 armeabi 但不支持的 Android 4+ 设备 armeabi-v7a，因此可以安全地放弃armeabi。
 *
 * 总结: android 4.0+ 可以只用 armeabi-v7a 不用 armeabi 但可能有风险, android 4.4+ 绝对没问题
 *
 * --------------------------------------------------------------------------------
 * cpu arch                 primary-abi             secondary-abi
 * --------------------------------------------------------------------------------
 * armeabi                  armeabi                 none
 * armeabi-v7a              armeabi-v7a             armeabi
 * arm64-v8a                arm64-v8a               armeabi-v7a / armeabi
 * --------------------------------------------------------------------------------
 * x86                      x86                     armeabi-v7a / armeabi
 * x86_64                   x86_64                  x86 / armeabi-v7a / armeabi
 * --------------------------------------------------------------------------------
 * mips                     mips                    none
 * mips64                   mips64                  mips
 * --------------------------------------------------------------------------------
 *
 */
```

### Android Preview
```
自定义设备:竖屏大屏低屏幕密度显示屏

设备参数
————
分辨率		1080x1920
屏幕尺寸	 	6.88inch(实际21.5inch)
屏幕密度 		160dpi
屏幕像素密度 	320ppi(ppi = Math.sqrt(1080 * 1080 + 1920 * 1920) / 6.88 = 2202.9 / 6.88)
https://zhuanlan.zhihu.com/p/158099749


Android Preview Hardware
————
Resolution 1080x1920

Screen size = (6.88 = Math.sqrt(1080 * 1080 + 1920 * 1920) / 320 = 2202.9 / 320)
填写15inch时, density 为 mdpi, 在设备上显示刚好
填写21.5inch时, density 为 ldpi, 未在设备上验证
————


Density
————
ldpi=120
mdpi=160
hdpi=240
xhdpi=320
xxhdpi=480
```