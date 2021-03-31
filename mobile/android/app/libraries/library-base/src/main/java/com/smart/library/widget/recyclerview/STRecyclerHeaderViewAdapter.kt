package com.smart.library.widget.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.util.*

@Suppress("MemberVisibilityCanBePrivate", "unused")
@Keep
class STRecyclerHeaderViewAdapter<Entity>(@NonNull val innerAdapter: STRecyclerViewAdapter<Entity, RecyclerView.ViewHolder>) : STRecyclerViewAdapter<Entity, RecyclerView.ViewHolder>(innerAdapter.context, innerAdapter.dataList) {

    private val headerViewDataList: MutableList<HeaderFooterViewData> = ArrayList()
    private val footerViewDataList: MutableList<HeaderFooterViewData> = ArrayList()
    private val observer: AdapterDataObserver = object : AdapterDataObserver() {
        override fun onChanged() = notifyDataSetChanged()
        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) = notifyItemRangeChanged(headersCount + positionStart, itemCount, payload)
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = notifyItemRangeInserted(headersCount + positionStart, itemCount)
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = notifyItemRangeRemoved(headersCount + positionStart, itemCount)
        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) = notifyItemMoved(headersCount + fromPosition, headersCount + toPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder { // 根据viewType查找对应的HeaderView 或 FooterView。如果没有找到则表示该viewType是普通的列表项。
        return findHeaderOrFooterView(viewType)?.let { InnerHeaderViewHolder(it) } ?: innerAdapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) { // 如果是HeaderView 或者是 FooterView则不绑定数据。
        // 因为HeaderView和FooterView是由外部传进来的，它们不由列表去更新。
        if (isHeader(position) || isFooter(position)) {
            return
        }
        //将列表实际的position调整成mAdapter对应的position。
        val adjPosition: Int = position - headersCount
        innerAdapter.onBindViewHolder(holder, adjPosition)
    }

    override fun getItemCount(): Int = headerViewDataList.size + footerViewDataList.size + innerAdapter.itemCount

    override fun getItemViewType(position: Int): Int { //如果当前item是HeaderView，则返回HeaderView对应的itemViewType。
        if (isHeader(position)) {
            return headerViewDataList[position].itemViewType
        }
        //如果当前item是HeaderView，则返回HeaderView对应的itemViewType。
        if (isFooter(position)) {
            return footerViewDataList[position - headerViewDataList.size - innerAdapter.itemCount].itemViewType
        }
        //将列表实际的position调整成mAdapter对应的position。
        val adjPosition: Int = position - headersCount
        return innerAdapter.getItemViewType(adjPosition)
    }

    /**
     * 判断当前位置是否是头部View。
     *
     * @param position 这里的position是整个列表(包含HeaderView和FooterView)的position。
     * @return
     */
    fun isHeader(position: Int): Boolean {
        return position < headersCount
    }

    /**
     * 判断当前位置是否是尾部View。
     *
     * @param position 这里的position是整个列表(包含HeaderView和FooterView)的position。
     * @return
     */
    fun isFooter(position: Int): Boolean {
        return itemCount - position <= footersCount
    }

    /**
     * 获取HeaderView的个数
     *
     * @return
     */
    val headersCount: Int
        get() = headerViewDataList.size

    /**
     * 获取FooterView的个数
     *
     * @return
     */
    val footersCount: Int
        get() = footerViewDataList.size

    /**
     * 添加HeaderView
     *
     * @param view
     */
    fun addHeaderView(view: View, notifyDataSetChanged: Boolean = true) {
        addHeaderView(view, generateUniqueViewType(), notifyDataSetChanged)
    }

    private fun addHeaderView(view: View?, viewType: Int, notifyDataSetChanged: Boolean) { //包装HeaderView数据并添加到列表
        if (view != null) {
            val data = HeaderFooterViewData()
            data.view = view
            data.itemViewType = viewType
            headerViewDataList.add(data)
            if (notifyDataSetChanged) notifyDataSetChanged()
        }
    }

    /**
     * 添加HeaderView
     *
     * @param view
     */
    fun addHeaderViews(notifyDataSetChanged: Boolean = true, vararg views: View?) {
        views.forEach { view: View? ->
            addHeaderView(view, generateUniqueViewType(), false)
        }
        if (notifyDataSetChanged) notifyItemRangeChanged()
    }

    /**
     * 删除HeaderView
     *
     * @param view
     * @return 是否删除成功
     */
    fun removeHeaderView(view: View): Boolean {
        for (data in headerViewDataList) {
            if (data.view === view) {
                headerViewDataList.remove(data)
                notifyDataSetChanged()
                return true
            }
        }
        return false
    }

    /**
     * 添加FooterView
     *
     * @param view
     */
    fun addFooterView(view: View, notifyDataSetChanged: Boolean = true) {
        addFooterView(view, generateUniqueViewType(), notifyDataSetChanged)
    }

    private fun addFooterView(view: View, viewType: Int, notifyDataSetChanged: Boolean) { // 包装FooterView数据并添加到列表
        val data = HeaderFooterViewData()
        data.view = view
        data.itemViewType = viewType
        footerViewDataList.add(data)
        if (notifyDataSetChanged) notifyDataSetChanged()
    }

    /**
     * 删除FooterView
     *
     * @param view
     * @return 是否删除成功
     */
    fun removeFooterView(view: View): Boolean {
        for (data in footerViewDataList) {
            if (data.view === view) {
                footerViewDataList.remove(data)
                notifyDataSetChanged()
                return true
            }
        }
        return false
    }

    /**
     * 生成一个唯一的数，用于标识HeaderView或FooterView的type类型，并且保证类型不会重复。
     *
     * @return
     */
    private fun generateUniqueViewType(): Int {
        val count: Int = itemCount
        while (true) { //生成一个随机数。
            val viewType: Int = (Math.random() * Int.MAX_VALUE).toInt() + 1
            //判断该viewType是否已使用。
            var isExist = false
            for (i: Int in 0 until count) {
                if (viewType == getItemViewType(i)) {
                    isExist = true
                    break
                }
            }
            //判断该viewType还没被使用，则返回。否则进行下一次循环，重新生成随机数。
            if (!isExist) {
                return viewType
            }
        }
    }

    /**
     * 根据viewType查找对应的HeaderView 或 FooterView。没有找到则返回null。
     *
     * @param viewType 查找的viewType
     * @return
     */
    private fun findHeaderOrFooterView(viewType: Int): View? {
        for (headerViewItem: HeaderFooterViewData in headerViewDataList) {
            if (headerViewItem.itemViewType == viewType) {
                return headerViewItem.view
            }
        }
        for (footerViewItem: HeaderFooterViewData in footerViewDataList) {
            if (footerViewItem.itemViewType == viewType) {
                return footerViewItem.view
            }
        }
        return null
    }

    override fun onViewAttachedToWindow(@NonNull holder: RecyclerView.ViewHolder) {
        if (holder is InnerHeaderViewHolder) {
            super.onViewAttachedToWindow(holder)
        } else {
            innerAdapter.onViewAttachedToWindow(holder)
        }
        //处理StaggeredGridLayout，保证HeaderView和FooterView占满一行。
        if (isStaggeredGridLayout(holder)) {
            handleLayoutIfStaggeredGridLayout(holder, holder.layoutPosition)
        }
    }

    private fun isStaggeredGridLayout(holder: RecyclerView.ViewHolder): Boolean {
        val layoutParams: ViewGroup.LayoutParams? = holder.itemView.layoutParams
        return layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams
    }

    private fun handleLayoutIfStaggeredGridLayout(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeader(position) || isFooter(position)) {
            val layoutParams: StaggeredGridLayoutManager.LayoutParams? = holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams
            layoutParams?.isFullSpan = true
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) = if (holder is InnerHeaderViewHolder) super.onViewDetachedFromWindow(holder) else innerAdapter.onViewDetachedFromWindow(holder)
    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean = if (holder is InnerHeaderViewHolder) super.onFailedToRecycleView(holder) else innerAdapter.onFailedToRecycleView(holder)
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        innerAdapter.onAttachedToRecyclerView(recyclerView)
        innerAdapter.registerAdapterDataObserver(observer)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        innerAdapter.unregisterAdapterDataObserver(observer)
        innerAdapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) = if (holder is InnerHeaderViewHolder) super.onViewRecycled(holder) else innerAdapter.onViewRecycled(holder)

    /**
     * 用于包装HeaderView和FooterView的数据类
     */
    private class HeaderFooterViewData {
        var view: View? = null
        var itemViewType = 0
    }

    private class InnerHeaderViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
}