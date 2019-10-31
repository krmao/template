package com.smart.library.widget.recyclerview.snap

import android.os.Looper
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.smart.library.util.STLogUtil
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper
import kotlin.math.abs

/**
 * 实验证明: START/END 与 reverseLayout 强相关
 * 实验证明: firstVisible/lastCompletelyVisible 与 reverseLayout 强相关
 * recyclerView 滚动时, 每次滚动结束 第一个可见项将自适应完全可见并对其 recyclerView 顶部(顶部测试没问题,底部如果有 loadMore/emptyView 等可能有问题)
 * 暂不支持 clipToPadding and rtl
 *
 * 注意:
 *     如果想首次加载触发 onSnap 0 回调, 则初始化 adapter 时传入空的数组, 然后调用 STRecyclerViewAdapter.add
 *     强制触发 onSnap (在 STRecyclerViewAdapter.onInnerDataChanged 中调用 STSnapGravityHelper.forceSnap)
 */
/*

    private var pageIndex = 0
    private var pageSize = 10
    private fun getDataList(): MutableList<String> {
        val toPageIndex = pageIndex + 1
        val tmpList = ((pageIndex * pageSize) until toPageIndex * pageSize).map { "第 $it 天" }.toMutableList()
        pageIndex = toPageIndex
        return tmpList
    }

    @Suppress("PrivatePropertyName")
    private val adapter: STRecyclerViewAdapter<String, RecyclerView.ViewHolder> by lazy {
        object : STRecyclerViewAdapter<String, RecyclerView.ViewHolder>(context, mutableListOf()) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): STViewHolder {
                return STViewHolder(LayoutInflater.from(context).inflate(R.layout.home_fragment_recycler_view_drag_and_transfer_item_days, container, false))
            }

            override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
                viewHolder.itemView.textViewDays.text = dataList[position]
            }
        }
    }

    private val adapterWrapper by lazy { STEmptyLoadingWrapper(adapter) }
    private val snapGravityHelper by lazy {
        STSnapGravityHelper(
                Gravity.START,
                object : STSnapGravityHelper.SnapListener {
                    override fun onSnap(position: Int) {
                        STLogUtil.e("Snapped", position.toString())
                    }
                },
                debug = true
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        removeAll.setOnClickListener {
            adapterWrapper.removeAll()
        }
        removeOne.setOnClickListener {
            adapterWrapper.remove(0)
        }
        addAll.setOnClickListener {
            if (adapterWrapper.isInnerDataEmpty()) pageIndex = 0
            adapterWrapper.add(getDataList())
        }
        addEnd.setOnClickListener {
            adapterWrapper.add("insert at end of list at ${STTimeUtil.HmsS(System.currentTimeMillis())}")
        }
        addAt0.setOnClickListener {
            adapterWrapper.add("insert at 0 at ${STTimeUtil.HmsS(System.currentTimeMillis())}", 0)
        }
        disable.setOnClickListener {
            adapterWrapper.enable = !adapterWrapper.enable
        }
        showFailure.setOnClickListener {
            adapterWrapper.showLoadFailure()
        }
        showNoMore.setOnClickListener {
            adapterWrapper.showNoMore()
        }
        showLoading.setOnClickListener {
            adapterWrapper.showLoading()
        }

        // divider between items
        recyclerView.addItemDecoration(STRecyclerViewItemDecoration(5))
        // custom loading views
        adapterWrapper.viewNoMore = adapterWrapper.createDefaultFooterView("-- 呵呵, 真的没有更多了 --")
        adapterWrapper.viewLoadFailure = adapterWrapper.createDefaultFooterView("啊哟, 加载失败了哟")
        adapterWrapper.viewLoading = adapterWrapper.createDefaultFooterView("哼哈, 火速请求中...")

        adapterWrapper.onInnerDataChanged = {
            snapGravityHelper.forceSnap(recyclerView.layoutManager, it.isEmpty()) // force snap after inner data changed
        }

        // onLoadMore listener
        var flag = true
        adapterWrapper.onLoadMoreListener = {
            recyclerView.postDelayed({
                if (flag) {
                    if (adapterWrapper.itemCount >= 30) {
                        adapterWrapper.showNoMore()
                    } else {
                        adapterWrapper.add(getDataList())
                    }
                    if (adapterWrapper.itemCount == 20 + 1) flag = false
                } else {
                    adapterWrapper.showLoadFailure()
                    flag = true
                }
            }, 1000)
        }

        recyclerView.adapter = adapterWrapper
        // gravity snap
        snapGravityHelper.attachToRecyclerView(recyclerView)

        // if want force invoke onSnap 0, must call adapterWrapper.add after setAdapter and snapGravityHelper.attachToRecyclerView(recyclerView)
        adapterWrapper.add(getDataList())
    }
 */
