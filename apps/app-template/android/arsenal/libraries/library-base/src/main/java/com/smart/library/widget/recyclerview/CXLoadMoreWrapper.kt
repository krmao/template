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

@Suppress("unused", "unused")
@SuppressLint("SetTextI18n")
class CXLoadMoreWrapper(private val mContext: Context, private val innerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM_TYPE_LOAD_FAILED_VIEW = Integer.MAX_VALUE - 1
        const val ITEM_TYPE_NO_MORE_VIEW = Integer.MAX_VALUE - 2
        const val ITEM_TYPE_LOAD_MORE_VIEW = Integer.MAX_VALUE - 3
        const val ITEM_TYPE_NO_VIEW = Integer.MAX_VALUE - 4//不展示footer view
    }

    private var mLoadMoreView: View? = null
    private var mLoadMoreFailedView: View? = null
    private var mNoMoreView: View? = null

    private var mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW
    private val mLoadMoreScrollListener: LoadMoreScrollListener


    private var isLoadError = false//标记是否加载出错
    private var isHaveStatesView = true

    private fun wrapperFullSpan(holder: CXRecyclerViewAdapter.ViewHolder): CXRecyclerViewAdapter.ViewHolder {
        val lp = holder.itemView.layoutParams
        if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) lp.isFullSpan = true
        return holder
    }

    //region Get ViewHolder
    private val loadMoreViewHolder: CXRecyclerViewAdapter.ViewHolder
        get() {
            if (mLoadMoreView == null) {
                mLoadMoreView = TextView(mContext)
                mLoadMoreView!!.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                mLoadMoreView!!.setPadding(20, 20, 20, 20)
                (mLoadMoreView as TextView).text = "正在加载中"
                (mLoadMoreView as TextView).gravity = Gravity.CENTER
            }
            return wrapperFullSpan(CXRecyclerViewAdapter.ViewHolder(mLoadMoreView!!))
        }

    private val loadFailedViewHolder: CXRecyclerViewAdapter.ViewHolder
        get() {
            if (mLoadMoreFailedView == null) {
                mLoadMoreFailedView = TextView(mContext)
                mLoadMoreFailedView!!.setPadding(20, 20, 20, 20)
                mLoadMoreFailedView!!.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                (mLoadMoreFailedView as TextView).text = "加载失败，请点我重试"
                (mLoadMoreFailedView as TextView).gravity = Gravity.CENTER
            }
            return wrapperFullSpan(CXRecyclerViewAdapter.ViewHolder(mLoadMoreFailedView!!))
        }


    private val noMoreViewHolder: CXRecyclerViewAdapter.ViewHolder
        get() {
            if (mNoMoreView == null) {
                mNoMoreView = TextView(mContext)
                mNoMoreView!!.setPadding(20, 20, 20, 20)
                mNoMoreView!!.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                (mNoMoreView as TextView).text = "--end--"
                (mNoMoreView as TextView).gravity = Gravity.CENTER
            }
            return wrapperFullSpan(CXRecyclerViewAdapter.ViewHolder(mNoMoreView!!))
        }

    private var mOnLoadListener: OnLoadListener? = null

    init {
        mLoadMoreScrollListener = object : LoadMoreScrollListener() {
            override fun loadMore() {
                if (mOnLoadListener != null && isHaveStatesView) {
                    if (!isLoadError) {
                        showLoadMore()
                        mOnLoadListener!!.onLoadMore()
                    }
                }
            }
        }
    }

    fun showLoadMore() {
        mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW
        isLoadError = false
        isHaveStatesView = true
        notifyItemChanged(itemCount)
    }

    fun showLoadError() {
        mCurrentItemType = ITEM_TYPE_LOAD_FAILED_VIEW
        isLoadError = true
        isHaveStatesView = true
        notifyItemChanged(itemCount)
    }

    fun showLoadComplete() {
        mCurrentItemType = ITEM_TYPE_NO_MORE_VIEW
        isLoadError = false
        isHaveStatesView = true
        notifyItemChanged(itemCount)
    }

    fun disableLoadMore() {
        mCurrentItemType = ITEM_TYPE_NO_VIEW
        isHaveStatesView = false
        notifyDataSetChanged()
    }
    //endregion

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isHaveStatesView) {
            mCurrentItemType
        } else innerAdapter.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_NO_MORE_VIEW -> noMoreViewHolder
            ITEM_TYPE_LOAD_MORE_VIEW -> loadMoreViewHolder
            ITEM_TYPE_LOAD_FAILED_VIEW -> loadFailedViewHolder
            else -> innerAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ITEM_TYPE_LOAD_FAILED_VIEW) {
            mLoadMoreFailedView!!.setOnClickListener {
                if (mOnLoadListener != null) {
                    mOnLoadListener!!.onRetry()
                    showLoadMore()
                }
            }
            return
        }
        if (!isFooterType(holder.itemViewType))
            innerAdapter.onBindViewHolder(holder, position)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        onAttachedToRecyclerView(innerAdapter, recyclerView, object : SpanSizeCallback {
            override fun getSpanSize(layoutManager: GridLayoutManager, oldLookup: GridLayoutManager.SpanSizeLookup, position: Int): Int {
                if (position == itemCount - 1 && isHaveStatesView) {
                    return layoutManager.spanCount
                }
                return if (isHaveStatesView) {
                    oldLookup.getSpanSize(position)
                } else 1
            }
        })
        recyclerView.addOnScrollListener(mLoadMoreScrollListener)
    }

    private fun onAttachedToRecyclerView(innerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, recyclerView: RecyclerView, callback: SpanSizeCallback) {
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

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        innerAdapter.onViewAttachedToWindow(holder)

        if (holder.layoutPosition == itemCount - 1 && isHaveStatesView) {
            val lp = holder.itemView.layoutParams

            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {

                lp.isFullSpan = true
            }
        }
    }

    override fun getItemCount(): Int {
        return innerAdapter.itemCount + if (isHaveStatesView) 1 else 0
    }

    private fun isFooterType(type: Int): Boolean {
        return type == ITEM_TYPE_NO_VIEW ||
            type == ITEM_TYPE_LOAD_FAILED_VIEW ||
            type == ITEM_TYPE_NO_MORE_VIEW ||
            type == ITEM_TYPE_LOAD_MORE_VIEW
    }
    //region 加载监听

    interface OnLoadListener {
        fun onRetry()

        fun onLoadMore()
    }

    fun setOnLoadListener(onLoadListener: OnLoadListener): CXLoadMoreWrapper {
        mOnLoadListener = onLoadListener
        return this
    }

    //endregion

    interface SpanSizeCallback {
        fun getSpanSize(layoutManager: GridLayoutManager, oldLookup: GridLayoutManager.SpanSizeLookup, position: Int): Int
    }

    /**
     * Created by _SOLID
     * Date:2016/10/9
     * Time:16:12
     * Desc:用于RecyclerView加载更多的监听，实现滑动到底部自动加载更多
     */
    abstract inner class LoadMoreScrollListener : RecyclerView.OnScrollListener() {

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
