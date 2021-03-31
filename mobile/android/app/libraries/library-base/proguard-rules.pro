# https://developer.android.com/studio/build/shrink-code

# This is a configuration file for ProGuard.
# http://proguard.sourceforge.net/index.html#manual/usage.html
# 成员 = 变量+方法

# https://www.jianshu.com/p/b5b2a5dfaaf4
# keep	                        保留类和类中的成员, 防止被混淆或移除
# keepnames	                    保留类和类中的成员, 防止被混淆, 成员没有被引用会被移除
# keepclassmembers	            只保留类中的成员, 防止被混淆或移除
# keepclassmembernames	        只保留类中的成员, 防止被混淆, 成员没有引用会被移除
# keepclasseswithmembers	    保留类和类中的成员, 防止被混淆或移除, 保留指明的成员
# keepclasseswithmembernames    保留类和类中的成员, 防止被混淆, 保留指明的成员, 成员没有引用会被移除

# -printseeds
# -printconfiguration proguardConfiguration.txt
# -printmapping proguardMapping.txt
-repackageclasses 'codesdancing'
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
# https://www.guardsquare.com/en/products/proguard/manual/usage
####################################################
# 混淆 library
# @Keep 会导致私有变量/方法内容也不会被混淆
# ** 代表包下的所有类文件
# 保留所有类名/嵌套类名
# 保留所有接口/嵌套接口
# public/protexted 变量名/方法名/方法参数名
# public/protexted static 变量名/方法名/方法参数名
# public/protexted static final 变量名/方法名/方法参数名
####################################################

# 第一步 不删除未使用的类和类成员(字段和方法)
-dontshrink # 指定不缩小输入。默认情况下，ProGuard会收缩代码：删除所有未使用的类和类成员。它只保留由各种-keep选项列出的选项，以及它们直接或间接依赖的选项。它还会在每个优化步骤之后应用缩小步骤，因为某些优化可能会打开删除更多类和类成员的可能性。

# 第二步 保留参数名称和方法类型
-keepparameternames

# 第三步 保留方法参数的名称和访问标记
-keepattributes MethodParameters

# 第四步 保留其它重要属性
-keepattributes InnerClasses,EnclosingMethod,JavascriptInterface,LineNumberTable,Signature,Deprecated,SourceFile,Exceptions

# 第五步 保留指定包名下的所有的接口
-keep interface com.smart.library.**

# 第六步 保留指定包名下的类和类成员(字段和方法); 允许混淆但不被删除或优化
-keep, allowobfuscation class com.smart.library.**

# 第七步 保留指定包名下的类成员(字段和方法), 如果它们的类也被保留
-keepclassmembers, allowobfuscation class com.smart.library.**

# 第八步 保留指定包名下的类和类成员(字段和方法)的名称, 如果在 shrinking 阶段未删除它们
-keepnames class com.smart.library.** {
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

# 指定要保留其名称的类成员（如果在缩小阶段未删除它们）
# 例如，在处理由JDK 1.2或更早版本编译的库时，您可能想要保留合成方法的名称，因此混淆器可以在处理使用处理后的库的应用程序时再次检测到它（尽管ProGuard本身不需要此方法） 。仅在混淆时适用。
#-keepclassmembernames class com.smart.library.** {
#    public <init>(...);
#    protected <init>(...);
#    public <fields>;
#    protected <fields>;
#    public <methods>;
#    protected <methods>;
#    public final <fields>;
#    protected final <fields>;
#    public final <methods>;
#    protected final <methods>;
#    public static <fields>;
#    protected static <fields>;
#    public static <methods>;
#    protected static <methods>;
#    public static final <fields>;
#    protected static final <fields>;
#    public static final <methods>;
#    protected static final <methods>;
#    native <methods>;
#}
####################################################