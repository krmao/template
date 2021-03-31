package com.smart.library.widget.recyclerview

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Keep
import androidx.recyclerview.widget.RecyclerView

/**
 * 禁用 底部/顶部 fading edge 渐变效果
 * https://stackoverflow.com/a/27473014/4348530
 */
@Suppress("unused")
@Keep
class STRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RecyclerView(context, attrs, defStyleAttr) {

    private var bottomFadingEdgeStrength: Float? = null

    fun setBottomFadingEdgeStrength(strength: Float?) {
        this.bottomFadingEdgeStrength = strength
    }

    private var topFadingEdgeStrength: Float? = null

    fun setTopFadingEdgeStrength(strength: Float?) {
        this.topFadingEdgeStrength = strength
    }

    override fun getTopFadingEdgeStrength(): Float {
        return this.topFadingEdgeStrength ?: super.getTopFadingEdgeStrength()
    }

    override fun getBottomFadingEdgeStrength(): Float {
        return this.bottomFadingEdgeStrength ?: super.getBottomFadingEdgeStrength()
    }

}