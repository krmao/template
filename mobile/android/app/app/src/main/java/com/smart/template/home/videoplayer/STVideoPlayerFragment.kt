package com.smart.template.home.videoplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.smart.library.base.STBaseFragment
import com.smart.template.R

class STVideoPlayerFragment : STBaseFragment() {

    private var videoPlayer: StandardGSYVideoPlayer? = null
    private val videoUrl =
        "http://tanzi27niu.cdsb.mobi/wps/wp-content/uploads/2017/04/2017-04-28_18-20-56.mp4"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val contentView: View =
            layoutInflater.inflate(R.layout.st_video_player_fragment, container, false)
        videoPlayer = contentView.findViewById(R.id.videoPlayer)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoPlayer?.setUpLazy(videoUrl, true, null, null, "这是title");
        videoPlayer?.startButton?.performClick()

    }

}