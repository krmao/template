package com.smart.template.home.animation.wave

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.template.R

@Suppress("unused")
class STRippleFragment : STBaseFragment() {

    private var rippleLineView: STRippleLineView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.st_ripple_fragment, container, false)
        rippleLineView = contentView.findViewById<STRippleLineView>(R.id.rippleLineView)
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val innerWaterCenterWaveView = rippleLineView
        if (innerWaterCenterWaveView != null) {
            innerWaterCenterWaveView.durationMs = 2000
            innerWaterCenterWaveView.setStyle(Paint.Style.FILL) // STROKE
            innerWaterCenterWaveView.createCircleSpeedMs = 400
            innerWaterCenterWaveView.setColor(Color.BLUE)
            innerWaterCenterWaveView.interpolator = LinearOutSlowInInterpolator() // AccelerateInterpolator(1.2f) //
            innerWaterCenterWaveView.start()
            innerWaterCenterWaveView.postDelayed(
                {
                    innerWaterCenterWaveView.stop()
                },
                2000
            )
        }
    }

    companion object {

        fun goTo(activity: Context?) {
            STActivity.start(activity, STRippleFragment::class.java)
        }

    }

}