package com.smart.library.util

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.smart.library.base.STBaseApplication

@Suppress("unused")
object STVectorDrawableUtil {
    fun getVectorDrawableCompat(vectorResId: Int): Drawable = AppCompatDrawableManager.get().getDrawable(STBaseApplication.INSTANCE, vectorResId)

    fun getAnimatedVectorDrawableCompat(animatedVectorResId: Int): AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(STBaseApplication.INSTANCE, animatedVectorResId)
}
