package com.smart.library.widget.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import com.smart.library.util.CXSystemUtil

@Suppress("unused", "unused")
@SuppressLint("SetTextI18n")
class CXLoadMoreWrapper<Entity, ViewHolder>(private val mContext: Context?, private val innerAdapter: CXRecyclerViewAdapter<Entity, CXViewHolder>) : RecyclerView.Adapter<CXViewHolder>() {

    companion object {
        private const val ITEM_TYPE_LOAD_FAILED = Integer.MAX_VALUE - 1
        private const val ITEM_TYPE_NO_MORE = Integer.MAX_VALUE - 2
        private const val ITEM_TYPE_LOADING = Integer.MAX_VALUE - 3
        private const val ITEM_TYPE_NONE = Integer.MAX_VALUE - 4 //不展示footer view

        private val DP_30 = CXSystemUtil.getPxFromDp(20f).toInt()
    }

    private var loadMoreView: View? = null
    private var loadMoreFailedView: View? = null
    private var noMoreView: View? = null

    private var currentItemType = ITEM_TYPE_LOADING
    private val scrollListener: LoadMoreScrollListener

    private var isLoadFailure = false//标记是否加载出错
    private var isHaveStatesView = true

    private fun wrapperFullSpan(holder: CXViewHolder): CXViewHolder {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) layoutParams.isFullSpan = true
        return holder
    }

    private val loadMoreViewHolder: CXViewHolder
        get() {
            if (loadMoreView == null) {
                loadMoreView = TextView(mContext)
                loadMoreView!!.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                loadMoreView!!.setPadding(DP_30, DP_30, DP_30, DP_30)
                (loadMoreView as TextView).text = "正在加载中"
                (loadMoreView as TextView).gravity = Gravity.CENTER
            }
            return wrapperFullSpan(CXViewHolder(loadMoreView!!))
        }

    private val loadFailedViewHolder: CXViewHolder
        get() {
            if (loadMoreFailedView == null) {
                loadMoreFailedView = TextView(mContext)
                loadMoreFailedView!!.setPadding(DP_30, DP_30, DP_30, DP_30)
                loadMoreFailedView!!.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                (loadMoreFailedView as TextView).text = "加载失败，请点我重试"
                (loadMoreFailedView as TextView).gravity = Gravity.CENTER
            }
            return wrapperFullSpan(CXViewHolder(loadMoreFailedView!!))
        }


    private val noMoreViewHolder: CXViewHolder
        get() {
            if (noMoreView == null) {
                noMoreView = TextView(mContext)
                noMoreView?.setPadding(DP_30, DP_30, DP_30, DP_30)
                noMoreView?.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                (noMoreView as TextView).text = "没有更多了"
                (noMoreView as TextView).gravity = Gravity.CENTER
            }
            return wrapperFullSpan(CXViewHolder(noMoreView!!))
        }

    private var loadListener: OnLoadListener? = null

    init {
        scrollListener = object : LoadMoreScrollListener() {
            override fun loadMore() {
                if (loadListener != null && isHaveStatesView) {
                    if (!isLoadFailure && currentItemType != ITEM_TYPE_NO_MORE) {
                        showLoading()
                        loadListener?.onLoadMore()
                    }
                }
            }
        }
    }

    fun showLoading() {
        currentItemType = ITEM_TYPE_LOADING
        isLoadFailure = false
        isHaveStatesView = true
        notifyItemChanged(itemCount)
    }

    fun showLoadFailure() {
        currentItemType = ITEM_TYPE_LOAD_FAILED
        isLoadFailure = true
        isHaveStatesView = true
        notifyItemChanged(itemCount)
    }

    fun showNoMoreView() {
        currentItemType = ITEM_TYPE_NO_MORE
        isLoadFailure = false
        isHaveStatesView = true
        notifyItemChanged(itemCount)
    }

    fun disable() {
        currentItemType = ITEM_TYPE_NONE
        isHaveStatesView = false
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isHaveStatesView) currentItemType else innerAdapter.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CXViewHolder {
        return when (viewType) {
            ITEM_TYPE_NO_MORE -> noMoreViewHolder
            ITEM_TYPE_LOADING -> loadMoreViewHolder
            ITEM_TYPE_LOAD_FAILED -> loadFailedViewHolder
            else -> innerAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: CXViewHolder, position: Int) {
        if (holder.itemViewType == ITEM_TYPE_LOAD_FAILED) {
            loadMoreFailedView?.setOnClickListener {
                if (loadListener != null) {
                    loadListener?.onRetry()
                    showLoading()
                }
            }
            return
        }
        if (!isFooterType(holder.itemViewType)) innerAdapter.onBindViewHolder(holder, position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        onAttachedToRecyclerView(innerAdapter, recyclerView, object : SpanSizeCallback {
            override fun getSpanSize(layoutManager: GridLayoutManager, oldLookup: GridLayoutManager.SpanSizeLookup, position: Int): Int {
                if (position == itemCount - 1 && isHaveStatesView) {
                    return layoutManager.spanCount
                }
                return if (isHaveStatesView) oldLookup.getSpanSize(position) else 1
            }
        })
        recyclerView.addOnScrollListener(scrollListener)
    }

    private fun onAttachedToRecyclerView(innerAdapter: RecyclerView.Adapter<CXViewHolder>, recyclerView: RecyclerView, callback: SpanSizeCallback) {
        innerAdapter.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanSizeLookup = layoutManager.spanSizeLookup
            layoutManager.spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int = callback.getSpanSize(layoutManager, spanSizeLookup, position)
            }

        }
    }

    override fun onViewAttachedToWindow(holder: CXViewHolder) {
        innerAdapter.onViewAttachedToWindow(holder)

        if (holder.layoutPosition == itemCount - 1 && isHaveStatesView) {
            val layoutParams = holder.itemView.layoutParams
            if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                layoutParams.isFullSpan = true
            }
        }
    }

    override fun getItemCount(): Int = innerAdapter.itemCount + if (isHaveStatesView) 1 else 0

    fun remove(position: Int) {
        innerAdapter.remove(position)
        notifyItemChanged(itemCount)
    }

    fun removeAll() {
        innerAdapter.removeAll()
        notifyItemChanged(itemCount)
    }

    fun add(newList: List<Entity>?) {
        innerAdapter.add(newList)
        notifyItemChanged(itemCount)
    }

    fun add(entity: Entity, position: Int) {
        innerAdapter.add(entity, position)
        notifyItemChanged(itemCount)
    }

    private fun isFooterType(type: Int): Boolean = type == ITEM_TYPE_NONE || type == ITEM_TYPE_LOAD_FAILED || type == ITEM_TYPE_NO_MORE || type == ITEM_TYPE_LOADING

    interface OnLoadListener {
        fun onRetry()

        fun onLoadMore()
    }

    fun setOnLoadListener(onLoadListener: OnLoadListener): CXLoadMoreWrapper<Entity, ViewHolder> {
        loadListener = onLoadListener
        return this
    }

    private interface SpanSizeCallback {
        fun getSpanSize(layoutManager: GridLayoutManager, oldLookup: GridLayoutManager.SpanSizeLookup, position: Int): Int
    }

    private abstract inner class LoadMoreScrollListener : RecyclerView.OnScrollListener() {

        private var previousTotal: Int = 0
        private var isLoading = true
        private var lm: LinearLayoutManager? = null
        private var sm: StaggeredGridLayoutManager? = null
        private var lastPositions: IntArray? = null
        private var totalItemCount: Int = 0
        private var lastVisibleItemPosition: Int = 0

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (recyclerView.layoutManager is LinearLayoutManager)
                lm = recyclerView.layoutManager as LinearLayoutManager
            else if (recyclerView.layoutManager is StaggeredGridLayoutManager) {
                sm = recyclerView.layoutManager as StaggeredGridLayoutManager
                lastPositions = sm!!.findLastVisibleItemPositions(null)
            }

            val visibleItemCount = recyclerView.childCount
            if (lm != null) {
                totalItemCount = lm!!.itemCount
                lastVisibleItemPosition = lm!!.findLastVisibleItemPosition()
            } else if (sm != null) {
                totalItemCount = sm!!.itemCount
                lastVisibleItemPosition = lastPositions!![0]
            }

            if (isLoading) {
                when {
                    totalItemCount > previousTotal -> {//加载更多结束
                        isLoading = false
                        previousTotal = totalItemCount
                    }
                    totalItemCount < previousTotal -> {//用户刷新结束
                        previousTotal = totalItemCount
                        isLoading = false
                    }
                    else -> {//有可能是在第一页刷新也可能是加载完毕
                    }
                }

            }
            if (!isLoading && visibleItemCount > 0 && totalItemCount - 1 == lastVisibleItemPosition && recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                loadMore()
            }
        }

        abstract fun loadMore()
    }
}
