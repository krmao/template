package com.smart.library.widget.recyclerview.snap

import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import android.view.View

@Suppress("unused", "MemberVisibilityCanBePrivate")
class STSnapGravityPagerHelper @JvmOverloads constructor(enableLoadingFooterView: Boolean, snap: STSnapHelper.Snap, onSnap: ((position: Int) -> Unit)? = null) : PagerSnapHelper(), STSnapHelper {

    private val delegate: STSnapHelper.STSnapGravityDelegate = STSnapHelper.STSnapGravityDelegate(enableLoadingFooterView, snap, onSnap)

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
    override fun switchSnap(snap: STSnapHelper.Snap) {
        delegate.snap = snap
    }

    override fun enableDebug(enable: Boolean) {
        delegate.debugLog = enable
    }
}
