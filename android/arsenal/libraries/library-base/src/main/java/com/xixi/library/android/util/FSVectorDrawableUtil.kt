package com.xixi.library.android.util

import android.graphics.drawable.Drawable
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.widget.AppCompatDrawableManager
import com.xixi.library.android.base.FSBaseApplication

@Suppress("unused")
object FSVectorDrawableUtil {
    fun getVectorDrawableCompat(vectorResId: Int): Drawable {
        return AppCompatDrawableManager.get().getDrawable(FSBaseApplication.INSTANCE, vectorResId)
    }

    fun getAnimatedVectorDrawableCompat(animatedVectorResId: Int): AnimatedVectorDrawableCompat? {
        return AnimatedVectorDrawableCompat.create(FSBaseApplication.INSTANCE, animatedVectorResId)
    }
}