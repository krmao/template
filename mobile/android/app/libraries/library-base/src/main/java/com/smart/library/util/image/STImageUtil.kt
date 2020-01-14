package com.smart.library.util.image

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.smart.library.base.STBaseApplication

@Suppress("unused")
object STImageUtil {

    /**
     * vectorDrawables.useSupportLibrary = true
     */
    @JvmStatic
    fun getBitmapFromVectorDrawable(drawableId: Int): Bitmap? {
        var drawable = ContextCompat.getDrawable(STBaseApplication.INSTANCE, drawableId) ?: return null

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) drawable = DrawableCompat.wrap(drawable).mutate()

        var bitmap: Bitmap?
        try {
            bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            if (bitmap!=null){
                val canvas = Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
            }
        } catch (e: Exception) {
            bitmap = null
        }

        return bitmap
    }

}