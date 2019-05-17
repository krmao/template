package com.smart.library.widget.recyclerview

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.TextView
import com.smart.library.util.CXSystemUtil

/*
    add loadMore/emptyView for RecyclerView.Adapter
    default is loading state and have default loadingViews

    > attention:
    avoid use onScrollListener to detect scroll to end list
    because sometimes scroll event doesn't call scroll_event_setting and scroll_event_idle
    after fast scroll_event_drag to end list

    auth by https://github.com/krmao

    > examples code below:

    // divider between items
    recyclerView.addItemDecoration(CXRecyclerViewItemDecoration(5))

    val adapterWrapper = CXEmptyLoadingWrapper(adapter)

    // custom loading views
    adapterWrapper.viewNoMore = adapterWrapper.createDefaultFooterView("-- 呵呵, 真的没有更多了 --")
    adapterWrapper.viewLoadFailure = adapterWrapper.createDefaultFooterView("啊哟, 加载失败了哟")
    adapterWrapper.viewLoading = adapterWrapper.createDefaultFooterView("哼哈, 火速请求中...")

    // onLoadMore listener
    var flag = true
    adapterWrapper.onLoadMoreListener = {
        recyclerView.postDelayed({
            if (flag) {
                if (adapterWrapper.itemCount >= 30) {
                    adapterWrapper.showNoMore()

                    // test removeAll
                    recyclerView.postDelayed({
                        adapterWrapper.removeAll()

                        // test disable
                        recyclerView.postDelayed({
                            adapterWrapper.enable = false

                            // test add one
                            recyclerView.postDelayed({
                                adapterWrapper.add("0 test")

                                // test remove one
                                recyclerView.postDelayed({
                                    adapterWrapper.remove(0)

                                    // test addAll
                                    recyclerView.postDelayed({
                                        pageIndex = 0
                                        adapterWrapper.add(getDataList())
                                        adapterWrapper.enable = true
                                        adapterWrapper.showLoading()
                                    }, 3000)
                                }, 3000)
                            }, 3000)
                        }, 3000)
                    }, 3000)

                } else {
                    adapterWrapper.add(getDataList())
                    // adapterWrapper.showLoading()
                }
                if (adapterWrapper.itemCount == 20 + 1) flag = false
            } else {
                adapterWrapper.showLoadFailure()
                flag = true
            }
        }, 1000)
    }

    recyclerView.adapter = adapterWrapper

 */
