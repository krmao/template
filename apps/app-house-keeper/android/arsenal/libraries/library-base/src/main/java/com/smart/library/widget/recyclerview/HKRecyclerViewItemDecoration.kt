package com.smart.library.widget.recyclerview

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View


@Suppress("unused", "MemberVisibilityCanPrivate")
class HKRecyclerViewItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter.itemCount
        val columnCount = getColumnCount(parent)
        val rowCount = (itemCount - 1) / columnCount + 1
        val rightSpace = if (isLastColumn(itemPosition, columnCount)) 0 else space  // 最后一列
        val bottomSpace = if (isLastRow(itemPosition, rowCount, columnCount)) 0 else space  // 最后一行
        outRect.set(0, 0, rightSpace, bottomSpace)
    }

    fun isLastRow(itemPosition: Int, rowCount: Int, colCount: Int): Boolean =
        itemPosition / colCount == rowCount - 1

    fun isLastColumn(itemPosition: Int, colCount: Int): Boolean =
        itemPosition % colCount == colCount - 1

    fun getColumnCount(parent: RecyclerView): Int {
        val layoutManager = parent.layoutManager
        var spanCount = 1
        if (layoutManager is GridLayoutManager) spanCount = layoutManager.spanCount
        else if (layoutManager is StaggeredGridLayoutManager) spanCount = layoutManager.spanCount
        return spanCount
    }
}
