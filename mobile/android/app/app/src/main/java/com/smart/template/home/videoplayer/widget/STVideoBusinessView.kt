package com.smart.template.home.videoplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.smart.library.util.STLogUtil
import com.smart.template.R
import com.smart.template.home.videoplayer.STVideoPagerActivity

@Suppress("unused")
class STVideoBusinessView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.st_video_business_view, this, true)
    }

    fun init(data: Any) {
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        STLogUtil.w(TAG, "onAttachedToWindow businessView=$this")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        STLogUtil.w(TAG, "onDetachedFromWindow businessView=$this")
    }

    companion object {
        const val TAG = "${STVideoPagerActivity.TAG}:ITEM:BUSINESS"
    }
}