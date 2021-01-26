package com.smart.library.widget.recyclerview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.*
import com.smart.library.widget.recyclerview.helper.STRecyclerViewItemTouchHelperAdapter
import java.util.*
import kotlin.math.abs

@Suppress("unused", "MemberVisibilityCanPrivate")
abstract class STRecyclerViewAdapter<Entity, ViewHolder : RecyclerView.ViewHolder>(var context: Context?, var dataList: MutableList<Entity>) : RecyclerView.Adapter<ViewHolder>(), STRecyclerViewItemTouchHelperAdapter {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class ExtrasViewHolder(itemView: View, val extras: Any? = null) : RecyclerView.ViewHolder(itemView)
    class BindingViewHolder<T>(val binding: T, rootView: View) : RecyclerView.ViewHolder(rootView)

    fun resetDataList(dataList: MutableList<Entity>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

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
        private set
    val orientation: Int
        get() = when (val layoutManager: RecyclerView.LayoutManager? = this.recyclerView?.layoutManager) {
            is LinearLayoutManager -> layoutManager.orientation
            is GridLayoutManager -> layoutManager.orientation
            is StaggeredGridLayoutManager -> layoutManager.orientation
            else -> LinearLayoutManager.VERTICAL
        }

    private var oldAddDuration: Long = 120
    private var oldRemoveDuration: Long = 120
    private var oldMoveDuration: Long = 250
    private var oldChangeDuration: Long = 250
    private var oldItemAnimator: RecyclerView.ItemAnimator? = null

    /**
     * 注意: notifyDataSetChanged 依然会有闪动问题, 该方法不起作用, 详见第二种设置方法 {@link "https://stackoverflow.com/a/32488059/4348530"}
     */
    fun enableChangeAnimations(enableChangeAnimations: Boolean) {
        if (enableChangeAnimations) {
            recyclerView?.post {
                recyclerView?.itemAnimator?.addDuration = oldAddDuration
                recyclerView?.itemAnimator?.removeDuration = oldRemoveDuration
                recyclerView?.itemAnimator?.moveDuration = oldMoveDuration
                recyclerView?.itemAnimator?.changeDuration = oldChangeDuration
                (recyclerView?.itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = true
                oldItemAnimator?.let { recyclerView?.itemAnimator = it }
            }
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
            recyclerView?.itemAnimator?.let { oldItemAnimator = it }
            recyclerView?.itemAnimator = null
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    /**
     * 备忘: 当 RecyclerView 固定高度(xx dp)时, 调用 notifyDataSetChanged 以及设置 enableChangeAnimations(false) 可能已经解决了闪动问题
     * 但是当 RecyclerView 的高度为 match_parent 时, 调用 notifyDataSetChanged 发现 enableChangeAnimations(false) 已经不起作用, 有闪动问题(父布局为 FrameLayout, FrameLayout 父布局为 ConstraintLayout)
     * 此时终极方案是 setHasStableIds(true) 以及重写 getItemId 返回 "相同数据的相同id" 解决
     *
     * fix blinking step 0 need set adapter.setHasStableIds(true)
     * fix blinking step 1 override getItemId and return same id if content is equal
     * @see "https://stackoverflow.com/a/32488059/4348530"
     */
    override fun getItemId(position: Int): Long {
        return dataList[position].hashCode().toLong()
    }
}
