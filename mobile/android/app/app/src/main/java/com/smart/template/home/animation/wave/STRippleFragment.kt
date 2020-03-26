package com.smart.template.home.animation.wave

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.base.toPxFromDp
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
            innerWaterCenterWaveView.setOnClickListener {
                innerWaterCenterWaveView.setColor(Color.parseColor("#FFFFFF"))
                innerWaterCenterWaveView.fromRadiusPx = 10f.toPxFromDp()
                innerWaterCenterWaveView.maxRadiusRateOnMinEdge = 1f
                innerWaterCenterWaveView.fromAlpha = 0.50f
                innerWaterCenterWaveView.toAlpha = 0f
                innerWaterCenterWaveView.durationMs = 2000
                innerWaterCenterWaveView.createCircleSpeedMs = 800
                innerWaterCenterWaveView.start()
            }
            innerWaterCenterWaveView.performClick()
        }
    }

    companion object {
        fun goTo(activity: Context?) {
            STActivity.start(activity, STRippleFragment::class.java)
        }
    }
}