package com.smart.library.widget.recyclerview

import android.graphics.Rect
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.annotation.Keep

/**
 * 左右两边各绘制一半 divider padding 有利于 snap center 的 计算
 */
@Suppress("unused", "MemberVisibilityCanPrivate")
//@Keep
class STRecyclerViewLinearCenterItemDecoration @JvmOverloads constructor(private val dividerPadding: Int = 0, private val startPadding: Int = 0, private val enableWrapperLoading: Boolean = false) : RecyclerView.ItemDecoration() {

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

                    if (itemCount > if (enableWrapperLoading) 2 else 1) {
                        right = dividerPadding / 2
                    }
                } else if (itemPosition == (itemCount - (if (enableWrapperLoading) 2 else 1))) {
                    // first
                    left = dividerPadding / 2

                } else if (itemPosition < (itemCount - (if (enableWrapperLoading) 2 else 1))) {
                    // first
                    left = dividerPadding / 2
                    right = dividerPadding / 2
                }
            } else {
                if (itemPosition == 0) {
                    // first
                    top = if (startPadding > 0) startPadding else 0

                    if (itemCount > if (enableWrapperLoading) 2 else 1) {
                        bottom = dividerPadding / 2
                    }
                } else if (itemPosition == (itemCount - (if (enableWrapperLoading) 2 else 1))) {
                    // first
                    top = dividerPadding / 2

                } else if (itemPosition < (itemCount - (if (enableWrapperLoading) 2 else 1))) {
                    // first
                    top = dividerPadding / 2
                    bottom = dividerPadding / 2
                }
            }
            outRect.set(left, top, right, bottom)
        }
    }
}
