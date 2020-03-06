package com.example.gsyvideoplayer

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gsyvideoplayer.simple.SimpleActivity
import com.example.gsyvideoplayer.utils.JumpUtils
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.Debuger
import kotlinx.android.synthetic.main.activity_main.*
import permissions.dispatcher.PermissionUtils

class VideoPlayerExamplesActivity : AppCompatActivity() {
    val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Debuger.enable()
        val hadPermission = PermissionUtils.hasSelfPermissions(this, *permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !hadPermission) {
            val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissions(permissions, 1110)
        }
        handleClicks()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val sdPermissionResult = PermissionUtils.verifyPermissions(*grantResults)
        if (!sdPermissionResult) {
            Toast.makeText(this, "没获取到sd卡权限，无法播放本地视频哦", Toast.LENGTH_LONG).show()
        }
    }

    private fun handleClicks() {
        open_simple.setOnClickListener {
            //简单的播放
            startActivity(Intent(this, SimpleActivity::class.java))
        }
        open_btn.setOnClickListener {
            //直接一个页面播放的
            JumpUtils.goToVideoPlayer(this, open_btn)
        }
        list_btn.setOnClickListener {
            //普通列表播放，只支持全屏，但是不支持屏幕重力旋转，滑动后不持有
            JumpUtils.goToVideoPlayer(this)
        }
        list_btn_2.setOnClickListener {
            //支持全屏重力旋转的列表播放，滑动后不会被销毁
            JumpUtils.goToVideoPlayer2(this)
        }
        recycler.setOnClickListener {
            //recycler的demo
            JumpUtils.goToVideoRecyclerPlayer(this)
        }
        recycler_2.setOnClickListener {
            //recycler的demo
            JumpUtils.goToVideoRecyclerPlayer2(this)
        }
        list_detail.setOnClickListener {
            //支持旋转全屏的详情模式
            JumpUtils.goToDetailPlayer(this)
        }
        list_detail_list.setOnClickListener {
            //播放一个连续列表
            JumpUtils.goToDetailListPlayer(this)
        }
        web_detail.setOnClickListener {
            //正常播放，带preview
            JumpUtils.gotoWebDetail(this)
        }
        danmaku_video.setOnClickListener {
            //播放一个弹幕视频
            JumpUtils.gotoDanmaku(this)
        }
        fragment_video.setOnClickListener {
            //播放一个弹幕视频
            JumpUtils.gotoFragment(this)
        }
        more_type.setOnClickListener {
            //跳到多类型详情播放器，比如切换分辨率，旋转等
            JumpUtils.gotoMoreType(this)
        }
        input_type.setOnClickListener {
            JumpUtils.gotoInput(this)
        }
        open_btn_empty.setOnClickListener {
            JumpUtils.goToPlayEmptyControlActivity(this, open_btn_empty)
        }
        open_control.setOnClickListener {
            JumpUtils.gotoControl(this)
        }
        open_filter.setOnClickListener {
            JumpUtils.gotoFilter(this)
        }
        open_btn_pick.setOnClickListener {
            //无缝切换
            JumpUtils.goToVideoPickPlayer(this, open_btn)
        }
        open_btn_auto.setOnClickListener {
            //列表自动播放
            JumpUtils.goToAutoVideoPlayer(this)
        }
        open_scroll.setOnClickListener {
            //列表自动播放
            JumpUtils.goToScrollDetailPlayer(this)
        }
        open_window.setOnClickListener {
            //多窗体下的悬浮窗
            JumpUtils.goToScrollWindow(this)
        }
        open_btn_ad.setOnClickListener {
            //广告
            JumpUtils.goToVideoADPlayer(this)
        }
        open_btn_multi.setOnClickListener {
            //多个同时播放
            JumpUtils.goToMultiVideoPlayer(this)
        }
        open_btn_ad2.setOnClickListener {
            //多个同时播放
            JumpUtils.goToVideoADPlayer2(this)
        }
        open_list_ad.setOnClickListener {
            //多个同时播放
            JumpUtils.goToADListVideoPlayer(this)
        }
        open_custom_exo.setOnClickListener {
            //多个同时播放
            JumpUtils.goToDetailExoListPlayer(this)
        }
        open_switch.setOnClickListener {
            JumpUtils.goToSwitch(this)
        }
        media_codec.setOnClickListener {
            JumpUtils.goMediaCodec(this)
        }
        detail_normal_activity.setOnClickListener {
            JumpUtils.goToDetailNormalActivity(this)
        }
        detail_download_activity.setOnClickListener {
            JumpUtils.goToDetailDownloadActivity(this)
        }
        detail_subtitle_activity.setOnClickListener {
            JumpUtils.goToGSYExoSubTitleDetailPlayer(this)
        }
        detail_audio_activity.setOnClickListener {
            JumpUtils.goToDetailAudioActivity(this)
        }
        clear_cache.setOnClickListener {
            //清理缓存
            GSYVideoManager.instance().clearAllDefaultCache(this@VideoPlayerExamplesActivity)
            //String url = "https://res.exexm.com/cw_145225549855002";
            //GSYVideoManager.clearDefaultCache(MainActivity222.this, url);
        }
    }
}