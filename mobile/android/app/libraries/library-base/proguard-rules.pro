# https://developer.android.com/studio/build/shrink-code

# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
# 成员 = 变量+方法

# -keep                  保留指定的类和成员名称和内容
# -keepnames             保留类和成员名称, 混淆内容
# -keepclassmembers      保留类成员名称和内容
# -keepclassmembernames  保留类成员名称, 混淆内容

# -printseeds
# -printconfiguration proguardConfiguration.txt
# -printmapping proguardMapping.txt
-repackageclasses 'codesdancing'
-keepparameternames
-keepattributes JavascriptInterface,Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod
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