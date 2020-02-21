package com.smart.library.reactnative.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.smart.library.base.toPxFromDp
import com.smart.library.util.STLogUtil
import com.smart.library.util.STRandomUtil
import com.smart.library.util.STRecyclerViewStickyHeaderUtil
import com.smart.library.util.image.STImageManager
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper
import com.smart.library.widget.recyclerview.STRecyclerHeaderViewAdapter
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter

@Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
class RCTRecyclerViewManager : ViewGroupManager<RCTRecyclerViewManager.RCTRecyclerViewInFrameLayout>() {

    private val tag: String get() = name

    override fun createViewInstance(reactContext: ThemedReactContext): RCTRecyclerViewInFrameLayout {
        STLogUtil.d(tag, "createViewInstance thread=${Thread.currentThread().name}")
        return RCTRecyclerViewInFrameLayout(reactContext)
    }

    override fun getName(): String = "RCTRecyclerView"

    /**
     * @param orientation VERTICAL==1 or HORIZONTAL==0
     */
    @ReactProp(name = "orientation", defaultInt = 1)
    fun setOrientation(recyclerViewInFrameLayout: RCTRecyclerViewInFrameLayout, orientation: Int) {
        val staggeredGridLayoutManager: StaggeredGridLayoutManager? = recyclerViewInFrameLayout.innerRecyclerView.layoutManager as? StaggeredGridLayoutManager
        if (staggeredGridLayoutManager != null) {
            staggeredGridLayoutManager.orientation = orientation
            setLayoutManager(recyclerViewInFrameLayout, staggeredGridLayoutManager)
        } else {
            setLayoutManager(recyclerViewInFrameLayout, StaggeredGridLayoutManager(1, orientation))
        }

        updateRecyclerView(recyclerViewInFrameLayout)

        STLogUtil.d(tag, "setOrientation thread=${Thread.currentThread().name} orientation=$orientation")
    }

    /**
     * @param spanCount   If orientation is vertical, spanCount is number of columns. If
     *                    orientation is horizontal, spanCount is number of rows.
     */
    @ReactProp(name = "spanCount", defaultInt = 1)
    fun setSpanCount(recyclerViewInFrameLayout: RCTRecyclerViewInFrameLayout, spanCount: Int) {
        val staggeredGridLayoutManager: StaggeredGridLayoutManager? = recyclerViewInFrameLayout.innerRecyclerView.layoutManager as? StaggeredGridLayoutManager
        if (staggeredGridLayoutManager != null) {
            staggeredGridLayoutManager.spanCount = spanCount
            setLayoutManager(recyclerViewInFrameLayout, staggeredGridLayoutManager)
        } else {
            setLayoutManager(recyclerViewInFrameLayout, StaggeredGridLayoutManager(spanCount, 1))
        }

        updateRecyclerView(recyclerViewInFrameLayout)

        STLogUtil.d(tag, "setSpanCount thread=${Thread.currentThread().name} spanCount=$spanCount")
    }

    private var stickyHeaderViewHeight: Int = 80
    @ReactProp(name = "stickyHeaderViewHeight", defaultInt = 80)
    fun setStickyHeaderViewHeight(recyclerViewInFrameLayout: RCTRecyclerViewInFrameLayout, stickyHeaderViewHeight: Int) {
        this.stickyHeaderViewHeight = stickyHeaderViewHeight
        STLogUtil.d(tag, "setStickyHeaderViewHeight thread=${Thread.currentThread().name} stickyHeaderViewHeight=$stickyHeaderViewHeight stickyHeaderView==null?${stickyHeaderView == null}")
    }

    private fun setLayoutManager(recyclerViewInFrameLayout: RCTRecyclerViewInFrameLayout, layoutManager: RecyclerView.LayoutManager? = null): RecyclerView.LayoutManager {
        val layoutManager: RecyclerView.LayoutManager = layoutManager ?: recyclerViewInFrameLayout.innerRecyclerView.layoutManager ?: StaggeredGridLayoutManager(1, 1)
        recyclerViewInFrameLayout.innerRecyclerView.layoutManager = layoutManager

        // config stickyHeaderView
        val innerStickyHeaderView: View? = stickyHeaderView
        if (innerStickyHeaderView != null) {
            STLogUtil.w(tag, "setLayoutManager recyclerViewInFrameLayout.childViewCount=${recyclerViewInFrameLayout.childCount},innerStickyHeaderView.top=${innerStickyHeaderView.top}, innerStickyHeaderView.y=${innerStickyHeaderView.y}, stickyHeaderViewHeight.toPxFromDp()=${stickyHeaderViewHeight.toPxFromDp()}")
            removeViewParent(innerStickyHeaderView)
            recyclerViewInFrameLayout.addView(innerStickyHeaderView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, stickyHeaderViewHeight.toPxFromDp()))

            val stickyHeaderUtil = STRecyclerViewStickyHeaderUtil(
                    layoutManager,
                    innerStickyHeaderView,
                    onUpdateStickyHeaderView = { stickyHeaderView: View, currentMinVisiblePosition: Int ->

                    },
                    findHeaderPositionBeforeCurrentMinVisiblePosition = { minVisiblePosition: Int ->
                        if (minVisiblePosition <= 1) Int.MIN_VALUE else 1
                    },
                    findHeaderPositionAfterCurrentMinVisiblePosition = { minVisiblePosition: Int ->
                        if (minVisiblePosition >= 1) Int.MIN_VALUE else 1
                    }
            )
            recyclerViewInFrameLayout.innerRecyclerView.clearOnScrollListeners()
            recyclerViewInFrameLayout.innerRecyclerView.addOnScrollListener(stickyHeaderUtil.onStickyHeaderScrollListener)
        }

