package com.smart.template.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseApplication
import com.smart.library.base.STBaseFragment
import com.smart.library.livestreaming.SettingsActivity
import com.smart.library.livestreaming.VideoActivity
import com.smart.library.livestreaming.push.STVideoPushActivity
import com.smart.library.util.STFileUtil
import com.smart.library.util.STLogUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.cache.STCacheManager
import com.smart.library.util.rx.permission.RxPermissions
import com.smart.template.R
import com.smart.template.home.test.*
import io.flutter.util.PathUtils
import kotlinx.android.synthetic.main.home_fragment.*
import java.io.File

class HomeFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.home_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text1.setOnClickListener {
            STActivity.start(activity, Test1Fragment::class.java)
        }
        text2.setOnClickListener {
            STActivity.start(activity, Test2Fragment::class.java)
        }
        text3.setOnClickListener {
            STToastUtil.show("Hello World")
            STToastUtil.show("system toast")
        }
        text4.setOnClickListener {
            //adb push assets/ /sdcard/Android/data/com.smart.template/cache/
            val copySuccess = STFileUtil.copyDirectory(STCacheManager.getCacheChildDir("assets"), File(PathUtils.getDataDirectory(STBaseApplication.INSTANCE)))
            STToastUtil.show("copySuccess?$copySuccess")
        }
        text5.setOnClickListener {
            PullToNextPageFragment.goTo(context)
        }
        text6.setOnClickListener {
            ViewPagerScrollViewFragment.goTo(context)
        }
        text7.setOnClickListener {
            RecyclerViewDragAndTransferFragment.goTo(context)
        }
        text8.setOnClickListener {
            RecyclerViewSnapTopFragment.goTo(context)
        }

        text9.requestFocus()
        text9.setOnClickListener {
            val url = rtmpURLET.text.toString()
            VideoActivity.intentTo(activity, url, url.substringAfterLast("/"))
        }
        text10.setOnClickListener {
            SettingsActivity.intentTo(activity)
        }
        text11.setOnClickListener {
            activity?.apply {
                RxPermissions(this).requestEachCombined(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO).subscribe {
                    STLogUtil.e(RxPermissions.TAG, "request permissions callback -> $it")
                    if (it.granted) {
                        val url = "rtmp://10.32.33.20:5388/rtmplive/room-mobile"
                        STVideoPushActivity.intentTo(activity, url)
                    } else {
                        STToastUtil.show("相机需要权限")
                    }
                }
            }
        }
    }
}
