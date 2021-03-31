# https://developer.android.com/studio/build/shrink-code

# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
# 成员 = 变量+方法

# -keep                  保留指定的类和成员名称和内容
# -keepnames             保留类和成员名称, 混淆内容
# -keepclassmembers      保留类成员名称和内容
# -keepclassmembernames  保留类成员名称, 混淆内容

# -printseeds
# -printconfiguration proguard-configuration.txt
# -printmapping proguard-mapping.txt
-repackageclasses 'codesdancing'
-keepparameternames
-keepattributes JavascriptInterface,Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod
-renamesourcefileattribute SourceFile

# https://stackoverflow.com/a/30532633/4348530
# https://www.guardsquare.com/en/products/proguard/manual/examples#serializable
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class * implements java.lang.Comparable {
    int compareTo(***);
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-dontnote android.**
-dontwarn android.**
-dontnote java.**
-dontwarn java.**

#
#
# normal
# ======================================================================

# ======================================================================
# custom
#
#

#noinspection ShrinkerUnresolvedReference

# databinding https://android.googlesource.com/platform/frameworks/data-binding/+/studio-master-dev/proguard.cfg
-keep class android.databinding.**{*;}
-keep class * extends androidx.databinding.DataBinderMapper {*;}
-keepattributes javax.xml.bind.annotation.*
-keepattributes javax.annotation.processing.*

-keep class kotlin.**{*;}
-dontnote kotlin.**
-dontwarn kotlin.**

-keep class com.google.**{*;}
-dontnote com.google.**
-dontwarn com.google.**

-keep class com.tbruyelle.**{*;}
-dontnote com.tbruyelle.**
-dontwarn com.tbruyelle.**

-keep class com.facebook.**{*;}
-dontnote com.facebook.**
-dontwarn com.facebook.**

-keep class org.jetbrains.**{*;}
-dontnote org.jetbrains.**
-dontwarn org.jetbrains.**

-keep class io.reactivex.**{*;}
-dontnote io.reactivex.**
-dontwarn io.reactivex.**

-keep class com.gyf.**{*;}
-dontnote com.gyf.**
-dontwarn com.gyf.**

-keep class com.transitionseverywhere.**{*;}
-dontnote com.transitionseverywhere.**
-dontwarn com.transitionseverywhere.**

-keep class okhttp3.**{*;}
-dontnote okhttp3.**
-dontwarn okhttp3.**

-keep class okio.**{*;}
-dontnote okio.**
-dontwarn okio.**

-keep class retrofit2.**{*;}
-dontnote retrofit2.**
-dontwarn retrofit2.**

-keep class com.jude.**{*;}
-dontnote com.jude.**
-dontwarn com.jude.**

# 百度地图
-keep class com.baidu.**{*;}
-keep class mapsdkvi.com.**{*;}
-dontwarn com.baidu.**
-dontnote com.baidu.**
-dontwarn mapsdkvi.com.**
-dontnote mapsdkvi.com.**

# 高德地图 2D/3D/定位/搜索/导航
-keep class com.amap.api.**{*;}
-keep class com.autonavi.**{*;}
-dontnote com.amap.**
-dontwarn com.amap.**
-dontnote com.autonavi.**
-dontwarn com.autonavi.**

# GSYVideoPlayer
-keep class com.shuyu.gsyvideoplayer.**{*;}
-dontwarn com.shuyu.gsyvideoplayer.**
-dontnote com.shuyu.gsyvideoplayer.**
-keep class tv.danmaku.**{*;}
-dontwarn tv.danmaku.**
-dontnote tv.danmaku.**

#
#
# custom
# ======================================================================

# ======================================================================
# modify
#
#

#https://mp.weixin.qq.com/s/DE4gr8cTRQp2jQq3c6wGHQ
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** e(...);
    public static *** i(...);
    public static *** v(...);
    public static *** println(...);
    public static *** w(...);
    public static *** wtf(...);
}

-keep class com.smart.library.reactnative.RNBusHandler{*;}
-keep class com.airbnb.lottie.** {*;}
-dontwarn com.airbnb.lottie.**
