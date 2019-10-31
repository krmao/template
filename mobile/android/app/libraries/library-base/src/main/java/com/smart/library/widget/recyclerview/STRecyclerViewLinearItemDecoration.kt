package com.smart.library.widget.recyclerview

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

@Suppress("unused", "MemberVisibilityCanPrivate")
class STRecyclerViewLinearItemDecoration @JvmOverloads constructor(private val space: Int = 0, private val startPadding: Int = 0, private val enableWrapperLoading: Boolean = false) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 1

        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        (parent.layoutManager as? LinearLayoutManager)?.let {
            if (it.orientation == LinearLayoutManager.HORIZONTAL) {
                if (itemPosition == 0) {
                    // first
                    left = if (startPadding > 0) startPadding else 0
                }

                if (itemPosition < (itemCount - (if (enableWrapperLoading) 2 else 1))) {
                    right = space
                }
            } else {
                if (itemPosition == 0) {
                    // first
                    top = if (startPadding > 0) startPadding else 0
                }
                if (itemPosition < (itemCount - (if (enableWrapperLoading) 2 else 1))) {
                    bottom = space
                }
            }
            outRect.set(left, top, right, bottom)
        }
    }
}
