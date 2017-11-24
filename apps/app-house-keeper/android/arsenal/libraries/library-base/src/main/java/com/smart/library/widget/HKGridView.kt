package com.smart.library.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridView

@Suppress("unused")
class HKGridView : GridView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) = super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST))
}
