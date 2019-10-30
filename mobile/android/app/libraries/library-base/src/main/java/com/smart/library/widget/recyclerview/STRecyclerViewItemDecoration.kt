package com.smart.library.widget.recyclerview

import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import kotlin.math.floor
import kotlin.math.max


@Suppress("unused", "MemberVisibilityCanPrivate")
class STRecyclerViewItemDecoration(private val space: Int, private val leftPadding: Int = 0) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 1

        val columnCount = getColumnCount(parent, itemCount)
        val rowCount = getRowCount(parent, itemCount, columnCount)

        val rightSpace = if (isLastColumn(itemPosition, columnCount)) 0 else space  // 最后一列
        val bottomSpace = if (isLastRow(itemPosition, rowCount, columnCount)) 0 else space  // 最后一行
        outRect.set(
                if (leftPadding > 0 && isFirstColumn(itemPosition, columnCount)) leftPadding else 0,
                0,
                rightSpace,
                bottomSpace
        )
    }

    private fun isLastRow(itemPosition: Int, rowCount: Int, colCount: Int): Boolean = itemPosition / colCount == rowCount - 1

    private fun isLastColumn(itemPosition: Int, colCount: Int): Boolean = itemPosition % colCount == colCount - 1
    private fun isFirstColumn(itemPosition: Int, colCount: Int): Boolean = itemPosition % colCount == 0
    private fun isFirstRow(itemPosition: Int, colCount: Int): Boolean = (floor(1f * itemPosition / colCount)).toInt() == 0

    /**
     * >=1
     */
    private fun getRowCount(parent: RecyclerView, itemCount: Int, columnCount: Int): Int {
        val layoutManager = parent.layoutManager
        var rowCount = 1
        if (layoutManager is LinearLayoutManager) {
            if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                rowCount = itemCount - 1
            } else {
                rowCount = 1
            }
        }
        if (layoutManager is GridLayoutManager) rowCount = ((itemCount - 1) / columnCount) + 1
        else if (layoutManager is StaggeredGridLayoutManager) rowCount = ((itemCount - 1) / columnCount) + 1
        return max(1, rowCount)
    }

    /**
     * >=1
     */
    private fun getColumnCount(parent: RecyclerView, itemCount: Int): Int {
        val layoutManager = parent.layoutManager
        var columnCount = 1
        if (layoutManager is LinearLayoutManager) {
            if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
                columnCount = 1
            } else {
                columnCount = itemCount - 1
            }
        }
        if (layoutManager is GridLayoutManager) columnCount = layoutManager.spanCount
        else if (layoutManager is StaggeredGridLayoutManager) columnCount = layoutManager.spanCount
        return max(1, columnCount)
    }
}
