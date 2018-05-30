### [android 标准图标尺寸](http://iconhandbook.co.uk/reference/chart/android/)
```
Pixel Densities
Android icons require five separate sizes for different screen pixel densities. Icons for lower resolution are created automatically from the baseline.

mdpi (Baseline):	160 dpi	1×
hdpi:	240 dpi	1.5×
xhdpi:	320 dpi	2×
xxhdpi:	480 dpi	3×
xxxhdpi:	640 dpi	4×
Sizes (px)	Format and naming	Notes
```
### 应用图标 api <= 7.1(25)
```
Launcher icons
48 × 48 (mdpi)
72 × 72 (hdpi)
96 × 96 (xhdpi)
144 × 144 (xxhdpi)
192 × 192 (xxxhdpi)
512 × 512 (Google Play store)	.png	Three-dimensional, front view, with a slight perspective as if viewed from above, so that users perceive some depth.
```

### 应用图标 api >= 8.0(26) [adaptive icons](https://developer.android.com/guide/practices/ui_guidelines/icon_design_adaptive)

* 在Android 7.1（API级别25）及更早版本中，启动器图标大小为48 x 48 dp。您现在必须使用以下准则来调整图标图层的大小：
    * 两层的尺寸必须为108 x 108 dp。
    * 图标的内部72 x 72 dp出现在蒙版视口内。
    * 系统在四面各留出18dp，以产生有趣的视觉效果，如视差或脉冲。

![android 8.0 adaptive icons](readme/adaptive_icons.gif)
------------------------------------------------------------------------------
------------------------------------------------------------------------------
|  target  |    layer      |   icon(6:4)      |   padding(6:1)  |    final   |
|   :-:    |      :-:      |        :-:       |       :-:       |     :-:    |
|anydpi-v26|               |                  |                 |            |
|xxxhdpi   |  432 x 432    | 288 x 288(6:4)   |  72 x 72(6:1)   |  192 × 192 |
| xxhdpi   |  324 x 324    |                  |                 |  144 × 144 |
|  xhdpi   |  216 x 216    |                  |                 |  96 × 96   |
|   hdpi   |  162 x 162    |                  |                 |  72 × 72   |
|   mdpi   |  108 x 108    |  72 x 72 (6:4)   |  18 x 18(6:1)   |  48 × 48   |
------------------------------------------------------------------------------
------------------------------------------------------------------------------


```
Action bar, Dialog & Tab icons
24 × 24 area in 32 × 32 (mdpi)
36 × 36 area in 48 × 48 (hdpi)
48 × 48 area in 64 × 64 (xhdpi)
72 × 72 area in 96 × 96 (xxhdpi)
96 × 96 area in 128 × 128 (xxxhdpi)	.png	These icons are used in the action bar menu. The first number is the size of the icon area, and the second is file size.
```
```
Small Contextual Icons
16 × 16 (mdpi)
24 × 24 (hdpi)
32 × 32 (xhdpi)
48 × 48 (xxhdpi)
64 × 64 (xxxhdpi)
.png	
Small icons are used to surface actions and/or provide status for specific items. For example, in the Gmail app, each message has a star icon that marks the message as important.
```

* 通知栏小图标必须为纯透明背景,白色 icon 内容
    * @see https://blog.csdn.net/u013706904/article/details/51912634
    * @see https://stackoverflow.com/questions/30795431/icon-not-displaying-in-notification-white-square-shown-instead
```
Notification icons
22 × 22 area in 24 × 24 (mdpi)
33 × 33 area in 36 × 36 (hdpi)
44 × 44 area in 48 × 48 (xhdpi)
66 × 66 area in 72 × 72 (xxhdpi)
88 × 88 area in 96 × 96 (xxxhdpi)
.png	These are used to represent application notifications in the status bar. They should be flat (no gradients), white and face-on perspective
```
