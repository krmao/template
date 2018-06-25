package com.smart.library.util.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import com.smart.library.base.CXBaseApplication

@Suppress("unused")
object CXImageUtil {

    /**
     * vectorDrawables.useSupportLibrary = true
     */
    @JvmStatic
    fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(CXBaseApplication.INSTANCE, drawableId) ?: return null

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) drawable = DrawableCompat.wrap(drawable).mutate()

        var bitmap: Bitmap?
        try {
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
        } catch (e: Exception) {
            bitmap = null
        }

        return bitmap
    }

}