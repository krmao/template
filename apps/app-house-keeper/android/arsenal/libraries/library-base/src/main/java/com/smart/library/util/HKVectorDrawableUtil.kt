package com.smart.library.util

import android.graphics.drawable.Drawable
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.widget.AppCompatDrawableManager
import com.smart.library.base.HKBaseApplication

@Suppress("unused")
object HKVectorDrawableUtil {
    fun getVectorDrawableCompat(vectorResId: Int): Drawable = AppCompatDrawableManager.get().getDrawable(HKBaseApplication.INSTANCE, vectorResId)

    fun getAnimatedVectorDrawableCompat(animatedVectorResId: Int): AnimatedVectorDrawableCompat? = AnimatedVectorDrawableCompat.create(HKBaseApplication.INSTANCE, animatedVectorResId)
}
