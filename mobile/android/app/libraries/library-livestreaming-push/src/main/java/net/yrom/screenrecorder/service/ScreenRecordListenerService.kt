package net.yrom.screenrecorder.service

import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.view.Surface
import androidx.core.app.NotificationCompat
import com.smart.library.util.STLogUtil
import net.ossrs.yasea.R
import net.yrom.screenrecorder.IScreenRecorderAidlInterface
import net.yrom.screenrecorder.core.RESAudioClient
import net.yrom.screenrecorder.core.RESCoreParameters
import net.yrom.screenrecorder.rtmp.RESFlvData
import net.yrom.screenrecorder.rtmp.RESFlvDataCollecter
import net.yrom.screenrecorder.task.RtmpStreamingSender
import net.yrom.screenrecorder.task.ScreenRecorder
import net.yrom.screenrecorder.ui.activity.ScreenRecordActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ScreenRecordListenerService : Service() {

    private val mediaProjectionManager: MediaProjectionManager by lazy { getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager }
    private val executorService: ExecutorService by lazy { Executors.newCachedThreadPool() }
    private val streamingSender: RtmpStreamingSender by lazy { RtmpStreamingSender() }
    private val audioClient: RESAudioClient by lazy { RESAudioClient(RESCoreParameters()) }
    private var isStartedScreenRecord: Boolean = false
    private var isStartedScreenCapture: Boolean = false
    private var screenRecorder: ScreenRecorder? = null
    private var mediaProjectionForRecord: MediaProjection? = null
    private var rtmpURL: String? = ""
    private var resultCode = 0
    private var resultData: Intent = Intent()
    private var surface: Surface? = null
    private var surfaceWidth: Int = 100
    private var surfaceHeight: Int = 100
    private var screenDensity: Int = -1
    private var virtualDisplay: VirtualDisplay? = null
    private val notificationBuilder: NotificationCompat.Builder by lazy {
        NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher)
            .setContentTitle("SMART APP")
            .setContentText("正在录制屏幕")
            .setOngoing(true)
            .setDefaults(Notification.DEFAULT_VIBRATE)
    }
    private val binder: IScreenRecorderAidlInterface.Stub = object : IScreenRecorderAidlInterface.Stub() {
        override fun startScreenRecord() {
            this@ScreenRecordListenerService.startScreenRecord()
        }

        override fun stopScreenRecord() {
            this@ScreenRecordListenerService.stopScreenRecord()
        }

        override fun isStartedScreenRecord(): Boolean {
            return this@ScreenRecordListenerService.isStartedScreenRecord
        }

        override fun isStartedScreenCapture(): Boolean {
            return this@ScreenRecordListenerService.isStartedScreenCapture
        }

        override fun stopScreenCapture() {
            this@ScreenRecordListenerService.stopScreenCapture()
        }

        override fun startScreenCapture() {
            this@ScreenRecordListenerService.startScreenCapture()
        }
    }

    override fun onBind(intent: Intent): IBinder? = binder

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotification()

        surfaceWidth = intent.getIntExtra("surfaceWidth", 0)
        surfaceHeight = intent.getIntExtra("surfaceHeight", 0)
        screenDensity = intent.getIntExtra("screenDensity", 0)

        resultCode = intent.getIntExtra("resultCode", 0)
        resultData = intent.getParcelableExtra("resultData") ?: Intent()

        surface = intent.getParcelableExtra("surface")
        rtmpURL = intent.getStringExtra("rtmpURL")

        STLogUtil.w(TAG, "surfaceWidth=$surfaceWidth, surfaceHeight=$surfaceHeight")
        STLogUtil.w(TAG, "screenDensity=$screenDensity, resultCode=$resultCode, resultData=$resultData")
        STLogUtil.w(TAG, "surface=$surface, rtmpURL=$rtmpURL")

        startScreenRecord()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startScreenRecord() {
        STLogUtil.w(TAG, "startScreenRecord start isStartedScreenRecord=$isStartedScreenRecord")
        if (isStartedScreenRecord) return

        if (!audioClient.prepare()) {
            STLogUtil.e(TAG, "audioClient.prepare() failed")
            return
        }
        mediaProjectionForRecord = mediaProjectionManager.getMediaProjection(resultCode, resultData)
        STLogUtil.e(TAG, "mediaProjectionForRecord=$mediaProjectionForRecord")

        streamingSender.sendStart(rtmpURL)

        val collector = RESFlvDataCollecter { flvData, type -> streamingSender.sendFood(flvData, type) }
        screenRecorder = ScreenRecorder(collector, RESFlvData.VIDEO_WIDTH, RESFlvData.VIDEO_HEIGHT, RESFlvData.VIDEO_BITRATE, 1, mediaProjectionForRecord)
        screenRecorder?.start()
        audioClient.start(collector)
        executorService.execute(streamingSender)

        startScreenCapture()
        isStartedScreenRecord = true
        STLogUtil.w(TAG, "startScreenRecord end")
    }

    private fun stopScreenRecord() {
        if (!isStartedScreenRecord) return
        screenRecorder?.quit()
        screenRecorder = null
        streamingSender.sendStop()
        streamingSender.quit()
        executorService.shutdown()
        mediaProjectionForRecord?.stop()
        mediaProjectionForRecord = null

        stopScreenCapture()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopScreenRecord()
    }

    @Suppress("DEPRECATION")
    private fun createNotification() {
        notificationBuilder.setContentIntent(PendingIntent.getActivity(this, ScreenRecordActivity.PENDING_REQUEST_CODE, Intent(this, ScreenRecordActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW))

        }
        val notification = notificationBuilder.build()
        notification.defaults = Notification.DEFAULT_SOUND
        startForeground(NOTIFICATION_ID, notification)
    }

    //region capture
    private fun startScreenCapture() {
        STLogUtil.w(TAG, "startScreenCapture start")
        if (isStartedScreenCapture) {
            return
        }
        stopScreenCapture()
        if (surface == null) {
            return
        }

        STLogUtil.w(TAG, "surfaceWidth=$surfaceWidth, surfaceHeight=$surfaceHeight")
        STLogUtil.w(TAG, "screenDensity=$screenDensity, resultCode=$resultCode, resultData=$resultData")
        STLogUtil.w(TAG, "surface=$surface, rtmpURL=$rtmpURL, mediaProjectionForRecord=$mediaProjectionForRecord")
        virtualDisplay = mediaProjectionForRecord?.createVirtualDisplay(
            "ScreenCapture:" + System.currentTimeMillis(),
            surfaceWidth,
            surfaceHeight,
            screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            surface,
            object : VirtualDisplay.Callback() {
                override fun onPaused() {
                    super.onPaused()
                    STLogUtil.w(TAG, "VirtualDisplay Callback onPaused")
                }

                override fun onResumed() {
                    super.onResumed()
                    STLogUtil.w(TAG, "VirtualDisplay Callback onResumed")
                }

                override fun onStopped() {
                    super.onStopped()
                    STLogUtil.w(TAG, "VirtualDisplay Callback onStopped")
                }
            },
            Handler()
        )
        isStartedScreenCapture = true
        STLogUtil.w(TAG, "startScreenCapture end mediaProjectionForRecord=$mediaProjectionForRecord, virtualDisplay=$virtualDisplay")
    }

    private fun stopScreenCapture() {
        STLogUtil.w(TAG, "stopScreenCapture start virtualDisplay=$virtualDisplay")
        if (virtualDisplay == null) {
            return
        }
        virtualDisplay?.release()
        virtualDisplay = null
        isStartedScreenCapture = false
        STLogUtil.w(TAG, "stopScreenCapture end")
    }
    //endregion

    companion object {
        private const val TAG = "SCREEN_RECORDER_SERVICE"
        private const val NOTIFICATION_ID = 3
        private const val NOTIFICATION_CHANNEL_ID = "NOTIFICATION_CHANNEL_ID"
        private const val NOTIFICATION_CHANNEL_NAME = "NOTIFICATION_CHANNEL_name"
    }
}