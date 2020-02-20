package com.smart.library.reactnative.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
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
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.smart.library.util.STLogUtil
import com.smart.library.util.STRandomUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.image.STImageManager
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper
import com.smart.library.widget.recyclerview.STRecyclerHeaderViewAdapter
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter

@Suppress("unused", "MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
class RCTRecyclerViewManager : SimpleViewManager<RCTRecyclerViewManager.RCTRecyclerView>() {

    private val tag: String get() = name

    override fun createViewInstance(reactContext: ThemedReactContext): RCTRecyclerView {
        STLogUtil.d(tag, "createViewInstance thread=${Thread.currentThread().name}")
        return RCTRecyclerView(reactContext)
    }

    override fun getName(): String = "RCTRecyclerView"

    /**
     * @param orientation VERTICAL==1 or HORIZONTAL==0
     */
    @ReactProp(name = "orientation", defaultInt = 1)
    fun setOrientation(recyclerView: RCTRecyclerView, orientation: Int) {
        val staggeredGridLayoutManager: StaggeredGridLayoutManager? = recyclerView.layoutManager as? StaggeredGridLayoutManager
        if (staggeredGridLayoutManager != null) {
            staggeredGridLayoutManager.orientation = orientation
            recyclerView.layoutManager = staggeredGridLayoutManager
        } else {
            recyclerView.layoutManager = StaggeredGridLayoutManager(1, orientation)
        }

        updateRecyclerView(recyclerView)

        STLogUtil.d(tag, "setOrientation thread=${Thread.currentThread().name} orientation=$orientation")
    }

    /**
     * @param spanCount   If orientation is vertical, spanCount is number of columns. If
     *                    orientation is horizontal, spanCount is number of rows.
     */
    @ReactProp(name = "spanCount", defaultInt = 1)
    fun setSpanCount(recyclerView: RCTRecyclerView, spanCount: Int) {
        val staggeredGridLayoutManager: StaggeredGridLayoutManager? = recyclerView.layoutManager as? StaggeredGridLayoutManager
        if (staggeredGridLayoutManager != null) {
            staggeredGridLayoutManager.spanCount = spanCount
            recyclerView.layoutManager = staggeredGridLayoutManager
        } else {
            recyclerView.layoutManager = StaggeredGridLayoutManager(spanCount, 1)
        }

        updateRecyclerView(recyclerView)

        STLogUtil.d(tag, "setSpanCount thread=${Thread.currentThread().name} spanCount=$spanCount")
    }


    @SuppressLint("SetTextI18n")
    @ReactProp(name = "initData")
    fun setInitData(recyclerView: RCTRecyclerView, initData: ReadableArray?) {
        STLogUtil.d(tag, "setInitData thread=${Thread.currentThread().name} initData=$initData")
        @Suppress("UNCHECKED_CAST")
        val dataList: MutableList<Int> = (initData?.toArrayList() as? ArrayList<Int>)?.toMutableList() ?: arrayListOf()
        STLogUtil.d(tag, "setData dataList=$dataList")

        val cachedHeightMap: SparseArray<Int> = SparseArray()
        val originAdapter = object : STRecyclerViewAdapter<Int, RecyclerView.ViewHolder>(recyclerView.context, dataList) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                STLogUtil.d(tag, "onCreateViewHolder thread=${Thread.currentThread().name}")
                return ViewHolder(FrameLayout(recyclerView.context).apply {
                    val imageView: SimpleDraweeView = SimpleDraweeView(recyclerView.context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }

                    val textView: TextView = TextView(recyclerView.context).apply {
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
                    sendRNMessage(recyclerView, "onItemClickedEvent", event)
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
        headerAdapter.addHeaderView(TextView(recyclerView.context).apply {
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, STSystemUtil.screenHeight)
            setBackgroundColor(Color.BLUE)
            text = "header view 0"
            textSize = 20f
            setTextColor(Color.WHITE)
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        })

        headerAdapter.addHeaderView(TextView(recyclerView.context).apply {
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, STSystemUtil.screenHeight)
            setBackgroundColor(Color.RED)
            text = "header view 1"
            textSize = 20f
            setTextColor(Color.WHITE)
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
        })

        val loadingAdapter: STEmptyLoadingWrapper<Int> = STEmptyLoadingWrapper(headerAdapter)

        // onLoadMore listener
        loadingAdapter.onLoadMoreListener = {
            requestLoadMore(recyclerView)
        }
        recyclerView.adapter = loadingAdapter

        val animator: DefaultItemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
        recyclerView.itemAnimator = animator
    }

    fun requestLoadMore(recyclerView: RCTRecyclerView) {
        STLogUtil.d(tag, "requestLoadMore thread=${Thread.currentThread().name}")
        sendRNMessage(recyclerView, "onRequestLoadMoreEvent")
    }

    @ReactProp(name = "loadMoreData")
    fun setLoadMoreData(recyclerView: RCTRecyclerView, data: ReadableArray?) {
        STLogUtil.d(tag, "setLoadMoreData thread=${Thread.currentThread().name} data=$data")
        val adapterWrapper: STEmptyLoadingWrapper<Int>? = recyclerView.adapter as? STEmptyLoadingWrapper<Int>
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

    fun updateRecyclerView(recyclerView: RCTRecyclerView) {
        recyclerView.adapter = recyclerView.adapter
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

    override fun receiveCommand(root: RCTRecyclerView, commandId: Int, args: ReadableArray?) {
        STLogUtil.w(tag, "receiveCommand Int thread=${Thread.currentThread().name}, commandId=$commandId, args=$args")
        when (commandId) {
            commandShowMoreData -> {
                setLoadMoreData(root, args)
            }
        }
    }

    fun sendRNMessage(recyclerView: RCTRecyclerView, eventName: String, event: WritableMap? = null) {
        var eventEmitter: RCTEventEmitter? = null
        try {
            eventEmitter = (recyclerView.context as? ReactContext)?.getJSModule(RCTEventEmitter::class.java)
        } catch (ignore: Exception) {
        }
        if (eventEmitter != null) {
            eventEmitter.receiveEvent(recyclerView.id, eventName, event)
            STLogUtil.w(tag, "sendRNMessage thread=${Thread.currentThread().name} success eventName=$eventName")
        } else {
            STLogUtil.e(tag, "sendRNMessage thread=${Thread.currentThread().name} failure eventName=$eventName")
        }
    }

    class RCTRecyclerView(context: Context) : RecyclerView(context) {
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
    }
}