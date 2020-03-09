package com.smart.library.util

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.smart.library.base.STBaseApplication
import java.io.File

/*
<!--安卓 N 文件访问方式适配-->
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="app-package-name.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/st_provider_paths" />
</provider>
*/
/**
 * 传递软件包网域外的 file:// URI 可能给接收器留下无法访问的路径。因此，尝试传递 file:// URI 会触发 FileUriExposedException。分享私有文件内容的推荐方法是使用 FileProvider。
 *
 * 对于面向 Android 7.0 的应用，Android 框架执行的 StrictMode API 政策禁止在您的应用外部公开 file:// URI。
 * 如果一项包含文件 URI 的 intent 离开您的应用，则应用出现故障，并出现 FileUriExposedException 异常。
 *
 * 要在应用间共享文件，您应发送一项 content:// URI，并授予 URI 临时访问权限。
 *
 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
 */
@Suppress("MemberVisibilityCanPrivate", "unused", "MemberVisibilityCanBePrivate")
object STUriUtil {
    private val TAG = STUriUtil::class.java.simpleName
    val AUTHORITY = STBaseApplication.INSTANCE.packageName + ".provider"

    fun intentForFileProvider(action: String? = null): Intent? {
        val intent = Intent(action)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        return intent
    }

    fun fromFileProvider(filePath: String): Uri? = fromFileProvider(File(filePath))

    fun fromFileProvider(file: File): Uri? = fromFileProvider(AUTHORITY, file)

    fun fromFileProvider(uri: Uri?): Uri? = fromFileProvider(AUTHORITY, uri)

    fun fromFileProvider(authority: String, uri: Uri?): Uri? =
            if (uri != null && uri.scheme?.startsWith("file://") == true) fromFileProvider(authority, File(uri.toString().replace(uri.scheme ?: "", ""))) else uri

    fun fromFileProvider(authority: String, file: File): Uri? {
        var result: Uri? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                result = FileProvider.getUriForFile(STBaseApplication.INSTANCE, authority, file)
            } else {
                result = Uri.fromFile(file)
            }
        } catch (exception: Exception) {
            STLogUtil.e(TAG, "fromFileProvider failure !", exception)
        }
        return result
    }
}
