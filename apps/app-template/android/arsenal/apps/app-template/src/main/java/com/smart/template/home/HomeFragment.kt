package com.smart.template.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXActivity
import com.smart.library.base.CXBaseApplication
import com.smart.library.base.CXBaseFragment
import com.smart.library.livestreaming.SettingsActivity
import com.smart.library.livestreaming.VideoActivity
import com.smart.library.livestreaming.push.CXVideoPushActivity
import com.smart.library.util.CXFileUtil
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXToastUtil
import com.smart.library.util.cache.CXCacheManager
import com.smart.library.util.map.location.CXLocationManager
import com.smart.library.util.rx.permission.RxPermissions
import com.smart.template.R
import com.smart.template.home.test.*
import io.flutter.util.PathUtils
import kotlinx.android.synthetic.main.home_fragment.*
import java.io.File

class HomeFragment : CXBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.home_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text1.setOnClickListener {
            CXActivity.start(activity, Test1Fragment::class.java)
        }
        text2.setOnClickListener {
            CXActivity.start(activity, Test2Fragment::class.java)
        }
        text3.setOnClickListener {
            CXToastUtil.show("Hello World")
            CXToastUtil.show("system toast")
        }
        text4.setOnClickListener {
            //adb push assets/ /sdcard/Android/data/com.smart.template/cache/
            val copySuccess = CXFileUtil.copyDirectory(CXCacheManager.getCacheChildDir("assets"), File(PathUtils.getDataDirectory(CXBaseApplication.INSTANCE)))
            CXToastUtil.show("copySuccess?$copySuccess")
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
        text9.setOnClickListener {
            val name = "rmtp"
            val url = "rtmp://10.32.33.20:5388/rtmplive/room-mobile"
            VideoActivity.intentTo(activity, url, name)
        }
        text10.setOnClickListener {
            SettingsActivity.intentTo(activity)
        }
        text11.setOnClickListener {
            activity?.apply {
                RxPermissions(this).requestEachCombined(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO).subscribe {
                    CXLogUtil.e(RxPermissions.TAG, "request permissions callback -> $it")
                    if (it.granted) {
                        val url = "rtmp://10.32.33.20:5388/rtmplive/room-mobile"
                        CXVideoPushActivity.intentTo(activity, url)
                    } else {
                        CXToastUtil.show("相机需要权限")
                    }
                }
            }
        }
    }
}
