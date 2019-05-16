package com.smart.library.widget.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
class CXLoadMoreWrapper<Entity, ViewHolder>(private val context: Context?, private val innerAdapter: CXRecyclerViewAdapter<Entity, CXViewHolder>) : RecyclerView.Adapter<CXViewHolder>() {

    companion object {
        private val TAG: String = CXLoadMoreWrapper::class.java.simpleName

        private const val ITEM_TYPE_LOAD_FAILED = Integer.MAX_VALUE - 1
        private const val ITEM_TYPE_NO_MORE = Integer.MAX_VALUE - 2
        private const val ITEM_TYPE_LOADING = Integer.MAX_VALUE - 3
        private const val ITEM_TYPE_NONE = Integer.MAX_VALUE - 4

        @Suppress("MemberVisibilityCanBePrivate")
        @JvmStatic
        @JvmOverloads
        fun createDefaultFooterView(context: Context?, text: String, height: Int = CXSystemUtil.getPxFromDp(60f).toInt(), backgroundColor: Int = Color.YELLOW, textColor: Int = Color.BLACK): TextView {
            val itemView = TextView(context)
            itemView.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, height)
            itemView.text = text
            itemView.setBackgroundColor(backgroundColor)
            itemView.setTextColor(textColor)
            itemView.gravity = Gravity.CENTER
            return itemView
        }
    }

    private var isLoadFailure = false
    private var isHaveStatesView = true
    private var currentItemType = ITEM_TYPE_LOADING

    private var onLoadMore: (() -> Unit)? = null

    var viewNoMore: View? = null
    var viewLoading: View? = null
    var viewLoadFailure: View? = null
    private val viewHolderNoMore: CXViewHolder by lazy { wrapperFullSpan(CXViewHolder(viewNoMore ?: createDefaultFooterView(context, "没有更多了"))) }
    private val viewHolderLoading: CXViewHolder by lazy { wrapperFullSpan(CXViewHolder(viewLoading ?: createDefaultFooterView(context, "正在加载中..."))) }
    private val viewHolderLoadFailure: CXViewHolder by lazy { wrapperFullSpan(CXViewHolder(viewLoadFailure ?: createDefaultFooterView(context, "加载失败，请点我重试"))) }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1 && isHaveStatesView) currentItemType else innerAdapter.getItemViewType(position)
    }

    override fun getItemCount(): Int = innerAdapter.itemCount + if (isHaveStatesView) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CXViewHolder {
        return when (viewType) {
            ITEM_TYPE_NO_MORE -> viewHolderNoMore
            ITEM_TYPE_LOADING -> viewHolderLoading
            ITEM_TYPE_LOAD_FAILED -> viewHolderLoadFailure
            else -> innerAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: CXViewHolder, position: Int) {
        if (!isFooterType(holder.itemViewType)) {
            innerAdapter.onBindViewHolder(holder, position)
        } else if (holder.itemViewType == ITEM_TYPE_LOADING && position == itemCount - 1) {
            onLoadMore?.invoke()
        } else if (holder.itemViewType == ITEM_TYPE_LOAD_FAILED) {
            holder.itemView.setOnClickListener {
                if (onLoadMore != null) {
                    showLoading()
                }
            }
        }
    }

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

    override fun onViewAttachedToWindow(holder: CXViewHolder) {
        innerAdapter.onViewAttachedToWindow(holder)
        if (holder.layoutPosition == itemCount - 1 && isHaveStatesView) {
            val layoutParams = holder.itemView.layoutParams
            if (layoutParams is StaggeredGridLayoutManager.LayoutParams) layoutParams.isFullSpan = true
        }
    }

    private fun isFooterType(type: Int): Boolean = type == ITEM_TYPE_NONE || type == ITEM_TYPE_LOAD_FAILED || type == ITEM_TYPE_NO_MORE || type == ITEM_TYPE_LOADING

    private fun wrapperFullSpan(holder: CXViewHolder): CXViewHolder {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams != null && layoutParams is StaggeredGridLayoutManager.LayoutParams) layoutParams.isFullSpan = true
        return holder
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
        currentItemType = ITEM_TYPE_NONE
        isHaveStatesView = false
        notifyDataSetChanged()
    }

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
}