package com.smart.library.widget.recyclerview

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import com.smart.library.widget.recyclerview.helper.STRecyclerViewItemTouchHelperAdapter
import java.util.*
import kotlin.math.abs

@Suppress("unused", "MemberVisibilityCanPrivate")
abstract class STRecyclerViewAdapter<Entity, ViewHolder : RecyclerView.ViewHolder>(var context: Context?, var dataList: MutableList<Entity>) : RecyclerView.Adapter<ViewHolder>(), STRecyclerViewItemTouchHelperAdapter {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun getItemCount(): Int = dataList.size

    fun remove(position: Int) {
        if (position >= 0 && position < dataList.size) {
            dataList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, dataList.size - position)
        }
    }

    fun removeAll() {
        val oldSize = dataList.size
        dataList.clear()
        notifyItemRangeRemoved(0, oldSize)
    }

    fun add(newList: List<Entity>?) {
        if (newList != null && newList.isNotEmpty()) {
            val oldSize = dataList.size
            dataList.addAll(newList)
            notifyItemRangeChanged(oldSize, newList.size)
        }
    }

    fun add(entity: Entity) {
        add(entity, dataList.size)
    }

    fun add(entity: Entity, position: Int) {
        if (position >= 0 && position <= dataList.size) {
            dataList.add(position, entity)
            notifyItemInserted(position)
            notifyItemRangeChanged(position, dataList.size - position)
        }
    }

    fun notifyItemRangeChanged() = notifyItemRangeChanged(0, dataList.size)

    override fun onItemDismiss(position: Int) = remove(position)

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(dataList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        notifyItemRangeChanged(fromPosition.coerceAtMost(toPosition), abs(fromPosition - toPosition) + 1)
        return true
    }

    var recyclerView: RecyclerView? = null
    val orientation: Int
        get() = (this.recyclerView?.layoutManager as? LinearLayoutManager)?.orientation ?: LinearLayoutManager.VERTICAL

    private var oldAddDuration: Long = 120
    private var oldRemoveDuration: Long = 120
    private var oldMoveDuration: Long = 250
    private var oldChangeDuration: Long = 250
    fun enableChangeAnimations(enableChangeAnimations: Boolean) {
        if (enableChangeAnimations) {
            recyclerView?.itemAnimator?.addDuration = oldAddDuration
            recyclerView?.itemAnimator?.removeDuration = oldRemoveDuration
            recyclerView?.itemAnimator?.moveDuration = oldMoveDuration
            recyclerView?.itemAnimator?.changeDuration = oldChangeDuration
            (recyclerView?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = true
        } else {
            oldAddDuration = recyclerView?.itemAnimator?.addDuration ?: oldAddDuration
            oldRemoveDuration = recyclerView?.itemAnimator?.removeDuration ?: oldRemoveDuration
            oldMoveDuration = recyclerView?.itemAnimator?.moveDuration ?: oldMoveDuration
            oldChangeDuration = recyclerView?.itemAnimator?.changeDuration ?: oldChangeDuration

            recyclerView?.itemAnimator?.addDuration = 0
            recyclerView?.itemAnimator?.removeDuration = 0
            recyclerView?.itemAnimator?.moveDuration = 0
            recyclerView?.itemAnimator?.changeDuration = 0
            (recyclerView?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }
}
