package com.smart.template.home.test

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.template.R
import kotlinx.android.synthetic.main.home_fragment_video_player.*

class VideoPlayerFragment : STBaseFragment() {

    companion object {
        fun goTo(context: Context?) {
            STActivity.start(context, VideoPlayerFragment::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment_video_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoView.setVideoPath("https://vodlnr6niz5.vod.126.net/vodlnr6niz5/c6bc3543-429a-4342-a555-702596131fe4.mp4")
        videoView.start()
    }

}
