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
    // divider between items
    recyclerView.addItemDecoration(CXRecyclerViewItemDecoration(5))

    val adapterWrapper = CXLoadMoreWrapper<String, CXViewHolder>(context, adapter)

    // custom loading views
    adapterWrapper.viewNoMore = CXLoadMoreWrapper.createDefaultFooterView(context, "呵呵, 真的没有更多了", CXSystemUtil.getPxFromDp(40f).toInt(), Color.DKGRAY)
    adapterWrapper.viewLoadFailure = CXLoadMoreWrapper.createDefaultFooterView(context, "呵呵, 加载失败了哟", CXSystemUtil.getPxFromDp(40f).toInt())
    adapterWrapper.viewLoading = CXLoadMoreWrapper.createDefaultFooterView(context, "呵呵, 火速请求中...", CXSystemUtil.getPxFromDp(40f).toInt())

    // onLoadMore listener
    var flag = true
    adapterWrapper.setOnLoadMoreListener {
        recyclerView.postDelayed({
            if (flag) {
                adapterWrapper.add(getDataList())
                if (adapterWrapper.itemCount >= 20) {
                    adapterWrapper.showNoMore()
                } else {
                    adapterWrapper.showLoading()
                }
                flag = false
            } else {
                adapterWrapper.showLoadFailure()
                flag = true
            }
        }, 1000)
    }

    recyclerView.adapter = adapterWrapper

    // gravity snap
    CXSnapGravityHelper(
            Gravity.TOP,
            object : CXSnapGravityHelper.SnapListener {
                override fun onSnap(position: Int) {
                    CXLogUtil.e("Snapped", position.toString())
                }
            },
            debug = true
    ).attachToRecyclerView(recyclerView)

 */
@Suppress("unused", "unused")
@SuppressLint("SetTextI18n")
class CXLoadMoreWrapper<Entity>(private val innerAdapter: CXRecyclerViewAdapter<Entity, RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private val TAG: String = CXLoadMoreWrapper::class.java.simpleName

        private const val ITEM_TYPE_LOAD_FAILED = Integer.MAX_VALUE - 1
        private const val ITEM_TYPE_NO_MORE = Integer.MAX_VALUE - 2
        private const val ITEM_TYPE_LOADING = Integer.MAX_VALUE - 3
        private const val ITEM_TYPE_NONE = Integer.MAX_VALUE - 4
    }

    private var isLoadFailure = false
    private var isHaveStatesView = true
    private var currentItemType = ITEM_TYPE_LOADING

    private var onLoadMore: (() -> Unit)? = null

    var viewNoMore: View? = null
    var viewLoading: View? = null
    var viewLoadFailure: View? = null
    private val viewHolderNoMore: CXViewHolder by lazy { wrapperFullSpan(CXViewHolder(viewNoMore ?: createDefaultFooterView("没有更多了"))) }
    private val viewHolderLoading: CXViewHolder by lazy { wrapperFullSpan(CXViewHolder(viewLoading ?: createDefaultFooterView("正在加载中..."))) }
    private val viewHolderLoadFailure: CXViewHolder by lazy { wrapperFullSpan(CXViewHolder(viewLoadFailure ?: createDefaultFooterView("加载失败，请点我重试"))) }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isHaveStatesView) currentItemType else innerAdapter.getItemViewType(position)
    }

    override fun getItemCount(): Int = innerAdapter.itemCount + if (isHaveStatesView) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_NO_MORE -> viewHolderNoMore
            ITEM_TYPE_LOADING -> viewHolderLoading
            ITEM_TYPE_LOAD_FAILED -> viewHolderLoadFailure
            else -> innerAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ITEM_TYPE_NO_MORE -> {
            }
            ITEM_TYPE_NONE -> {
            }
            ITEM_TYPE_LOAD_FAILED -> {
                holder.itemView.setOnClickListener {
                    if (onLoadMore != null) {
                        showLoading()
                    }
                }
            }
            ITEM_TYPE_LOADING -> {
                if (position == itemCount - 1) {
                    onLoadMore?.invoke()
                }
            }
            else -> {
                innerAdapter.onBindViewHolder(holder, position)
            }
        }
    }

    fun setOnLoadMoreListener(onLoadMore: (() -> Unit)?) {
        this.onLoadMore = onLoadMore
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

    fun showNoMore() {
        currentItemType = ITEM_TYPE_NO_MORE
        isLoadFailure = false
        isHaveStatesView = true
        notifyItemChanged(itemCount)
    }

    fun disable() {
        if (currentItemType != ITEM_TYPE_NONE && isHaveStatesView) {
            currentItemType = ITEM_TYPE_NONE
            isHaveStatesView = false
            notifyItemRemoved(itemCount)
        }
    }

    fun remove(position: Int) {
        if (position >= 0 && position < innerAdapter.dataList.size) {
            innerAdapter.dataList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount - position)
        }
    }

    fun removeAll() {
        val oldInnerDataListSize = innerAdapter.dataList.size
        innerAdapter.dataList.clear()
        notifyItemRangeRemoved(0, oldInnerDataListSize)
    }

    fun add(newList: List<Entity>?) {
        if (newList != null && newList.isNotEmpty()) {
            val oldSize = innerAdapter.dataList.size
            innerAdapter.dataList.addAll(newList)
            notifyItemRangeChanged(oldSize - 1, newList.size + 1)
        }
    }

    fun add(entity: Entity) {
        val oldInnerDataListSize = innerAdapter.dataList.size
        innerAdapter.dataList.add(entity)
        notifyItemInserted(oldInnerDataListSize)
    }

    fun add(entity: Entity, position: Int) {
        if (position >= 0 && position <= innerAdapter.dataList.size) {
            innerAdapter.dataList.add(position, entity)
            notifyItemInserted(position)
            notifyItemRangeChanged(position, itemCount - position)
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

    // 适配 GridLayoutManager & StaggeredGridLayoutManager start
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        innerAdapter.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val oldSpanSizeLookup = layoutManager.spanSizeLookup
            layoutManager.spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == itemCount - 1 && isHaveStatesView) {
                        layoutManager.spanCount
                    } else if (isHaveStatesView) {
                        oldSpanSizeLookup.getSpanSize(position)
                    } else {
                        1
                    }
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        innerAdapter.onViewAttachedToWindow(holder)
        if (holder.layoutPosition == itemCount - 1 && isHaveStatesView) {
            val layoutParams = holder.itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) layoutParams.isFullSpan = true
        }
    }

    private fun wrapperFullSpan(holder: CXViewHolder): CXViewHolder {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) layoutParams.isFullSpan = true
        return holder
    }
    // 适配 GridLayoutManager & StaggeredGridLayoutManager end
}