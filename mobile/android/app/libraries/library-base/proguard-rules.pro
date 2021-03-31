# https://developer.android.com/studio/build/shrink-code

# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
# 成员 = 变量+方法

# -keep
# -keepnames             保留类名称/方法名称, 混淆成员变量/方法参数
# -keepclassmembers      保留类成员名称和内容
# -keepclassmembernames  保留类名称

# -printseeds
# -printconfiguration proguardConfiguration.txt
# -printmapping proguardMapping.txt
-repackageclasses 'codesdancing'
-keepparameternames
-keepattributes MethodParameters,JavascriptInterface,Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod
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

# databinding https://android.googlesource.com/platform/frameworks/data-binding/+/studio-master-dev/proguard.cfg
#-keep class android.databinding.** { *; }
#-keep class * extends androidx.databinding.DataBinderMapper { *; }
#-keepattributes javax.xml.bind.annotation.*
#-keepattributes javax.annotation.processing.*

####################################################
# 混淆 library
# ** 代表包下的所有类文件
# 保留所有类名/嵌套类名
# 保留所有接口/嵌套接口
# public/protexted 变量名/方法名/方法参数名
# public/protexted static 变量名/方法名/方法参数名
# public/protexted static final 变量名/方法名/方法参数名
####################################################
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-keep, allowobfuscation class com.smart.library.*
-keepclassmembers, allowobfuscation class * {
    *;
}
-keep interface com.smart.library.**
-keepnames class com.smart.library.**
-keepclassmembernames class com.smart.library.** {
    public <init>(...);
    protected <init>(...);
    public <fields>;
    protected <fields>;
    public <methods>;
    protected <methods>;
    public final <fields>;
    protected final <fields>;
    public final <methods>;
    protected final <methods>;
    public static <fields>;
    protected static <fields>;
    public static <methods>;
    protected static <methods>;
    public static final <fields>;
    protected static final <fields>;
    public static final <methods>;
    protected static final <methods>;
    native <methods>;
}
####################################################