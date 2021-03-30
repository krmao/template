package com.smart.library.util

import android.view.View
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView

/**
 * @reference https://github.com/eggsywelsh/RecyclerStaggeredStickyHeaderView
 */
@Suppress("unused")
class STRecyclerViewStickyHeaderUtil(layoutManager: RecyclerView.LayoutManager, val stickyHeaderView: View, onUpdateStickyHeaderView: (stickyHeaderView: View, currentMinVisiblePosition: Int) -> Unit, findHeaderPositionBeforeCurrentMinVisiblePosition: (minVisiblePosition: Int) -> Int, findHeaderPositionAfterCurrentMinVisiblePosition: (minVisiblePosition: Int) -> Int) {

    init {
        stickyHeaderView.visibility = View.INVISIBLE // view render in react native must not be gone, or view.top is invalid
    }

    val onStickyHeaderScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        private val spanCount: Int = when (layoutManager) {
            is androidx.recyclerview.widget.StaggeredGridLayoutManager -> layoutManager.spanCount
            is androidx.recyclerview.widget.LinearLayoutManager -> 1
            is androidx.recyclerview.widget.GridLayoutManager -> layoutManager.spanCount
            else -> 1
        }
        private val firstVisibleItemPositions: IntArray by lazy { IntArray(spanCount) }
        private val lastVisibleItemPositions: IntArray by lazy { IntArray(spanCount) }
        private var stickyHeadViewHeight = 0

