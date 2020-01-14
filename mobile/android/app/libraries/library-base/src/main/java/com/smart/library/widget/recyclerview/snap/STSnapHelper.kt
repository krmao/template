package com.smart.library.widget.recyclerview.snap

import android.graphics.Rect
import androidx.recyclerview.widget.*
import android.view.View
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper
import kotlin.math.abs
import kotlin.math.min

interface STSnapHelper {

    fun attachToRecyclerView(recyclerView: RecyclerView?)

    fun orientation(): Int
    fun lastSnappedPosition(): Int
    fun recyclerView(): RecyclerView?
    fun switchSnap(snap: Snap)
    fun forceSnap()
    fun forceSnap(onSnappedCallback: ((success: Boolean) -> Unit)? = null)
    fun forceSnap(targetPosition: Int, onSnappedCallback: ((success: Boolean) -> Unit)? = null)
    fun scrollToPositionWithOnSnap(position: Int)
    fun scrollToPositionWithOnSnap(position: Int, onScrolledCallback: ((success: Boolean) -> Unit)? = null)
    fun scrollToPositionWithOnSnap(position: Int, smooth: Boolean = true, onScrolledCallback: ((success: Boolean) -> Unit)? = null)
    fun scrollToPositionWithoutOnSnap(position: Int, onScrolledCallback: ((success: Boolean) -> Unit)? = null)
    fun enableDebug(enable: Boolean)
    fun debugLog(logHandler: () -> Unit)

    enum class Snap {
        START,
        CENTER,
        END
    }

    class STSnapGravityDelegate(private val enableLoadingFooterView: Boolean, var snap: Snap, private val onSnap: ((position: Int) -> Unit)? = null) {

        private val tag = "Gravity-Snap"
        private var verticalHelper: OrientationHelper? = null
        private var horizontalHelper: OrientationHelper? = null
        private var willScrollToTargetPosition: Int = RecyclerView.NO_POSITION
        private val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && willScrollToTargetPosition != RecyclerView.NO_POSITION) {
                    willScrollToTargetPosition = RecyclerView.NO_POSITION // 拖拽情况下 强制清空之前的指定目标
                    debugLog { STLogUtil.e(tag, "拖拽情况下 强制清空之前的指定目标 willScrollToTargetPosition=$willScrollToTargetPosition") }
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        }

        var recyclerView: RecyclerView? = null
        val orientation: Int
            get() = when (val layoutManager: RecyclerView.LayoutManager? = this.recyclerView?.layoutManager) {
                is LinearLayoutManager -> layoutManager.orientation
                is GridLayoutManager -> layoutManager.orientation
                is StaggeredGridLayoutManager -> layoutManager.orientation
                else -> LinearLayoutManager.VERTICAL
            }
        var lastSnappedPosition: Int = RecyclerView.NO_POSITION

        var debugLog = false
        fun debugLog(logHandler: () -> Unit) {
            if (debugLog) logHandler()
        }

        @Throws(IllegalStateException::class)
        fun attachToRecyclerView(recyclerView: RecyclerView?) {
            if (recyclerView == null || recyclerView.layoutManager !is LinearLayoutManager) {
                throw IllegalStateException("must be set LinearLayoutManager for recyclerView")
            }
            recyclerView.onFlingListener = null
            recyclerView.removeOnScrollListener(scrollListener)
            recyclerView.addOnScrollListener(scrollListener)
            this.recyclerView = recyclerView
        }

