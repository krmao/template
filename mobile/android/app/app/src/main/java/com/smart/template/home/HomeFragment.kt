package com.smart.template.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STDialogManager
import com.smart.library.util.STLogUtil
import com.smart.library.util.STRouteManager
import com.smart.library.util.bus.STBusManager
import com.smart.template.R
import com.smart.template.home.test.PullToNextPageFragment
import com.smart.template.home.test.RecyclerViewDragAndTransferFragment
import com.smart.template.home.test.RecyclerViewSnapTopFragment
import com.smart.template.home.test.VideoPlayerFragment
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheet.setOnClickListener {
            startActivity(Intent(context, STBehaviorBottomSheetActivity::class.java))
        }
        baiduMap.setOnClickListener {
            STRouteManager.goToFragment(activity, "com.smart.library.map.MapFragment") {
                STLogUtil.w("home", it.toString())
            }
        }
        gaodeMap.setOnClickListener {
            STRouteManager.goToFragment(activity, "com.smart.library.map.MapFragment") {
                STLogUtil.w("home", it.toString())
            }
        }
        dialogText.setOnClickListener {
            val loadingDialog: Dialog? = STDialogManager.createLoadingDialog(context, "规划中...", canceledOnTouchOutside = true)
            loadingDialog?.show()
        }
        dialogLoading.setOnClickListener {
            val loadingDialog: Dialog? = STDialogManager.createLoadingDialog(context)
            loadingDialog?.show()
        }
        flutterHotUpdate.setOnClickListener {
            //adb push assets/ /sdcard/Android/data/com.smart.template/cache/
            //val copySuccess = STFileUtil.copyDirectory(STCacheManager.getCacheChildDir("assets"), File(PathUtils.getDataDirectory(STBaseApplication.INSTANCE)))
            //STToastUtil.show("copySuccess?$copySuccess")
        }
        slideToNextPage.setOnClickListener {
            PullToNextPageFragment.goTo(context)
        }
        recyclersTransferItem.setOnClickListener {
            RecyclerViewDragAndTransferFragment.goTo(context)
        }
        recyclerItemSnapTop.setOnClickListener {
            RecyclerViewSnapTopFragment.goTo(context)
        }

        ijkPullRtmp.requestFocus()
        ijkPullRtmp.setOnClickListener {
            val url = rtmpURLET.text.toString()
            STBusManager.call(context, "livestreaming/play", url, url.substringAfterLast("/"))
        }
        ijkSettings.setOnClickListener {
            STBusManager.call(context, "livestreaming/opensettings")
        }
        yeseaPushRtmp.setOnClickListener {
            STBusManager.call(context, "livestreamingpush/push", "rtmp://10.32.33.20:5388/rtmplive/room-mobile")
        }
        playVideo.setOnClickListener {
            STBusManager.call(context, "livestreaming/playvideo", "https://vodlnr6niz5.vod.126.net/vodlnr6niz5/c6bc3543-429a-4342-a555-702596131fe4.mp4")
        }
        playVideoNormal.setOnClickListener {
            VideoPlayerFragment.goTo(context)
        }

    }
}
