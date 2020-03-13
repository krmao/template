package com.smart.template.home.animation.magic.captureanim

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.base.toPxFromDp
import com.smart.library.util.STLogUtil
import com.smart.library.util.animation.STInterpolatorFactory
import com.smart.template.R
import kotlinx.android.synthetic.main.st_magic_fragment.*

@Suppress("unused", "DEPRECATION")
class STMagicFragment : STBaseFragment() {

    private val bitmap: Bitmap by lazy { BitmapFactory.decodeResource(resources, R.drawable.st_beauty) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.st_magic_fragment, container, false)
    }

    private var cachedBitmap: Bitmap? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val innerWaterCenterWaveView = rippleLineView
        if (innerWaterCenterWaveView != null) {
            innerWaterCenterWaveView.duration = 600 // 0 - 300 - 600 三个圈
            innerWaterCenterWaveView.setStyle(Paint.Style.FILL) // STROKE
            innerWaterCenterWaveView.speed = 300
            innerWaterCenterWaveView.setColor(Color.parseColor("#22555555"))
            innerWaterCenterWaveView.interpolator = STInterpolatorFactory.createDecelerateInterpolator()
            innerWaterCenterWaveView.initialRadius = 20.toPxFromDp().toFloat()
            innerWaterCenterWaveView.maxRadiusRate = 0.8f
        }

        seekBar?.max = 1000
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                magicView?.setProgress(progress.toFloat() / 1000f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        magicView?.setOnProgressListener {
            STLogUtil.w(STMagicView.TAG, "animation finished")
        }

        btnLeft.setOnClickListener {
            magicView?.setLineRatio(leftLintToXRatio = 0.1f, rightLineToXRatio = 0.15f)
            magicView?.start()
        }
        btnCenter.setOnClickListener {
            magicView?.setLineRatio(leftLintToXRatio = 0.475f, rightLineToXRatio = 0.525f)
            magicView?.start()
        }

        btnRight.setOnClickListener {
            innerWaterCenterWaveView.start()

            // 销毁旧的缓存 bitmap
            if (cachedBitmap != null) {
                imageView.destroyDrawingCache()
                imageView.isDrawingCacheEnabled = false
            }

            // 创建新的缓存 bitmap
            imageView.isDrawingCacheEnabled = true
            imageView.buildDrawingCache()
            cachedBitmap = Bitmap.createBitmap(imageView.getDrawingCache(true))

            val innerCachedBitmap = cachedBitmap

            STLogUtil.w(STMagicView.TAG, "innerCachedBitmap==null?${innerCachedBitmap == null}")
            STLogUtil.w(STMagicView.TAG, "magicView.width=${magicView.width}, magicView.height=${magicView.height}")
            STLogUtil.w(STMagicView.TAG, "imageView.width=${imageView.width}, btnRight.height=${imageView.height}")

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
                magicView.setLineRatio(leftLintToXRatio = 0.85f, rightLineToXRatio = 0.9f, leftLineToYRatio = 1.2f, rightLineToYRatio = 1.2f)
                magicView.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                magicView.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cachedBitmap?.recycle()
        cachedBitmap = null
        imageView?.destroyDrawingCache()
    }

    companion object {
        fun goTo(activity: Context?) {
            STActivity.start(activity, STMagicFragment::class.java)
        }
    }
}