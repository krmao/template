package com.smart.library.util.image.impl

import android.graphics.Bitmap
import android.net.Uri
import android.widget.ImageView
import com.facebook.binaryresource.BinaryResource
import com.facebook.binaryresource.FileBinaryResource
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.executors.UiThreadImmediateExecutorService
import com.facebook.common.references.CloseableReference
import com.facebook.datasource.DataSource
import com.facebook.datasource.DataSources
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.facebook.imagepipeline.image.CloseableBitmap
import com.facebook.imagepipeline.image.CloseableImage
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.md5
import com.smart.library.util.CXLogUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.library.util.image.CXIImageHandler
import okhttp3.OkHttpClient
import java.io.File

/**
 * 欲使用此类, 须先依赖 fresco 库, react native 中目前支持的且依赖的 fresco 版本目前为 1.3.0
 *
 * compileOnly "com.facebook.fresco:fresco:$rootProject.ext.frescoVersion"
 * compileOnly "com.facebook.fresco:imagepipeline-okhttp3:$rootProject.ext.frescoVersion"
 *
 * @see <a href="https://www.fresco-cn.org/docs/using-drawees-xml.html">https://www.fresco-cn.org/docs/using-drawees-xml.html</a>
 */
@Suppress("MemberVisibilityCanBeprotected", "protectedPropertyName", "unused", "MemberVisibilityCanBePrivate", "PropertyName")
open class CXImageFrescoHandler(val config: ImagePipelineConfig) : CXIImageHandler {

    protected var TAG: String = CXImageFrescoHandler::class.java.name

    companion object {

        @JvmStatic
        @JvmOverloads
        fun getConfigBuilder(debug: Boolean = CXBaseApplication.DEBUG, okHttpClient: OkHttpClient, cacheDir: File = CXCacheManager.getChildCacheDir(if (debug) "fresco" else "fresco".md5()), mainCacheDirName: String = if (debug) "main" else "main".md5(), smallCacheDirName: String = if (debug) "small" else "small".md5(), maxCacheSize: Long = 70 * 1024 * 1024, maxCacheSizeOnLowDiskSpace: Long = 40 * 1024 * 1024, maxCacheSizeOnVeryLowDiskSpace: Long = 10 * 1024 * 1024): ImagePipelineConfig.Builder {
            return OkHttpImagePipelineConfigFactory
                    .newBuilder(CXBaseApplication.INSTANCE, okHttpClient)
                    .setMainDiskCacheConfig(
                            DiskCacheConfig.newBuilder(CXBaseApplication.INSTANCE)
                                    .setBaseDirectoryPath(cacheDir)
                                    .setBaseDirectoryName(mainCacheDirName)
                                    .setMaxCacheSize(maxCacheSize)
                                    .setMaxCacheSizeOnLowDiskSpace(maxCacheSizeOnLowDiskSpace)
                                    .setMaxCacheSizeOnVeryLowDiskSpace(maxCacheSizeOnVeryLowDiskSpace)
                                    .build()
                    ).setSmallImageDiskCacheConfig(
                            DiskCacheConfig.newBuilder(CXBaseApplication.INSTANCE)
                                    .setBaseDirectoryPath(cacheDir)
                                    .setBaseDirectoryName(smallCacheDirName)
                                    .setMaxCacheSize(maxCacheSize)
                                    .setMaxCacheSizeOnLowDiskSpace(maxCacheSizeOnLowDiskSpace)
                                    .setMaxCacheSizeOnVeryLowDiskSpace(maxCacheSizeOnVeryLowDiskSpace)
                                    .build()
                    ).setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
        }
    }

    override fun initialize() {
        Fresco.initialize(CXBaseApplication.INSTANCE, config)
    }

    /**
     * 渐进式加载
     */
    override fun show(imageView: ImageView?, uri: Uri?) {
        if (uri == null || imageView == null || imageView !is SimpleDraweeView) {
            CXLogUtil.w(TAG, "uri == null or imageView !is SimpleDraweeView, return. ")
        } else {
            val request = ImageRequestBuilder.newBuilderWithSource(uri).setProgressiveRenderingEnabled(true).build()
            imageView.controller = ((Fresco.newDraweeControllerBuilder().setImageRequest(request) as PipelineDraweeControllerBuilder).setOldController(imageView.controller) as PipelineDraweeControllerBuilder).build()
        }
    }

    override fun download(uri: Uri?, callback: (bitmap: Bitmap?) -> Unit?) {
        val dataSource: DataSource<CloseableReference<CloseableImage>> = Fresco.getImagePipeline().fetchDecodedImage(ImageRequest.fromUri(uri), null)
        dataSource.subscribe(object : BaseBitmapDataSubscriber() {
            override fun onFailureImpl(dataSource: DataSource<CloseableReference<CloseableImage>>?) {
                dataSource?.close()
                callback.invoke(null)
            }

            override fun onNewResultImpl(bitmap: Bitmap?) {
                var newBitmap: Bitmap? = null
                try {
                    newBitmap = bitmap?.copy(bitmap.config, bitmap.isMutable)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    dataSource.close()
                }
                callback.invoke(newBitmap)
            }
        }, UiThreadImmediateExecutorService.getInstance())

    }

    override fun downloadSync(uri: Uri?): Bitmap? {
        var newBitmap: Bitmap? = null
        val dataSource: DataSource<CloseableReference<CloseableImage>> = Fresco.getImagePipeline().fetchDecodedImage(ImageRequest.fromUri(uri), null)
        try {
            val result = DataSources.waitForFinalResult(dataSource)

            // Do something with the image, but do not keep the reference to it!
            // The image may get recycled as soon as the reference gets closed below.
            // If you need to keep a reference to the image, read the following sections.
            result?.use {
                val bitmap = (it.get() as CloseableBitmap).underlyingBitmap
                try {
                    newBitmap = bitmap?.copy(bitmap.config, bitmap.isMutable)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } finally {
            dataSource.close()
        }
        return newBitmap
    }

    override fun isDownloaded(uri: Uri?): Boolean {
        if (uri == null) {
            return false
        } else {
            val cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(uri), null)
            return ImagePipelineFactory.getInstance().mainFileCache.hasKey(cacheKey) || ImagePipelineFactory.getInstance().smallImageFileCache.hasKey(cacheKey)
        }
    }

    override fun fetchImageFromCache(uri: Uri?): File? {
        var localFile: File? = null
        if (uri != null) {
            val cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(uri), null)
            val resource: BinaryResource
            if (ImagePipelineFactory.getInstance().mainFileCache.hasKey(cacheKey)) {
                resource = ImagePipelineFactory.getInstance().mainFileCache.getResource(cacheKey)
                localFile = (resource as FileBinaryResource).file
            } else if (ImagePipelineFactory.getInstance().smallImageFileCache.hasKey(cacheKey)) {
                resource = ImagePipelineFactory.getInstance().smallImageFileCache.getResource(cacheKey)
                localFile = (resource as FileBinaryResource).file
            }
        }
        return localFile
    }

    override fun type(): CXIImageHandler.Type = CXIImageHandler.Type.FRESCO
}