@Suppress("unused", "unused", "MemberVisibilityCanBePrivate")
@SuppressLint("SetTextI18n")
class CXEmptyLoadingWrapper<Entity>(private val innerAdapter: CXRecyclerViewAdapter<Entity, RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG: String = CXEmptyLoadingWrapper::class.java.simpleName

        private const val ITEM_TYPE_LOAD_FAILED = Integer.MAX_VALUE - 1
        private const val ITEM_TYPE_NO_MORE = Integer.MAX_VALUE - 2
        private const val ITEM_TYPE_LOADING = Integer.MAX_VALUE - 3
        private const val ITEM_TYPE_EMPTY = Integer.MAX_VALUE - 4
    }

    /**
     * default currentItemType = ITEM_TYPE_LOADING && enable = true
     */
    private var currentItemType = ITEM_TYPE_LOADING
    var enableEmptyView = true
    var enable = true
        set(value) {
            if (field != value) {
                field = value

                if (field) {
                    currentItemType = ITEM_TYPE_LOADING
                    notifyItemChanged(itemCount)
                } else {
                    currentItemType = RecyclerView.INVALID_TYPE
                    notifyItemRemoved(itemCount)
                }
            }
        }

    var onLoadMoreListener: (() -> Unit)? = null

    var viewLoadFailure: View? = null
    var viewLoading: View? = null
    var viewNoMore: View? = null
    var viewEmpty: View? = null
    private val viewHolderNoMore: CXViewHolder by lazy { CXViewHolder(viewNoMore ?: createDefaultFooterView("没有更多了")) }
    private val viewHolderLoading: CXViewHolder by lazy { CXViewHolder(viewLoading ?: createDefaultFooterView("正在加载中...")) }
    private val viewHolderLoadFailure: CXViewHolder by lazy { CXViewHolder(viewLoadFailure ?: createDefaultFooterView("加载失败，请点我重试")) }
    private val viewHolderEmpty: CXViewHolder by lazy { CXViewHolder(viewEmpty ?: createDefaultEmptyView("EMPTY DATA NOW ... \nCLICK TO REFRESH ...")) }

    override fun getItemViewType(position: Int): Int = if (isNeedShowEmptyView()) ITEM_TYPE_EMPTY else (if ((position == itemCount - 1) && enable) currentItemType else innerAdapter.getItemViewType(position))
    override fun getItemCount(): Int = if (isNeedShowEmptyView()) 1 else (innerAdapter.itemCount + (if (enable) 1 else 0))

    private fun isNeedShowEmptyView() = enableEmptyView && innerAdapter.itemCount == 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_EMPTY -> viewHolderEmpty
            ITEM_TYPE_NO_MORE -> viewHolderNoMore
            ITEM_TYPE_LOADING -> viewHolderLoading
            ITEM_TYPE_LOAD_FAILED -> viewHolderLoadFailure
            else -> innerAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_TYPE_EMPTY -> {
            }
            ITEM_TYPE_NO_MORE -> {
            }
            ITEM_TYPE_LOAD_FAILED -> {
                holder.itemView.setOnClickListener {
                    if (onLoadMoreListener != null) {
                        showLoading()
                    }
                }
            }
            ITEM_TYPE_LOADING -> {
                if (position == itemCount - 1) {
                    onLoadMoreListener?.invoke()
                }
            }
            else -> {
                innerAdapter.onBindViewHolder(holder, position)
            }
        }
    }

    fun showLoading() {
        if (enable) {
            currentItemType = ITEM_TYPE_LOADING
            notifyItemChanged(itemCount)
        }
    }

    fun showLoadFailure() {
        if (enable) {
            currentItemType = ITEM_TYPE_LOAD_FAILED
            notifyItemChanged(itemCount)
        }
    }

    fun showNoMore() {
        if (enable) {
            currentItemType = ITEM_TYPE_NO_MORE
            notifyItemChanged(itemCount)
        }
    }

    fun remove(position: Int) {
        if (position >= 0 && position < innerAdapter.dataList.size) {
            innerAdapter.dataList.removeAt(position)
            if (innerAdapter.dataList.isEmpty() && enableEmptyView) {
                notifyDataSetChanged()
            } else {
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount - position)
            }
        }
    }

    fun isEmpty() = innerAdapter.dataList.isEmpty()

    fun removeAll() {
        val oldInnerDataListSize = innerAdapter.dataList.size
        innerAdapter.dataList.clear()
        if (innerAdapter.dataList.isEmpty() && enableEmptyView) {
            notifyDataSetChanged()
        } else {
            notifyItemRangeRemoved(0, oldInnerDataListSize)
        }
    }

    fun add(newList: List<Entity>?) {
        if (newList != null && newList.isNotEmpty()) {
            val oldInnerDataListSize = innerAdapter.dataList.size
            innerAdapter.dataList.addAll(newList)
            if (oldInnerDataListSize == 0 && enableEmptyView) {
                notifyDataSetChanged()
            } else {
                notifyItemRangeChanged(oldInnerDataListSize - 1, newList.size + 1)
            }
        }
    }

    fun add(entity: Entity) {
        add(entity, innerAdapter.dataList.size)
    }

    fun add(entity: Entity, position: Int) {
        if (position >= 0 && position <= innerAdapter.dataList.size) {
            val oldInnerDataListSize = innerAdapter.dataList.size
            innerAdapter.dataList.add(position, entity)
            if (oldInnerDataListSize == 0 && enableEmptyView) {
                notifyDataSetChanged()
            } else {
                notifyItemInserted(position)
                notifyItemRangeChanged(position - 1, itemCount - position + 1)
            }
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    @JvmOverloads
    fun createDefaultFooterView(text: String, height: Int = CXSystemUtil.getPxFromDp(80f).toInt(), backgroundColor: Int = Color.DKGRAY, textColor: Int = Color.LTGRAY): TextView {
        val itemView = TextView(innerAdapter.context)
        itemView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, height)
        itemView.text = text
        itemView.setBackgroundColor(backgroundColor)
        itemView.setTextColor(textColor)
        itemView.gravity = Gravity.CENTER
        itemView.typeface = Typeface.DEFAULT_BOLD
        return itemView
    }

    @Suppress("MemberVisibilityCanBePrivate")
    @JvmOverloads
    fun createDefaultEmptyView(text: String, backgroundColor: Int = Color.DKGRAY, textColor: Int = Color.LTGRAY): TextView {
        val itemView = TextView(innerAdapter.context)
        itemView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        itemView.text = text
        itemView.setBackgroundColor(backgroundColor)
        itemView.setTextColor(textColor)
        itemView.gravity = Gravity.CENTER
        itemView.typeface = Typeface.DEFAULT_BOLD
        return itemView
    }

    // 适配 GridLayoutManager
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        innerAdapter.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val oldSpanSizeLookup = layoutManager.spanSizeLookup
            layoutManager.spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (enable) {
                        if (position == itemCount - 1) layoutManager.spanCount else oldSpanSizeLookup.getSpanSize(position)
                    } else {
                        1
                    }
                }
            }
        }
    }

    /**
     * 适配 StaggeredGridLayoutManager
     */
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        innerAdapter.onViewAttachedToWindow(holder)
        if (holder.layoutPosition == itemCount - 1 && enable) {
            (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.isFullSpan = true
        }
    }
}