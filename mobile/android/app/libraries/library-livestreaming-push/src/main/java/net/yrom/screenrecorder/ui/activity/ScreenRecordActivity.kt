/*
 * Copyright (c) 2014 Yrom Wang <http://www.yrom.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.yrom.screenrecorder.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.IBinder
import android.util.DisplayMetrics
import androidx.core.content.ContextCompat
import com.smart.library.util.STLogUtil
import com.smart.library.util.STToastUtil
import kotlinx.android.synthetic.main.screen_record_activity.*
import kotlinx.android.synthetic.main.st_activity_video_push.view.*
import net.ossrs.yasea.R
import net.yrom.screenrecorder.IScreenRecorderAidlInterface
import net.yrom.screenrecorder.service.ScreenRecordListenerService

class ScreenRecordActivity : Activity() {
    private var recorderAidlInterface: IScreenRecorderAidlInterface? = null
    private val serviceIntent: Intent by lazy { Intent(this, ScreenRecordListenerService::class.java) }
    private val serviceConnection by lazy {
        object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                recorderAidlInterface = IScreenRecorderAidlInterface.Stub.asInterface(service)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                recorderAidlInterface = null
            }
        }
    }

    //region capture
    private val mScreenDensity by lazy {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.densityDpi
    }

    //endregion
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.screen_record_activity)
        button.setOnClickListener {
            if (recorderAidlInterface?.isStartedScreenRecord == true) {
                stop()
            } else {
                start()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // recorderAidlInterface?.startScreenCapture()
    }

    override fun onPause() {
        super.onPause()
        // recorderAidlInterface?.stopScreenCapture()
    }

    @SuppressLint("SetTextI18n")
    fun stop() {
        recorderAidlInterface?.stopScreenRecord()
        recorderAidlInterface = null
        unbindService(serviceConnection)
        stopService(serviceIntent)
        button.text = "Start Recorder"
    }

    @SuppressLint("SetTextI18n")
    fun start() {
        if (recorderAidlInterface?.isStartedScreenRecord == true) {
            stop()
        }
        val captureIntent = (getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager).createScreenCaptureIntent()
        startActivityForResult(captureIntent, REQUEST_CAPETURE_CODE)
        button.text = "Stop Recorder"
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CAPETURE_CODE) {
            if (resultCode != RESULT_OK) {
                STLogUtil.i(TAG, "用户已取消")
                STToastUtil.show("用户已取消")
                return
            }

            serviceIntent.putExtra("resultCode", resultCode)
            serviceIntent.putExtra("resultData", data)
            serviceIntent.putExtra("rtmpURL", et_rtmp_address.text.toString().trim { it <= ' ' })
            serviceIntent.putExtra("surfaceWidth", mSurfaceView.width)
            serviceIntent.putExtra("surfaceHeight", mSurfaceView.height)
            serviceIntent.putExtra("screenDensity", mScreenDensity)
            serviceIntent.putExtra("surface", mSurfaceView.holder.surface)

            bindService(
                serviceIntent,
                serviceConnection,
                BIND_AUTO_CREATE
            )
            ContextCompat.startForegroundService(this, serviceIntent)
            // moveTaskToBack(true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }


    companion object {
        const val TAG = "SCREEN_RECORDER"
        const val PENDING_REQUEST_CODE = 0x01

        private const val REQUEST_CAPETURE_CODE = 1

        fun start(ctx: Context) {
            val intent = Intent(ctx, ScreenRecordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startActivity(intent)
        }
    }
}