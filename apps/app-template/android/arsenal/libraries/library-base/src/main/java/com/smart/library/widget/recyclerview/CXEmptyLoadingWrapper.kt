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

    private var pageIndex = 0
    private var pageSize = 10
    private fun getDataList(): MutableList<String> {
        val toPageIndex = pageIndex + 1
        val tmpList = ((pageIndex * pageSize) until toPageIndex * pageSize).map { "第 $it 天" }.toMutableList()
        pageIndex = toPageIndex
        return tmpList
    }

    @Suppress("PrivatePropertyName")
    private val adapter: CXRecyclerViewAdapter<String, RecyclerView.ViewHolder> by lazy {
        object : CXRecyclerViewAdapter<String, RecyclerView.ViewHolder>(context, mutableListOf()) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): CXViewHolder {
                return CXViewHolder(LayoutInflater.from(context).inflate(R.layout.home_fragment_recycler_view_drag_and_transfer_item_days, container, false))
            }

            override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
                viewHolder.itemView.textViewDays.text = dataList[position]
            }
        }
    }

    private val adapterWrapper by lazy { CXEmptyLoadingWrapper(adapter) }
    private val snapGravityHelper by lazy {
        CXSnapGravityHelper(
                Gravity.TOP,
                object : CXSnapGravityHelper.SnapListener {
                    override fun onSnap(position: Int) {
                        CXLogUtil.e("Snapped", position.toString())
                    }
                },
                debug = true
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        removeAll.setOnClickListener {
            adapterWrapper.removeAll()
        }
        removeOne.setOnClickListener {
            adapterWrapper.remove(0)
        }
        addAll.setOnClickListener {
            if (adapterWrapper.isInnerDataEmpty()) pageIndex = 0
            adapterWrapper.add(getDataList())
        }
        addEnd.setOnClickListener {
            adapterWrapper.add("insert at end of list at ${CXTimeUtil.HmsS(System.currentTimeMillis())}")
        }
        addAt0.setOnClickListener {
            adapterWrapper.add("insert at 0 at ${CXTimeUtil.HmsS(System.currentTimeMillis())}", 0)
        }
        disable.setOnClickListener {
            adapterWrapper.enable = !adapterWrapper.enable
        }
        showFailure.setOnClickListener {
            adapterWrapper.showLoadFailure()
        }
        showNoMore.setOnClickListener {
            adapterWrapper.showNoMore()
        }
        showLoading.setOnClickListener {
            adapterWrapper.showLoading()
        }

        // divider between items
        recyclerView.addItemDecoration(CXRecyclerViewItemDecoration(5))
        // custom loading views
        adapterWrapper.viewNoMore = adapterWrapper.createDefaultFooterView("-- 呵呵, 真的没有更多了 --")
        adapterWrapper.viewLoadFailure = adapterWrapper.createDefaultFooterView("啊哟, 加载失败了哟")
        adapterWrapper.viewLoading = adapterWrapper.createDefaultFooterView("哼哈, 火速请求中...")

        adapterWrapper.onInnerDataChanged = {
            snapGravityHelper.forceSnap(recyclerView.layoutManager, it.isEmpty()) // force snap after inner data changed
        }

        // onLoadMore listener
        var flag = true
        adapterWrapper.onLoadMoreListener = {
            recyclerView.postDelayed({
                if (flag) {
                    if (adapterWrapper.itemCount >= 30) {
                        adapterWrapper.showNoMore()
                    } else {
                        adapterWrapper.add(getDataList())
                    }
                    if (adapterWrapper.itemCount == 20 + 1) flag = false
                } else {
                    adapterWrapper.showLoadFailure()
                    flag = true
                }
            }, 1000)
        }

        recyclerView.adapter = adapterWrapper
        // gravity snap
        snapGravityHelper.attachToRecyclerView(recyclerView)

        // if want force invoke onSnap 0, must call adapterWrapper.add after setAdapter and snapGravityHelper.attachToRecyclerView(recyclerView)
        adapterWrapper.add(getDataList())
    }

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
    /**
     * 数据变动后，可以在这个回调中强制刷新 onSnap 等
     */
    var onInnerDataChanged: ((MutableList<Entity>) -> Unit)? = null

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
            if (innerAdapter.dataList.isEmpty()) {
                if (enable) currentItemType = ITEM_TYPE_LOADING // 数据从 无 -> 有后确保是 loading 状态
                if (enableEmptyView) notifyDataSetChanged()
            } else {
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount - position)
            }
            onInnerDataChanged?.invoke(innerAdapter.dataList)
        }
    }

    fun isInnerDataNotEmpty() = innerData().isNotEmpty()
    fun isInnerDataEmpty() = innerData().isEmpty()
    fun innerData() = innerAdapter.dataList

    fun removeAll() {
        val oldInnerDataListSize = innerAdapter.dataList.size
        if (oldInnerDataListSize != 0) {
            innerAdapter.dataList.clear()
            if (enable) currentItemType = ITEM_TYPE_LOADING // 数据从 无 -> 有后确保是 loading 状态
            if (enableEmptyView) {
                notifyDataSetChanged()
            } else {
                notifyItemRangeRemoved(0, oldInnerDataListSize)
            }
            onInnerDataChanged?.invoke(innerAdapter.dataList)
        }
    }

    fun add(newList: List<Entity>?) {
        if (newList != null && newList.isNotEmpty()) {
            val oldInnerDataListSize = innerAdapter.dataList.size
            innerAdapter.dataList.addAll(newList)
            if (oldInnerDataListSize == 0) {
                if (enable) currentItemType = ITEM_TYPE_LOADING // 数据从 无 -> 有后确保是 loading 状态
                if (enableEmptyView) notifyDataSetChanged()
            } else {
                notifyItemRangeChanged(oldInnerDataListSize - 1, newList.size + 1)
            }
            onInnerDataChanged?.invoke(innerAdapter.dataList)
        }
    }

    fun add(entity: Entity) {
        add(entity, innerAdapter.dataList.size)
    }

    fun add(entity: Entity, position: Int) {
        if (position >= 0 && position <= innerAdapter.dataList.size) {
            val oldInnerDataListSize = innerAdapter.dataList.size
            innerAdapter.dataList.add(position, entity)
            if (oldInnerDataListSize == 0) {
                if (enable) currentItemType = ITEM_TYPE_LOADING // 数据从 无 -> 有后确保是 loading 状态
                if (enableEmptyView) notifyDataSetChanged()
            } else {
                notifyItemInserted(position)
                notifyItemRangeChanged(position - 1, itemCount - position + 1)
            }
            onInnerDataChanged?.invoke(innerAdapter.dataList)
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