        return layoutManager
    }


    @SuppressLint("SetTextI18n")
    @ReactProp(name = "initData")
    fun setInitData(recyclerViewInFrameLayout: RCTRecyclerViewInFrameLayout, initData: ReadableArray?) {
        STLogUtil.d(tag, "setInitData thread=${Thread.currentThread().name} headerView=$headerView0, initData=$initData")
        @Suppress("UNCHECKED_CAST")
        val dataList: MutableList<Int> = (initData?.toArrayList() as? ArrayList<Int>)?.toMutableList() ?: arrayListOf()
        STLogUtil.d(tag, "setData dataList=$dataList")

        val cachedHeightMap: SparseArray<Int> = SparseArray()
        val originAdapter = object : STRecyclerViewAdapter<Int, RecyclerView.ViewHolder>(recyclerViewInFrameLayout.innerRecyclerView.context, dataList) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                STLogUtil.d(tag, "onCreateViewHolder thread=${Thread.currentThread().name}")
                return ViewHolder(FrameLayout(recyclerViewInFrameLayout.innerRecyclerView.context).apply {
                    val imageView: SimpleDraweeView = SimpleDraweeView(recyclerViewInFrameLayout.innerRecyclerView.context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }

                    val textView: TextView = TextView(recyclerViewInFrameLayout.innerRecyclerView.context).apply {
                        this.setPadding(10, 10, 10, 10)
                    }

                    addView(imageView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                    addView(textView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))

                    tag = arrayOf(imageView, textView)
                })
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                STLogUtil.d(tag, "onBindViewHolder thread=${Thread.currentThread().name}, position=$position")
                val itemView: View = holder.itemView
                val tagViewArray: Array<*>? = itemView.tag as? Array<*>
                val imageView: SimpleDraweeView? = tagViewArray?.get(0) as? SimpleDraweeView
                val textView: TextView? = tagViewArray?.get(1) as? TextView

                val params: ViewGroup.LayoutParams = itemView.layoutParams ?: RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                var cachedHeight: Int = cachedHeightMap.get(position, -1)
                if (cachedHeight == -1) {
                    cachedHeight = STRandomUtil.getRandom(100, 1000)
                }
                cachedHeightMap.put(position, cachedHeight)
                params.height = cachedHeight
                itemView.layoutParams = params

                itemView.setOnClickListener {
                    val event: WritableMap = Arguments.createMap()
                    event.putInt("position", position)
                    sendRNMessage(recyclerViewInFrameLayout, "onItemClickedEvent", event)
                }


                if (imageView != null) {
                    imageView.setBackgroundColor(STRandomUtil.randomColor)
                    STImageManager.show(imageView, STRandomUtil.getImageUrl(position))
                }
                if (textView != null) {
                    textView.text = "value:${(dataList[position] as? Number)?.toString()}"
                }
            }
        }

        val headerAdapter: STRecyclerHeaderViewAdapter<Int> = STRecyclerHeaderViewAdapter(originAdapter)
        headerAdapter.addHeaderViews(true, headerView0, headerView1)


        val loadingAdapter: STEmptyLoadingWrapper<Int> = STEmptyLoadingWrapper(headerAdapter)

        // onLoadMore listener
        loadingAdapter.onLoadMoreListener = {
            requestLoadMore(recyclerViewInFrameLayout)
        }

        setLayoutManager(recyclerViewInFrameLayout, recyclerViewInFrameLayout.innerRecyclerView.layoutManager)

        recyclerViewInFrameLayout.innerRecyclerView.swapAdapter(loadingAdapter, false)

        val animator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        recyclerViewInFrameLayout.innerRecyclerView.itemAnimator = animator
    }

    private var headerView0: View? = null
    private var headerView1: View? = null
    private var stickyHeaderView: View? = null
    override fun addView(parent: RCTRecyclerViewInFrameLayout?, child: View?, index: Int) {
        super.addView(parent, child, index)
        STLogUtil.w(tag, "addView index=$index, name=${child?.javaClass?.simpleName}")
        if (index == 0 && child != null) {
            val childViewGroup: ViewGroup? = child as? ViewGroup
            headerView0 = childViewGroup?.getChildAt(0)
            headerView1 = childViewGroup?.getChildAt(1)
            stickyHeaderView = childViewGroup?.getChildAt(2)

            removeViewParent(child)
            removeViewParent(headerView0)
            removeViewParent(headerView1)
            removeViewParent(stickyHeaderView)
        }
    }

    private fun removeViewParent(child: View?): View? {
        (child?.parent as? ViewGroup)?.removeView(child)
        return child
    }

    override fun onAfterUpdateTransaction(viewInFrameLayout: RCTRecyclerViewInFrameLayout) {
        super.onAfterUpdateTransaction(viewInFrameLayout)
        STLogUtil.w(tag, "onAfterUpdateTransaction")
    }

    fun requestLoadMore(recyclerViewInFrameLayout: RCTRecyclerViewInFrameLayout) {
        STLogUtil.d(tag, "requestLoadMore thread=${Thread.currentThread().name}")
        sendRNMessage(recyclerViewInFrameLayout, "onRequestLoadMoreEvent")
    }

    @ReactProp(name = "loadMoreData")
    fun setLoadMoreData(recyclerViewInFrameLayout: RCTRecyclerViewInFrameLayout, data: ReadableArray?) {
        STLogUtil.d(tag, "setLoadMoreData thread=${Thread.currentThread().name} data=$data")
        val adapterWrapper: STEmptyLoadingWrapper<Int>? = recyclerViewInFrameLayout.innerRecyclerView.adapter as? STEmptyLoadingWrapper<Int>
        if (adapterWrapper != null) {
            val newList: MutableList<Int>? = (data?.toArrayList() as? ArrayList<Int>)?.toMutableList()
            when {
                newList == null -> { // load failure
                    adapterWrapper.showLoadFailure()
                }
                newList.isNotEmpty() -> { // load success
                    adapterWrapper.add(newList)
                    adapterWrapper.completelyLoadMoreRequesting()
                }
                else -> { // load no more
                    adapterWrapper.showNoMore()
                }
            }
        }
    }

    fun updateRecyclerView(recyclerViewInFrameLayout: RCTRecyclerViewInFrameLayout) {
        recyclerViewInFrameLayout.innerRecyclerView.adapter = recyclerViewInFrameLayout.innerRecyclerView.adapter
    }

    /**
     * 映射连续的比如滑动Event事件
     */
    override fun getExportedCustomBubblingEventTypeConstants(): MutableMap<String, Any> {
        return MapBuilder.builder<String, Any>()
                .build()
    }

    /**
     * 映射指向的比如点击事件
     */
    override fun getExportedCustomDirectEventTypeConstants(): MutableMap<String, Any> {
        return MapBuilder.builder<String, Any>()
                .put("onItemClickedEvent", MapBuilder.of("registrationName", "onItemClicked"))
                .put("onRequestLoadMoreEvent", MapBuilder.of("registrationName", "onRequestLoadMore"))
                .build()
    }

    private val commandShowMoreData: Int = 1
    override fun getCommandsMap(): Map<String, Int> {
        return mapOf("showMoreData" to commandShowMoreData)
    }

    override fun receiveCommand(root: RCTRecyclerViewInFrameLayout, commandId: Int, args: ReadableArray?) {
        STLogUtil.w(tag, "receiveCommand Int thread=${Thread.currentThread().name}, commandId=$commandId, args=$args")
        when (commandId) {
            commandShowMoreData -> {
                setLoadMoreData(root, args)
            }
        }
    }

    fun sendRNMessage(recyclerViewInFrameLayout: RCTRecyclerViewInFrameLayout, eventName: String, event: WritableMap? = null) {
        var eventEmitter: RCTEventEmitter? = null
        try {
            eventEmitter = (recyclerViewInFrameLayout.innerRecyclerView.context as? ReactContext)?.getJSModule(RCTEventEmitter::class.java)
        } catch (ignore: Exception) {
        }
        if (eventEmitter != null) {
            eventEmitter.receiveEvent(recyclerViewInFrameLayout.innerRecyclerView.id, eventName, event)
            STLogUtil.w(tag, "sendRNMessage thread=${Thread.currentThread().name} success eventName=$eventName")
        } else {
            STLogUtil.e(tag, "sendRNMessage thread=${Thread.currentThread().name} failure eventName=$eventName")
        }
    }

    class RCTRecyclerViewInFrameLayout(context: Context) : FrameLayout(context) {

        val innerRecyclerView: RecyclerView = RecyclerView(context)

        private var requestedLayout = false

        @SuppressLint("WrongCall")
        override fun requestLayout() {
            super.requestLayout()
            // We need to intercept this method because if we don't our children will never update, for example that notifyItemChanged will not work
            // https://stackoverflow.com/questions/49371866/recyclerview-wont-update-child-until-i-scroll
            // https://stackoverflow.com/questions/44868899/recyclerview-in-react-native-notifyiteminserted-and-notifydatasetchanged-ha?noredirect=1&lq=1
            if (!requestedLayout) {
                requestedLayout = true
                post {
                    requestedLayout = false
                    layout(left, top, right, bottom)
                    onLayout(false, left, top, right, bottom) // must be need this
                }
            }
        }

        init {
            addView(innerRecyclerView)
        }

    }
}