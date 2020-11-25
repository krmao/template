package com.smart.template.home.test.widget

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet

class FinalRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : RecyclerView(context, attrs, defStyle) {

    fun canPullDown(): Boolean {
        return canScrollVertically(-1)
    }

    fun canPullUp(): Boolean {
        return canScrollVertically(1)
    }
}