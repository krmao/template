package com.smart.template.home.animation.magic.captureanim

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.base.toPxFromDp
import com.smart.library.util.STLogUtil
import com.smart.template.R
import kotlinx.android.synthetic.main.st_magic_fragment.*

@Suppress("unused", "DEPRECATION")
class STMagicFragment : STBaseFragment() {

    private val bitmap: Bitmap by lazy { BitmapFactory.decodeResource(resources, R.drawable.st_beauty) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.st_magic_fragment, container, false)
    }

    private var trackingTouch: Boolean = false
    private var cachedBitmap: Bitmap? = null
    private val progressMax: Float = 1000f
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val innerWaterCenterWaveView = rippleLineView
        if (innerWaterCenterWaveView != null) {
            innerWaterCenterWaveView.initialRadiusPx = 20.toPxFromDp().toFloat()
            innerWaterCenterWaveView.maxRadiusRateOnMinEdge = 0.8f
        }

        seekBar.max = progressMax.toInt()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (trackingTouch) {
                    magicView.setProgress(progress.toFloat() / progressMax)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                trackingTouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                trackingTouch = false
            }
        })

        magicView.setOnProgressListener {
            seekBar.progress = (progressMax * it).toInt()
            STLogUtil.w(STMagicView.TAG, "setOnProgressListener $it")
        }

        btnLeft.setOnClickListener {
            magicView.setLineRatio(leftLintToXRatio = 0.1f, rightLineToXRatio = 0.15f)
            magicView.start()
        }
        btnCenter.setOnClickListener {
            magicView.setLineRatio(leftLintToXRatio = 0.475f, rightLineToXRatio = 0.525f)
            magicView.start()
        }
        btnReset.setOnClickListener {
            seekBar.progress = 0
            magicView.reset()
            innerWaterCenterWaveView.stop()
        }
        btnMeshBottom.setOnClickListener {
            magicView.enableBottomLineMesh(!magicView.enableBottomLineMesh())
        }

        btnRight.setOnClickListener {
            val startTime = System.currentTimeMillis()
            STLogUtil.w(STMagicView.TAG, "startTime=$startTime")
            innerWaterCenterWaveView.start()
            STLogUtil.w(STMagicView.TAG, "startTime1=${System.currentTimeMillis()}")
            // 销毁旧的缓存 bitmap
            if (cachedBitmap == null) {
                // 创建新的缓存 bitmap
                imageView.isDrawingCacheEnabled = true
                imageView.buildDrawingCache()
                cachedBitmap = Bitmap.createBitmap(imageView.getDrawingCache(true))
            }
            STLogUtil.w(STMagicView.TAG, "startTime2=${System.currentTimeMillis()}")
            val innerCachedBitmap = cachedBitmap
            if (innerCachedBitmap != null) {
                /**
                 * 控制 bitmap 容器大小, 可以控制缩放与留白等
                 */
                magicView.setBitmap(
                    innerCachedBitmap,
                    meshWidth = 50,
                    meshHeight = 50,
                    bitmapContainerWidth = imageView.width.toFloat(),
                    bitmapContainerHeight = 230f.toPxFromDp()
                )
                STLogUtil.w(STMagicView.TAG, "startTime3=${System.currentTimeMillis()}")
                magicView.setLineRatio(leftLintToXRatio = 0.85f, rightLineToXRatio = 0.9f, leftLineToYRatio = 1.2f, rightLineToYRatio = 1.2f)
                magicView.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                magicView.start()
                STLogUtil.w(STMagicView.TAG, "startTime4=${System.currentTimeMillis()}")
            }
        }
    }

    override fun onDestroy() {
        imageView?.destroyDrawingCache()
        cachedBitmap?.recycle()
        cachedBitmap = null
        super.onDestroy()
    }

    companion object {
        fun goTo(activity: Context?) {
            STActivity.start(activity, STMagicFragment::class.java)
        }
    }
}