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
import com.smart.library.base.setOnLayoutListener
import com.smart.library.util.STToastUtil
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

        magicView?.setOnProgressListener { if (it == 1f) STToastUtil.show("监听到动画停止") }
        magicView?.setOnLayoutListener {
            magicView?.setBitmap(bitmap)
        }
        btnStart.setOnClickListener {
            magicView?.visibility = View.VISIBLE
            magicView?.start()
        }
    }

    companion object {
        fun goTo(activity: Context?) {
            STActivity.start(activity, STMagicFragment::class.java)
        }
    }
}