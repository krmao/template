package com.smart.library.map.layer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STBaseFragment
import com.smart.library.map.R
import com.smart.library.support.constraint.motion.MotionLayout

class STPanelFragment : STBaseFragment() {

    private lateinit var touchMotionLayout: SignleTouchMotionLayout

    override fun onAttach(context: Context?) {
        super.onAttach(context)/*
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true, 0.2f)
                .init()*/
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.st_panel_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        touchMotionLayout = view.findViewById(R.id.bottom_motionlayout)
        touchMotionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(motionLayout: MotionLayout, i: Int, i1: Int) {
            }

            override fun onTransitionChange(motionLayout: MotionLayout, i: Int, i1: Int, v: Float) {
                val progress = Math.abs(v)
                if (progress > SignleTouchMotionLayout.PROGRESS_BOTTOM) {
                } else {
                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {
            }

            override fun onTransitionTrigger(motionLayout: MotionLayout, i: Int, b: Boolean, v: Float) {

            }
        })
        setHasMiddle(true)
    }

    fun setHasMiddle(hasMiddle: Boolean) {
        touchMotionLayout.setHasMiddle(hasMiddle)
    }
}
