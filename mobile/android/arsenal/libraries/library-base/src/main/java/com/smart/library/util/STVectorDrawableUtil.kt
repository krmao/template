package com.smart.library.util

import android.graphics.drawable.Drawable
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.widget.AppCompatDrawableManager
import com.smart.library.base.STBaseApplication

@Suppress("unused")
object STVectorDrawableUtil {
    fun getVectorDrawableCompat(vectorResId: Int): Drawable = AppCompatDrawableManager.get().getDrawable(STBaseApplication.INSTANCE, vectorResId)

    fun getAnimatedVectorDrawableCompat(animatedVectorResId: Int): AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(STBaseApplication.INSTANCE, animatedVectorResId)
}
