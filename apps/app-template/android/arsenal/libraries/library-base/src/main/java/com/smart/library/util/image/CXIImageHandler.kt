package com.smart.library.util.image

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import java.io.File

interface CXIImageHandler {
    fun initialize()
    fun show(imageView: ImageView?, uri: Uri?)
    fun showBlur(imageView: ImageView?, uri: Uri?, blurRadius: Int?)
    fun download(uri: Uri?, callback: (bitmap: Bitmap?) -> Unit?)
    fun downloadSync(uri: Uri?): Bitmap?
    fun isDownloaded(uri: Uri?): Boolean
    fun fetchImageFromCache(uri: Uri?): File?
    fun type(): Type

    enum class Type {
        FRESCO,
        GLIDE
    }
}