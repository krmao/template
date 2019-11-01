package com.smart.library.widget.recyclerview.snap

import android.os.Looper
import android.support.v7.widget.*
import android.view.View
import com.smart.library.util.STLogUtil
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper

@Suppress("unused", "MemberVisibilityCanBePrivate")
class STSnapGravityPagerHelper @JvmOverloads constructor(private var snap: STSnapGravityHelper.Snap, private val onSnap: ((position: Int) -> Unit)? = null) : PagerSnapHelper() {

    private val delegate: STSnapGravityHelper.GSSnapGravityDelegate = STSnapGravityHelper.GSSnapGravityDelegate(snap, onSnap)

    fun switchSnap(snap: STSnapGravityHelper.Snap) {
        this.snap = snap
        delegate.snap = snap
    }

    var debugLog = false
        set(value) {
            field = value
            delegate.debugLog = value
        }
        get() = delegate.debugLog

    private fun debugLog(logHandler: () -> Unit) {
        delegate.debugLog(logHandler)
    }

    var recyclerView: RecyclerView? = null
        private set
    val orientation: Int
        get() = when (val layoutManager: RecyclerView.LayoutManager? = this.recyclerView?.layoutManager) {
            is LinearLayoutManager -> layoutManager.orientation
            is GridLayoutManager -> layoutManager.orientation
            is StaggeredGridLayoutManager -> layoutManager.orientation
            else -> LinearLayoutManager.VERTICAL
        }

    @Throws(IllegalArgumentException::class, IllegalStateException::class)
    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
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

    /**
     * force snap after inner data changed, for example snap immediately after recyclerView init or adapter data changed
     * called must be after recycler setAdapter
     *
     * 注意:
     *    如果想首次加载触发 onSnap 0 回调, 则初始化 adapter 时传入空的数组, 然后调用 STRecyclerViewAdapter.add
     *    强制触发 onSnap (在 STRecyclerViewAdapter.onInnerDataChanged 中调用 STSnapGravityHelper.forceSnap)
     *    这里不去重是为了 防止 adapter.remove(0) 的时候, onSnap 都是 0, 但是真实数据已经改变了, 虽然索引没有变化
     */
    fun forceSnap(targetPosition: Int = lastSnappedPosition, onSnappedCallback: ((success: Boolean) -> Unit)? = null) {
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
            delegate.lastSnappedPosition = RecyclerView.NO_POSITION
            onSnap?.invoke(RecyclerView.NO_POSITION)
            onSnappedCallback?.invoke(false)
        } else {
            scrollToPosition(targetPosition, false, onSnappedCallback)
        }
    }

    val lastSnappedPosition: Int
        get() = delegate.lastSnappedPosition

    @JvmOverloads
    fun scrollToPosition(position: Int, smooth: Boolean = true, onScrolledCallback: ((success: Boolean) -> Unit)? = null) = delegate.scrollToPosition(position, smooth, onScrolledCallback)

}
