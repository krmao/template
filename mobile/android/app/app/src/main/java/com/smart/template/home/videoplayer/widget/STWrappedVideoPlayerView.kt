package com.smart.template.home.videoplayer.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.UiThread
import androidx.constraintlayout.widget.ConstraintLayout
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.smart.library.util.STLogUtil
import com.smart.template.R
import com.smart.template.home.videoplayer.STVideoPagerActivity

@Suppress("unused", "MemberVisibilityCanBePrivate")
class STWrappedVideoPlayerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(context).inflate(R.layout.st_video_player_view, this, true)
    }

    private val innerVideoPlayer: StandardGSYVideoPlayer by lazy { findViewById<StandardGSYVideoPlayer>(R.id.innerVideoPlayer) }
    private val innerVideoPlayerContainer: FrameLayout by lazy { findViewById<FrameLayout>(R.id.innerVideoPlayerContainer) }
    private var isVideoPlayerRemoved: Boolean = false

    /**
     * 初始化参数
     * recyclerView item attached to window 时调用
     */
    fun init(videoUrl: String) {
        val videoPlayer: StandardGSYVideoPlayer = getVideoPlayer()
        // videoPlayer.setBgTransparent() // 播放器背景视频空余地方透明
        videoPlayer.setUpLazy(videoUrl, true, null, null, "这是title")
        videoPlayer.titleTextView?.visibility = View.GONE
        videoPlayer.backButton?.visibility = View.GONE
        videoPlayer.fullscreenButton?.setOnClickListener {
            videoPlayer.startWindowFullscreen(
                context,
                false,
                true
            )
        }
        videoPlayer.isAutoFullWithSize = false
        videoPlayer.isReleaseWhenLossAudio = false
        videoPlayer.isShowFullAnimation = false
        videoPlayer.setIsTouchWiget(false)
        videoPlayer.startButton?.performClick()
    }

    private fun wrappedPlayerState(playerState: String): String = when (playerState) {
        "-1" -> "STATE_ERROR"
        "0" -> "STATE_IDLE"
        "1" -> "STATE_PLAYING"
        "2" -> "STATE_PAUSED"
        "3" -> "STATE_BUFFERING_PLAYING || STATE_BUFFERING_PAUSED"
        "4" -> "STATE_COMPLETED"
        "8" -> "STATE_PREPARING || STATE_PREPARED"
        else -> playerState
    }

    /**
     * 开始/继续播放
     */
    fun play() {
        innerVideoPlayer.startAfterPrepared()
    }

    /**
     * 重新播放
     */
    fun replay() {
        release()
        play()
    }

    /**
     * 暂停播放
     */
    fun pause() {
        innerVideoPlayer.onVideoPause()
    }

    /**
     * 释放资源
     */
    fun release() {
        innerVideoPlayer.release()
    }

    fun getVideoPlayer(): StandardGSYVideoPlayer = innerVideoPlayer

    /**
     * 删除视频播放器, 但保持引用
     */
    @UiThread
    fun removeVideoPlayer(): StandardGSYVideoPlayer {
        innerVideoPlayerContainer.removeView(innerVideoPlayer)
        isVideoPlayerRemoved = true
        return innerVideoPlayer
    }

    /**
     * 重新添加视频播放器
     */
    @UiThread
    fun addVideoPlayer() {
        if (isVideoPlayerRemoved) {
            innerVideoPlayerContainer.addView(innerVideoPlayer, 0, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
            isVideoPlayerRemoved = false
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        STLogUtil.w(TAG, "onAttachedToWindow playerView=$this")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        STLogUtil.w(TAG, "onDetachedFromWindow playerView=$this")
    }

    companion object {
        const val TAG = "${STVideoPagerActivity.TAG}:ITEM:PLAYER"
    }
}