        /**
         * force snap after inner data changed, for example snap immediately after recyclerView init or adapter data changed
         * called must be after recycler setAdapter
         *
         * 注意:
         *    如果想首次加载触发 onSnap 0 回调, 则初始化 adapter 时传入空的数组, 然后调用 STRecyclerViewAdapter.add
         *    强制触发 onSnap (在 STRecyclerViewAdapter.onInnerDataChanged 中调用 STSnapGravityHelper.forceSnap)
         *    这里不去重是为了 防止 adapter.remove(0) 的时候, onSnap 都是 0, 但是真实数据已经改变了, 虽然索引没有变化
         */
        fun forceSnap(targetPosition: Int, onSnappedCallback: ((success: Boolean) -> Unit)?) {
            val isInnerDataEmpty: Boolean
            val isValidPosition: Boolean
            val adapter = recyclerView?.adapter

            if (adapter is STEmptyLoadingWrapper<*>) {
                isInnerDataEmpty = adapter.isInnerDataEmpty()
                isValidPosition = !isInnerDataEmpty && (targetPosition in (0 until adapter.innerData().size))
            } else {
                isInnerDataEmpty = adapter?.itemCount == 0
                isValidPosition = !isInnerDataEmpty && (targetPosition in (0 until (adapter?.itemCount ?: 0)))
            }

            debugLog { STLogUtil.e("forceSnap", "targetPosition:$targetPosition, isInnerDataEmpty:$isInnerDataEmpty , recyclerView==null?${recyclerView == null}") }
            if (isInnerDataEmpty || !isValidPosition) {
                lastSnappedPosition = RecyclerView.NO_POSITION
                onSnap?.invoke(RecyclerView.NO_POSITION)
                onSnappedCallback?.invoke(false)
            } else {
                scrollToPositionWithOnSnap(targetPosition, false, onSnappedCallback)
            }
        }

        @JvmOverloads
        fun scrollToPositionWithOnSnap(position: Int, smooth: Boolean = true, onScrolledCallback: ((success: Boolean) -> Unit)? = null) {
            innerScrollToPositionWithOnSnap(position, smooth, false, onScrolledCallback)
        }

