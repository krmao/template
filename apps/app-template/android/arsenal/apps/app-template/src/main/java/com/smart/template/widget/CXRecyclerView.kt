package com.smart.template.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet

class CXRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

    fun canPullDown(): Boolean {
        return canScrollVertically(-1)
    }

    fun canPullUp(): Boolean {
        return canScrollVertically(1)
    }
}