package com.smart.library.widget.recyclerview.snap

import android.os.Looper
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View

@Suppress("unused")
class STSnapGravityPagerHelper @JvmOverloads constructor(snap: STSnapGravityHelper.Snap, onSnap: ((position: Int) -> Unit)? = null) : PagerSnapHelper() {

    private val delegate: STSnapGravityHelper.GSSnapGravityDelegate = STSnapGravityHelper.GSSnapGravityDelegate(snap, onSnap)

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        delegate.attachToRecyclerView(recyclerView)
        super.attachToRecyclerView(recyclerView)
    }

    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? = delegate.calculateDistanceToFinalSnap(layoutManager, targetView)

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? = delegate.findSnapView(layoutManager)

    /**
     * force snap after inner data changed, for example snap immediately after recyclerView init or adapter data changed
     * called must be after recycler setAdapter
     *
     * 注意:
     *    如果想首次加载触发 onSnap 0 回调, 则初始化 adapter 时传入空的数组, 然后调用 STRecyclerViewAdapter.add
     *    强制触发 onSnap (在 STRecyclerViewAdapter.onInnerDataChanged 中调用 STSnapGravityHelper.forceSnap)
     */
    fun forceSnap(layoutManager: RecyclerView.LayoutManager?) {
        if (layoutManager != null) {
            Looper.myQueue().addIdleHandler {
                delegate.findSnapView(layoutManager)
                false
            }
        }
    }

    @JvmOverloads
    fun scrollToPosition(position: Int, smooth: Boolean = true) = delegate.scrollToPosition(position, smooth)
}
