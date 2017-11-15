package com.saike.library.util

import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import com.saike.library.base.CXBaseApplication
import java.io.File

/*
<!--安卓 N 文件访问方式适配-->
<provider
    android:name="android.support.v4.content.FileProvider"
    android:authorities="com.saike.android.mongo.provider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/cx_provider_paths" />
</provider>
*/
/**
 * if Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
 *     intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
 */
@Suppress("MemberVisibilityCanPrivate")
object HKUriUtil {
    private val TAG = HKUriUtil::class.java.simpleName
    val AUTHORITY = CXBaseApplication.INSTANCE.packageName + ".provider"

    fun fromFileProvider(filePath: String?): Uri? {
        return fromFileProvider(File(filePath))
    }

    fun fromFileProvider(file: File?): Uri? {
        return fromFileProvider(AUTHORITY, file)
    }

    fun fromFileProvider(uri: Uri?): Uri? {
        return fromFileProvider(AUTHORITY, uri)
    }

    fun fromFileProvider(authority: String, uri: Uri?): Uri? {
        return if (uri != null && uri.scheme.startsWith("file://")) fromFileProvider(authority, File(uri.toString().replace(uri.scheme, ""))) else uri
    }

    fun fromFileProvider(authority: String?, file: File?): Uri? {
        var result: Uri? = null
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                result = FileProvider.getUriForFile(CXBaseApplication.INSTANCE, authority, file)
            } else if (file != null) {
                result = Uri.fromFile(file)
            }
        } catch (exception: Exception) {
            CXLogUtil.e(TAG, "fromFileProvider failure !", exception)
        }
        return result
    }
}
