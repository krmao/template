package com.smart.library.widget.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView

class STDividerItemDecoration(context: Context?, orientation: Int) : DividerItemDecoration(context, orientation) {

    override fun onDraw(c: Canvas?, parent: RecyclerView?, state: RecyclerView.State?) {
        if ((parent?.adapter?.itemCount ?: 0) > 1) {
            super.onDraw(c, parent, state)
        }
    }

}