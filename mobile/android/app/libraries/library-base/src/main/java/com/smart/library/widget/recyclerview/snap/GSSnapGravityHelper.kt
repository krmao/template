package com.smart.library.widget.recyclerview.snap

import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * 实验证明: START/END 与 reverseLayout 强相关
 * 实验证明: firstVisible/lastCompletelyVisible 与 reverseLayout 强相关
 * recyclerView 滚动时, 每次滚动结束 第一个可见项将自适应完全可见并对其 recyclerView 顶部(顶部测试没问题,底部如果有 loadMore/emptyView 等可能有问题)
 * 暂不支持 clipToPadding and rtl
 *
 * 注意:
 *     如果想首次加载触发 onSnap 0 回调, 则初始化 adapter 时传入空的数组, 然后调用 GSRecyclerViewAdapter.add
 *     强制触发 onSnap (在 GSRecyclerViewAdapter.onInnerDataChanged 中调用 GSSnapGravityHelper.forceSnap)
 */
/*
    val snapGravityHelper: GSSnapHelper = if (recyclerViewOrientation == LinearLayoutManager.HORIZONTAL && enableHorizontalSnapOnlySmoothScrollOneItem) {
                    GSSnapGravityPagerHelper(viewLoadingEnabled, snap) { position: Int ->
                        if (position >= 0 && position < adapterWrapper.innerData().size) {
                            onSnap.invoke(pagerIndex, position, adapterWrapper.innerData()[position])
                        }
                    }
                } else {
                    GSSnapGravityHelper(viewLoadingEnabled, snap) { position: Int ->
                        if (position >= 0 && position < adapterWrapper.innerData().size) {
                            onSnap.invoke(pagerIndex, position, adapterWrapper.innerData()[position])
                        }
                    }
                }
    // gravity snap
    snapGravityHelper.attachToRecyclerView(recyclerView)
 */
@Suppress("unused")
class GSSnapGravityHelper @JvmOverloads constructor(enableLoadingFooterView: Boolean, snap: GSSnapHelper.Snap, onSnap: ((position: Int) -> Unit)? = null) : LinearSnapHelper(), GSSnapHelper {
    private val delegate: GSSnapHelper.GSSnapGravityDelegate = GSSnapHelper.GSSnapGravityDelegate(enableLoadingFooterView, snap, onSnap)

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        delegate.attachToRecyclerView(recyclerView)
        super.attachToRecyclerView(recyclerView)
    }

    override fun debugLog(logHandler: () -> Unit) = delegate.debugLog(logHandler)
    override fun orientation(): Int = delegate.orientation
    override fun lastSnappedPosition(): Int = delegate.lastSnappedPosition
    override fun recyclerView(): RecyclerView? = delegate.recyclerView
    override fun forceSnap() = forceSnap(lastSnappedPosition(), null)
    override fun forceSnap(onSnappedCallback: ((success: Boolean) -> Unit)?) = forceSnap(lastSnappedPosition(), onSnappedCallback)
    override fun scrollToPositionWithOnSnap(position: Int) = scrollToPositionWithOnSnap(position, true, null)
    override fun scrollToPositionWithOnSnap(position: Int, onScrolledCallback: ((success: Boolean) -> Unit)?) = scrollToPositionWithOnSnap(position, true, onScrolledCallback)
    override fun scrollToPositionWithOnSnap(position: Int, smooth: Boolean, onScrolledCallback: ((success: Boolean) -> Unit)?) = delegate.scrollToPositionWithOnSnap(position, smooth, onScrolledCallback)
    override fun scrollToPositionWithoutOnSnap(position: Int, onScrolledCallback: ((success: Boolean) -> Unit)?) = delegate.scrollToPositionWithoutOnSnap(position, onScrolledCallback)
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? = delegate.findSnapView(layoutManager)
    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? = delegate.calculateDistanceToFinalSnap(layoutManager, targetView)
    override fun forceSnap(targetPosition: Int, onSnappedCallback: ((success: Boolean) -> Unit)?) = delegate.forceSnap(targetPosition, onSnappedCallback)
    override fun switchSnap(snap: GSSnapHelper.Snap) {
        delegate.snap = snap
    }

    override fun enableDebug(enable: Boolean) {
        delegate.debugLog = enable
    }
}

