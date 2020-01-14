package com.smart.library.widget.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.smart.library.R
import com.smart.library.base.toPxFromDp
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil

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
    private val adapter: STRecyclerViewAdapter<String, RecyclerView.ViewHolder> by lazy {
        object : STRecyclerViewAdapter<String, RecyclerView.ViewHolder>(context, mutableListOf()) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): STViewHolder {
                return STViewHolder(LayoutInflater.from(context).inflate(R.layout.home_fragment_recycler_view_drag_and_transfer_item_days, container, false))
            }

            override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
                viewHolder.itemView.textViewDays.text = dataList[position]
            }
        }
    }

    private val adapterWrapper by lazy { STEmptyLoadingWrapper(adapter) }
    private val snapGravityHelper by lazy {
        STSnapGravityHelper(
                Gravity.TOP,
                object : STSnapHelper.SnapListener {
                    override fun onSnap(position: Int) {
                        STLogUtil.e("Snapped", position.toString())
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
            adapterWrapper.add("insert at end of list at ${STTimeUtil.HmsS(System.currentTimeMillis())}")
        }
        addAt0.setOnClickListener {
            adapterWrapper.add("insert at 0 at ${STTimeUtil.HmsS(System.currentTimeMillis())}", 0)
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
/**
 * STSnapGravityHelper center 适合 STRecyclerViewLinearStartItemDecoration
 */
@Suppress("unused", "unused", "MemberVisibilityCanBePrivate")
@SuppressLint("SetTextI18n")
class STEmptyLoadingWrapper<Entity>(private val innerAdapter: STRecyclerViewAdapter<Entity, RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val tag = "STEmptyLoadingWrapper:${hashCode()}"
    private var currentItemType = ITEM_TYPE_EMPTY_NONE // recyclerView 初始化后, 如果没有数据, 显示的一个透明的占位空状态
    var enableEmptyView = true
    var enableLoadMore = true

    var onLoadMoreListener: ((refresh: Boolean) -> Unit)? = null
    /**
     * 数据变动后，可以在这个回调中强制刷新 onSnap 等
     */
    var onInnerDataChanged: ((MutableList<Entity>) -> Unit)? = null

    var viewLoadFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    var viewLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    var viewNoMore: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    var viewEmpty: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    var viewEmptyNone: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    var viewEmptyLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    var viewEmptyLoadingFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    var recyclerView: RecyclerView? = null
        private set
    val orientation: Int
        get() = when (val layoutManager: RecyclerView.LayoutManager? = this.recyclerView?.layoutManager) {
            is LinearLayoutManager -> layoutManager.orientation
            is GridLayoutManager -> layoutManager.orientation
            is StaggeredGridLayoutManager -> layoutManager.orientation
            else -> LinearLayoutManager.VERTICAL
        }

    fun enableChangeAnimations(enableChangeAnimations: Boolean) = innerAdapter.enableChangeAnimations(enableChangeAnimations)

    init {
        currentItemType = if ((enableLoadMore || enableEmptyView) && isInnerDataNotEmpty()) ITEM_TYPE_LOADING else ITEM_TYPE_EMPTY_NONE
    }

    fun isCurrentItemTypeEmpty(): Boolean = currentItemType == ITEM_TYPE_EMPTY
    fun isCurrentItemTypeEmptyLoadingFailure(): Boolean = currentItemType == ITEM_TYPE_EMPTY_LOADING_FAILURE

    fun resetDataList(dataList: MutableList<Entity>) {
        innerAdapter.resetDataList(dataList)
        currentItemType = if (isInnerDataNotEmpty()) ITEM_TYPE_LOADING else ITEM_TYPE_EMPTY_NONE
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_EMPTY_NONE -> STViewHolder(viewEmptyNone?.invoke(parent, viewType, orientation) ?: createDefaultEmptyNone(innerAdapter.context, orientation))
            ITEM_TYPE_EMPTY, ITEM_TYPE_EMPTY_LOADING, ITEM_TYPE_EMPTY_LOADING_FAILURE -> STViewHolder(
                    FrameLayout(parent.context).apply {
                        val viewEmptyLoadingFailure: View = viewEmptyLoadingFailure?.invoke(parent, viewType, orientation) ?: createDefaultEmptyLoadingFailure(innerAdapter.context, "加载出错...")
                        val viewEmptyLoading: View = (viewEmptyLoading?.invoke(parent, viewType, orientation) ?: createDefaultEmptyLoadingView(innerAdapter.context, "加载中..."))
                        val viewEmpty: View = viewEmpty?.invoke(parent, viewType, orientation) ?: createDefaultEmptyView(innerAdapter.context, "数据维护中...")

                        this.removeAllViews()

                        // must set before addView, because could reset view layout params by addView with layout params
                        this.layoutParams = RecyclerView.LayoutParams(
                                if (orientation == LinearLayoutManager.VERTICAL) RecyclerView.LayoutParams.MATCH_PARENT else viewEmptyLoading.layoutParams.width,
                                if (orientation == LinearLayoutManager.VERTICAL) RecyclerView.LayoutParams.MATCH_PARENT else viewEmptyLoading.layoutParams.height
                        )

                        this.addView(viewEmptyLoadingFailure, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                        this.addView(viewEmptyLoading, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                        this.addView(viewEmpty, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                    }
            )
            ITEM_TYPE_NO_MORE, ITEM_TYPE_LOADING, ITEM_TYPE_LOAD_FAILED -> STViewHolder(
                    FrameLayout(parent.context).apply {
                        val viewNoMore: View = viewNoMore?.invoke(parent, viewType, orientation) ?: createDefaultFooterView(innerAdapter.context, "没有更多了", orientation = orientation)
                        val viewLoading: View = viewLoading?.invoke(parent, viewType, orientation) ?: createDefaultFooterLoadingView(innerAdapter.context, "加载中...", orientation = orientation)
                        val viewLoadFailure: View = viewLoadFailure?.invoke(parent, viewType, orientation) ?: createDefaultFooterView(innerAdapter.context, "加载出错了", orientation = orientation)

                        this.removeAllViews()

                        this.layoutParams = RecyclerView.LayoutParams(viewLoading.layoutParams.width, viewLoading.layoutParams.height)

                        this.addView(viewNoMore)
                        this.addView(viewLoading)
                        this.addView(viewLoadFailure)
                    }
            )
            else -> innerAdapter.onCreateViewHolder(parent, viewType)
        }
    }

    var onBindEmptyLoadingViewHolder: ((holder: RecyclerView.ViewHolder, position: Int, rootView: FrameLayout, viewEmptyLoadingFailure: View, viewEmptyLoading: View, viewEmpty: View) -> Unit)? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        STLogUtil.e(tag, "onBindViewHolder:${getItemTypeName(holder.itemViewType)}, position=$position == itemCount=$itemCount -1")
        when (holder.itemViewType) {
            ITEM_TYPE_EMPTY_NONE -> {

            }
            ITEM_TYPE_EMPTY_LOADING_FAILURE, ITEM_TYPE_EMPTY_LOADING, ITEM_TYPE_EMPTY -> {
                val itemView = holder.itemView as FrameLayout
                val viewEmptyLoadingFailure: View = itemView.getChildAt(0)
                val viewEmptyLoading: View = itemView.getChildAt(1)
                val viewEmpty: View = itemView.getChildAt(2)

                when (holder.itemViewType) {
                    ITEM_TYPE_EMPTY_LOADING_FAILURE -> {
                        viewEmptyLoadingFailure.visibility = View.VISIBLE
                        viewEmptyLoading.visibility = View.GONE
                        viewEmpty.visibility = View.GONE

                        val refreshBtn: View = (viewEmptyLoadingFailure.tag as? View) ?: holder.itemView
                        refreshBtn.setOnClickListener {
                            showEmptyLoading()
                        }
                    }
                    ITEM_TYPE_EMPTY_LOADING -> {
                        viewEmptyLoadingFailure.visibility = View.GONE
                        viewEmptyLoading.visibility = View.VISIBLE
                        viewEmpty.visibility = View.GONE
                        callLoadMore(true) // 一进来就触发 刷新一次
                    }
                    ITEM_TYPE_EMPTY -> {
                        viewEmptyLoadingFailure.visibility = View.GONE
                        viewEmptyLoading.visibility = View.GONE
                        viewEmpty.visibility = View.VISIBLE
                    }
                }
                onBindEmptyLoadingViewHolder?.invoke(holder, position, itemView, viewEmptyLoadingFailure, viewEmptyLoading, viewEmpty)
            }
            ITEM_TYPE_NO_MORE, ITEM_TYPE_LOAD_FAILED, ITEM_TYPE_LOADING -> {
                val itemView = holder.itemView as FrameLayout

                val viewNoMore: View = itemView.getChildAt(0)
                val viewLoading: View = itemView.getChildAt(1)
                val viewLoadFailure: View = itemView.getChildAt(2)
                if (holder.itemViewType == ITEM_TYPE_LOADING) {
                    viewLoading.visibility = View.VISIBLE
                    viewNoMore.visibility = View.GONE
                    viewLoadFailure.visibility = View.GONE
                    if (position == itemCount - 1) {
                        callLoadMore(false)
                    }
                } else if (holder.itemViewType == ITEM_TYPE_LOAD_FAILED) {
                    viewLoading.visibility = View.GONE
                    viewNoMore.visibility = View.GONE
                    viewLoadFailure.visibility = View.VISIBLE
                    holder.itemView.setOnClickListener {
                        if (onLoadMoreListener != null) {
                            showLoading()
                        }
                    }
                } else if (holder.itemViewType == ITEM_TYPE_NO_MORE) {
                    viewLoading.visibility = View.GONE
                    viewNoMore.visibility = View.VISIBLE
                    viewLoadFailure.visibility = View.GONE
                }
            }
            else -> {
                innerAdapter.onBindViewHolder(holder, position)
            }
        }
    }

    fun remove(position: Int) {
        if (position >= 0 && position < innerAdapter.dataList.size) {
            innerAdapter.dataList.removeAt(position)
            if (innerAdapter.dataList.isEmpty()) {
                showEmptyNone()
            } else {
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, itemCount - position)
            }
            onInnerDataChanged?.invoke(innerAdapter.dataList)
        }
    }

    fun removeAll() {
        val oldInnerDataListSize = innerAdapter.dataList.size
        if (oldInnerDataListSize != 0) {
            innerAdapter.dataList.clear()
        }
        showEmptyNone()
        onInnerDataChanged?.invoke(innerAdapter.dataList)
    }

    fun add(newList: List<Entity>?) {
        STLogUtil.e(tag, "add newList=${newList?.size}")
        if (newList != null && newList.isNotEmpty()) {
            val oldInnerDataListSize = innerAdapter.dataList.size
            innerAdapter.dataList.addAll(newList)
            if (oldInnerDataListSize == 0) {
                if (enableLoadMore) currentItemType = ITEM_TYPE_LOADING // 数据从 无 -> 有后确保是 loading 状态
                if (enableEmptyView) {
                    notifyDataSetChanged()
                } else {
                    notifyItemRangeChanged(oldInnerDataListSize, newList.size)
                    notifyItemRangeChanged(oldInnerDataListSize, itemCount - oldInnerDataListSize)
                }
            } else {
                notifyItemRangeInserted(oldInnerDataListSize, newList.size)
                notifyItemRangeChanged(oldInnerDataListSize, itemCount - oldInnerDataListSize)
            }
            onInnerDataChanged?.invoke(innerAdapter.dataList)
        }
    }

    fun add(entity: Entity, position: Int) {
        if (position >= 0 && position <= innerAdapter.dataList.size) {
            val oldInnerDataListSize = innerAdapter.dataList.size
            innerAdapter.dataList.add(position, entity)
            if (oldInnerDataListSize == 0) {
                if (enableLoadMore) currentItemType = ITEM_TYPE_LOADING // 数据从 无 -> 有后确保是 loading 状态
                if (enableEmptyView) {
                    notifyDataSetChanged()
                } else {
                    notifyItemInserted(position)
                    notifyItemRangeChanged(position, itemCount - position)
                }
            } else {
                notifyItemInserted(position)
                notifyItemRangeChanged(position, itemCount - position)
            }
            onInnerDataChanged?.invoke(innerAdapter.dataList)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (enableEmptyView && isInnerDataEmpty()) {
            return currentItemType
        } else if (enableLoadMore && (position == itemCount - 1)) {
            return currentItemType
        } else {
            innerAdapter.getItemViewType(position)
        }
    }

    override fun getItemCount(): Int = if (isInnerDataEmpty() && enableEmptyView) 1 else (innerAdapter.itemCount + (if (enableLoadMore) 1 else 0))

    fun add(entity: Entity) {
        add(entity, innerAdapter.dataList.size)
    }

    @Volatile
    private var isLoadMoreRequestingNow: Boolean = false // 避免重复请求

    fun callLoadMore(refresh: Boolean) {
        if (!isLoadMoreRequestingNow) {
            isLoadMoreRequestingNow = true
            STLogUtil.v(tag, "callLoadMore >>>>>>>> isLoadMoreRequestingNow=$isLoadMoreRequestingNow, refresh=$refresh")
            onLoadMoreListener?.invoke(refresh)
        } else {
            STLogUtil.e(tag, "callLoadMore -------[ignore]------- isLoadMoreRequestingNow=$isLoadMoreRequestingNow, refresh=$refresh")
        }
    }

    fun isInnerDataNotEmpty() = innerData().isNotEmpty()
    fun isInnerDataEmpty() = innerData().isEmpty()
    fun innerData() = innerAdapter.dataList

    fun showEmpty() {
        completelyLoadMoreRequesting()
        if (isInnerDataEmpty()) {
            STLogUtil.v(tag, "showEmpty success, enableEmptyView=$enableEmptyView, innerDataSize=${innerData().size}")
            currentItemType = ITEM_TYPE_EMPTY

            val innerRecyclerView = recyclerView
            innerRecyclerView?.post {
                if (!innerRecyclerView.isComputingLayout) notifyDataSetChanged()

            }
        } else {
            STLogUtil.v(tag, "showEmpty failure, enableEmptyView=$enableEmptyView, innerDataSize=${innerData().size}")
        }
    }

    fun showEmptyNone() {
        completelyLoadMoreRequesting()
        if (isInnerDataEmpty()) {
            STLogUtil.v(tag, "showEmptyNone success, enableEmptyView=$enableEmptyView, innerDataSize=${innerData().size}")
            currentItemType = ITEM_TYPE_EMPTY_NONE

            val innerRecyclerView = recyclerView
            innerRecyclerView?.post {
                if (!innerRecyclerView.isComputingLayout) notifyDataSetChanged()
            }
        } else {
            STLogUtil.v(tag, "showEmptyNone failure, enableEmptyView=$enableEmptyView, innerDataSize=${innerData().size}")
        }
    }

    /**
     * 触发 loadMore(refresh=true)
     */
    fun showEmptyLoading() {
        if (isInnerDataEmpty()) {
            STLogUtil.v(tag, "showEmptyLoading success, enableEmptyView=$enableEmptyView, innerDataSize=${innerData().size}")
            currentItemType = ITEM_TYPE_EMPTY_LOADING

            val innerRecyclerView = recyclerView
            innerRecyclerView?.post {
                if (!innerRecyclerView.isComputingLayout) notifyDataSetChanged()
            }
        } else {
            STLogUtil.v(tag, "showEmptyLoading failure, enableEmptyView=$enableEmptyView, innerDataSize=${innerData().size}")
        }
    }

    fun showEmptyLoadFailure() {
        completelyLoadMoreRequesting()
        if (isInnerDataEmpty()) {
            STLogUtil.v(tag, "showEmptyLoadFailure success, enableEmptyView=$enableEmptyView, innerDataSize=${innerData().size}")
            currentItemType = ITEM_TYPE_EMPTY_LOADING_FAILURE

            val innerRecyclerView = recyclerView
            innerRecyclerView?.post {
                if (!innerRecyclerView.isComputingLayout) notifyDataSetChanged()
            }
        } else {
            STLogUtil.v(tag, "showEmptyLoadFailure failure, enableEmptyView=$enableEmptyView, innerDataSize=${innerData().size}")
        }
    }

    /**
     * 触发 loadMore(refresh=false)
     */
    fun showLoading() {
        STLogUtil.v(tag, "showLoading success, enableLoadMore=$enableLoadMore, innerDataSize=${innerData().size}")
        currentItemType = if (isInnerDataNotEmpty()) ITEM_TYPE_LOADING else ITEM_TYPE_EMPTY_LOADING
        enableChangeAnimations(false)

        val innerRecyclerView = recyclerView
        innerRecyclerView?.post {
            if (!innerRecyclerView.isComputingLayout) notifyItemChanged(itemCount)
            enableChangeAnimations(true)
        }
    }

    fun showLoadFailure() {
        completelyLoadMoreRequesting()
        STLogUtil.v(tag, "showLoadFailure success, enableLoadMore=$enableLoadMore, innerDataSize=${innerData().size}")
        currentItemType = if (isInnerDataNotEmpty()) ITEM_TYPE_LOAD_FAILED else ITEM_TYPE_EMPTY_LOADING_FAILURE
        enableChangeAnimations(false)

        // https://stackoverflow.com/questions/27070220/android-recyclerview-notifydatasetchanged-illegalstateexception

        val innerRecyclerView = recyclerView
        innerRecyclerView?.post {
            if (!innerRecyclerView.isComputingLayout) notifyItemChanged(itemCount)
            enableChangeAnimations(true)
        }
    }

    fun showNoMore() {
        completelyLoadMoreRequesting()
        STLogUtil.v(tag, "showNoMore success, enableLoadMore=$enableLoadMore, innerDataSize=${innerData().size}")
        currentItemType = if (isInnerDataNotEmpty()) ITEM_TYPE_NO_MORE else ITEM_TYPE_EMPTY
        enableChangeAnimations(false)
        val innerRecyclerView = recyclerView
        innerRecyclerView?.post {
            if (!innerRecyclerView.isComputingLayout) notifyItemChanged(itemCount)
            enableChangeAnimations(true)
        }
    }

    fun completelyLoadMoreRequesting() {
        isLoadMoreRequestingNow = false
        STLogUtil.v(tag, "completelyLoadMoreRequesting, enableLoadMore=$enableLoadMore, innerDataSize=${innerData().size}, isLoadMoreRequestingNow=$isLoadMoreRequestingNow")
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        innerAdapter.onAttachedToRecyclerView(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val oldSpanSizeLookup = layoutManager.spanSizeLookup
            layoutManager.spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (enableLoadMore) {
                        if (position == itemCount - 1) layoutManager.spanCount else oldSpanSizeLookup.getSpanSize(position)
                    } else {
                        1
                    }
                }
            }
        }
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        innerAdapter.onViewAttachedToWindow(holder)
        if (holder.layoutPosition == itemCount - 1 && enableLoadMore) {
            (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)?.isFullSpan = true
        }
    }

    companion object {
        private val TAG: String = STEmptyLoadingWrapper::class.java.simpleName

        private const val ITEM_TYPE_EMPTY_NONE = -8888880
        private const val ITEM_TYPE_LOAD_FAILED = -8888881
        private const val ITEM_TYPE_NO_MORE = -8888882
        private const val ITEM_TYPE_LOADING = -8888883

        private const val ITEM_TYPE_EMPTY = -8888884
        private const val ITEM_TYPE_EMPTY_LOADING = -8888885
        private const val ITEM_TYPE_EMPTY_LOADING_FAILURE = -8888886

        fun getItemTypeName(itemType: Int): String = when (itemType) {
            ITEM_TYPE_EMPTY_NONE -> "ITEM_TYPE_EMPTY_NONE"
            ITEM_TYPE_LOAD_FAILED -> "ITEM_TYPE_LOAD_FAILED"
            ITEM_TYPE_NO_MORE -> "ITEM_TYPE_NO_MORE"
            ITEM_TYPE_LOADING -> "ITEM_TYPE_LOADING"
            ITEM_TYPE_EMPTY -> "ITEM_TYPE_EMPTY"
            ITEM_TYPE_EMPTY_LOADING -> "ITEM_TYPE_EMPTY_LOADING"
            ITEM_TYPE_EMPTY_LOADING_FAILURE -> "ITEM_TYPE_EMPTY_LOADING_FAILURE"
            else -> "INNER_NORMAL_BIND_VIEW_HOLDER"
        }

        @Suppress("MemberVisibilityCanBePrivate")
        @JvmStatic
        @JvmOverloads
        fun createDefaultFooterView(context: Context?, text: String, textSize: Float = 12.0f, mainAxisSize: Int = STSystemUtil.getPxFromDp(52f).toInt(), crossAxisSize: Int = MATCH_PARENT, orientation: Int = LinearLayout.VERTICAL, backgroundColor: Int = Color.WHITE, textColor: Int = Color.parseColor("#666666")): View {
            val itemView = TextView(context)
            itemView.layoutParams = ViewGroup.LayoutParams(
                    if (orientation == LinearLayout.VERTICAL) crossAxisSize else mainAxisSize,
                    if (orientation == LinearLayout.VERTICAL) mainAxisSize else crossAxisSize
            )
            itemView.text = text
            itemView.textSize = textSize
            itemView.setTextColor(textColor)
            itemView.gravity = Gravity.CENTER

            if (orientation == LinearLayout.HORIZONTAL) {
                itemView.gravity = Gravity.CENTER
                itemView.setBackgroundResource(R.drawable.st_drawable_radius_4dp_white)
            } else {
                itemView.setBackgroundColor(backgroundColor)
            }
            itemView.tag = itemView
            return itemView
        }

        @Suppress("MemberVisibilityCanBePrivate")
        @JvmStatic
        @JvmOverloads
        fun createDefaultFooterLoadingView(context: Context?, text: String, textSize: Float = 12.0f, mainAxisSize: Int = STSystemUtil.getPxFromDp(52f).toInt(), crossAxisSize: Int = MATCH_PARENT, orientation: Int = LinearLayout.VERTICAL, backgroundColor: Int = Color.WHITE, textColor: Int = Color.parseColor("#666666"), @Suppress("DEPRECATION") indeterminateDrawable: Drawable? = context?.resources?.getDrawable(R.drawable.st_footer_loading_rotate), indeterminateDrawableSize: Int = 12.toPxFromDp()): View {
            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = ViewGroup.LayoutParams(
                    if (orientation == LinearLayout.VERTICAL) crossAxisSize else mainAxisSize,
                    if (orientation == LinearLayout.VERTICAL) mainAxisSize else crossAxisSize
            )

            val textView = TextView(context)
            textView.text = text
            textView.textSize = textSize
            textView.setTextColor(textColor)

            if (orientation == LinearLayout.HORIZONTAL) {
                linearLayout.orientation = LinearLayout.VERTICAL
                textView.gravity = Gravity.CENTER
                linearLayout.gravity = Gravity.CENTER
                linearLayout.setBackgroundResource(R.drawable.st_drawable_radius_4dp_white)
            } else {
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.gravity = Gravity.CENTER
                textView.gravity = Gravity.CENTER
                linearLayout.setBackgroundColor(backgroundColor)
            }

            val progressBar = ProgressBar(context)
            progressBar.indeterminateDrawable = indeterminateDrawable
            progressBar.layoutParams = LinearLayout.LayoutParams(indeterminateDrawableSize, indeterminateDrawableSize).apply {
                if (orientation == LinearLayout.VERTICAL) {
                    leftMargin = STSystemUtil.getPxFromDp(2f).toInt()
                } else {
                    topMargin = STSystemUtil.getPxFromDp(2f).toInt()
                }
            }

            linearLayout.addView(textView)
            linearLayout.addView(progressBar)
            linearLayout.tag = textView
            return linearLayout
        }

        @SuppressLint("InflateParams")
        @Suppress("MemberVisibilityCanBePrivate")
        @JvmStatic
        @JvmOverloads
        fun createDefaultEmptyView(context: Context?, text: String = "没有更多内容...", textSize: Float = 12.0f, textColor: Int = Color.parseColor("#666666"), orientation: Int = LinearLayout.HORIZONTAL, mainAxisSize: Int = MATCH_PARENT, crossAxisSize: Int = STSystemUtil.getPxFromDp(52f).toInt()): View {
            var itemView: View? = null
            if (itemView == null) {
                itemView = TextView(context)
                itemView.layoutParams = ViewGroup.LayoutParams(
                        if (orientation == LinearLayout.VERTICAL) crossAxisSize else mainAxisSize,
                        if (orientation == LinearLayout.VERTICAL) mainAxisSize else crossAxisSize
                )
                itemView.text = text
                itemView.textSize = textSize
                itemView.setTextColor(textColor)
                itemView.gravity = Gravity.CENTER
                itemView.tag = itemView
            }

            itemView.layoutParams = if (orientation == LinearLayout.VERTICAL) ViewGroup.LayoutParams(crossAxisSize, mainAxisSize) else ViewGroup.LayoutParams(mainAxisSize, crossAxisSize)
            return itemView
        }

        @SuppressLint("InflateParams")
        @Suppress("MemberVisibilityCanBePrivate")
        @JvmStatic
        @JvmOverloads
        fun createDefaultEmptyLoadingView(context: Context?, text: String = "加载中...", textSize: Float = 12.0f, orientation: Int = LinearLayout.HORIZONTAL, mainAxisSize: Int = MATCH_PARENT, crossAxisSize: Int = STSystemUtil.getPxFromDp(52f).toInt(), textColor: Int = Color.parseColor("#666666"), @Suppress("DEPRECATION") indeterminateDrawable: Drawable? = context?.resources?.getDrawable(R.drawable.st_footer_loading_rotate), indeterminateDrawableSize: Int = STSystemUtil.getPxFromDp(22.5f).toInt()): View {
            var contentView: View? = null
            if (contentView == null) {
                contentView = LinearLayout(context)
                contentView.layoutParams = ViewGroup.LayoutParams(
                        if (orientation == LinearLayout.VERTICAL) crossAxisSize else mainAxisSize,
                        if (orientation == LinearLayout.VERTICAL) mainAxisSize else crossAxisSize
                )
                contentView.orientation = orientation
                contentView.gravity = Gravity.CENTER

                val textView = TextView(context)
                textView.text = text
                textView.textSize = textSize
                textView.setTextColor(textColor)
                textView.gravity = Gravity.CENTER

                val progressBar = ProgressBar(context)
                progressBar.indeterminateDrawable = indeterminateDrawable
                progressBar.layoutParams = LinearLayout.LayoutParams(indeterminateDrawableSize, indeterminateDrawableSize).apply { leftMargin = STSystemUtil.getPxFromDp(2f).toInt() }

                contentView.addView(textView)
                contentView.addView(progressBar)
                contentView.tag = textView
            }
            contentView.layoutParams = if (orientation == LinearLayout.VERTICAL) ViewGroup.LayoutParams(crossAxisSize, mainAxisSize) else ViewGroup.LayoutParams(mainAxisSize, crossAxisSize)
            return contentView
        }

        @SuppressLint("InflateParams")
        @Suppress("MemberVisibilityCanBePrivate")
        @JvmStatic
        @JvmOverloads
        fun createDefaultEmptyLoadingFailure(context: Context?, text: String = "加载出错了, 点击重试...", textSize: Float = 12.0f, orientation: Int = LinearLayout.HORIZONTAL, mainAxisSize: Int = MATCH_PARENT, crossAxisSize: Int = STSystemUtil.getPxFromDp(52f).toInt(), textColor: Int = Color.parseColor("#666666")): View {
            var linearLayout: View? = null
            if (linearLayout == null) {
                linearLayout = LinearLayout(context)
                linearLayout.layoutParams = if (orientation == LinearLayout.VERTICAL) ViewGroup.LayoutParams(crossAxisSize, mainAxisSize) else ViewGroup.LayoutParams(mainAxisSize, crossAxisSize)
                linearLayout.orientation = orientation
                linearLayout.gravity = Gravity.CENTER

                val textView = TextView(context)
                textView.text = text
                textView.textSize = textSize
                textView.setTextColor(textColor)
                textView.gravity = Gravity.CENTER

                linearLayout.addView(textView)
                linearLayout.tag = textView
            }
            linearLayout.layoutParams = if (orientation == LinearLayout.VERTICAL) ViewGroup.LayoutParams(crossAxisSize, mainAxisSize) else ViewGroup.LayoutParams(mainAxisSize, crossAxisSize)
            return linearLayout
        }

        @Suppress("MemberVisibilityCanBePrivate")
        @JvmStatic
        @JvmOverloads
        fun createDefaultEmptyNone(context: Context?, orientation: Int = LinearLayout.HORIZONTAL, mainAxisSize: Int = MATCH_PARENT): View {
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = orientation
            linearLayout.layoutParams = ViewGroup.LayoutParams(
                    if (orientation == LinearLayout.VERTICAL) MATCH_PARENT else mainAxisSize,
                    if (orientation == LinearLayout.VERTICAL) mainAxisSize else MATCH_PARENT
            )
            return linearLayout
        }
    }
}