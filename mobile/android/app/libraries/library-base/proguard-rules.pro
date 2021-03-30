# https://developer.android.com/studio/build/shrink-code

# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
# 成员 = 变量+方法

# -keep                  保留指定的类和成员名称和内容
# -keepnames             保留类和成员名称, 混淆内容
# -keepclassmembers      保留类成员名称和内容
# -keepclassmembernames  保留类成员名称, 混淆内容

# 关闭混淆
# -dontobfuscate

# 混淆时是否记录日志
# -verbose
# -dontshrink

# 混淆时是否做预校验
# -dontpreverify
# 混淆时应用侵入式重载
# -overloadaggressively
# 是否使用大小写混合
# -dontusemixedcaseclassnames
# 指定不去忽略非公共的库类
# -dontskipnonpubliclibraryclasses
# -printseeds
# -printconfiguration proguardConfiguration.txt
# -printmapping proguardMapping.txt
# 确定统一的混淆类的成员名称来增加混淆
-useuniqueclassmembernames
# 重新包装所有重命名的包并放在给定的单一包中
-flattenpackagehierarchy 'codesdancing'
-repackageclasses 'codesdancing'
# 优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification
-keepparameternames
-keepattributes JavascriptInterface,Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,Annotation,EnclosingMethod
-renamesourcefileattribute SourceFile

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

#
#
# normal
# ======================================================================

# ======================================================================
# custom
#
#

#https://mp.weixin.qq.com/s/DE4gr8cTRQp2jQq3c6wGHQ
#-dontoptimize
#-assumenosideeffects class android.util.Log {
#    public static *** d(...);
#    public static *** e(...);
#    public static *** i(...);
#    public static *** v(...);
#    public static *** println(...);
#    public static *** w(...);
#    public static *** wtf(...);
#}

##############################################
# databinding https://android.googlesource.com/platform/frameworks/data-binding/+/studio-master-dev/proguard.cfg
#-keep class android.databinding.** { *; }
#-keep class * extends androidx.databinding.DataBinderMapper { *; }
#-keepattributes javax.xml.bind.annotation.*
#-keepattributes javax.annotation.processing.*

-keep class com.smart.library.widget.behavior.STBottomSheetBackdropBehavior{*;}
#-keep public class * extends android.app.Activity