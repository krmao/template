package com.smart.library.widget.titlebar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import androidx.core.widget.ContentLoadingProgressBar
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.smart.library.R
import com.smart.library.util.STCustomViewUtil
import com.smart.library.util.STSystemUtil
import kotlinx.android.synthetic.main.st_widget_titlebar.view.*

@Suppress("MemberVisibilityCanPrivate", "MemberVisibilityCanBePrivate")
/**
 * TitleView样式一
 * “左0--左1(hide)--Title--右1(hide)--右0”
 * “图片、颜色、文字”
 */
class STTitleBar(val mContext: Context, attrs: AttributeSet?) : RelativeLayout(mContext, attrs) {

    companion object {
        @JvmStatic
        var DEFAULT_TEXT_SIZE = 18f
        @JvmStatic
        var DEFAULT_TEXT_COLOR = Color.parseColor("#333333")
        @JvmStatic
        var DEFAULT_BACKGROUND_COLOR = Color.parseColor("#FAFBFC")
    }

    val titleText: TextView by lazy { title_text }
    val left0BgView: RelativeLayout by lazy { left0_bg_view }
    val left0Btn: TextView by lazy { left0_btn }
    val left1BgView: RelativeLayout by lazy { left1_bg_view }
    val left1Btn: TextView by lazy { left1_btn }
    val right0BgView: RelativeLayout by lazy { right0_bg_view }
    val right0Btn: TextView by lazy { right0_btn }
    val right1BgView: RelativeLayout by lazy { right1_bg_view }
    val progressBar: ContentLoadingProgressBar by lazy { progress_bar }
    val right1Btn: TextView  by lazy { right1_btn }

    private val mScaledDensity by lazy { resources.displayMetrics.scaledDensity }
    private var mIsLeft0Set = false

    constructor(context: Context) : this(context, null)

