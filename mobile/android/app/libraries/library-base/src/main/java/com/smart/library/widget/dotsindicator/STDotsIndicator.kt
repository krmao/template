package com.smart.library.widget.dotsindicator

import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.Keep
import com.smart.library.R
import com.smart.library.util.STLogUtil

@Keep
@Suppress("DEPRECATION", "MemberVisibilityCanBePrivate")
class STDotsIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : STBaseDotsIndicator(context, attrs, defStyleAttr) {

    private var dotsWidthFactor: Float = DEFAULT_WIDTH_FACTOR

    // 滚动过后的上一次选中的 dot 是否保留选中色, false 切换为非选中色, true 保留选中色(滚动到最后所有的 dot 都是选中色)
    private var progressMode: Boolean = false
    private val argbEvaluator = ArgbEvaluator()
    private val dotsContainerLinearLayout: LinearLayout by lazy { LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL } }
    private var selectedDotColor: Int = DEFAULT_POINT_COLOR

    init {
        addView(dotsContainerLinearLayout, WRAP_CONTENT, WRAP_CONTENT)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.STDotsIndicator)
            dotsColor = typedArray.getColor(R.styleable.STDotsIndicator_stDotsColor, DEFAULT_POINT_COLOR)
            dotsSize = typedArray.getDimension(R.styleable.STDotsIndicator_stDotsSize, dotsSize)
            dotsCornerRadius = typedArray.getDimension(R.styleable.STDotsIndicator_stDotsCornerRadius, dotsCornerRadius)
            dotsSpacing = typedArray.getDimension(R.styleable.STDotsIndicator_stDotsSpacing, dotsSpacing)
            selectedDotColor = typedArray.getColor(R.styleable.STDotsIndicator_stSelectedDotColor, DEFAULT_POINT_COLOR)
            progressMode = typedArray.getBoolean(R.styleable.STDotsIndicator_stProgressMode, progressMode)

            dotsWidthFactor = typedArray.getFloat(R.styleable.STDotsIndicator_stDotsWidthFactor, DEFAULT_WIDTH_FACTOR)
            if (dotsWidthFactor < 1) dotsWidthFactor = 2.5f

            typedArray.recycle()
        }

        if (isInEditMode) {
            addDots(5)
            refreshDots()
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun addDot(index: Int) {
        val dotView = LayoutInflater.from(context).inflate(R.layout.st_indicator_dots_dot_layout, this, false)
        val dotImageView = dotView.findViewById<ImageView>(R.id.dotImageView)
        val dotImageViewParams = dotImageView.layoutParams as RelativeLayout.LayoutParams

        if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN_MR1) dotView.layoutDirection = View.LAYOUT_DIRECTION_LTR

        dotImageViewParams.height = dotsSize.toInt()
        dotImageViewParams.width = dotImageViewParams.height
        dotImageViewParams.setMargins(dotsSpacing.toInt(), 0, dotsSpacing.toInt(), 0)
        val background = STDotsGradientDrawable()
        background.cornerRadius = dotsCornerRadius
        if (isInEditMode) {
            background.setColor(if (0 == index) selectedDotColor else dotsColor)
        } else {
            val localPage = pager
            if (localPage != null) {
                background.setColor(if (localPage.currentItem == index) selectedDotColor else dotsColor)
            }
        }
        dotImageView.setBackgroundDrawable(background)

        dotView.setOnClickListener {
            val localPage = pager
            if (localPage != null) {
                if (dotsClickable && index < localPage.count) {
                    localPage.setCurrentItem(index, true)
                }
            }
        }

        dotsImageViewList.add(dotImageView)
        dotsContainerLinearLayout.addView(dotView)
    }

    override fun removeDot(index: Int) {
        dotsContainerLinearLayout.removeViewAt(childCount - 1)
        dotsImageViewList.removeAt(dotsImageViewList.size - 1)
    }

    override fun buildOnPageChangedListener(): OnPageChangeListenerHelper {
        return object : OnPageChangeListenerHelper() {
            override fun onPageScrolled(selectedPosition: Int, nextPosition: Int, positionOffset: Float) {
                STLogUtil.w(TAG, "onPageScrolled selectedPosition=$selectedPosition, nextPosition=$nextPosition, positionOffset=$positionOffset")
                val selectedDotImageView = dotsImageViewList[selectedPosition]
                val selectedDotImageViewWidth = (dotsSize + dotsSize * (dotsWidthFactor - 1) * (1 - positionOffset)).toInt()
                setDotImageViewWidth(selectedDotImageView, selectedDotImageViewWidth)

                if (isNextPositionExists(nextPosition, dotsImageViewList)) {
                    val nextDotImageView = dotsImageViewList[nextPosition]
                    val nextDotImageViewWidth = (dotsSize + dotsSize * (dotsWidthFactor - 1) * positionOffset).toInt()
                    setDotImageViewWidth(nextDotImageView, nextDotImageViewWidth)

                    val selectedDotImageViewBackground = selectedDotImageView.background as STDotsGradientDrawable
                    val nextDotImageViewBackground = nextDotImageView.background as STDotsGradientDrawable

                    if (selectedDotColor != dotsColor) {
                        val selectedColor = argbEvaluator.evaluate(positionOffset, selectedDotColor, dotsColor) as Int
                        val nextColor = argbEvaluator.evaluate(positionOffset, dotsColor, selectedDotColor) as Int

                        nextDotImageViewBackground.setColor(nextColor)

                        if (progressMode && selectedPosition <= (pager?.currentItem ?: 0)) {
                            selectedDotImageViewBackground.setColor(selectedDotColor)
                        } else {
                            selectedDotImageViewBackground.setColor(selectedColor)
                        }
                    }
                }

                invalidate()
            }

            override fun resetPosition(position: Int) {
                setDotImageViewWidth(dotsImageViewList[position], dotsSize.toInt())
                refreshDotColor(position)
            }

            override val pageCount: Int get() = dotsImageViewList.size
        }
    }

    override fun refreshDotColor(index: Int) {
        val dotImageView = dotsImageViewList[index]
        val dotImageViewBackgroundDrawable = dotImageView.background as STDotsGradientDrawable

        if (index == (pager?.currentItem ?: 0) || progressMode && index < (pager?.currentItem ?: 0)) {
            dotImageViewBackgroundDrawable.setColor(selectedDotColor)
        } else {
            dotImageViewBackgroundDrawable.setColor(dotsColor)
        }

        dotImageView.setBackgroundDrawable(dotImageViewBackgroundDrawable)
        dotImageView.invalidate()
    }

    class STDotsGradientDrawable : GradientDrawable()

    companion object {
        const val DEFAULT_WIDTH_FACTOR = 2.5f
    }
}