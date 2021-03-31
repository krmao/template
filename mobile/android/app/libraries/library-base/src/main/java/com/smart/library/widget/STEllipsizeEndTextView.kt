package com.smart.library.widget

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.text.*
import android.text.TextUtils.TruncateAt
import android.util.AttributeSet
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatTextView
import com.smart.library.R
import kotlin.math.min

/**
 * 多行文本, 最后一行的省略号右侧部分手机存在空白
 * https://github.com/chenshi011/EllipsizeEndTextView/blob/master/src/com/cs/ellipsizetextview/ui/EllipsizeEndTextView.java
 */
@Suppress("unused")
@Keep
class STEllipsizeEndTextView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : AppCompatTextView(context, attrs, defStyleAttr) {

    var isEllipsized = false
        private set

    private var isStale = false

    private var mLastLineMaxWidthScale = 1.0f
    private var mProgrammaticChange = false
    private var mFullText: String? = null
    private var mLineSpacingMultiplier = 1.0f
    private var mLineAdditionalVerticalPadding = 0.0f

    init {
        if (attrs != null) {
            val attributes = context.obtainStyledAttributes(attrs, R.styleable.STEllipsizeEndTextView)
            mLastLineMaxWidthScale = attributes.getFloat(R.styleable.STEllipsizeEndTextView_stLastScale, 1.0f)
            attributes.recycle()
        }
    }

    override fun setLineSpacing(add: Float, mult: Float) {
        mLineAdditionalVerticalPadding = add
        mLineSpacingMultiplier = mult
        super.setLineSpacing(add, mult)
    }

    override fun onTextChanged(text: CharSequence, start: Int, before: Int, after: Int) {
        super.onTextChanged(text, start, before, after)
        if (!mProgrammaticChange) {
            mFullText = text.toString()
            isStale = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        if (isStale && measuredWidth > 0) {
            super.setEllipsize(null)
            resetText()
        }
        super.onDraw(canvas)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        isStale = true
    }

    private fun resetText() {
        val maxLines = maxLines
        if (maxLines == -1) {
            return
        }
        val text: CharSequence? = mFullText
        if (text == null || text.isEmpty()) {
            return
        }
        var layout = layout
        if (layout == null) {
            layout = createWorkingLayout(text)
        }
        // find the last line of text and chop it according to available space
        val linCount = layout.lineCount
        if (maxLines > linCount) {
            return
        }
        val lastLineStart = layout.getLineStart(maxLines - 1)
        val remainder = TextUtils.ellipsize(text.subSequence(lastLineStart, text.length), paint, layout.width * mLastLineMaxWidthScale, TruncateAt.END)
        // assemble just the text portion, without spans
        val builder = SpannableStringBuilder()
        builder.append(text.toString(), 0, lastLineStart)
        if (!TextUtils.isEmpty(remainder)) {
            builder.append(remainder.toString())
        }
        // Now copy the original spans into the assembled string, modified for any ellipsizing.
        //
        // Merely assembling the Spanned pieces together would result in duplicate CharacterStyle
        // spans in the assembled version if a CharacterStyle spanned across the lastLineStart
        // offset.
        if (text is Spanned) {
            val spans = text.getSpans(0, text.length, Any::class.java)
            val destLen = builder.length
            for (i in spans.indices) {
                val span = spans[i]
                val start = text.getSpanStart(span)
                val end = text.getSpanEnd(span)
                val flags = text.getSpanFlags(span)
                if (start <= destLen) {
                    builder.setSpan(span, start, min(end, destLen), flags)
                }
            }
        }
        if (builder != getText()) {
            mProgrammaticChange = true
            try {
                setText(builder)
            } finally {
                mProgrammaticChange = false
            }
        }
        isStale = false
        val ellipsized = builder.toString() != text
        if (ellipsized != isEllipsized) {
            isEllipsized = ellipsized
        }
    }

    @Suppress("DEPRECATION")
    private fun createWorkingLayout(workingText: CharSequence): Layout {
        return StaticLayout(workingText, paint, width - paddingLeft - paddingRight, Layout.Alignment.ALIGN_NORMAL, mLineSpacingMultiplier, mLineAdditionalVerticalPadding, false)
    }

    override fun setEllipsize(where: TruncateAt) {
        // Ellipsize settings are not respected
    }
}