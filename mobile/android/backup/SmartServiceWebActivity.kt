package com.smart.webview


import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.smart.activity.device.base.BaseActivity
import com.smart.BuildConfig
import com.smart.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


@Suppress("DEPRECATED_IDENTITY_EQUALS", "KotlinConstantConditions", "DEPRECATION")
class SmartServiceWebActivity : BaseActivity() {

    private lateinit var webView: WebView

    private val authorityOfFileProvider: String = "${BuildConfig.APPLICATION_ID}.fileprovider"
    private var filePathCallback: ValueCallback<Array<Uri>>? = null
    private var cameraPhotoPath: String? = null
    private var cameraVideoPath: String? = null


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_smart_service)
        findViewById<View>(R.id.back).setOnClickListener {
            finish()
        }
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.loadsImagesAutomatically = true
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.webViewClient = MyWebViewClient()
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                this@SmartServiceWebActivity.filePathCallback = filePathCallback
                val acceptTypes = fileChooserParams.acceptTypes
                if (acceptTypes.isNotEmpty()) {
                    val acceptType = acceptTypes[0]

                    if (acceptType == "video/*" || acceptType == "image/*") {
                        if (ContextCompat.checkSelfPermission(
                                this@SmartServiceWebActivity, Manifest.permission.CAMERA
                            ) !== PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                this@SmartServiceWebActivity,
                                arrayOf(Manifest.permission.CAMERA),
                                if (acceptType == "video/*") CAMERA_VIDEO_PERMISSION_REQUEST_CODE else CAMERA_PERMISSION_REQUEST_CODE
                            )
                            return true
                        } else {
                            if (acceptType == "image/*") {
                                openImageChooser()
                                return true
                            } else if (acceptType == "video/*") {
                                openVideoChooser()
                                return true
                            }
                        }
                    }
                }

                openFileChooser()
                return true
            }
        }
        val url = intent.getStringExtra(KEY_URL)
        if (!url.isNullOrBlank()) {
            webView.loadUrl(url)
        }
    }

    private fun openImageChooser() {
        // 创建用于调用相机的Intent
        var takePictureIntent: Intent? = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent!!.resolveActivity(packageManager) != null) {
            // 创建用于存储拍摄图片的文件
            var photoFile: File? = null
            try {
                photoFile = createImageFile()
                takePictureIntent.putExtra("PhotoPath", cameraPhotoPath)
            } catch (ex: IOException) {
                // 错误处理
                ex.printStackTrace()
            }

            if (photoFile != null) {
                cameraPhotoPath = "file:" + photoFile.absolutePath
                val photoURI = FileProvider.getUriForFile(
                    this, authorityOfFileProvider, photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            } else {
                takePictureIntent = null
            }
        }
        if (takePictureIntent != null) {
            startActivityForResult(takePictureIntent, FILE_CHOOSER_REQUEST_CODE)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // 创建图片文件名
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    private fun openVideoChooser() {
        // 创建用于调用相机录像的Intent
        var takeVideoIntent: Intent? = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (takeVideoIntent!!.resolveActivity(packageManager) != null) {
            // 创建用于存储录像文件
            var videoFile: File? = null
            try {
                videoFile = createVideoFile()
                takeVideoIntent.putExtra("VideoPath", cameraVideoPath)
            } catch (ex: IOException) {
                // 错误处理
                ex.printStackTrace()
            }

            if (videoFile != null) {
                cameraVideoPath = "file:" + videoFile.absolutePath
                val videoURI = FileProvider.getUriForFile(
                    this, authorityOfFileProvider, videoFile
                )
                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI)
            } else {
                takeVideoIntent = null
            }
        }

        if (takeVideoIntent != null) {
            startActivityForResult(takeVideoIntent, FILE_CHOOSER_REQUEST_CODE)
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createVideoFile(): File {
        // 创建视频文件名
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val videoFileName = "MP4_" + timeStamp + "_"
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile(videoFileName, ".mp4", storageDir)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            var results: Array<Uri>? = null
            if (resultCode == RESULT_OK) {
                if (data == null || data.data == null) {
                    if (cameraPhotoPath != null) {
                        results = arrayOf(Uri.parse(cameraPhotoPath))
                    }
                } else {
                    val dataString = data.dataString
                    if (dataString != null) {
                        results = arrayOf(Uri.parse(dataString))
                    }
                }
            }
            filePathCallback?.onReceiveValue(results)
            filePathCallback = null
        } else {
            if (filePathCallback != null) {
                filePathCallback?.onReceiveValue(null)
                filePathCallback = null
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE || requestCode == CAMERA_VIDEO_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (requestCode == CAMERA_VIDEO_PERMISSION_REQUEST_CODE) {
                    openVideoChooser() // 点击录像申请权限时同意授权
                } else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
                    openImageChooser() // 点击拍照申请权限时同意授权
                }
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.CAMERA
                    )
                ) {
                    showPermissionDeniedDialog() // 权限被拒绝，并且勾选了“不再询问”。
                } else {
                    Toast.makeText(this, "需要相机权限才能拍照和录屏。", Toast.LENGTH_SHORT).show()
                }
                if (filePathCallback != null) {
                    filePathCallback?.onReceiveValue(null)
                    filePathCallback = null
                }
            }
        }
    }


    private fun openFileChooser() {
        val takePictureIntent = Intent(Intent.ACTION_GET_CONTENT)
        takePictureIntent.setType("*/*")
        startActivityForResult(takePictureIntent, FILE_CHOOSER_REQUEST_CODE)
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this).setTitle("需要权限")
            .setMessage("拍照需要相机权限。请在设置中启用该权限。")
            .setPositiveButton("去设置") { _, _ -> openAppSettings() }
            .setNegativeButton("取消", null).show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }


    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private inner class MyWebViewClient : WebViewClient() {
        @Deprecated("Deprecated in Java")
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (isImageUrl(url)) {
                // 如果是图片链接，调用下载方法
                downloadImage(this@SmartServiceWebActivity, url)
                return true
            } else {
                // 如果不是图片链接，继续加载URL
                return false
            }
        }

        private fun isImageUrl(url: String): Boolean {
            // 检查URL是否为图片格式
            return (url.endsWith(".jpg") || url.endsWith(".jpeg") || url.endsWith(".png") || url.endsWith(
                ".gif"
            ) || url.contains(".jpg?") || url.contains(".jpeg?") || url.contains(".png?") || url.contains(
                ".gif?"
            ))
        }

        private fun downloadImage(context: Context, url: String) {
            val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(url))
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS, url.substring(url.lastIndexOf("/") + 1)
            )
            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val downloadManager: DownloadManager =
                context.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
            Toast.makeText(context, "正在下载...", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val KEY_URL = "KEY_URL"

        private const val FILE_CHOOSER_REQUEST_CODE: Int = 1
        private const val CAMERA_PERMISSION_REQUEST_CODE: Int = 2
        private const val CAMERA_VIDEO_PERMISSION_REQUEST_CODE: Int = 3

        @JvmStatic
        @JvmOverloads
        fun open(
            context: Context,
            url: String
        ) {
            val intent = Intent(context, SmartServiceWebActivity::class.java)
            intent.putExtra(KEY_URL, url)
            context.startActivity(intent)
        }
    }
}
