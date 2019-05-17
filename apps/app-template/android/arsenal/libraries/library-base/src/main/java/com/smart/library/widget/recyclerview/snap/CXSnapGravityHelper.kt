package com.smart.library.widget.recyclerview.snap

import android.support.v4.text.TextUtilsCompat
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import com.smart.library.util.CXLogUtil
import java.util.*

/**
 * @param enableSnapAtEndOfList 当滚动到列表尾部时, true 将自动滚动保持第一个可见项完全显示(比如可能回滚导致加载更多不完全显示), false 不进行自动滚动
 * @param enableSnapLastPositionCompletelyVisible 当滚动到列表尾部时 onSnap 返回的是完全可见的(true)或者部分可见的(false)
 */
@Suppress("unused")
class CXSnapGravityHelper @JvmOverloads constructor(gravity: Int, snapListener: SnapListener? = null, debug: Boolean = false, enableSnapAtEndOfList: Boolean = false, enableSnapLastPositionCompletelyVisible: Boolean = true) : LinearSnapHelper() {

    private val delegate: GSSnapGravityDelegate = GSSnapGravityDelegate(gravity, debug, enableSnapAtEndOfList, enableSnapLastPositionCompletelyVisible, snapListener)

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        delegate.attachToRecyclerView(recyclerView)
        super.attachToRecyclerView(recyclerView)
    }

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? = delegate.calculateDistanceToFinalSnap(layoutManager, targetView)

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? = delegate.findSnapView(layoutManager)

    /**
     * Enable snapping of the last item that's snappable.
     * The default value is false, because you can't see the last item completely
     * if this is enabled.
     *
     * @param snap true if you want to enable snapping of the last snappable item
     */
    fun enableLastItemSnap(snap: Boolean) = delegate.enableLastItemSnap(snap)

    fun smoothScrollToPosition(position: Int) = delegate.smoothScrollToPosition(position)

    fun scrollToPosition(position: Int) = delegate.scrollToPosition(position)

    interface SnapListener {
        /**
         * -1 is invalid
         */
        fun onSnap(position: Int)
    }

    internal class GSSnapGravityDelegate(private val gravity: Int, private var debug: Boolean = false, private var enableSnapAtEndOfList: Boolean = false, private var enableSnapLastPositionCompletelyVisible: Boolean = true, private val listener: SnapListener?) {

        private val tag = "Gravity-Snap"
        private var verticalHelper: OrientationHelper? = null
        private var horizontalHelper: OrientationHelper? = null
        private var isRightToLeft: Boolean = false
        private var lastSnappedPositionAfterOnSnap: Int = -1 // 去重
        private var recyclerView: RecyclerView? = null

        @Throws(IllegalArgumentException::class, IllegalStateException::class)
        fun attachToRecyclerView(recyclerView: RecyclerView?) {
            if (gravity != Gravity.START && gravity != Gravity.END && gravity != Gravity.BOTTOM && gravity != Gravity.TOP) {
                throw IllegalArgumentException("Invalid gravity value. Use START " + "| END | BOTTOM | TOP constants")
            }
            if (recyclerView == null || recyclerView.layoutManager !is LinearLayoutManager) {
                throw IllegalStateException("needs a recyclerView with a LinearLayoutManager")
            }

            recyclerView.onFlingListener = null
            if (gravity == Gravity.START || gravity == Gravity.END) isRightToLeft = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL
            // if (listener != null) recyclerView.addOnScrollListener(scrollListener)
            this.recyclerView = recyclerView

            // invoke first item
            if (recyclerView.layoutManager?.itemCount ?: 0 > 0) {
                notifyOnSnapped(0)
            }
        }

        fun smoothScrollToPosition(position: Int) = scrollTo(position, true)

        fun scrollToPosition(position: Int) = scrollTo(position, false)

        private fun scrollTo(position: Int, smooth: Boolean) {
            if (recyclerView?.layoutManager != null) {
                val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position)
                if (viewHolder != null) {
                    val distances = calculateDistanceToFinalSnap(recyclerView?.layoutManager!!, viewHolder.itemView)
                    if (smooth) {
                        recyclerView?.smoothScrollBy(distances[0], distances[1])
                    } else {
                        recyclerView?.scrollBy(distances[0], distances[1])
                    }
                } else {
                    if (smooth) {
                        recyclerView?.smoothScrollToPosition(position)
                    } else {
                        recyclerView?.scrollToPosition(position)
                    }
                }
            }
        }

        fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray {
            val distanceArray = IntArray(2)

            if (layoutManager !is LinearLayoutManager) {
                return distanceArray
            }

            if (isAtEndOfList(layoutManager) && !enableSnapAtEndOfList) {
                return distanceArray
            }

            if (layoutManager.canScrollHorizontally()) {
                if (isRightToLeft && gravity == Gravity.END || !isRightToLeft && gravity == Gravity.START) {
                    distanceArray[0] = distanceToStart(targetView, layoutManager, getHorizontalHelper(layoutManager))
                } else {
                    distanceArray[0] = distanceToEnd(targetView, layoutManager, getHorizontalHelper(layoutManager))
                }
            } else {
                distanceArray[0] = 0
            }

            if (layoutManager.canScrollVertically()) {
                if (gravity == Gravity.TOP) {
                    distanceArray[1] = distanceToStart(targetView, layoutManager, getVerticalHelper(layoutManager))
                } else {
                    distanceArray[1] = distanceToEnd(targetView, layoutManager, getVerticalHelper(layoutManager))
                }
            } else {
                distanceArray[1] = 0
            }

            return distanceArray
        }

        /**
         * 该方法会找到当前layoutManager上最接近对齐位置的那个view，该view称为SnapView
         * 对应的position称为SnapPosition
         * 如果返回null,就表示没有需要对齐的View,也就不会做滚动对齐调整
         */
        fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
            val tmpRecyclerView = recyclerView
            if (tmpRecyclerView == null || layoutManager !is LinearLayoutManager) {
                return null
            }
            var snapView: View? = null
            when (gravity) {
                Gravity.START -> snapView = findStartOrEndView(layoutManager, getHorizontalHelper(layoutManager), true)
                Gravity.END -> snapView = findStartOrEndView(layoutManager, getHorizontalHelper(layoutManager), false)
                Gravity.TOP -> snapView = findStartOrEndView(layoutManager, getVerticalHelper(layoutManager), true)
                Gravity.BOTTOM -> snapView = findStartOrEndView(layoutManager, getVerticalHelper(layoutManager), false)
            }

            val willSnapPosition: Int
            if (snapView != null) {
                willSnapPosition = tmpRecyclerView.getChildAdapterPosition(snapView)
            } else {
                willSnapPosition =
                        if (layoutManager.childCount > 0) {
                            if (enableSnapLastPositionCompletelyVisible) {
                                if (gravity == Gravity.START || gravity == Gravity.TOP) layoutManager.findFirstCompletelyVisibleItemPosition() else layoutManager.findLastCompletelyVisibleItemPosition()
                            } else {
                                if (gravity == Gravity.START || gravity == Gravity.TOP) layoutManager.findFirstVisibleItemPosition() else layoutManager.findLastVisibleItemPosition()
                            }
                        } else {
                            RecyclerView.NO_POSITION
                        }
            }
            debugLog(tag, "snapView=$snapView, willSnapPosition=$willSnapPosition")

            notifyOnSnapped(willSnapPosition)
            return snapView
        }

        /**
         * 返回recyclerView停止滚动时 将被滚动完全显示到 顶部/底部/中间的 itemView, 返回 null 则不滚动
         */
        private fun findStartOrEndView(linearLayoutManager: LinearLayoutManager, helper: OrientationHelper, start: Boolean): View? {
            if (linearLayoutManager.childCount <= 0) return null

            val isAtEndOfList = isAtEndOfList(linearLayoutManager)
            var minItemViewDecoratedStartView: View? = null
            var minItemViewDecoratedStart = Integer.MAX_VALUE
            var decoratedDistance: Int

            for (i in 0 until linearLayoutManager.childCount) {
                val tmpItemView = linearLayoutManager.getChildAt(i)
                val tmpItemViewDecoratedStart: Int
                if (start && !isRightToLeft || !start && isRightToLeft) {
                    decoratedDistance = helper.getDecoratedStart(tmpItemView)
                    tmpItemViewDecoratedStart = decoratedDistance
                } else {
                    decoratedDistance = helper.getDecoratedEnd(tmpItemView) - helper.end
                    tmpItemViewDecoratedStart = decoratedDistance
                }
                if ((!enableSnapLastPositionCompletelyVisible && !isAtEndOfList) || ((enableSnapLastPositionCompletelyVisible && !isAtEndOfList) || (enableSnapLastPositionCompletelyVisible && isAtEndOfList && ((!enableSnapAtEndOfList && start && decoratedDistance >= 0 || !start && decoratedDistance <= 0) || enableSnapAtEndOfList)))) {
                    if (Math.abs(tmpItemViewDecoratedStart) < Math.abs(minItemViewDecoratedStart)) {
                        minItemViewDecoratedStart = tmpItemViewDecoratedStart
                        minItemViewDecoratedStartView = tmpItemView
                    }
                }
                debugLog(tag, "index=$i,isAtEndOfList=$isAtEndOfList,distanceToEdge=$minItemViewDecoratedStart,decoratedDistance=$decoratedDistance,childCount=${linearLayoutManager.childCount}, itemCount=${linearLayoutManager.itemCount},edgeView=$minItemViewDecoratedStartView")
            }
            return minItemViewDecoratedStartView
        }

        private fun notifyOnSnapped(willSnapPosition: Int) {
            // process position changed
            if (listener != null && willSnapPosition != RecyclerView.NO_POSITION && willSnapPosition != lastSnappedPositionAfterOnSnap) {
                listener.onSnap(willSnapPosition)
                lastSnappedPositionAfterOnSnap = willSnapPosition
            }
        }

        fun enableLastItemSnap(snap: Boolean) {
            enableSnapAtEndOfList = snap
        }

        private fun distanceToStart(targetView: View, linearLayoutManager: LinearLayoutManager, helper: OrientationHelper): Int {
            val tmpRecyclerView = recyclerView ?: return 0

            val position = tmpRecyclerView.getChildLayoutPosition(targetView)
            val distance: Int
            if (
                    (position == 0
                            && (!isRightToLeft || linearLayoutManager.reverseLayout)
                            || position == linearLayoutManager.itemCount - 1
                            && (isRightToLeft || linearLayoutManager.reverseLayout)
                            )
                    && !tmpRecyclerView.clipToPadding
            ) {
                val childStart = helper.getDecoratedStart(targetView)
                if (childStart >= helper.startAfterPadding / 2) {
                    distance = childStart - helper.startAfterPadding
                } else {
                    distance = childStart
                }
            } else {
                distance = helper.getDecoratedStart(targetView)
            }
            return distance
        }

        private fun distanceToEnd(targetView: View, linearLayoutManager: LinearLayoutManager, helper: OrientationHelper): Int {
            val tmpRecyclerView = recyclerView ?: return 0
            val position = tmpRecyclerView.getChildLayoutPosition(targetView)
            val distance: Int

            // The last position or the first position
            // (when there's a reverse layout or we're on RTL mode) must collapse to the padding edge.
            if ((position == 0 && (isRightToLeft || linearLayoutManager.reverseLayout) || position == linearLayoutManager.itemCount - 1 && (!isRightToLeft || linearLayoutManager.reverseLayout)) && !tmpRecyclerView.clipToPadding) {
                val childEnd = helper.getDecoratedEnd(targetView)
                if (childEnd >= helper.end - (helper.end - helper.endAfterPadding) / 2) {
                    distance = helper.getDecoratedEnd(targetView) - helper.end
                } else {
                    distance = childEnd - helper.endAfterPadding
                }
            } else {
                distance = helper.getDecoratedEnd(targetView) - helper.end
            }
            return distance
        }

        private fun isAtEndOfList(linearLayoutManager: LinearLayoutManager): Boolean {
            return if (!linearLayoutManager.reverseLayout && (gravity == Gravity.START || gravity == Gravity.TOP) || linearLayoutManager.reverseLayout && (gravity == Gravity.END || gravity == Gravity.BOTTOM)) {
                linearLayoutManager.findLastCompletelyVisibleItemPosition() == linearLayoutManager.itemCount - 1
            } else {
                linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
            }
        }

        private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
            val oldVerticalHelper = this.verticalHelper
            val newVerticalHelper: OrientationHelper = if (oldVerticalHelper?.layoutManager != layoutManager) OrientationHelper.createVerticalHelper(layoutManager) else oldVerticalHelper
            this.verticalHelper = newVerticalHelper
            return newVerticalHelper
        }

        private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
            val oldHorizontalHelper = this.horizontalHelper
            val newHorizontalHelper: OrientationHelper = if (oldHorizontalHelper?.layoutManager != layoutManager) OrientationHelper.createHorizontalHelper(layoutManager) else oldHorizontalHelper
            this.horizontalHelper = newHorizontalHelper
            return newHorizontalHelper
        }

        private fun debugLog(tag: String, message: String) = if (debug) CXLogUtil.d(tag, message) else Unit
    }
}
