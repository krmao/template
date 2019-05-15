package com.smart.library.widget.recyclerview.snap

import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View

@Suppress("unused")
class CXSnapGravityPagerHelper @JvmOverloads constructor(gravity: Int, enableSnapLastItem: Boolean = false, enableLastSnapCompletelyVisible: Boolean = true, snapListener: CXSnapGravityHelper.SnapListener? = null) : PagerSnapHelper() {

    private val delegate: CXSnapGravityHelper.GSSnapGravityDelegate = CXSnapGravityHelper.GSSnapGravityDelegate(gravity, enableSnapLastItem, enableLastSnapCompletelyVisible, snapListener)

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
}
