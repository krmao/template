package com.smart.template.home.videoplayer

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smart.library.base.STBaseActivity
import com.smart.library.util.STLogUtil
import com.smart.library.util.STStatusBarUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.library.widget.recyclerview.snap.STSnapGravityPagerHelper
import com.smart.library.widget.recyclerview.snap.STSnapHelper
import com.smart.template.R
import com.smart.template.home.videoplayer.widget.STVideoPagerView
import com.smart.template.home.videoplayer.widget.STWrappedVideoPlayerView
import java.lang.ref.WeakReference


@Suppress("RedundantSamConstructor", "unused")
class STVideoPagerActivity : STBaseActivity() {

    private var lastOrientation: Int = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    private val verticalRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.verticalRecyclerView) }
    private val isTransparentStatusBarSupported: Boolean by lazy { STStatusBarUtil.isTransparentStatusBarSupported }
    private var currentPosition: Int = RecyclerView.NO_POSITION
    private val videoPlayerWeakReferenceArray: SparseArray<WeakReference<STWrappedVideoPlayerView>> = SparseArray()
    private val videoUrlList: MutableList<String> = arrayListOf(
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-28_18-20-56.mp4",
        "https://api.huoshan.com/hotsoon/item/video/_playback/?video_id=3addc2c120c64055b4d8d5aa02ed0358&line=1&app_id=1115&quality=720p",
        "https://api.huoshan.com/hotsoon/item/video/_playback/?video_id=f56df03dea804541a9fcd4d1cc26eefd&line=1&app_id=1115&quality=720p",
        "https://api.huoshan.com/hotsoon/item/video/_playback/?video_id=dcb9b966feaf44b9bbb182ed6ec18905&line=0&app_id=1115&watermark=1",
        "https://api.huoshan.com/hotsoon/item/video/_playback/?video_id=e3da4d9917364f2b8f5826487563d763&line=0&app_id=1115&watermark=1",
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-28_18-20-56.mp4",
        "https://api.huoshan.com/hotsoon/item/video/_playback/?video_id=3addc2c120c64055b4d8d5aa02ed0358&line=1&app_id=1115&quality=720p",
        "https://api.huoshan.com/hotsoon/item/video/_playback/?video_id=f56df03dea804541a9fcd4d1cc26eefd&line=1&app_id=1115&quality=720p",
        "https://api.huoshan.com/hotsoon/item/video/_playback/?video_id=dcb9b966feaf44b9bbb182ed6ec18905&line=0&app_id=1115&watermark=1",
        "https://api.huoshan.com/hotsoon/item/video/_playback/?video_id=e3da4d9917364f2b8f5826487563d763&line=0&app_id=1115&watermark=1"
    )
    private val snapPagerHelper: STSnapGravityPagerHelper = STSnapGravityPagerHelper(false, STSnapHelper.Snap.CENTER) { position: Int ->
        STLogUtil.d(TAG, "onSnapped start position=$position, currentPosition=$currentPosition, playingVideoArray=${videoPlayerWeakReferenceArray.size()}")
        ensureRecyclerViewHolder(position) { _position: Int, _: RecyclerView.ViewHolder ->
            val videoPlayer = videoPlayerWeakReferenceArray.get(_position)?.get()
            videoPlayer?.play()
            currentPosition = _position
            STLogUtil.e(TAG, "onSnapped end position=$position, currentPosition=$currentPosition, playingVideoArray=${videoPlayerWeakReferenceArray.size()}, videoPlayer==null?${videoPlayer == null}")
        }
    }

    /**
     * 初始化 recyclerView 的时候有数据, onSnap 中的 findViewHolderForAdapterPosition 可能依然为 null
     */
    private fun ensureRecyclerViewHolder(position: Int, callback: ((position: Int, viewHolder: RecyclerView.ViewHolder) -> Unit)? = null) {
        if (position >= 0 && position < (verticalRecyclerView.adapter?.itemCount ?: 0) && !isFinishing) {
            val viewHolder = verticalRecyclerView.findViewHolderForAdapterPosition(position)
            if (viewHolder != null) {
                STLogUtil.d(TAG, "ensureRecyclerViewHolder viewHolder!=null, position=$position, currentPosition=$currentPosition, playingVideoArray=${videoPlayerWeakReferenceArray.size()}")
                callback?.invoke(position, viewHolder)
            } else {
                STLogUtil.w(TAG, "ensureRecyclerViewHolder viewHolder==null, position=$position, currentPosition=$currentPosition, playingVideoArray=${videoPlayerWeakReferenceArray.size()}")
                // https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html#getAdapterPosition()
                // This inconsistency is not important since it will be less than 16ms but it might be a problem if you want to use ViewHolder position to access the adapter.
                verticalRecyclerView.postDelayed({
                    ensureRecyclerViewHolder(position, callback)
                }, 16)
            }
        } else {
            STLogUtil.e(TAG, "ensureRecyclerViewHolder position=$position is not valid, currentPosition=$currentPosition, playingVideoArray=${videoPlayerWeakReferenceArray.size()}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_video_pager_activity)
        lastOrientation = requestedOrientation

        // 沉浸式状态栏适配
        if (isTransparentStatusBarSupported) {
            STStatusBarUtil.setTransparentForWindow(this) // 设置状态栏全透明
            STStatusBarUtil.setStatusBarLightMode(this, true) // 设置状态栏字体黑色
        }

        test()

        verticalRecyclerView.setItemViewCacheSize(5)
        verticalRecyclerView.setHasFixedSize(true)
        verticalRecyclerView.layoutManager = LinearLayoutManager(this)
        verticalRecyclerView.clearOnChildAttachStateChangeListeners()
        verticalRecyclerView.addOnChildAttachStateChangeListener(object : RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(itemView: View) {
                val position = verticalRecyclerView.getChildAdapterPosition(itemView)
                // 统一在此处 release, 不在 STWrappedVideoPlayerView 中, 与 videoPagerView.init 结对出现方便管理
                videoPlayerWeakReferenceArray.get(position)?.get()?.release()
                videoPlayerWeakReferenceArray.remove(position)
                STLogUtil.w(TAG, "onChildViewDetachedFromWindow position=$position, playingVideoArray=${videoPlayerWeakReferenceArray.size()}, itemView==null?false")
            }

            override fun onChildViewAttachedToWindow(itemView: View) {
                val position = verticalRecyclerView.getChildAdapterPosition(itemView)
                val videoPagerView: STVideoPagerView = itemView as STVideoPagerView
                // 由于在 onDetachedFromWindow 之前调用了 onBindViewHolder, 则在 onSnap 中 play 不会再调用 onBindViewHolder 而导致黑屏问题
                videoPagerView.init(videoUrlList[position])
                videoPlayerWeakReferenceArray.put(position, WeakReference(videoPagerView.getVideoPlayer()))
                STLogUtil.d(TAG, "onChildViewAttachedToWindow position=$position, playingVideoArray=${videoPlayerWeakReferenceArray.size()}, itemView==null?${false}")
            }
        })
        snapPagerHelper.attachToRecyclerView(verticalRecyclerView)
        verticalRecyclerView.adapter = object : STRecyclerViewAdapter<String, STRecyclerViewAdapter.ViewHolder>(this, videoUrlList) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
                STLogUtil.d(TAG, "onCreateViewHolder position=$position")
                return ViewHolder(LayoutInflater.from(context).inflate(R.layout.st_video_pager_recycler_item_layout, container, false))
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
                STLogUtil.d(TAG, "onBindViewHolder position=$position")
                // 由于 recyclerView 可能预先初始化下一页而提前调用 onBindViewHolder, 紧接着 recyclerView 可能调用 onChildViewDetachedFromWindow 中 release,
                // 导致在 onSnap 中播放的时候黑屏, 所以转移到 onChildViewAttachedToWindow 中初始化数据
            }
        }

        // 第一个默认播放
        if (videoUrlList.isNotEmpty()) {
            currentPosition = 0
            snapPagerHelper.scrollToPositionWithOnSnap(currentPosition, true)
        }
    }

    /**
     * 测试环境才会执行
     */
    private fun test() {
        STSystemUtil.isAppOnTestEnvironment {
            val testLayout: View by lazy { findViewById<View>(R.id.testLayout) }
            val btnNextVideo: View by lazy { findViewById<View>(R.id.btnNextVideo) }
            val btnPauseVideo: View by lazy { findViewById<View>(R.id.btnPauseVideo) }
            val btnPlayVideo: View by lazy { findViewById<View>(R.id.btnPlayVideo) }
            val btnReplayVideo: View by lazy { findViewById<View>(R.id.btnReplayVideo) }
            val btnReleaseVideo: View by lazy { findViewById<View>(R.id.btnReleaseVideo) }

            testLayout.visibility = View.VISIBLE
            btnNextVideo.setOnClickListener { snapPagerHelper.scrollToPositionWithOnSnap(currentPosition + 1) }
            btnPauseVideo.setOnClickListener { getVideoPlayer(currentPosition)?.pause() }
            btnPlayVideo.setOnClickListener { getVideoPlayer(currentPosition)?.play() }
            btnReplayVideo.setOnClickListener { getVideoPlayer(currentPosition)?.replay() }
            btnReleaseVideo.setOnClickListener { getVideoPlayer(currentPosition)?.release() }
        }
    }

    override fun onRestart() {
        super.onRestart()
        STLogUtil.w(TAG, "onRestart currentPosition=$currentPosition")
        getVideoPlayer(currentPosition)?.play()
    }

    override fun onStop() {
        super.onStop()
        STLogUtil.w(TAG, "onStop currentPosition=$currentPosition")
        getVideoPlayer(currentPosition)?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        STLogUtil.w(TAG, "onDestroy currentPosition=$currentPosition")
        releaseAllPlayers()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val newOrientation = newConfig.orientation
        STLogUtil.w(TAG, "onConfigurationChanged newOrientation=$newOrientation, lastOrientation=$lastOrientation")
        if ((newOrientation == 0 || newOrientation == 1) && (lastOrientation == 0 || lastOrientation == 1) && (lastOrientation != newOrientation)) {
            onOrientationChanged(newOrientation == 0)
            lastOrientation = newOrientation
        }
    }

    private fun onOrientationChanged(isLandscape: Boolean) {
        STLogUtil.w(TAG, "onOrientationChanged isLandscape=$isLandscape")
        for (index in 0 until videoPlayerWeakReferenceArray.size()) {
            val position = videoPlayerWeakReferenceArray.keyAt(index)
            videoPlayerWeakReferenceArray.get(position)?.get()
        }
    }

    private fun getVideoPlayer(position: Int): STWrappedVideoPlayerView? = videoPlayerWeakReferenceArray.get(position)?.get()
    private fun releaseVideoPlayer(position: Int) {
        videoPlayerWeakReferenceArray.get(position)?.get()?.release()
        videoPlayerWeakReferenceArray.remove(position)
    }

    // 释放所有
    private fun releaseAllPlayers() {
        for (index in 0 until videoPlayerWeakReferenceArray.size()) {
            val position = videoPlayerWeakReferenceArray.keyAt(index)
            val videoPlayer: STWrappedVideoPlayerView? = videoPlayerWeakReferenceArray.get(position)?.get()
            videoPlayer?.release()
        }
        videoPlayerWeakReferenceArray.clear()
    }

    companion object {
        const val TAG = "[ST_VIDEO_PAGER]"

        @JvmStatic
        @JvmOverloads
        fun goTo(activity: Activity?, source: String? = null) {
            if (activity == null || activity.isFinishing) {
                return
            }
            val intent = Intent(activity, STVideoPagerActivity::class.java)
            source?.let { intent.putExtra("source", it) }
            activity.runOnUiThread { activity.startActivity(intent) } // 防止调用方非主线程
        }
    }
}