package com.smart.library.widget.roundlayout

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import android.widget.RelativeLayout
import android.widget.TextView

@Suppress("unused")
class STRoundTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : androidx.appcompat.widget.AppCompatTextView(context, attrs, defStyleAttr), STRoundDelegate {

    //region round layout support
    private val roundLayoutHelper: STRoundLayoutHelper by lazy { STRoundLayoutHelper(this) }
    override fun setBackgroundColor(color: Int) {
        roundLayoutHelper.setBackgroundColor(color)
    }

    override fun dispatchDraw(canvas: Canvas) {
        roundLayoutHelper.dispatchDraw(canvas)
        super.dispatchDraw(canvas)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutlineProvider(): ViewOutlineProvider {
        return roundLayoutHelper.getOutlineProvider()
    }

    //endregion
    init {
        roundLayoutHelper.render(attrs)
    }

}
