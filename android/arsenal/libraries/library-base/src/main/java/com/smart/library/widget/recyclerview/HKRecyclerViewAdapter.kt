package com.smart.library.widget.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.smart.library.widget.recyclerview.helper.HKRecyclerViewItemTouchHelperAdapter
import java.util.*

abstract class HKRecyclerViewAdapter<Entity, ViewHolder : RecyclerView.ViewHolder>(var context: Context, var dataList: ArrayList<Entity>) : RecyclerView.Adapter<ViewHolder>(), HKRecyclerViewItemTouchHelperAdapter {

    override fun getItemCount(): Int {
        return dataList.size
    }

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
        if (newList != null && !newList.isEmpty()) {
            val oldSize = dataList.size
            dataList.addAll(newList)
            notifyItemRangeChanged(oldSize - 1, newList.size + 1)
        }
    }

    fun add(entity: Entity, position: Int) {
        if (position >= 0 && position <= dataList.size) {
            dataList.add(entity)
            notifyItemInserted(position)
            notifyItemRangeChanged(position, dataList.size - position)
        }
    }

    fun notifyItemRangeChanged() {
        notifyItemRangeChanged(0, dataList.size)
    }

    override fun onItemDismiss(position: Int) {
        remove(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        Collections.swap(dataList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        return true
    }
}