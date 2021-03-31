package com.smart.library.widget.roundable

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.util.AttributeSet
import android.view.ViewOutlineProvider
import android.widget.LinearLayout
import androidx.annotation.Keep

@Suppress("unused")
@Keep
class STRoundableLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr), STRoundableDelegate {

    //region round layout support
    private val roundLayoutHelper: STRoundableLayoutHelper by lazy { STRoundableLayoutHelper(this) }
    override fun setBackgroundColor(color: Int) {
        roundLayoutHelper.setBackgroundColor(color)
    }

    override fun dispatchDraw(canvas: Canvas) {
        roundLayoutHelper.dispatchDraw(canvas)
        super.dispatchDraw(canvas)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutlineProvider(): ViewOutlineProvider {
        return roundLayoutHelper.getOutlineProvider() ?: super.getOutlineProvider()
    }

    //endregion
    init {
        roundLayoutHelper.render(attrs)
    }

}
