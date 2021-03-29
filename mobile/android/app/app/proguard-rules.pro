-dontnote com.google.**
-dontwarn com.google.**
-keep class com.google.** { *; }

-dontnote com.tbruyelle.**
-dontwarn com.tbruyelle.**
-keep class com.tbruyelle.** { *; }

-dontnote com.facebook.**
-dontwarn com.facebook.**
-keep class com.facebook.** { *; }

-dontnote org.jetbrains.**
-dontwarn org.jetbrains.**
-keep class org.jetbrains.** { *; }

-dontnote kotlin.**
-dontwarn kotlin.**
-keep class kotlin.** { *; }

-dontnote io.reactivex.**
-dontwarn io.reactivex.**
-keep class io.reactivex.** { *; }

-dontnote com.gyf.**
-dontwarn com.gyf.**
-keep class com.gyf.** { *; }

-dontnote com.transitionseverywhere.**
-dontwarn com.transitionseverywhere.**
-keep class com.transitionseverywhere.** { *; }

-dontnote okhttp3.**
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }

-dontnote okio.**
-dontwarn okio.**
-keep class okio.** { *; }

-dontnote retrofit2.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-dontnote com.jude.**
-dontwarn com.jude.**
-keep class com.jude.** { *; }

# 百度地图
-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**

# 高德地图
# 高德地图 3D 地图 V5.0.0之前：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
-keep   class com.amap.api.trace.**{*;}
# 高德地图 3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}
# 高德地图 定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
# 高德地图 搜索
-keep   class com.amap.api.services.**{*;}
# 高德地图 2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
# 高德地图 导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

# support
-keep class com.smart.library.widget.behavior.STBottomSheetBackdropBehavior{*;}
-keep class com.smart.library.reactnative.RNBusHandler{*;}

# GSYVideoPlayer
-keep class com.shuyu.gsyvideoplayer.video.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.**
-keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.base.**
-keep class com.shuyu.gsyvideoplayer.utils.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.utils.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

-keep class com.simple.spiderman.** { *; }
-keepnames class com.simple.spiderman.** { *; }
-keep public class * extends android.app.Activity
-keep class * implements Android.os.Parcelable {
    public static final Android.os.Parcelable$Creator *;
}
# support
-keep public class * extends android.support.annotation.** { *; }
-keep public class * extends android.support.v4.content.FileProvider
# androidx
-keep public class * extends androidx.annotation.** { *; }
-keep public class * extends androidx.core.content.FileProvider