        private fun innerScrollToPositionWithOnSnap(position: Int, smooth: Boolean = true, isAutoRetry: Boolean = false, onScrolledCallback: ((success: Boolean) -> Unit)? = null) {
            if (position >= 0 && position < recyclerView?.adapter?.itemCount ?: 0) {
                var needScroll = true
                if (recyclerView?.layoutManager != null) {
                    val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position)
                    if (viewHolder != null) {
                        val distanceArray = calculateDistanceToFinalSnap(recyclerView?.layoutManager!!, viewHolder.itemView)
                        needScroll = distanceArray[0] != 0 || distanceArray[1] != 0
                        debugLog { STLogUtil.e(tag, "--> 执行滚动到指定位置 由于目标 itemView 在缓存内 直接计算滚动距离 smoothScrollBy:(x:${distanceArray[0]}, y:${distanceArray[1]})  ${if (needScroll) "需要滚动" else "无需滚动"} ") }
                        if (needScroll) {
                            if (smooth) {
                                recyclerView?.smoothScrollBy(distanceArray[0], distanceArray[1])
                            } else {
                                val offset = STSystemUtil.screenWidth / 3
                                val dx: Int = if (distanceArray[0] < 0) min(abs(distanceArray[0]) - 1, offset) else if (distanceArray[0] > 0) -min(abs(distanceArray[0]) - 1, offset) else 0
                                val dy: Int = if (distanceArray[1] < 0) min(abs(distanceArray[1]) - 1, offset) else if (distanceArray[1] > 0) -min(abs(distanceArray[1]) - 1, offset) else 0
                                recyclerView?.scrollBy(distanceArray[0] + dx, distanceArray[1] + dy)
                                recyclerView?.smoothScrollBy(-dx, -dy)
                            }
                        }
                        onScrolledCallback?.invoke(true)
                    } else {
                        debugLog { STLogUtil.e(tag, "--> 执行滚动到指定位置 由于目标 itemView 不在缓存内 直接滚动 position -> smoothScrollToPosition:$position") }
                        willScrollToTargetPosition = position
                        if (smooth) {
                            recyclerView?.smoothScrollToPosition(position)
                        } else {
                            recyclerView?.scrollToPosition(position)
                        }
                        if (!isAutoRetry) {
                            recyclerView?.postDelayed(
                                    {
                                        innerScrollToPositionWithOnSnap(position, smooth, true, onScrolledCallback)
                                    },
                                    // https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html#getAdapterPosition()
                                    // This inconsistency is not important since it will be less than 16ms but it might be a problem if you want to use ViewHolder position to access the adapter.
                                    16
                            )
                        } else {
                            onScrolledCallback?.invoke(true)
                        }
                    }
                } else {
                    onScrolledCallback?.invoke(false)
                }
                if (!needScroll) {
                    debugLog { STLogUtil.e(tag, "--> 执行滚动到指定位置 由于目标可见且无法继续滚动, 强制回调 onSnap($position)") }
                } else {
                    onScrolledCallback?.invoke(false)
                }
            }
        }

        @JvmOverloads
        fun scrollToPositionWithoutOnSnap(position: Int, onScrolledCallback: ((success: Boolean) -> Unit)? = null) {
            innerScrollToPositionWithoutOnSnap(position, true, onScrolledCallback)
        }

        private fun innerScrollToPositionWithoutOnSnap(position: Int, @Suppress("SameParameterValue") needCalculateDistanceToFinalSnap: Boolean = true, onScrolledCallback: ((success: Boolean) -> Unit)? = null) {
            if (position >= 0 && position < recyclerView?.adapter?.itemCount ?: 0) {
                if (recyclerView?.layoutManager != null) {
                    val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position)
                    if (viewHolder != null) {
                        val distanceArray = calculateDistanceToFinalSnap(recyclerView?.layoutManager, viewHolder.itemView)
                        val needScroll = distanceArray[0] != 0 || distanceArray[1] != 0
                        debugLog { STLogUtil.e(tag, "--> 执行滚动到指定位置 由于目标 itemView 在缓存内 直接计算滚动距离 smoothScrollBy:(x:${distanceArray[0]}, y:${distanceArray[1]})  ${if (needScroll) "需要滚动" else "无需滚动"} ") }
                        if (needScroll) {
                            recyclerView?.scrollBy(distanceArray[0], distanceArray[1])
                        }
                        lastSnappedPosition = position
                        onScrolledCallback?.invoke(true)
                    } else {
                        debugLog { STLogUtil.e(tag, "--> 执行滚动到指定位置 由于目标 itemView 不在缓存内 直接滚动 position -> $position") }
                        willScrollToTargetPosition = position
                        recyclerView?.scrollToPosition(position)
                        if (needCalculateDistanceToFinalSnap) {
                            recyclerView?.postDelayed(
                                    {
                                        innerScrollToPositionWithoutOnSnap(position, false, onScrolledCallback)
                                    },
                                    // https://developer.android.com/reference/android/support/v7/widget/RecyclerView.ViewHolder.html#getAdapterPosition()
                                    // This inconsistency is not important since it will be less than 16ms but it might be a problem if you want to use ViewHolder position to access the adapter.
                                    16
                            )
                        } else {
                            lastSnappedPosition = position
                            onScrolledCallback?.invoke(true)
                        }
                    }
                } else {
                    onScrolledCallback?.invoke(false)
                }
            } else {
                onScrolledCallback?.invoke(false)
            }
        }

        /**
         * 该方法会找到当前layoutManager上最接近对齐位置的那个view，该view称为SnapView
         * 对应的position称为SnapPosition
         * 如果返回null,就表示没有需要对齐的View,也就不会做滚动对齐调整
         */
        fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
            debugLog { STLogUtil.e(tag, "检测到滚动即将结束, 开始查找目标, 即将执行 findSnapView") }
            val tmpRecyclerView = recyclerView
            if (tmpRecyclerView == null || layoutManager !is LinearLayoutManager) {
                debugLog { STLogUtil.e(tag, "开始查找 返回空, 原因 tmpRecyclerView == null 或者 layoutManager !is LinearLayoutManager") }
                willScrollToTargetPosition = RecyclerView.NO_POSITION
                debugLog { STLogUtil.v(tag, ".\n\n滚动结束...\n\n.") }
                return null
            }

            val linearLayoutManager: LinearLayoutManager = layoutManager
            val isAtStartOfList = isAtStartOfList(linearLayoutManager)
            val isAtEndOfList = isAtEndOfList(linearLayoutManager)
            debugLog { STLogUtil.w(tag, "开始查找 isAtStartOfList:$isAtStartOfList, isAtEndOfList:$isAtEndOfList, firstVisible:${linearLayoutManager.findFirstVisibleItemPosition()},firstCompletelyV:${linearLayoutManager.findFirstCompletelyVisibleItemPosition()},lastVisible:${linearLayoutManager.findLastVisibleItemPosition()},lastCompletelyV:${linearLayoutManager.findLastCompletelyVisibleItemPosition()}, childCount=${linearLayoutManager.childCount}, itemCount=${linearLayoutManager.itemCount}") }

            var targetSnapView: View? = null

            val currentVisibleItemsCount: Int = layoutManager.childCount
            if (currentVisibleItemsCount > 0) {
                // 当滚动到边界时, 且不需要强制回滚时, 强制设置目标 position
                // isAtStartOfList 与下面的 snap == Snap.START 息息相关
                if (isAtStartOfList || isAtEndOfList) {
                    /**
                     * START 与 reverseLayout 无关
                     * reverseLayout 与 isAtEndOfList 方向 强相关
                     * reverseLayout 与 findLastCompletelyVisibleItemPosition 方向 强相关
                     *
                     * 即 reverseLayout==true, 则 isAtEndOfList 以及 lastVisibleItem 在顶部
                     * 即 reverseLayout==false, 则 isAtEndOfList 以及 lastVisibleItem 在底部
                     *
                     * enableLoadingFooterView 如果由上至下的布局设置了 snap==bottom, 则最后一个 loadingView 将会无法完全显示(回滚到最后一个非 loading view), 所以需要做特殊处理, 这里不再 -1, 最计算那边 -1
                     */
                    val firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    if (isAtStartOfList) {
                        if (snap == Snap.START) {
                            willScrollToTargetPosition = firstCompletelyVisibleItemPosition
                        } else if (snap == Snap.CENTER) {
                            willScrollToTargetPosition = firstCompletelyVisibleItemPosition + ((lastCompletelyVisibleItemPosition - firstCompletelyVisibleItemPosition) / 2)
                        } else {
                            willScrollToTargetPosition = firstCompletelyVisibleItemPosition
                        }
                    } else if (isAtEndOfList) {
                        if (snap == Snap.START) {
                            willScrollToTargetPosition = lastCompletelyVisibleItemPosition
                        } else if (snap == Snap.CENTER) {
                            willScrollToTargetPosition = lastCompletelyVisibleItemPosition - ((lastCompletelyVisibleItemPosition - firstCompletelyVisibleItemPosition) / 2)
                        } else {
                            willScrollToTargetPosition = lastCompletelyVisibleItemPosition
                        }
                    }
                    debugLog { STLogUtil.e(tag, "开始计算 findSnapView 由于滚动到边界, 根据规则强制指定 willScrollToTargetPosition=$willScrollToTargetPosition") }
                }

                if (willScrollToTargetPosition != RecyclerView.NO_POSITION) {
                    for (childIndex: Int in 0 until linearLayoutManager.childCount) {
                        val tmpItemView: View? = linearLayoutManager.getChildAt(childIndex)
                        if (tmpItemView != null) {
                            if (linearLayoutManager.getPosition(tmpItemView) == willScrollToTargetPosition) {
                                targetSnapView = tmpItemView
                                debugLog { STLogUtil.d(tag, "开始查找 findSnapView 找到 willScrollToTargetPosition 相对应的 targetSnapView:${targetSnapView.hashCode()}, 位于可见索引为:$childIndex") }
                                break
                            }
                        }
                    }
                } else {
                    var minIndex = 0
                    var minDistanceToTargetLocation: Int = Integer.MAX_VALUE

                    for (childIndex: Int in 0 until currentVisibleItemsCount) {
                        val tmpItemView: View? = linearLayoutManager.getChildAt(childIndex)
                        if (tmpItemView != null) {
                            val distanceArray: IntArray = innerCalculateDistanceToFinalSnap(linearLayoutManager, tmpItemView)
                            val distanceDirectionIndex = if (linearLayoutManager.orientation == LinearLayoutManager.HORIZONTAL) 0 else 1
                            if (abs(distanceArray[distanceDirectionIndex]) < abs(minDistanceToTargetLocation)) {
                                minDistanceToTargetLocation = distanceArray[distanceDirectionIndex]
                                targetSnapView = tmpItemView
                                minIndex = childIndex
                            }
                            debugLog { STLogUtil.d(tag, "-- 获取 距离目标位置最近的当前屏幕内可见childView索引:$childIndex, minIndex=$minIndex minDistanceToTargetLocation=$minDistanceToTargetLocation") }
                        }
                    }
                }
            }
            debugLog { STLogUtil.w(tag, "查找结束 findSnapView targetSnapView:${targetSnapView.hashCode()}, targetPosition:${targetSnapView?.let { tmpRecyclerView.getChildAdapterPosition(it) }}") }
            return targetSnapView
        }

        /**
         * Return the child view that is currently closest to the center of this parent.
         *
         * @param layoutManager The [RecyclerView.LayoutManager] associated with the attached
         * [RecyclerView].
         * @param helper The relevant [OrientationHelper] for the attached [RecyclerView].
         *
         * @return the child view that is currently closest to the center of this parent.
         */
        private fun findCenterView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
            val childCount = layoutManager.childCount
            if (childCount == 0) {
                return null
            }

            var closestChild: View? = null
            val center: Int
            if (layoutManager.clipToPadding) {
                center = helper.startAfterPadding + helper.totalSpace / 2
            } else {
                center = helper.end / 2
            }
            var absClosest = Integer.MAX_VALUE

            for (i in 0 until childCount) {
                val child = layoutManager.getChildAt(i)
                val childCenter = helper.getDecoratedStart(child) + helper.getDecoratedMeasurement(child) / 2
                val absDistance = Math.abs(childCenter - center)

                /** if child center is closer than previous closest, set it as closest   */
                if (absDistance < absClosest) {
                    absClosest = absDistance
                    closestChild = child
                }
            }
            return closestChild
        }

        fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager?, targetView: View): IntArray {
            debugLog { STLogUtil.e(tag, "计算继续滚动的距离, 即将执行 calculateDistanceToFinalSnap(targetView=${targetView.hashCode()})") }
            var distanceArray = IntArray(2)
            val tmpRecyclerView = recyclerView
            if (tmpRecyclerView == null || layoutManager !is LinearLayoutManager) {
                debugLog { STLogUtil.e(tag, "开始计算 calculateDistanceToFinalSnap return, tmpRecyclerView == null 或者 layoutManager !is LinearLayoutManager") }
                willScrollToTargetPosition = RecyclerView.NO_POSITION
                debugLog { STLogUtil.v(tag, ".\n\n滚动结束...\n\n.") }
                return distanceArray
            }
            val targetPosition: Int = tmpRecyclerView.getChildAdapterPosition(targetView)
            debugLog { STLogUtil.d(tag, "开始计算 targetPosition=$targetPosition") }
            distanceArray = innerCalculateDistanceToFinalSnap(layoutManager, targetView)

            // start scroll to end 为正
            if ((distanceArray[0] > 0 || distanceArray[1] > 0) && canScrollStartToEnd(layoutManager)) {
                debugLog { STLogUtil.e(tag, ".\n\n继续滚动指定距离到目标位置...start scroll to end\n\n.") }
            } else if ((distanceArray[0] < 0 || distanceArray[1] < 0) && canScrollEndToStart(layoutManager)) {
                debugLog { STLogUtil.e(tag, ".\n\n继续滚动指定距离到目标位置...end scroll to start\n\n.") }
            } else {
                debugLog { STLogUtil.e(tag, "滚动即将结束, 此时(dx:${distanceArray[0]}, dy:${distanceArray[1]})") }
                distanceArray[0] = 0
                distanceArray[1] = 0
                debugLog { STLogUtil.e(tag, ".\n\n滚动彻底结束...强制返回(dx:${distanceArray[0]}, dy:${distanceArray[1]})\n\n.") }

                notifyOnSnapped(targetPosition, layoutManager)
            }
            return distanceArray
        }

        private fun canScrollStartToEnd(linearLayoutManager: LinearLayoutManager): Boolean = !isAtEndOfList(linearLayoutManager)
        private fun canScrollEndToStart(linearLayoutManager: LinearLayoutManager): Boolean = !isAtStartOfList(linearLayoutManager)

        private fun innerCalculateDistanceToFinalSnap(linearLayoutManager: LinearLayoutManager, itemView: View): IntArray {
            val distanceArray = IntArray(2)
            if (linearLayoutManager.canScrollHorizontally()) {
                if (snap == Snap.START && !linearLayoutManager.reverseLayout || snap == Snap.END && linearLayoutManager.reverseLayout) {
                    distanceArray[0] = distanceToStart(itemView, linearLayoutManager, getHorizontalHelper(linearLayoutManager))
                } else if (snap == Snap.CENTER) {
                    distanceArray[0] = distanceToCenter(linearLayoutManager, itemView, getHorizontalHelper(linearLayoutManager))
                } else {
                    distanceArray[0] = distanceToEnd(itemView, linearLayoutManager, getHorizontalHelper(linearLayoutManager))
                }
            } else {
                distanceArray[0] = 0
            }

            if (linearLayoutManager.canScrollVertically()) {
                if (snap == Snap.START && !linearLayoutManager.reverseLayout || snap == Snap.END && linearLayoutManager.reverseLayout) {
                    distanceArray[1] = distanceToStart(itemView, linearLayoutManager, getVerticalHelper(linearLayoutManager))
                } else if (snap == Snap.CENTER) {
                    distanceArray[1] = distanceToCenter(linearLayoutManager, itemView, getVerticalHelper(linearLayoutManager))
                } else {
                    distanceArray[1] = distanceToEnd(itemView, linearLayoutManager, getVerticalHelper(linearLayoutManager))
                }
            } else {
                distanceArray[1] = 0
            }
            debugLog { STLogUtil.d(tag, "-- 计算 距离目标位置 待滚动距离:(dx:${distanceArray[0]}, dy:${distanceArray[1]})") }
            return distanceArray
        }

        private fun notifyOnSnapped(willSnapPosition: Int, linearLayoutManager: LinearLayoutManager) {
            debugLog { STLogUtil.e(tag, "notifyOnSnapped start") }
            var targetPosition = willSnapPosition
            debugLog { STLogUtil.v(tag, "notifyOnSnapped firstVisible:${linearLayoutManager.findFirstVisibleItemPosition()},firstCompletelyVisible:${linearLayoutManager.findFirstCompletelyVisibleItemPosition()},lastVisible:${linearLayoutManager.findLastVisibleItemPosition()},lastCompletelyVisible:${linearLayoutManager.findLastCompletelyVisibleItemPosition()}, isAtEndOfList=${isAtEndOfList(linearLayoutManager)}, isAtStartOfList=${isAtStartOfList(linearLayoutManager)}") }
            debugLog { STLogUtil.v(tag, "notifyOnSnapped willScrollToTargetPosition=$willScrollToTargetPosition, targetPosition=$targetPosition, childCount=${linearLayoutManager.childCount}, itemCount=${linearLayoutManager.itemCount}, enableLoadingFooterView=$enableLoadingFooterView") }

            val itemCount: Int = linearLayoutManager.itemCount
            if (targetPosition != RecyclerView.NO_POSITION && targetPosition == itemCount - 1) {
                targetPosition = if (enableLoadingFooterView) targetPosition - 1 else targetPosition
                debugLog { STLogUtil.w(tag, "notifyOnSnapped 检测到滚动到边界, ${if (enableLoadingFooterView) "由于 loading view, targetPosition 需要 -1" else "由于不包含 loading view, targetPosition 无需 -1"}") }
            }

            willScrollToTargetPosition = RecyclerView.NO_POSITION
            if (targetPosition != RecyclerView.NO_POSITION && targetPosition >= 0 && targetPosition < (if (enableLoadingFooterView) itemCount - 1 else itemCount)) {
                debugLog { STLogUtil.d(tag, "notifyOnSnapped(position=$targetPosition)") }
                onSnap?.invoke(targetPosition)
                lastSnappedPosition = targetPosition
            } else {
                debugLog { STLogUtil.e(tag, "targetPosition:$targetPosition is invalid, do not onSnap!") }
            }
            debugLog { STLogUtil.e(tag, "notifyOnSnapped end") }
        }

        private fun distanceToStart(targetView: View, linearLayoutManager: LinearLayoutManager, helper: OrientationHelper): Int {
            val tmpRecyclerView = recyclerView ?: return 0
            val position = tmpRecyclerView.getChildLayoutPosition(targetView)
            val decoratedStart = helper.getDecoratedStart(targetView)
            val startAfterPadding = helper.startAfterPadding

            debugLog { STLogUtil.w(tag, "-------- 0 distance=$decoratedStart, position=$position, reverse=${linearLayoutManager.reverseLayout}, itemCount=${linearLayoutManager.itemCount}, decoratedStart=$decoratedStart, startAfterPadding=$startAfterPadding") }
            return decoratedStart
        }

        private fun distanceToCenter(layoutManager: LinearLayoutManager, targetView: View, helper: OrientationHelper): Int {
            val insets = Rect()
            layoutManager.calculateItemDecorationsForChild(targetView, insets)
            val isHorizontal = layoutManager.orientation == LinearLayoutManager.HORIZONTAL
            val childCenter = helper.getDecoratedStart(targetView) + ((if (isHorizontal) insets.left else insets.top) / 2) + helper.getDecoratedMeasurement(targetView) / 2 - ((if (isHorizontal) insets.right else insets.bottom) / 2)
            val containerCenter: Int
            if (layoutManager.clipToPadding) {
                containerCenter = helper.startAfterPadding + helper.totalSpace / 2
            } else {
                containerCenter = helper.end / 2
            }
            return childCenter - containerCenter
        }

        private fun distanceToEnd(targetView: View, linearLayoutManager: LinearLayoutManager, helper: OrientationHelper): Int {
            val tmpRecyclerView = recyclerView ?: return 0
            val position = tmpRecyclerView.getChildLayoutPosition(targetView)
            val decoratedEnd: Int = helper.getDecoratedEnd(targetView) - helper.end
            val startAfterPadding = helper.startAfterPadding

            debugLog { STLogUtil.w(tag, "-------- 0 distance=$decoratedEnd, position=$position, reverse=${linearLayoutManager.reverseLayout}, itemCount=${linearLayoutManager.itemCount}, decoratedEnd=$decoratedEnd, startAfterPadding=$startAfterPadding") }
            return decoratedEnd
        }

        /**
         * reverseLayout 与 findLastCompletelyVisibleItemPosition 是正相关, 所以这里无需特殊处理
         */
        private fun isAtStartOfList(linearLayoutManager: LinearLayoutManager): Boolean {
            return linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0
        }

        /**
         * reverseLayout 与 findLastCompletelyVisibleItemPosition 是正相关, 所以这里无需特殊处理
         */
        private fun isAtEndOfList(linearLayoutManager: LinearLayoutManager): Boolean {
            return linearLayoutManager.findLastCompletelyVisibleItemPosition() == linearLayoutManager.itemCount - 1
        }

        private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
            val helper: OrientationHelper = this.verticalHelper ?: OrientationHelper.createVerticalHelper(layoutManager)
            if (this.verticalHelper == null) this.verticalHelper = helper
            return helper
        }

        private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
            val helper: OrientationHelper = this.horizontalHelper ?: OrientationHelper.createHorizontalHelper(layoutManager)
            if (this.horizontalHelper == null) this.horizontalHelper = helper
            return helper
        }
    }
}