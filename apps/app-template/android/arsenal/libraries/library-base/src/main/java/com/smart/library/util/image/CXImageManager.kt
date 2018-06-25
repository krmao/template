package com.smart.library.util.image

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import java.io.File


@Suppress("unused", "MemberVisibilityCanBePrivate")
object CXImageManager {

    private var imageHandler: CXIImageHandler? = null

    @JvmStatic
    fun initialize(imageHandler: CXIImageHandler) {
        if (CXImageManager.imageHandler == null) {
            CXImageManager.imageHandler = imageHandler
            imageHandler.initialize()
        }
    }

    @JvmStatic
    fun show(imageView: ImageView?, uri: Uri?) = imageHandler?.show(imageView, uri)

    @JvmStatic
    fun show(imageView: ImageView?, uriString: String?) = show(imageView, getUri(uriString))

    @JvmStatic
    fun show(imageView: ImageView?, resId: Int?) = show(imageView, getResUriString(resId))

    @JvmStatic
    fun download(uri: Uri?, callback: (bitmap: Bitmap?) -> Unit?) = imageHandler?.download(uri, callback)

    @JvmStatic
    fun downloadSync(uri: Uri?): Bitmap? = imageHandler?.downloadSync(uri)

    @JvmStatic
    fun isDownloaded(uri: Uri?): Boolean = imageHandler?.isDownloaded(uri) ?: false

    @JvmStatic
    fun fetchImageFromCache(uri: Uri?): File? = imageHandler?.fetchImageFromCache(uri)

    @JvmStatic
    fun getUri(uriString: String?): Uri? = if (uriString.isNullOrBlank()) null else Uri.parse(uriString)

    @JvmStatic
    fun getResUriString(resId: Int?): String? {
        var uriString: String? = null
        if (resId != null) {
            when (imageHandler?.type()) {
                CXIImageHandler.Type.FRESCO -> uriString = "res:///$resId"
                else -> uriString = null
            }
        }
        return uriString
    }

    @JvmStatic
    fun getAssetUriString(assetRelativePath: String?): String? {
        var uriString: String? = null
        if (!assetRelativePath.isNullOrBlank()) {
            when (imageHandler?.type()) {
                CXIImageHandler.Type.FRESCO -> uriString = "asset:///$assetRelativePath"
                else -> uriString = null
            }
        }
        return uriString
    }

    @JvmStatic
    fun getFileUriString(filePath: String?): String? {
        var uriString: String? = null
        if (!filePath.isNullOrBlank()) {
            when (imageHandler?.type()) {
                CXIImageHandler.Type.FRESCO -> uriString = "file:///$filePath"
                else -> uriString = null
            }
        }
        return uriString
    }
}