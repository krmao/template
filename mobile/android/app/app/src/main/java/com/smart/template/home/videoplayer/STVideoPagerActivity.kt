package com.smart.template.home.videoplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.cache.CacheFactory
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.smart.library.base.STBaseActivity
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.library.widget.recyclerview.snap.STSnapGravityPagerHelper
import com.smart.library.widget.recyclerview.snap.STSnapHelper
import com.smart.library.widget.viewpager.STVerticalViewPager
import com.smart.template.R

open class STVideoPagerActivity : STBaseActivity() {

    private val verticalViewPager: STVerticalViewPager by lazy { findViewById<STVerticalViewPager>(R.id.verticalViewPager) }
    private val verticalRecyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.verticalRecyclerView) }
    private val videoUrl =
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-28_18-20-56.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_video_pager_activity)

        CacheFactory.setCacheManager(ProxyCacheManager::class.java) //代理缓存模式，支持所有模式，不支持m3u8等

        verticalViewPager.isVerticalFadingEdgeEnabled = true
        verticalViewPager.offscreenPageLimit = 5
        /*verticalViewPager.adapter = STFragmentPagerAdapter(
            supportFragmentManager, arrayListOf(
                STVideoPlayerFragment(),
                STVideoPlayerFragment(),
                STVideoPlayerFragment(),
                STVideoPlayerFragment(),
                STVideoPlayerFragment(),
                STVideoPlayerFragment(),
                STVideoPlayerFragment(),
                STVideoPlayerFragment(),
                STVideoPlayerFragment(),
                STVideoPlayerFragment()
            )
        )*/

        verticalRecyclerView.layoutManager = LinearLayoutManager(this)

        STSnapGravityPagerHelper(false, STSnapHelper.Snap.CENTER) { position: Int ->
            val viewHolder = verticalRecyclerView.findViewHolderForAdapterPosition(position)
            val view: View? = viewHolder?.itemView

            var videoPlayer: StandardGSYVideoPlayer? = view?.tag as? StandardGSYVideoPlayer


        }.attachToRecyclerView(verticalRecyclerView)

        verticalRecyclerView.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
//                val position = verticalRecyclerView.getChildAdapterPosition(view)
//                val videoPlayer: StandardGSYVideoPlayer? = view.tag as? StandardGSYVideoPlayer
//                videoPlayer?.release()
                view.tag = null
            }

            override fun onChildViewAttachedToWindow(view: View) {
                if (view.tag == null) {
                    val videoPlayer: StandardGSYVideoPlayer = view.findViewById(R.id.videoPlayer)

                    videoPlayer.setUpLazy(videoUrl, true, null, null, "这是title");
                    videoPlayer.titleTextView?.visibility = View.GONE;
                    videoPlayer.backButton?.visibility = View.GONE;
                    videoPlayer.fullscreenButton?.setOnClickListener {
                        videoPlayer.startWindowFullscreen(
                            view.context,
                            false,
                            true
                        );
                    };
                    videoPlayer.isAutoFullWithSize = true;
                    videoPlayer.isReleaseWhenLossAudio = false;
                    videoPlayer.isShowFullAnimation = true;
                    videoPlayer.setIsTouchWiget(false);
                    videoPlayer.startButton?.performClick()
                    view.tag = videoPlayer
                } else {
                    (view.tag as? StandardGSYVideoPlayer)?.onVideoResume()
                }
            }
        })

        verticalRecyclerView.adapter = object :
            STRecyclerViewAdapter<Any, STRecyclerViewAdapter.ViewHolder>(
                this,
                arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 0)
            ) {
            override fun onCreateViewHolder(container: ViewGroup, p1: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(context).inflate(
                        R.layout.st_video_player_fragment,
                        container,
                        false
                    )
                )
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, p1: Int) {
                val view: View = viewHolder.itemView
                val videoPlayer: StandardGSYVideoPlayer = view.findViewById(R.id.videoPlayer)
            }
        }
    }

    override fun onBackPressed() {
        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    override fun onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    override fun onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }

    override fun onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }
}