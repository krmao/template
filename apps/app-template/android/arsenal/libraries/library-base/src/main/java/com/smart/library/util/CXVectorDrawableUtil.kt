package com.smart.library.util

import android.graphics.drawable.Drawable
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.widget.AppCompatDrawableManager
import com.smart.library.base.CXBaseApplication

@Suppress("unused")
object CXVectorDrawableUtil {
    fun getVectorDrawableCompat(vectorResId: Int): Drawable = AppCompatDrawableManager.get().getDrawable(CXBaseApplication.INSTANCE, vectorResId)

    fun getAnimatedVectorDrawableCompat(animatedVectorResId: Int): AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(CXBaseApplication.INSTANCE, animatedVectorResId)
}
