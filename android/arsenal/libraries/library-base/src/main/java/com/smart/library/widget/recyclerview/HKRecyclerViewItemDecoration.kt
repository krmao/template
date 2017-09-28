package com.smart.library.widget.recyclerview

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View


class HKRecyclerViewItemDecoration(private val space: Int, private val spanCount: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.left = if (parent.getChildLayoutPosition(view) % spanCount == 0) 0 else space
        outRect.bottom = space
    }
}
