package com.smart.library.util.image

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.os.Build
import android.view.View
import android.view.View.MeasureSpec
import androidx.annotation.RawRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.drawToBitmap
import com.smart.library.base.STBaseApplication
import java.io.InputStream


@Suppress("unused")
object STImageUtil {

    /**
     * vectorDrawables.useSupportLibrary = true
     */
    @SuppressLint("ObsoleteSdkInt")
    @JvmStatic
    fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(STBaseApplication.INSTANCE, drawableId) ?: return null

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) drawable = DrawableCompat.wrap(drawable).mutate()

        var bitmap: Bitmap?
        try {
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            if (bitmap != null) {
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            }
        } catch (e: Exception) {
            bitmap = null
        }

        return bitmap
    }

    /**
     * 合成图片
     */
    @JvmStatic
    fun mergeBitmap(firstBitmap: Bitmap?, secondBitmap: Bitmap?, secondBitmapLeft: Float = 0f, secondBitmapTop: Float = 0f): Bitmap? {
        firstBitmap ?: return null
        secondBitmap ?: return null

        val bitmap: Bitmap = Bitmap.createBitmap(firstBitmap.width, firstBitmap.height, firstBitmap.config)
        val canvas = Canvas(bitmap)
        canvas.drawBitmap(firstBitmap, Matrix(), null)
        canvas.drawBitmap(secondBitmap, secondBitmapLeft, secondBitmapTop, null)
        return bitmap
    }

    @JvmStatic
    fun getBitmapFromResource(imageResId: Int, resource: Resources = STBaseApplication.INSTANCE.resources): Bitmap? {
        return BitmapFactory.decodeResource(resource, imageResId)
    }

    @JvmStatic
    fun getBitmapFromFile(pathName: String?): Bitmap? {
        return BitmapFactory.decodeFile(pathName)
    }

    @JvmStatic
    fun getBitmapFromRaw(@RawRes rawResId: Int, resource: Resources = STBaseApplication.INSTANCE.resources): Bitmap? {
        return BitmapFactory.decodeStream(resource.openRawResource(rawResId))
    }

    @JvmStatic
    fun getBitmapFromAssets(fileName: String, resource: Resources = STBaseApplication.INSTANCE.resources): Bitmap? {
        return BitmapFactory.decodeStream(resource.assets.open(fileName))
    }

    fun getBitmapFromBytes(bytes: ByteArray): Bitmap? {
        return if (bytes.isNotEmpty()) BitmapFactory.decodeByteArray(bytes, 0, bytes.size) else null
    }

    fun getBitmapFromStream(inputStream: InputStream?): Bitmap? {
        return BitmapFactory.decodeStream(inputStream)
    }

    @JvmStatic
    fun getBitmapFromViewAfterLaidOut(canvasView: View, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap? {
        return try {
            canvasView.drawToBitmap(config)
        } catch (ignore: java.lang.Exception) {
            null
        }
    }

    @JvmStatic
    fun getBitmapFromViewBeforeLaidOut(view: View): Bitmap? {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
        view.layout(0, 0, view.measuredWidth, view.measuredWidth)
        val bitmap: Bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredWidth, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

}