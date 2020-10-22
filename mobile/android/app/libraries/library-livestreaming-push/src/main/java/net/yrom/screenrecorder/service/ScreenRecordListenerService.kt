package net.yrom.screenrecorder.service

import android.app.*
import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import net.ossrs.yasea.R
import net.yrom.screenrecorder.IScreenRecorderAidlInterface
import net.yrom.screenrecorder.core.RESAudioClient
import net.yrom.screenrecorder.core.RESCoreParameters
import net.yrom.screenrecorder.model.DanmakuBean
import net.yrom.screenrecorder.rtmp.RESFlvData
import net.yrom.screenrecorder.rtmp.RESFlvDataCollecter
import net.yrom.screenrecorder.task.RtmpStreamingSender
import net.yrom.screenrecorder.task.ScreenRecorder
import net.yrom.screenrecorder.ui.activity.ScreenRecordActivity
import net.yrom.screenrecorder.view.MyWindowManager
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScreenRecordListenerService : Service() {
    private val NOTIFICATION_ID = 3
    private val mediaProjectionManager: MediaProjectionManager by lazy { getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager }
    private val executorService: ExecutorService by lazy { Executors.newCachedThreadPool() }
    private val streamingSender: RtmpStreamingSender by lazy { RtmpStreamingSender() }
    private val audioClient: RESAudioClient by lazy { RESAudioClient(RESCoreParameters()) }
    private val binder: IScreenRecorderAidlInterface.Stub = object : IScreenRecorderAidlInterface.Stub() {
        override fun sendDanmaku(danmakuBean: List<DanmakuBean>) {
            if (MyWindowManager.isWindowShowing()) {
                MyWindowManager.getSmallWindow().setDataToList(danmakuBean)
            }
        }

        override fun startScreenRecord(bundleData: Intent) {}

        override fun stopScreenRecord() {
            this@ScreenRecordListenerService.stopScreenRecord()
        }

        override fun isStartedScreenRecord(): Boolean {
            return this@ScreenRecordListenerService.isStartedScreenRecord
        }
    }

    private var screenRecorder: ScreenRecorder? = null
    private val builder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle(resources.getString(R.string.app_name))
            .setContentText("您正在录制视频内容哦")
            .setOngoing(true)
            .setDefaults(Notification.DEFAULT_VIBRATE)
    }
    var mediaProjection: MediaProjection? = null
    var rtmpURL: String = ""

    override fun onBind(intent: Intent): IBinder? = binder

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        initNotification()
        val resultCode: Int = intent.getIntExtra("code", -1)
        val data: Intent = intent.getParcelableExtra("data")
        rtmpURL = intent.getStringExtra("rtmpURL")
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)
        startScreenRecord()
        return super.onStartCommand(intent, flags, startId)
    }

    private var isStartedScreenRecord: Boolean = false
    private fun startScreenRecord() {
        if (isStartedScreenRecord) return
        streamingSender.sendStart(rtmpURL)
        if (!audioClient.prepare()) {
            throw NullPointerException("audioClient.prepare() failed")
        }

        Log.e("@@", "mediaProjection=$mediaProjection")
        val collector = RESFlvDataCollecter { flvData, type -> streamingSender.sendFood(flvData, type) }
        screenRecorder = ScreenRecorder(collector, RESFlvData.VIDEO_WIDTH, RESFlvData.VIDEO_HEIGHT, RESFlvData.VIDEO_BITRATE, 1, mediaProjection)
        screenRecorder?.start()
        audioClient.start(collector)
        executorService.execute(streamingSender)

        // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
        /*if (!MyWindowManager.isWindowShowing()) {
            Handler().post {
                MyWindowManager.createSmallWindow(applicationContext)
            }
        }*/

        isStartedScreenRecord = true
    }

    private fun stopScreenRecord() {
        if (!isStartedScreenRecord) return
        screenRecorder?.quit()
        screenRecorder = null
        streamingSender.sendStop()
        streamingSender.quit()
        executorService.shutdown()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopScreenRecord()
        MyWindowManager.removeSmallWindow(applicationContext)
    }

    private fun initNotification() {
        builder.setContentIntent(PendingIntent.getActivity(this, ScreenRecordActivity.PENDING_REQUEST_CODE, Intent(this, ScreenRecordActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
        /*以下是对Android 8.0的适配*/
        //普通notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("notification_id")
        }
        //前台服务notification适配
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW))
        }
        val notification = builder.build() // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND //设置为默认的声音
        startForeground(NOTIFICATION_ID, notification)
    }
}