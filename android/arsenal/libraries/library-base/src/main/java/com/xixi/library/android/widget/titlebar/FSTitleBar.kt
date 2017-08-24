package com.xixi.library.android.widget.titlebar

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.support.v4.widget.ContentLoadingProgressBar
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.xixi.library.android.R
import com.xixi.library.android.util.FSCustomViewUtil
import com.xixi.library.android.util.FSSystemUtil
import kotlinx.android.synthetic.main.fs_widget_titlebar.view.*

/**
 * TitleView样式一
 * “左0--左1(hide)--Title--右1(hide)--右0”
 * “图片、颜色、文字”
 */
class FSTitleBar(val mContext: Context, attrs: AttributeSet?) : RelativeLayout(mContext, attrs) {

    companion object {
        var DEFAULT_TEXT_SIZE = 18f
        var DEFAULT_TEXT_COLOR = Color.parseColor("#333333")
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
        LayoutInflater.from(mContext).inflate(R.layout.fs_widget_titlebar, this, true)

        val iconPadding = resources.getDimensionPixelSize(R.dimen.fs_titlebar_icon_padding)

        if (attrs != null) {
            val typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.FSTitleBar)
            setBackground(this, R.styleable.FSTitleBar_android_background, DEFAULT_BACKGROUND_COLOR, -1, typedArray)

            //title
            setText(titleText, R.styleable.FSTitleBar_fsTitleText, null, typedArray)
            setTextColor(titleText, R.styleable.FSTitleBar_fsTitleTxtColor, DEFAULT_TEXT_COLOR, typedArray)
            setTextSize(titleText, R.styleable.FSTitleBar_fsTitleSize, DEFAULT_TEXT_SIZE, typedArray)
            setTextAppearance(titleText, R.styleable.FSTitleBar_fsTitleAppearance, typedArray)
            //left0
            setBackground(left0BgView, R.styleable.FSTitleBar_fsLeft0BgViewBackground, -1, R.drawable.fs_selector, typedArray)
            setLayoutParams(left0BgView, left0Btn, R.styleable.FSTitleBar_fsLeft0BgWidth, typedArray)
            setBgPadding(left0BgView, R.styleable.FSTitleBar_fsLeft0BgViewPadding, R.styleable.FSTitleBar_fsLeft0BgPaddingLeft, R.styleable.FSTitleBar_fsLeft0BgPaddingRight, R.styleable.FSTitleBar_fsLeft0BgPaddingTop, R.styleable.FSTitleBar_fsLeft0BgPaddingBottom, iconPadding, typedArray)
            left0BgView.visibility = typedArray.getInt(R.styleable.FSTitleBar_fsLeft0Visible, View.VISIBLE)
            setTextAppearance(left0Btn, R.styleable.FSTitleBar_fsLeft0Appearance, typedArray)
            @Suppress("DEPRECATION")
            val fs_transparentColor = resources.getColor(R.color.fs_transparent)
            setBackground(left0Btn, R.styleable.FSTitleBar_fsLeft0Background, fs_transparentColor, -1, typedArray)
            setText(left0Btn, R.styleable.FSTitleBar_fsLeft0Text, null, typedArray)
            setTextColor(left0Btn, R.styleable.FSTitleBar_fsLeft0TextColor, DEFAULT_TEXT_COLOR, typedArray)
            setTextSize(left0Btn, R.styleable.FSTitleBar_fsLeft0TextSize, DEFAULT_TEXT_SIZE, typedArray)
            //left1
            setBackground(left1BgView, R.styleable.FSTitleBar_fsLeft1BgViewBackground, -1, R.drawable.fs_selector, typedArray)
            setLayoutParams(left1BgView, left1Btn, R.styleable.FSTitleBar_fsLeft1BgWidth, typedArray)
            setBgPadding(left1BgView, R.styleable.FSTitleBar_fsLeft1BgViewPadding, R.styleable.FSTitleBar_fsLeft1BgPaddingLeft, R.styleable.FSTitleBar_fsLeft1BgPaddingRight, R.styleable.FSTitleBar_fsLeft1BgPaddingTop, R.styleable.FSTitleBar_fsLeft1BgPaddingBottom, iconPadding, typedArray)
            left1BgView.visibility = typedArray.getInt(R.styleable.FSTitleBar_fsLeft1Visible, View.INVISIBLE)
            setTextAppearance(left1Btn, R.styleable.FSTitleBar_fsLeft1Appearance, typedArray)
            setBackground(left1Btn, R.styleable.FSTitleBar_fsLeft1Background, fs_transparentColor, -1, typedArray)
            setText(left1Btn, R.styleable.FSTitleBar_fsLeft1Text, null, typedArray)
            setTextColor(left1Btn, R.styleable.FSTitleBar_fsLeft1TextColor, DEFAULT_TEXT_COLOR, typedArray)
            setTextSize(left1Btn, R.styleable.FSTitleBar_fsLeft1TextSize, DEFAULT_TEXT_SIZE, typedArray)
            //right0
            setBackground(right0BgView, R.styleable.FSTitleBar_fsRight0BgViewBackground, -1, R.drawable.fs_selector, typedArray)
            setLayoutParams(right0BgView, right0Btn, R.styleable.FSTitleBar_fsRight0BgWidth, typedArray)
            setBgPadding(right0BgView, R.styleable.FSTitleBar_fsRight0BgViewPadding, R.styleable.FSTitleBar_fsRight0BgPaddingLeft, R.styleable.FSTitleBar_fsRight0BgPaddingRight, R.styleable.FSTitleBar_fsRight0BgPaddingTop, R.styleable.FSTitleBar_fsRight0BgPaddingBottom, iconPadding, typedArray)
            right0BgView.visibility = typedArray.getInt(R.styleable.FSTitleBar_fsRight0Visible, View.INVISIBLE)
            setTextAppearance(right0Btn, R.styleable.FSTitleBar_fsRight0Appearance, typedArray)
            setBackground(right0Btn, R.styleable.FSTitleBar_fsRight0Background, fs_transparentColor, -1, typedArray)
            setText(right0Btn, R.styleable.FSTitleBar_fsRight0Text, null, typedArray)
            setTextColor(right0Btn, R.styleable.FSTitleBar_fsRight0TextColor, DEFAULT_TEXT_COLOR, typedArray)
            setTextSize(right0Btn, R.styleable.FSTitleBar_fsRight0TextSize, DEFAULT_TEXT_SIZE, typedArray)
            //right1
            setBackground(right1BgView, R.styleable.FSTitleBar_fsRight1BgViewBackground, -1, R.drawable.fs_selector, typedArray)
            setLayoutParams(right1BgView, right1Btn, R.styleable.FSTitleBar_fsRight1BgWidth, typedArray)
            setBgPadding(right1BgView, R.styleable.FSTitleBar_fsRight1BgViewPadding, R.styleable.FSTitleBar_fsRight1BgPaddingLeft, R.styleable.FSTitleBar_fsRight1BgPaddingRight, R.styleable.FSTitleBar_fsRight1BgPaddingTop, R.styleable.FSTitleBar_fsRight1BgPaddingBottom, iconPadding, typedArray)
            right1BgView.visibility = typedArray.getInt(R.styleable.FSTitleBar_fsRight1Visible, View.INVISIBLE)
            setTextAppearance(right1Btn, R.styleable.FSTitleBar_fsRight1Appearance, typedArray)
            setBackground(right1Btn, R.styleable.FSTitleBar_fsRight1Background, fs_transparentColor, -1, typedArray)
            setText(right1Btn, R.styleable.FSTitleBar_fsRight1Text, null, typedArray)
            setTextColor(right1Btn, R.styleable.FSTitleBar_fsRight1TextColor, DEFAULT_TEXT_COLOR, typedArray)
            setTextSize(right1Btn, R.styleable.FSTitleBar_fsRight1TextSize, DEFAULT_TEXT_SIZE, typedArray)

            progressBar.visibility = typedArray.getInt(R.styleable.FSTitleBar_fsProgressVisible, View.GONE)
            progressBar.progressDrawable = FSCustomViewUtil.getDrawable(typedArray, R.styleable.FSTitleBar_fsProgressDrawable, -1, R.drawable.fs_titlebar_progressbar_horizontal_progress_drawable)
            typedArray.recycle()

            if (!mIsLeft0Set) {
                setBackground(left0Btn, R.styleable.FSTitleBar_fsLeft0Background, fs_transparentColor, R.drawable.fs_menu_back_gray, typedArray)
            }
        }
        left0BgView.setOnClickListener { FSSystemUtil.sendKeyBackEvent(context) }
    }

    private fun setLayoutParams(bgView: View, childView: View, index: Int, typedArray: TypedArray) {
        try {
            val layoutParams = bgView.layoutParams as RelativeLayout.LayoutParams
            var configWidth = typedArray.getDimensionPixelSize(index, -3).toFloat()
            if (configWidth == RelativeLayout.LayoutParams.WRAP_CONTENT.toFloat() || configWidth >= 0)
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
            if (layoutParams.width != RelativeLayout.LayoutParams.WRAP_CONTENT) {
                val childLayoutParams = childView.layoutParams as RelativeLayout.LayoutParams
                childLayoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT
                childView.layoutParams = childLayoutParams
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

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

    private fun setBackground(view: View, index: Int, defaultColor: Int, defaultRes: Int, typedArray: TypedArray) {
        try {
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
                        if (resId != -1) {
                            view.setBackgroundResource(resId)
                            resetVisible(view)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

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

    private fun resetViewPadding(textView: TextView) {
        /*int viewId = textView.getId();
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
    }

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

    private fun setTextColor(textView: TextView, index: Int, defaultColor: Int?, typedArray: TypedArray) {
        try {
            if (defaultColor != null)
                textView.setTextColor(defaultColor)
            val color = typedArray.getColor(index, Integer.MAX_VALUE)
            if (color != Integer.MAX_VALUE) {
                textView.setTextColor(color)
            } else {
                val colorStr = typedArray.getString(index)
                if (!TextUtils.isEmpty(colorStr)) {
                    try {
                        textView.setTextColor(Color.parseColor(colorStr))
                    } catch (ignored: Exception) {
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun setTextAppearance(textView: TextView, index: Int, typedArray: TypedArray) {
        try {
            val resId = typedArray.getResourceId(index, -1)
            if (resId != -1) {
                @Suppress("DEPRECATION")
                textView.setTextAppearance(mContext, resId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
