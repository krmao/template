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

@Suppress("unused")
class STMagicFragment : STBaseFragment() {

    private val bitmap: Bitmap by lazy { BitmapFactory.decodeResource(resources, R.drawable.st_beauty) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.st_magic_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        /**
         * 控制 bitmap 容器大小, 可以控制缩放与留白等
         */
        magicView?.setBitmap(
            bitmap,
            meshWidth = 50,
            meshHeight = 50,
            bitmapContainerWidth = 120f.toPxFromDp() * (bitmap.width / bitmap.height.toFloat()),
            bitmapContainerHeight = 120f.toPxFromDp()
        )

        btnLeft.setOnClickListener {
            magicView?.setLineRatio(leftLintToXRatio = 0.1f, rightLineToXRatio = 0.15f)
            magicView?.start()
        }
        btnCenter.setOnClickListener {
            magicView?.setLineRatio(leftLintToXRatio = 2.3f, rightLineToXRatio = 2.25f)
            magicView?.start()
        }
        btnRight.setOnClickListener {
            magicView?.setLineRatio(leftLintToXRatio = 3.8f, rightLineToXRatio = 3.85f)
            magicView?.start()
        }
    }

    companion object {
        fun goTo(activity: Context?) {
            STActivity.start(activity, STMagicFragment::class.java)
        }
    }
}