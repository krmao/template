package com.smart.template.home.videoplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.smart.library.util.STLogUtil
import com.smart.template.R
import com.smart.template.home.videoplayer.STVideoPagerActivity

@Suppress("unused")
class STVideoPagerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.st_video_pager_view, this, true)
    }

    private val wrappedVideoPlayerView: STWrappedVideoPlayerView by lazy { findViewById<STWrappedVideoPlayerView>(R.id.videoPlayerView) }
    private val videoBusinessView: STVideoBusinessView by lazy { findViewById<STVideoBusinessView>(R.id.videoBusinessView) }

    fun getVideoPlayer(): STWrappedVideoPlayerView = wrappedVideoPlayerView

    /**
     * 初始化参数
     * recyclerView item attached to window 时调用
     */
    fun init(data: String) {
        wrappedVideoPlayerView.init(data)
        videoBusinessView.init(data)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        STLogUtil.w(TAG, "onAttachedToWindow itemView=$this")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        STLogUtil.w(TAG, "onDetachedFromWindow itemView=$this")
    }

    companion object {
        const val TAG = "${STVideoPagerActivity.TAG}:ITEM"
    }
}