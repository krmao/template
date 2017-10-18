# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
#成员 = 变量+方法

#-keep                  保留指定的类和成员名称和内容
#-keepnames             保留类和成员名称，混淆内容
#-keepclassmembers      保留指定的类成员名称和内容
#-keepclassmembernames  保留成员名称，混淆内容

#关闭混淆
#-dontobfuscate

# 混淆时是否记录日志
-verbose
-dontshrink

##############################################
#https://mp.weixin.qq.com/s/DE4gr8cTRQp2jQq3c6wGHQ
#-dontoptimize
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** e(...);
    public static *** i(...);
    public static *** v(...);
    public static *** println(...);
    public static *** w(...);
    public static *** wtf(...);
}
##############################################

#混淆时是否做预校验
-dontpreverify
#指定代码的压缩级别
-optimizationpasses 5
#混淆时应用侵入式重载
-overloadaggressively
#确定统一的混淆类的成员名称来增加混淆
-useuniqueclassmembernames
#重新包装所有重命名的包并放在给定的单一包中
-flattenpackagehierarchy 'M'
-repackageclasses 'template'
#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification
#是否使用大小写混合
-dontusemixedcaseclassnames
#指定不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
-renamesourcefileattribute SourceFile
#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-optimizations !code/allocation/variable

-printseeds
-printconfiguration proguardConfiguration.txt
-printmapping proguardMapping.txt

#保护属性 start
-keepparameternames
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-renamesourcefileattribute SourceFile
#保护属性 end

#保护指定的类和类的成员的名称 start
#假如指定的类成员存在的话
-keep class !android.support.v4.widget.**,* {
    native <methods>;
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    void set*(...);
    *** get*();
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#保护指定的类和类的成员的名称 end

#保护指定类成员的名称 start
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
#保护指定类成员的名称 end
#Serializable start
#-keepnames class * implements java.io.Serializable
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
#Serializable end
#不进行混淆保持原样 start
#绝对保护类和类成员在shrinking期间不被删除,并且在obfuscation期间不被重命名
#system start
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep class * implements java.lang.Comparable {
    int compareTo(***);
}
-keepclassmembers class * implements java.lang.annotation.Annotation {
    ** *();
}
#system end
# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

# Understand the @Keep support annotation.
-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

#
#
# normal
# ======================================================================

# ======================================================================
# custom
#
#

-dontwarn android.support.**
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**

#保护lambda start
-keepclassmembers class * {
    private static synthetic java.lang.Object $deserializeLambda$(java.lang.invoke.SerializedLambda);
}
-dontnote java.lang.invoke.SerializedLambda

-keepclassmembernames class * {
    private static synthetic *** lambda$*(...);
}
-adaptclassstrings com.example.Test
#保护lambda end

#通用 start
#如果确保警告是安全的,则可以忽略该警告 start
-dontwarn freemarker.**
#如果确保警告是安全的,则可以忽略该警告 end

#问题 start
#该指令解决了一个困扰很久的问题
#java.lang.IllegalArgumentException: name already added: string{"b"}
#while processing android/support/a/n.class
-keep class android.support.** { *; }
#问题 end


#javascript interface webview start
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-dontwarn fqcn.of.**
-dontnote fqcn.of.**
#javascript interface webview end

-keepclassmembers class * extends android.webkit.WebChromeClient{
       public void openFileChooser(...);
}


-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.google.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

#databinding start
#https://android.googlesource.com/platform/frameworks/data-binding/+/studio-master-dev/proguard.cfg
-keep class android.databinding.** { *; }
-keepattributes javax.xml.bind.annotation.*
-keepattributes javax.annotation.processing.*
-keepclassmembers class * extends java.lang.Enum { *; }
-keep class android.**
-keep interface android.**

#-libraryjars  <java.home>/lib/rt.jar
#-libraryjars  <java.home>/lib/jce.jar