@Suppress("unused")
class STSnapGravityHelper @JvmOverloads constructor(private var snap: Snap, private val onSnap: ((position: Int) -> Unit)? = null) : LinearSnapHelper() {
    private val tag = "Gravity-Snap"
    private val delegate: GSSnapGravityDelegate = GSSnapGravityDelegate(snap, onSnap)

    fun switchSnap(snap: Snap) {
        this.snap = snap
        delegate.snap = snap
    }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        delegate.attachToRecyclerView(recyclerView)
        super.attachToRecyclerView(recyclerView)
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        STLogUtil.e(tag, "检测到滚动即将结束, 开始查找目标, 即将执行 findSnapView")
        return delegate.findSnapView(layoutManager)
    }

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        STLogUtil.e(tag, "计算继续滚动的距离, 即将执行 calculateDistanceToFinalSnap(targetView=${targetView.hashCode()})")
        return delegate.calculateDistanceToFinalSnap(layoutManager, targetView)
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
    fun forceSnap(layoutManager: RecyclerView.LayoutManager?, isInnerDataEmpty: Boolean) {
        if (isInnerDataEmpty) {
            onSnap?.invoke(RecyclerView.NO_POSITION)
            delegate.lastSnappedPosition = RecyclerView.NO_POSITION
        } else if (layoutManager != null) {
            Looper.myQueue().addIdleHandler {
                delegate.findSnapView(layoutManager)
                false
            }
        }
    }

    val lastSnappedPosition: Int
        get() = delegate.lastSnappedPosition

    @JvmOverloads
    fun scrollToPosition(position: Int, smooth: Boolean = true) = delegate.scrollToPosition(position, smooth)

    interface SnapListener {
        /**
         * may be -1 or RecyclerView.NO_POSITION or other invalid/repeat position
         * please check the position invalid by self
         */
        fun onSnap(position: Int)
    }

    enum class Snap {
        START,
        CENTER,
        END
    }

    internal class GSSnapGravityDelegate(var snap: Snap, private val onSnap: ((position: Int) -> Unit)? = null) {

        private val tag = "Gravity-Snap"
        private var verticalHelper: OrientationHelper? = null
        private var horizontalHelper: OrientationHelper? = null
        private val enableLoadingFooterView: Boolean by lazy { recyclerView?.adapter is STEmptyLoadingWrapper<*> } // 是否开启了 loading footer view
        private var recyclerView: RecyclerView? = null
        private var willScrollToTargetPosition: Int = RecyclerView.NO_POSITION
        private val scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING && willScrollToTargetPosition != RecyclerView.NO_POSITION) {
                    willScrollToTargetPosition = RecyclerView.NO_POSITION // 拖拽情况下 强制清空之前的指定目标 position by scrollToPosition
                    STLogUtil.e(tag, "拖拽情况下 强制清空之前的指定目标 willScrollToTargetPosition=$willScrollToTargetPosition by scrollToPosition")
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
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

        @JvmOverloads
        fun scrollToPosition(position: Int, smooth: Boolean = true) {
            if (position >= 0 && position < recyclerView?.adapter?.itemCount ?: 0) {
                var needScroll = true
                if (recyclerView?.layoutManager != null) {
                    val viewHolder = recyclerView?.findViewHolderForAdapterPosition(position)
                    if (viewHolder != null) {
                        val distanceArray = calculateDistanceToFinalSnap(recyclerView?.layoutManager!!, viewHolder.itemView)
                        needScroll = distanceArray[0] != 0 || distanceArray[1] != 0
                        STLogUtil.e(tag, "--> 执行滚动到指定位置 由于目标 itemView 在缓存内 直接计算滚动距离 smoothScrollBy:(x:${distanceArray[0]}, y:${distanceArray[1]})  ${if (needScroll) "需要滚动" else "无需滚动"} ")
                        if (needScroll) {
                            if (smooth) {
                                recyclerView?.smoothScrollBy(distanceArray[0], distanceArray[1])
                            } else {
                                recyclerView?.scrollBy(distanceArray[0], distanceArray[1])
                            }
                        }
                    } else {
                        STLogUtil.e(tag, "--> 执行滚动到指定位置 由于目标 itemView 不在缓存内 直接滚动 position -> smoothScrollToPosition:$position")
                        if (smooth) {
                            willScrollToTargetPosition = position
                            recyclerView?.smoothScrollToPosition(position)
                        } else {
                            recyclerView?.scrollToPosition(position)
                        }
                    }
                }
                if (!needScroll) {
                    STLogUtil.e(tag, "--> 执行滚动到指定位置 由于目标可见且无法继续滚动, 强制回调 onSnap($position)")
                }
            }
        }

        /**
         * 该方法会找到当前layoutManager上最接近对齐位置的那个view，该view称为SnapView
         * 对应的position称为SnapPosition
         * 如果返回null,就表示没有需要对齐的View,也就不会做滚动对齐调整
         */
        fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
            val tmpRecyclerView = recyclerView
            if (tmpRecyclerView == null || layoutManager !is LinearLayoutManager) {
                STLogUtil.e(tag, "开始查找 返回空, 原因 tmpRecyclerView == null 或者 layoutManager !is LinearLayoutManager")
                willScrollToTargetPosition = RecyclerView.NO_POSITION
                STLogUtil.v(tag, ".\n\n滚动结束...\n\n.")
                return null
            }

            val linearLayoutManager: LinearLayoutManager = layoutManager
            val isAtStartOfList = isAtStartOfList(linearLayoutManager)
            val isAtEndOfList = isAtEndOfList(linearLayoutManager)
            STLogUtil.w(tag, "开始查找 isAtStartOfList:$isAtStartOfList, isAtEndOfList:$isAtEndOfList, firstVisible:${linearLayoutManager.findFirstVisibleItemPosition()},firstCompletelyV:${linearLayoutManager.findFirstCompletelyVisibleItemPosition()},lastVisible:${linearLayoutManager.findLastVisibleItemPosition()},lastCompletelyV:${linearLayoutManager.findLastCompletelyVisibleItemPosition()}, childCount=${linearLayoutManager.childCount}, itemCount=${linearLayoutManager.itemCount}")

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
                    STLogUtil.e(tag, "开始计算 findSnapView 由于滚动到边界, 根据规则强制指定 willScrollToTargetPosition=$willScrollToTargetPosition")
                }

                if (willScrollToTargetPosition != RecyclerView.NO_POSITION) {
                    for (childIndex: Int in 0 until linearLayoutManager.childCount) {
                        val tmpItemView: View? = linearLayoutManager.getChildAt(childIndex)
                        if (tmpItemView != null) {
                            if (linearLayoutManager.getPosition(tmpItemView) == willScrollToTargetPosition) {
                                targetSnapView = tmpItemView
                                STLogUtil.d(tag, "开始查找 findSnapView 找到 willScrollToTargetPosition 相对应的 targetSnapView:${targetSnapView.hashCode()}, 位于可见索引为:$childIndex")
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
                            STLogUtil.d(tag, "-- 获取 距离目标位置最近的当前屏幕内可见childView索引:$childIndex, minIndex=$minIndex minDistanceToTargetLocation=$minDistanceToTargetLocation")
                        }
                    }
                }
            }
            STLogUtil.w(tag, "查找结束 findSnapView targetSnapView:${targetSnapView.hashCode()}, targetPosition:${targetSnapView?.let { tmpRecyclerView.getChildAdapterPosition(it) }}")
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

        fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray {
            var distanceArray = IntArray(2)
            val tmpRecyclerView = recyclerView
            if (tmpRecyclerView == null || layoutManager !is LinearLayoutManager) {
                STLogUtil.e(tag, "开始计算 calculateDistanceToFinalSnap return, tmpRecyclerView == null 或者 layoutManager !is LinearLayoutManager")
                willScrollToTargetPosition = RecyclerView.NO_POSITION
                STLogUtil.v(tag, ".\n\n滚动结束...\n\n.")
                return distanceArray
            }
            val targetPosition: Int = tmpRecyclerView.getChildAdapterPosition(targetView)
            STLogUtil.d(tag, "开始计算 targetPosition=$targetPosition")
            distanceArray = innerCalculateDistanceToFinalSnap(layoutManager, targetView)

            // start scroll to end 为正
            if ((distanceArray[0] > 0 || distanceArray[1] > 0) && canScrollStartToEnd(layoutManager)) {
                STLogUtil.e(tag, ".\n\n继续滚动指定距离到目标位置...start scroll to end\n\n.")
            } else if ((distanceArray[0] < 0 || distanceArray[1] < 0) && canScrollEndToStart(layoutManager)) {
                STLogUtil.e(tag, ".\n\n继续滚动指定距离到目标位置...end scroll to start\n\n.")
            } else {
                STLogUtil.e(tag, "滚动即将结束, 此时(dx:${distanceArray[0]}, dy:${distanceArray[1]})")
                distanceArray[0] = 0
                distanceArray[1] = 0
                STLogUtil.e(tag, ".\n\n滚动彻底结束...强制返回(dx:${distanceArray[0]}, dy:${distanceArray[1]})\n\n.")

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
            STLogUtil.d(tag, "-- 计算 距离目标位置 待滚动距离:(dx:${distanceArray[0]}, dy:${distanceArray[1]})")
            return distanceArray
        }

        var lastSnappedPosition: Int = RecyclerView.NO_POSITION

        private fun notifyOnSnapped(willSnapPosition: Int, linearLayoutManager: LinearLayoutManager) {
            STLogUtil.e(tag, "notifyOnSnapped start")
            var targetPosition = willSnapPosition
            STLogUtil.v(tag, "notifyOnSnapped firstVisible:${linearLayoutManager.findFirstVisibleItemPosition()},firstCompletelyVisible:${linearLayoutManager.findFirstCompletelyVisibleItemPosition()},lastVisible:${linearLayoutManager.findLastVisibleItemPosition()},lastCompletelyVisible:${linearLayoutManager.findLastCompletelyVisibleItemPosition()}, isAtEndOfList=${isAtEndOfList(linearLayoutManager)}, isAtStartOfList=${isAtStartOfList(linearLayoutManager)}")
            STLogUtil.v(tag, "notifyOnSnapped willScrollToTargetPosition=$willScrollToTargetPosition, targetPosition=$targetPosition, childCount=${linearLayoutManager.childCount}, itemCount=${linearLayoutManager.itemCount}, enableLoadingFooterView=$enableLoadingFooterView")

            val itemCount: Int = linearLayoutManager.itemCount
            if (targetPosition != RecyclerView.NO_POSITION && targetPosition == itemCount - 1) {
                targetPosition = if (enableLoadingFooterView) targetPosition - 1 else targetPosition
                STLogUtil.w(tag, "notifyOnSnapped 检测到滚动到边界, ${if (enableLoadingFooterView) "由于 loading view, targetPosition 需要 -1" else "由于不包含 loading view, targetPosition 无需 -1"}")
            }

            willScrollToTargetPosition = RecyclerView.NO_POSITION
            if (targetPosition != RecyclerView.NO_POSITION && targetPosition >= 0 && targetPosition < (if (enableLoadingFooterView) itemCount - 1 else itemCount)) {
                STLogUtil.d(tag, "notifyOnSnapped(position=$targetPosition)")
                onSnap?.invoke(targetPosition)
                lastSnappedPosition = targetPosition
            } else {
                STLogUtil.e(tag, "targetPosition:$targetPosition is invalid, do not onSnap!")
            }
            STLogUtil.e(tag, "notifyOnSnapped end")
        }

        private fun distanceToStart(targetView: View, linearLayoutManager: LinearLayoutManager, helper: OrientationHelper): Int {
            val tmpRecyclerView = recyclerView ?: return 0
            val position = tmpRecyclerView.getChildLayoutPosition(targetView)
            val decoratedStart = helper.getDecoratedStart(targetView)
            val startAfterPadding = helper.startAfterPadding

            STLogUtil.w(tag, "-------- 0 distance=$decoratedStart, position=$position, reverse=${linearLayoutManager.reverseLayout}, itemCount=${linearLayoutManager.itemCount}, decoratedStart=$decoratedStart, startAfterPadding=$startAfterPadding")
            return decoratedStart
        }

        private fun distanceToCenter(layoutManager: RecyclerView.LayoutManager, targetView: View, helper: OrientationHelper): Int {
            val childCenter = helper.getDecoratedStart(targetView) + helper.getDecoratedMeasurement(targetView) / 2
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

            STLogUtil.w(tag, "-------- 0 distance=$decoratedEnd, position=$position, reverse=${linearLayoutManager.reverseLayout}, itemCount=${linearLayoutManager.itemCount}, decoratedEnd=$decoratedEnd, startAfterPadding=$startAfterPadding")
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
