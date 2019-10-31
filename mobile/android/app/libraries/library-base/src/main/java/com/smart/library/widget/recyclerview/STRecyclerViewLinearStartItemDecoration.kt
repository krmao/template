package com.smart.library.widget.recyclerview

import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 左右两边各绘制一半 divider padding 有利于 snap center 的 计算
 */
@Suppress("unused", "MemberVisibilityCanPrivate")
class STRecyclerViewLinearStartItemDecoration @JvmOverloads constructor(private val dividerPadding: Int = 0, private val startPadding: Int = 0, private val enableWrapperLoading: Boolean = false) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 1

        var left = 0
        var top = 0
        val right = 0
        val bottom = 0

        (parent.layoutManager as? LinearLayoutManager)?.let {
            if (it.orientation == LinearLayoutManager.HORIZONTAL) {
                if (itemPosition == 0) {
                    // first
                    left = if (startPadding > 0) startPadding else 0
                } else if (itemPosition <= (itemCount - (if (enableWrapperLoading) 2 else 1))) {
                    left = dividerPadding
                }
            } else {
                if (itemPosition == 0) {
                    // first
                    top = if (startPadding > 0) startPadding else 0
                } else if (itemPosition <= (itemCount - (if (enableWrapperLoading) 2 else 1))) {
                    top = dividerPadding
                }
            }
            outRect.set(left, top, right, bottom)
        }
    }
}
