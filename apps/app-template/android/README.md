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
```
Launcher icons
48 × 48 (mdpi)
72 × 72 (hdpi)
96 × 96 (xhdpi)
144 × 144 (xxhdpi)
192 × 192 (xxxhdpi)
512 × 512 (Google Play store)	.png	Three-dimensional, front view, with a slight perspective as if viewed from above, so that users perceive some depth.
```
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
