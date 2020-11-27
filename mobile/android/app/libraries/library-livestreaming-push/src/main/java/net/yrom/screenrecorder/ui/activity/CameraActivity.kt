package net.yrom.screenrecorder.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.ImageFormat
import android.hardware.Camera
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.SurfaceHolder
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_camera.*
import net.ossrs.yasea.R
import net.yrom.screenrecorder.core.Packager
import net.yrom.screenrecorder.rtmp.RESFlvData
import net.yrom.screenrecorder.rtmp.RESFlvDataCollecter
import net.yrom.screenrecorder.task.RtmpStreamingSender
import net.yrom.screenrecorder.tools.LogTools
import java.io.IOException
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.experimental.and

@Suppress("DEPRECATION")
class CameraActivity : AppCompatActivity(), Camera.PreviewCallback, SurfaceHolder.Callback {
    private val mQuit = AtomicBoolean(false)
    private val mBufferInfo = MediaCodec.BufferInfo()
    private var mDataCollecter: RESFlvDataCollecter? = null
    private var streamingSender: RtmpStreamingSender? = null
    private var rtmpAddr: String? = null
    private val mWidth = 0
    private val mHeight = 0
    private val mBitRate = 0
    private val mDpi = 0
    private var mEncoder: MediaCodec? = null
    private var startTime: Long = 0
    private var encoderThread: VideoEncoderThread? = null
    private var mSurfaceHolder: SurfaceHolder? = null
    private var mCamera: Camera? = null
    private var mCameraParams: Camera.Parameters? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        mSurfaceHolder = svCamera!!.holder
        mSurfaceHolder?.setFixedSize(VIDEO_WIDTH, VIDEO_HEIGHT)
        mSurfaceHolder?.addCallback(this)
        try {
            prepareEncoder()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        encoderThread = VideoEncoderThread()

        btnPushToRtmp.setOnClickListener {
            onViewClicked()
        }
    }

    override fun onPreviewFrame(data: ByteArray, camera: Camera) {
        dataToCallback(data)
    }

    private fun dataToCallback(data: ByteArray) {
        encoderThread!!.put(data)
    }