        override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val currentMinVisiblePosition: Int = firstVisibleItemPositions.minOrNull() ?: Int.MAX_VALUE
            if (dy == 0) { // the first time show the recyclerView items
                findFirstVisibleItemPositions(firstVisibleItemPositions)

                if (currentMinVisiblePosition >= 0) {
                    innerUpdateStickyHeaderView(stickyHeaderView, currentMinVisiblePosition)
                    STLogUtil.v("stickyHeaderView", "---- dy==0 currentMinVisiblePosition=$currentMinVisiblePosition")
                }
            } else if (dy != 0) { //  pull down the recyclerView then dy<0 , pull up the recyclerView then dy>0
                findFirstVisibleItemPositions(firstVisibleItemPositions)
                findLastVisibleItemPositions(lastVisibleItemPositions)
                val currentMaxVisiblePosition: Int = lastVisibleItemPositions.maxOrNull() ?: Int.MIN_VALUE
                if (currentMinVisiblePosition < 0) return

                val headerPositionBeforeCurrentMinVisiblePosition: Int = findHeaderPositionBeforeCurrentMinVisiblePosition(currentMinVisiblePosition) // get one header position before current minimum visible item position
                val headerPositionAfterCurrentMinVisiblePosition: Int = findHeaderPositionAfterCurrentMinVisiblePosition(currentMinVisiblePosition) // get the header position after current minimum visible item position

                // when next header item position after the current minimum visible exist,and not equals current item position
                if (headerPositionAfterCurrentMinVisiblePosition != Int.MIN_VALUE
                        && headerPositionAfterCurrentMinVisiblePosition != currentMinVisiblePosition
                ) { // determine whether next header item after the current minimum visible position is visible in the recyclerView
                    if (headerPositionAfterCurrentMinVisiblePosition <= currentMaxVisiblePosition) { // it means that next header item is visible in the recyclerView now
                        // find that suitable next header view
                        val nextHeaderView: View? = layoutManager.findViewByPosition(headerPositionAfterCurrentMinVisiblePosition)
                        if (nextHeaderView != null) {
                            val yOfHeaderView: Float = nextHeaderView.y
                            // if next header view is scroll into first header view's area
                            if (yOfHeaderView <= stickyHeadViewHeight) { // then fix the first header scroll y
                                // stickyHeaderView.scrollTo(0, (stickyHeadViewHeight - yOfHeaderView).toInt())
                                stickyHeaderView.translationY = -(stickyHeadViewHeight - yOfHeaderView) // scrollTo may cause background not scrolled while sticky header view in react native components
                                STLogUtil.v("stickyHeaderView", "---- 1 yOfHeaderView=$yOfHeaderView, stickyHeadViewHeight=$stickyHeadViewHeight, translationY=$${stickyHeaderView.translationY}")
                            } else { // others,next header away from the first header view
                                // then fix first header view scroll y
                                // stickyHeaderView.scrollTo(0, 0)
                                stickyHeaderView.translationY = 0f
                                STLogUtil.v("stickyHeaderView", "---- 2 yOfHeaderView=$yOfHeaderView, stickyHeadViewHeight=$stickyHeadViewHeight, translationY=$${stickyHeaderView.translationY}")
                            }

                            if ((dy > 0 && yOfHeaderView <= 0 && headerPositionBeforeCurrentMinVisiblePosition == Int.MIN_VALUE)
                                    || dy < 0 && headerPositionBeforeCurrentMinVisiblePosition != Int.MIN_VALUE
                            ) {
                                stickyHeaderView.translationY = 0f
                                stickyHeaderView.visibility = View.VISIBLE
                                STLogUtil.v("stickyHeaderView", "---- 3 yOfHeaderView=$yOfHeaderView, stickyHeadViewHeight=$stickyHeadViewHeight, translationY=$${stickyHeaderView.translationY}")
                            } else {
                                stickyHeaderView.visibility = View.GONE
                                STLogUtil.v("stickyHeaderView", "---- 4 yOfHeaderView=$yOfHeaderView, stickyHeadViewHeight=$stickyHeadViewHeight, translationY=$${stickyHeaderView.translationY}")
                            }
                        }
                    }
                }

                // when header item before the current maxNum visible position exist,and position not equals the current header view's tag
                // 以及当极快速滑动到底部的时候, 直接跳过了上面的判断 yOfHeaderView <= 0 导致 sticky header view 不显示的问题
                if (headerPositionBeforeCurrentMinVisiblePosition != Int.MIN_VALUE && stickyHeaderView.tag != null) {
                    innerUpdateStickyHeaderView(stickyHeaderView, headerPositionBeforeCurrentMinVisiblePosition)
                    STLogUtil.v("stickyHeaderView", "---- 5")
                    if (dy > 0) { // if user pull up the recyclerView,fix the scroll y.Sometimes,because of the next header item may be not visible in current recyclerView,
                        // so,we should force set header view visible once again
                        stickyHeaderView.visibility = View.VISIBLE
                        // stickyHeaderView.scrollTo(0, 0)
                        stickyHeaderView.translationY = 0f
                        STLogUtil.v("stickyHeaderView", "---- 6 visibility=${stickyHeaderView.visibility}, translationY=${stickyHeaderView.translationY}")
                    }
                }

                STLogUtil.e("stickyHeaderView", "tag=${stickyHeaderView.tag}, visibility=${stickyHeaderView.visibility}, translationY=${stickyHeaderView.translationY}, dy=$dy")
                STLogUtil.e("stickyHeaderView", "currentMinVisiblePosition=$currentMinVisiblePosition, currentMaxVisiblePosition=$currentMaxVisiblePosition, headerPositionBeforeCurrentMinVisiblePosition=$headerPositionBeforeCurrentMinVisiblePosition, headerPositionAfterCurrentMinVisiblePosition=$headerPositionAfterCurrentMinVisiblePosition \n\n")
                STLogUtil.e("stickyHeaderView", "******************")
                STLogUtil.e("stickyHeaderView", "******************")
            }
        }

        fun innerUpdateStickyHeaderView(stickyHeaderView: View, currentMinVisiblePosition: Int) {
            onUpdateStickyHeaderView(stickyHeaderView, currentMinVisiblePosition)
            stickyHeaderView.tag = currentMinVisiblePosition
            stickyHeadViewHeight = stickyHeaderView.measuredHeight
            STLogUtil.v("stickyHeaderView", "innerUpdateStickyHeaderView visibility=${stickyHeaderView.visibility}, currentMinVisiblePosition=$currentMinVisiblePosition, stickyHeadViewHeight=$stickyHeadViewHeight")
        }

        fun findFirstVisibleItemPositions(firstVisibleItemPositions: IntArray) {
            when (layoutManager) {
                is androidx.recyclerview.widget.StaggeredGridLayoutManager -> {
                    layoutManager.findFirstVisibleItemPositions(firstVisibleItemPositions)
                }
                is androidx.recyclerview.widget.LinearLayoutManager -> {
                    for (i: Int in 0 until spanCount) {
                        firstVisibleItemPositions[i] = layoutManager.findFirstVisibleItemPosition()
                    }
                }
                is androidx.recyclerview.widget.GridLayoutManager -> {
                    for (i: Int in 0 until spanCount) {
                        firstVisibleItemPositions[i] = layoutManager.findFirstVisibleItemPosition()
                    }
                }
            }
        }

        fun findLastVisibleItemPositions(lastVisibleItemPositions: IntArray) {
            when (layoutManager) {
                is androidx.recyclerview.widget.StaggeredGridLayoutManager -> {
                    layoutManager.findLastVisibleItemPositions(lastVisibleItemPositions)
                }
                is androidx.recyclerview.widget.LinearLayoutManager -> {
                    for (i: Int in 0 until spanCount) {
                        lastVisibleItemPositions[i] = layoutManager.findLastVisibleItemPosition()
                    }
                }
                is androidx.recyclerview.widget.GridLayoutManager -> {
                    for (i: Int in 0 until spanCount) {
                        lastVisibleItemPositions[i] = layoutManager.findLastVisibleItemPosition()
                    }
                }
            }
        }
    }
}

