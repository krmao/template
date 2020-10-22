package net.yrom.screenrecorder.ui.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_launch.*
import net.ossrs.yasea.R
import net.yrom.screenrecorder.ui.activity.CameraActivity.Companion.launchActivity

class ScreenRecorderLaunchActivity : AppCompatActivity() {
    var authorized = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)
        verifyPermissions()

        btnScreenRecord.setOnClickListener {
            ScreenRecordActivity.start(this)
        }
        btnCameraRecord.setOnClickListener {
            launchActivity(this)
        }
    }

    fun verifyPermissions() {
        val CAMERA_permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val RECORD_AUDIO_permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        val WRITE_EXTERNAL_STORAGE_permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        authorized = if (CAMERA_permission != PackageManager.PERMISSION_GRANTED || RECORD_AUDIO_permission != PackageManager.PERMISSION_GRANTED || WRITE_EXTERNAL_STORAGE_permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                PERMISSIONS_STREAM,
                REQUEST_STREAM
            )
            false
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_STREAM) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                authorized = true
            }
        }
    }


    companion object {
        private const val REQUEST_STREAM = 1
        private val PERMISSIONS_STREAM = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        fun newIntent(context: Context?, rtmpPath: String?): Intent? {
            val intent = Intent(context, ScreenRecorderLaunchActivity::class.java)
            intent.putExtra("rtmpPath", rtmpPath)
            return intent
        }

        fun intentTo(context: Context, rtmpPath: String?) {
            context.startActivity(newIntent(context, rtmpPath))
        }
    }
}