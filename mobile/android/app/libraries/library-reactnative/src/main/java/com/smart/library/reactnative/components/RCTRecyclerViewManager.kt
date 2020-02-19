package com.smart.library.reactnative.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter


@Suppress("unused", "MemberVisibilityCanBePrivate")
class RCTRecyclerViewManager : SimpleViewManager<RecyclerView>() {

    override fun createViewInstance(reactContext: ThemedReactContext): RecyclerView {
        return RecyclerView(reactContext).apply {
            fitsSystemWindows = true
            clipToPadding = false
        }
    }

    override fun getName(): String = "RCTRecyclerView"

    /**
     * @param orientation VERTICAL==1 or HORIZONTAL==0
     */
    @ReactProp(name = "orientation", defaultInt = 1)
    fun setOrientation(recyclerView: RecyclerView, orientation: Int) {
        val staggeredGridLayoutManager: StaggeredGridLayoutManager? = recyclerView.layoutManager as? StaggeredGridLayoutManager
        if (staggeredGridLayoutManager != null) {
            staggeredGridLayoutManager.orientation = orientation
            recyclerView.layoutManager = staggeredGridLayoutManager
        } else {
            recyclerView.layoutManager = StaggeredGridLayoutManager(1, orientation)
        }

        updateRecyclerView(recyclerView)

        STLogUtil.d("RCTRecyclerView", "setOrientation orientation=$orientation")
    }

    /**
     * @param spanCount   If orientation is vertical, spanCount is number of columns. If
     *                    orientation is horizontal, spanCount is number of rows.
     */
    @ReactProp(name = "spanCount", defaultInt = 1)
    fun setSpanCount(recyclerView: RecyclerView, spanCount: Int) {
        val staggeredGridLayoutManager: StaggeredGridLayoutManager? = recyclerView.layoutManager as? StaggeredGridLayoutManager
        if (staggeredGridLayoutManager != null) {
            staggeredGridLayoutManager.spanCount = spanCount
            recyclerView.layoutManager = staggeredGridLayoutManager
        } else {
            recyclerView.layoutManager = StaggeredGridLayoutManager(spanCount, 1)
        }

        updateRecyclerView(recyclerView)

        STLogUtil.d("RCTRecyclerView", "setSpanCount spanCount=$spanCount")
    }


    @ReactProp(name = "data")
    fun setData(recyclerView: RecyclerView, data: ReadableArray) {
        STLogUtil.d("RCTRecyclerView", "setData data=$data")
        @Suppress("UNCHECKED_CAST")
        val dataList: MutableList<Int> = (data.toArrayList() as? ArrayList<Int>)?.toMutableList() ?: arrayListOf()
        STLogUtil.d("RCTRecyclerView", "setData dataList=$dataList")

        val cachedHeightMap: SparseArray<Int> = SparseArray()
        recyclerView.adapter = object : STRecyclerViewAdapter<Int, STRecyclerViewAdapter.ViewHolder>(recyclerView.context, dataList) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(TextView(recyclerView.context).apply {
                    this.setPadding(10, 10, 10, 10)
                })
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val textView: TextView? = holder.itemView as? TextView
                if (textView != null) {
                    textView.text = "value:${dataList[position]}"
                    textView.setBackgroundColor(STRandomUtil.randomColor)

                    val params: ViewGroup.LayoutParams = textView.layoutParams ?: RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)

                    var cachedHeight: Int = cachedHeightMap.get(position, -1)
                    if (cachedHeight == -1) {
                        cachedHeight = STRandomUtil.getRandom(100, 1000)
                    }
                    cachedHeightMap.put(position, cachedHeight)

                    params.height = cachedHeight

                    textView.layoutParams = params

                    textView.setOnClickListener {
                        val context: Context = recyclerView.context
                        if (context is ReactContext) {
                            val event: WritableMap = Arguments.createMap()
                            event.putInt("position", position)

                            context.getJSModule(RCTEventEmitter::class.java).receiveEvent(recyclerView.id, "onItemClickedEvent", event)
                        }
                    }
                }
            }
        }
    }

    fun updateRecyclerView(recyclerView: RecyclerView) {
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
                .build()
    }

}