    fun onViewClicked() {
        rtmpAddr = etRtmpAddress!!.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(rtmpAddr)) {
            Toast.makeText(this, "rtmp address cannot be null", Toast.LENGTH_SHORT).show()
            return
        }
        streamingSender = RtmpStreamingSender()
        streamingSender!!.sendStart(rtmpAddr)
        mDataCollecter = RESFlvDataCollecter { flvData, type -> streamingSender!!.sendFood(flvData, type) }
        encoderThread!!.start()
    }

    val status: Boolean
        get() = !mQuit.get()

    @Throws(IOException::class)
    private fun prepareEncoder() {
        val format = MediaFormat.createVideoFormat(MIME_TYPE, VIDEO_WIDTH, VIDEO_HEIGHT)
        format.setInteger(
            MediaFormat.KEY_COLOR_FORMAT,
            MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface
        )
        format.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate)
        format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE)
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL)
        Log.d(TAG, "created video format: $format")
        try {
            mEncoder = MediaCodec.createEncoderByType(MIME_TYPE)
            mEncoder!!.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            mEncoder!!.start()
        }catch (exception:java.lang.Exception){
            exception.printStackTrace()
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        openCamera(Camera.CameraInfo.CAMERA_FACING_BACK, holder)
    }

    private fun openCamera(cameraType: Int, holder: SurfaceHolder) {
        releaseCamera()
        try {
            mCamera = Camera.open(cameraType)
        } catch (e: Exception) {
            mCamera = null
            e.printStackTrace()
        }
        if (mCamera == null) {
            Toast.makeText(this, "无法开启摄像头", Toast.LENGTH_SHORT).show()
            return
        }
        mCameraParams = mCamera!!.parameters
        mCameraParams?.setPreviewSize(VIDEO_WIDTH, VIDEO_HEIGHT)
        mCameraParams?.setPreviewFormat(ImageFormat.NV21)
        mCamera!!.parameters = mCameraParams
        try {
            mCamera!!.setPreviewDisplay(holder)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mCamera!!.setPreviewCallback(this)
        mCamera!!.startPreview()
    }

    @Synchronized
    private fun releaseCamera() {
        if (mCamera != null) {
            try {
                mCamera!!.setPreviewCallback(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                mCamera!!.stopPreview()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            try {
                mCamera!!.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mCamera = null
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {}
    private inner class VideoEncoderThread : Thread() {
        private val rawframes = ConcurrentLinkedQueue<ByteArray>()
        fun put(videoFrame: ByteArray) {
            if (rawframes.size <= Companion.MAX_QUEUE_COUNT) {
                rawframes.add(videoFrame)
            }
        }

        private fun get(): ByteArray? {
            return if (!rawframes.isEmpty()) {
                rawframes.poll()
            } else null
        }

        override fun run() {
            while (!mQuit.get()) {
                val inputBuffers = mEncoder!!.inputBuffers
                val rawFrame = get()
                if (rawFrame != null) {
                    val inputBufferIndex = mEncoder!!.dequeueInputBuffer(-1)
                    if (inputBufferIndex >= 0) {
                        val inputBuffer = inputBuffers[inputBufferIndex]
                        inputBuffer.clear()
                        val rotateDegree = 90
                        val begin = System.currentTimeMillis()
                        //                        byte[] rotatedFrame;
//                        rotatedFrame = scaleOrRotateData(rawframe.data, rotateDegree, rawframe.isPPTVideoFrame);

                        // color space transform. 下列方法是SRS项目中的格式转换方法，暂时用于调试，效率不一定高, 经过测试，平均 1帧需要12毫秒左右来转换
//                        if (convertedYUVBytes == null || convertedYUVBytes.length != rotatedFrame.length) {
//                            convertedYUVBytes = new byte[rotatedFrame.length];
//                        }
//                        YuvUtil.convertYV12ToSpecifiedYUV420(rotatedFrame, convertedYUVBytes, vColor,
//                                DES_FRAME_WIDTH, DES_FRAME_HEIGHT);
                        inputBuffer.put(rawFrame, 0, rawFrame.size)
                        mEncoder!!.queueInputBuffer(inputBufferIndex, 0, rawFrame.size, mBufferInfo.presentationTimeUs * 1000, 0) //MS -> US
                    }
                } else continue
                val eobIndex = mEncoder!!.dequeueOutputBuffer(mBufferInfo, TIMEOUT_US.toLong())
                when (eobIndex) {
                    MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED -> LogTools.d("VideoSenderThread,MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED")
                    MediaCodec.INFO_TRY_AGAIN_LATER -> {
                    }
                    MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> {
                        LogTools.d(
                            "VideoSenderThread,MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:" +
                                    mEncoder!!.outputFormat.toString()
                        )
                        sendAVCDecoderConfigurationRecord(0, mEncoder!!.outputFormat)
                    }
                    else -> {
                        LogTools.d("VideoSenderThread,MediaCode,eobIndex=$eobIndex")
                        if (startTime == 0L) {
                            startTime = mBufferInfo.presentationTimeUs / 1000
                        }
                        /**
                         * we send sps pps already in INFO_OUTPUT_FORMAT_CHANGED
                         * so we ignore MediaCodec.BUFFER_FLAG_CODEC_CONFIG
                         */
                        if (mBufferInfo.flags != MediaCodec.BUFFER_FLAG_CODEC_CONFIG && mBufferInfo.size != 0) {
                            val realData = mEncoder!!.outputBuffers[eobIndex]
                            realData.position(mBufferInfo.offset + 4)
                            realData.limit(mBufferInfo.offset + mBufferInfo.size)
                            sendRealData(mBufferInfo.presentationTimeUs / 1000 - startTime, realData)
                        }
                        mEncoder!!.releaseOutputBuffer(eobIndex, false)
                    }
                }
            }
        }

        private fun sendAVCDecoderConfigurationRecord(tms: Long, format: MediaFormat) {
            val AVCDecoderConfigurationRecord = Packager.H264Packager.generateAVCDecoderConfigurationRecord(format)
            val packetLen = Packager.FLVPackager.FLV_VIDEO_TAG_LENGTH +
                    AVCDecoderConfigurationRecord.size
            val finalBuff = ByteArray(packetLen)
            Packager.FLVPackager.fillFlvVideoTag(
                finalBuff,
                0,
                true,
                true,
                AVCDecoderConfigurationRecord.size
            )
            System.arraycopy(
                AVCDecoderConfigurationRecord, 0,
                finalBuff, Packager.FLVPackager.FLV_VIDEO_TAG_LENGTH, AVCDecoderConfigurationRecord.size
            )
            val resFlvData = RESFlvData()
            resFlvData.droppable = false
            resFlvData.byteBuffer = finalBuff
            resFlvData.size = finalBuff.size
            resFlvData.dts = tms.toInt()
            resFlvData.flvTagType = RESFlvData.FLV_RTMP_PACKET_TYPE_VIDEO
            resFlvData.videoFrameType = RESFlvData.NALU_TYPE_IDR
            mDataCollecter!!.collect(resFlvData, RESFlvData.FLV_RTMP_PACKET_TYPE_VIDEO)
        }

        private fun sendRealData(tms: Long, realData: ByteBuffer) {
            val realDataLength = realData.remaining()
            val packetLen = Packager.FLVPackager.FLV_VIDEO_TAG_LENGTH +
                    Packager.FLVPackager.NALU_HEADER_LENGTH +
                    realDataLength
            val finalBuff = ByteArray(packetLen)
            realData[finalBuff, Packager.FLVPackager.FLV_VIDEO_TAG_LENGTH +
                    Packager.FLVPackager.NALU_HEADER_LENGTH, realDataLength]
            val frameType: Int = (finalBuff[Packager.FLVPackager.FLV_VIDEO_TAG_LENGTH + Packager.FLVPackager.NALU_HEADER_LENGTH] and 0x1F).toInt()
            Packager.FLVPackager.fillFlvVideoTag(
                finalBuff,
                0,
                false,
                frameType == 5,
                realDataLength
            )
            val resFlvData = RESFlvData()
            resFlvData.droppable = true
            resFlvData.byteBuffer = finalBuff
            resFlvData.size = finalBuff.size
            resFlvData.dts = tms.toInt()
            resFlvData.flvTagType = RESFlvData.FLV_RTMP_PACKET_TYPE_VIDEO
            resFlvData.videoFrameType = frameType
            mDataCollecter!!.collect(resFlvData, RESFlvData.FLV_RTMP_PACKET_TYPE_VIDEO)
        }

    }

    companion object {
        private val TAG = CameraActivity::class.java.simpleName
        private const val MAX_QUEUE_COUNT = 5

        // parameters for the encoder
        private const val MIME_TYPE = "video/avc" // H.264 Advanced Video Coding
        private const val FRAME_RATE = 30 // 30 fps
        private const val IFRAME_INTERVAL = 10 // 10 seconds between I-frames
        private const val TIMEOUT_US = 10000
        private const val VIDEO_WIDTH = 1280
        private const val VIDEO_HEIGHT = 720

        @JvmStatic
        fun launchActivity(ctx: Context) {
            val it = Intent(ctx, CameraActivity::class.java)
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ctx.startActivity(it)
        }
    }
}