    init {
        LayoutInflater.from(mContext).inflate(R.layout.st_widget_titlebar, this, true)

        val iconPadding = resources.getDimensionPixelSize(R.dimen.st_titlebar_icon_padding)

        if (attrs != null) {
            val typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.STTitleBar)
            setBackground(this, R.styleable.STTitleBar_android_background, DEFAULT_BACKGROUND_COLOR, -1, typedArray)

            //title
            setText(titleText, R.styleable.STTitleBar_stTitleText, null, typedArray)
            setTextColor(titleText, R.styleable.STTitleBar_stTitleTxtColor, DEFAULT_TEXT_COLOR, typedArray)
            setTextSize(titleText, R.styleable.STTitleBar_stTitleSize, DEFAULT_TEXT_SIZE, typedArray)
            setTextAppearance(titleText, R.styleable.STTitleBar_stTitleAppearance, typedArray)
            //left0
            setBackground(left0BgView, R.styleable.STTitleBar_stLeft0BgViewBackground, -1, R.drawable.st_selector, typedArray)
            setLayoutParams(left0BgView, left0Btn, R.styleable.STTitleBar_stLeft0BgWidth, typedArray)
            setBgPadding(left0BgView, R.styleable.STTitleBar_stLeft0BgViewPadding, R.styleable.STTitleBar_stLeft0BgPaddingLeft, R.styleable.STTitleBar_stLeft0BgPaddingRight, R.styleable.STTitleBar_stLeft0BgPaddingTop, R.styleable.STTitleBar_stLeft0BgPaddingBottom, iconPadding, typedArray)
            left0BgView.visibility = typedArray.getInt(R.styleable.STTitleBar_stLeft0Visible, View.VISIBLE)
            setTextAppearance(left0Btn, R.styleable.STTitleBar_stLeft0Appearance, typedArray)
            @Suppress("DEPRECATION")
            val transparentColor = resources.getColor(R.color.finalTransparent)
            setBackground(left0Btn, R.styleable.STTitleBar_stLeft0Background, transparentColor, -1, typedArray)
            setText(left0Btn, R.styleable.STTitleBar_stLeft0Text, null, typedArray)
            setTextColor(left0Btn, R.styleable.STTitleBar_stLeft0TextColor, DEFAULT_TEXT_COLOR, typedArray)
            setTextSize(left0Btn, R.styleable.STTitleBar_stLeft0TextSize, DEFAULT_TEXT_SIZE, typedArray)
            //left1
            setBackground(left1BgView, R.styleable.STTitleBar_stLeft1BgViewBackground, -1, R.drawable.st_selector, typedArray)
            setLayoutParams(left1BgView, left1Btn, R.styleable.STTitleBar_stLeft1BgWidth, typedArray)
            setBgPadding(left1BgView, R.styleable.STTitleBar_stLeft1BgViewPadding, R.styleable.STTitleBar_stLeft1BgPaddingLeft, R.styleable.STTitleBar_stLeft1BgPaddingRight, R.styleable.STTitleBar_stLeft1BgPaddingTop, R.styleable.STTitleBar_stLeft1BgPaddingBottom, iconPadding, typedArray)
            left1BgView.visibility = typedArray.getInt(R.styleable.STTitleBar_stLeft1Visible, View.INVISIBLE)
            setTextAppearance(left1Btn, R.styleable.STTitleBar_stLeft1Appearance, typedArray)
            setBackground(left1Btn, R.styleable.STTitleBar_stLeft1Background, transparentColor, -1, typedArray)
            setText(left1Btn, R.styleable.STTitleBar_stLeft1Text, null, typedArray)
            setTextColor(left1Btn, R.styleable.STTitleBar_stLeft1TextColor, DEFAULT_TEXT_COLOR, typedArray)
            setTextSize(left1Btn, R.styleable.STTitleBar_stLeft1TextSize, DEFAULT_TEXT_SIZE, typedArray)
            //right0
            setBackground(right0BgView, R.styleable.STTitleBar_stRight0BgViewBackground, -1, R.drawable.st_selector, typedArray)
            setLayoutParams(right0BgView, right0Btn, R.styleable.STTitleBar_stRight0BgWidth, typedArray)
            setBgPadding(right0BgView, R.styleable.STTitleBar_stRight0BgViewPadding, R.styleable.STTitleBar_stRight0BgPaddingLeft, R.styleable.STTitleBar_stRight0BgPaddingRight, R.styleable.STTitleBar_stRight0BgPaddingTop, R.styleable.STTitleBar_stRight0BgPaddingBottom, iconPadding, typedArray)
            right0BgView.visibility = typedArray.getInt(R.styleable.STTitleBar_stRight0Visible, View.INVISIBLE)
            setTextAppearance(right0Btn, R.styleable.STTitleBar_stRight0Appearance, typedArray)
            setBackground(right0Btn, R.styleable.STTitleBar_stRight0Background, transparentColor, -1, typedArray)
            setText(right0Btn, R.styleable.STTitleBar_stRight0Text, null, typedArray)
            setTextColor(right0Btn, R.styleable.STTitleBar_stRight0TextColor, DEFAULT_TEXT_COLOR, typedArray)
            setTextSize(right0Btn, R.styleable.STTitleBar_stRight0TextSize, DEFAULT_TEXT_SIZE, typedArray)
            //right1
            setBackground(right1BgView, R.styleable.STTitleBar_stRight1BgViewBackground, -1, R.drawable.st_selector, typedArray)
            setLayoutParams(right1BgView, right1Btn, R.styleable.STTitleBar_stRight1BgWidth, typedArray)
            setBgPadding(right1BgView, R.styleable.STTitleBar_stRight1BgViewPadding, R.styleable.STTitleBar_stRight1BgPaddingLeft, R.styleable.STTitleBar_stRight1BgPaddingRight, R.styleable.STTitleBar_stRight1BgPaddingTop, R.styleable.STTitleBar_stRight1BgPaddingBottom, iconPadding, typedArray)
            right1BgView.visibility = typedArray.getInt(R.styleable.STTitleBar_stRight1Visible, View.INVISIBLE)
            setTextAppearance(right1Btn, R.styleable.STTitleBar_stRight1Appearance, typedArray)
            setBackground(right1Btn, R.styleable.STTitleBar_stRight1Background, transparentColor, -1, typedArray)
            setText(right1Btn, R.styleable.STTitleBar_stRight1Text, null, typedArray)
            setTextColor(right1Btn, R.styleable.STTitleBar_stRight1TextColor, DEFAULT_TEXT_COLOR, typedArray)
            setTextSize(right1Btn, R.styleable.STTitleBar_stRight1TextSize, DEFAULT_TEXT_SIZE, typedArray)

            progressBar.visibility = typedArray.getInt(R.styleable.STTitleBar_stProgressVisible, View.GONE)
            progressBar.progressDrawable = STCustomViewUtil.getDrawable(typedArray, R.styleable.STTitleBar_stProgressDrawable, -1, R.drawable.st_titlebar_progressbar_horizontal_progress_drawable)


            if (!mIsLeft0Set) {
                setBackground(left0Btn, R.styleable.STTitleBar_stLeft0Background, transparentColor, R.drawable.st_menu_back_gray, typedArray)
            }

            typedArray.recycle()
        }
        left0BgView.setOnClickListener { STSystemUtil.sendKeyDownEventBack(context) }
    }

    private fun setLayoutParams(bgView: View, childView: View, index: Int, typedArray: TypedArray) = try {
        val layoutParams = bgView.layoutParams as LayoutParams
        var configWidth = typedArray.getDimensionPixelSize(index, -3).toFloat()
        if (configWidth == LayoutParams.WRAP_CONTENT.toFloat() || configWidth >= 0)
            layoutParams.width = configWidth.toInt()
        else if (configWidth == -3f) {
            val resId = typedArray.getResourceId(index, -1)
            if (resId != -1) {
                configWidth = resources.getDimensionPixelSize(resId).toFloat()
                layoutParams.width = configWidth.toInt()
            }
        }
        //layoutParams.width = if (configWidth == RelativeLayout.LayoutParams.WRAP_CONTENT.toFloat() || configWidth >= 0) layoutParams.width else RelativeLayout.LayoutParams.WRAP_CONTENT
        bgView.layoutParams = layoutParams
        if (layoutParams.width == LayoutParams.WRAP_CONTENT) {
        } else {
            val childLayoutParams = childView.layoutParams as LayoutParams
            childLayoutParams.width = LayoutParams.MATCH_PARENT
            childView.layoutParams = childLayoutParams
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun setBgPadding(bgView: View, indexPadding: Int, indexLeft: Int, indexRight: Int, indexTop: Int, indexBottom: Int, _defaultPadding: Int, typedArray: TypedArray) {
        var defaultPadding = _defaultPadding
        try {
            var padding = typedArray.getDimensionPixelSize(indexPadding, -10000).toFloat()
            var paddingLeft = typedArray.getDimensionPixelSize(indexLeft, -10000).toFloat()
            var paddingRight = typedArray.getDimensionPixelSize(indexRight, -10000).toFloat()
            var paddingTop = typedArray.getDimensionPixelSize(indexTop, -10000).toFloat()
            var paddingBottom = typedArray.getDimensionPixelSize(indexBottom, -10000).toFloat()
            if (padding == -10000f) {
                val resId = typedArray.getResourceId(indexPadding, -10000)
                if (resId != -10000) {
                    padding = resources.getDimensionPixelSize(resId).toFloat()
                }
            }
            if (paddingLeft == -10000f) {
                val resId = typedArray.getResourceId(indexLeft, -10000)
                if (resId != -10000) {
                    paddingLeft = resources.getDimensionPixelSize(resId).toFloat()
                }
            }
            if (paddingRight == -10000f) {
                val resId = typedArray.getResourceId(indexRight, -10000)
                if (resId != -10000) {
                    paddingRight = resources.getDimensionPixelSize(resId).toFloat()
                }
            }
            if (paddingTop == -10000f) {
                val resId = typedArray.getResourceId(indexTop, -10000)
                if (resId != -10000) {
                    paddingTop = resources.getDimensionPixelSize(resId).toFloat()
                }
            }
            if (paddingBottom == -10000f) {
                val resId = typedArray.getResourceId(indexBottom, -10000)
                if (resId != -10000) {
                    paddingBottom = resources.getDimensionPixelSize(resId).toFloat()
                }
            }
            defaultPadding = if (padding == -10000f) defaultPadding else padding.toInt()
            bgView.setPadding(
                if (paddingLeft == -10000f) defaultPadding else paddingLeft.toInt(),
                if (paddingTop == -10000f) defaultPadding else paddingTop.toInt(),
                if (paddingRight == -10000f) defaultPadding else paddingRight.toInt(),
                if (paddingBottom == -10000f) defaultPadding else paddingBottom.toInt())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setBackground(view: View, index: Int, defaultColor: Int, defaultRes: Int, typedArray: TypedArray) = try {
        if (defaultColor != -1)
            view.setBackgroundColor(defaultColor)
        if (defaultRes != -1)
            view.setBackgroundResource(defaultRes)

        val drawable = typedArray.getDrawable(index)
        if (drawable != null) {
            @Suppress("DEPRECATION")
            view.setBackgroundDrawable(drawable)
            resetVisible(view)
        } else {
            val color = typedArray.getColor(index, Integer.MAX_VALUE)
            if (color != Integer.MAX_VALUE) {
                view.setBackgroundColor(color)
                resetVisible(view)
            } else {
                val colorStr = typedArray.getString(index)
                if (!TextUtils.isEmpty(colorStr)) {
                    try {
                        view.setBackgroundColor(Color.parseColor(colorStr))
                        resetVisible(view)
                    } catch (ignored: Exception) {
                    }

                } else {
                    val resId = typedArray.getResourceId(index, -1)
                    if (resId == -1) {
                    } else {
                        view.setBackgroundResource(resId)
                        resetVisible(view)
                    }
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun setText(textView: TextView, index: Int, _defaultString: String?, typedArray: TypedArray) {
        var defaultString = _defaultString
        try {
            textView.text = defaultString
            defaultString = typedArray.getString(index)
            if (defaultString == null) {
                val resId = typedArray.getResourceId(index, -1)
                if (resId != -1) {
                    textView.text = resources.getString(resId)
                    resetViewPadding(textView)
                }
            } else {
                textView.text = defaultString
                resetViewPadding(textView)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun resetViewPadding(textView: TextView) =/*int viewId = textView.getId();
        if (viewId == R.id.right0Btn) {
            right0BgView.setPadding(0, 0, 0, 0);
        } else if (viewId == R.id.right1Btn) {
            right1BgView.setPadding(0, 0, 0, 0);
        } else if (viewId == R.id.left0Btn) {
            left0BgView.setPadding(0, 0, 0, 0);
        } else if (viewId == R.id.left1Btn) {
            left1BgView.setPadding(0, 0, 0, 0);
        }*/
        resetVisible(textView)

    private fun resetVisible(view: View) {
        val viewId = view.id
        if (viewId == R.id.right0_btn || viewId == R.id.right0_bg_view) {
            right0BgView.visibility = View.VISIBLE
        } else if (viewId == R.id.right1_btn || viewId == R.id.right1_bg_view) {
            right1BgView.visibility = View.VISIBLE
        } else if (viewId == R.id.left0_btn || viewId == R.id.left0_bg_view) {
            mIsLeft0Set = true
            left0BgView.visibility = View.VISIBLE
        } else if (viewId == R.id.left1_btn || viewId == R.id.left1_bg_view) {
            left1BgView.visibility = View.VISIBLE
        }
    }

    private fun setTextColor(textView: TextView, index: Int, defaultColor: Int?, typedArray: TypedArray) = try {
        if (defaultColor != null)
            textView.setTextColor(defaultColor)
        val color = typedArray.getColor(index, Integer.MAX_VALUE)
        if (color != Integer.MAX_VALUE) {
            textView.setTextColor(color)
        } else {
            val colorStr = typedArray.getString(index)
            if (TextUtils.isEmpty(colorStr)) {
            } else {
                try {
                    textView.setTextColor(Color.parseColor(colorStr))
                } catch (ignored: Exception) {
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun setTextSize(textView: TextView, index: Int, _defaultSize: Float, typedArray: TypedArray) {
        var defaultSize = _defaultSize
        try {
            if (defaultSize != -1f)
                textView.textSize = defaultSize
            defaultSize = typedArray.getDimension(index, -1f)
            if (defaultSize != -1f)
                textView.textSize = defaultSize / mScaledDensity
            else {
                val resId = typedArray.getResourceId(index, -1)
                if (resId != -1) {
                    defaultSize = resources.getDimension(resId)
                    textView.textSize = defaultSize / mScaledDensity
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setTextAppearance(textView: TextView, index: Int, typedArray: TypedArray) = try {
        val resId = typedArray.getResourceId(index, -1)
        if (resId == -1) {
        } else {
            @Suppress("DEPRECATION")
            textView.setTextAppearance(mContext, resId)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
