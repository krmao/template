package com.smart.library.util.image

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.facebook.drawee.backends.pipeline.Fresco
import java.io.File

interface STIImageHandler {
    fun initialize(application: Application?)
    fun show(imageView: ImageView?, uri: Uri?)
    fun showBlur(imageView: ImageView?, uri: Uri?, blurRadius: Int?)
    fun download(uri: Uri?, callback: (bitmap: Bitmap?) -> Unit?)
    fun downloadSync(uri: Uri?): Bitmap?
    fun isDownloaded(uri: Uri?): Boolean
    fun fetchImageFromCache(uri: Uri?): File?
    fun type(): Type
    fun clearMemoryCaches()

    enum class Type {
        FRESCO,
        GLIDE
    }
}