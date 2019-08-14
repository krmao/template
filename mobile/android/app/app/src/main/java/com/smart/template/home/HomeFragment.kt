package com.smart.template.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.bus.STBusManager
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.template.R
import com.smart.template.home.test.*
import com.smart.template.widget.STCheckBoxGroupView
import kotlinx.android.synthetic.main.home_fragment.*
import org.jetbrains.anko.async

class HomeFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.home_fragment, container, false)

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        text1.setOnClickListener {
            STActivity.start(activity, Test1Fragment::class.java)
        }
        text2.setOnClickListener {
            STActivity.start(activity, Test2Fragment::class.java)
        }
        text3.setOnClickListener {
            STToastUtil.show("Hello World")
            STToastUtil.show("system toast")
        }
        text4.setOnClickListener {
            //adb push assets/ /sdcard/Android/data/com.smart.template/cache/
            //val copySuccess = STFileUtil.copyDirectory(STCacheManager.getCacheChildDir("assets"), File(PathUtils.getDataDirectory(STBaseApplication.INSTANCE)))
            //STToastUtil.show("copySuccess?$copySuccess")
        }
        text5.setOnClickListener {
            PullToNextPageFragment.goTo(context)
        }
        text6.setOnClickListener {
            ViewPagerScrollViewFragment.goTo(context)
        }
        text7.setOnClickListener {
            RecyclerViewDragAndTransferFragment.goTo(context)
        }
        text8.setOnClickListener {
            RecyclerViewSnapTopFragment.goTo(context)
        }

        text9.requestFocus()
        text9.setOnClickListener {
            val url = rtmpURLET.text.toString()
            STBusManager.call(context, "livestreaming/play", url, url.substringAfterLast("/"))
        }
        text10.setOnClickListener {
            STBusManager.call(context, "livestreaming/opensettings")
        }
        text11.setOnClickListener {
            STBusManager.call(context, "livestreamingpush/push", "rtmp://10.32.33.20:5388/rtmplive/room-mobile")
        }
        tv_play_video.setOnClickListener {
            STBusManager.call(context, "livestreaming/playvideo", "https://vodlnr6niz5.vod.126.net/vodlnr6niz5/c6bc3543-429a-4342-a555-702596131fe4.mp4")
        }
        tv_play_video_normal.setOnClickListener {
            VideoPlayerFragment.goTo(context)
        }

        // 模拟数据
        val allData: MutableList<HashMap<String, MutableList<Int>>> = mutableListOf(
                hashMapOf("全部" to mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)),
                hashMapOf("景点" to mutableListOf(10, 20, 30, 40, 50, 60)),
                hashMapOf("酒店" to mutableListOf(100, 200, 300, 400, 500, 600, 700)),
                hashMapOf("火车站" to mutableListOf()),
                hashMapOf("机场" to mutableListOf(1000, 2000, 3000, 4000, 5000, 6000, 7000))
        )

        /**
         * @param callback return null 代表请求失败, empty 代表没有更多, 有数据代表请求成功 page+1
         */
        var requestCount = 0

        fun requestData(pagerIndex: Int, recyclerPageIndex: Int, recyclerPageSize: Int, callback: (MutableList<Int>?) -> Unit) {
            async {
                requestCount++
                Thread.sleep(1000)
                when {
                    requestCount % 3 == 0 -> { // 请求失败
                        STLogUtil.d("request", "请求失败")
                        callback.invoke(null)
                    }
                    requestCount % 10 == 0 -> { // 没有更多数据
                        STLogUtil.d("request", "没有更多数据")
                        callback.invoke(mutableListOf())
                    }
                    else -> { // 请求成功
                        STLogUtil.d("request", "请求成功")
                        val list = mutableListOf<Int>()
                        val currentSize = recyclerPageIndex * recyclerPageSize
                        (0 until recyclerPageSize).forEach {
                            list.add(currentSize + it)
                        }
                        callback.invoke(list)
                    }
                }
            }
        }

        // 初始化 pagerRecyclerView
        pagerRecyclerView.connectToCheckBoxGroupView(checkBoxGroupView)
        pagerRecyclerView.initialize(
                pagerDataList = allData.flatMap { it.values.toMutableList() }.toMutableList(),
                onRecyclerViewLoadMore = { pagerIndex: Int, recyclerPageIndex: Int, recyclerPageSize: Int, callback: (MutableList<Int>?) -> Unit ->
                    requestData(pagerIndex, recyclerPageIndex, recyclerPageSize) { callback.invoke(it) }
                },
                onRecyclerViewCreateViewHolder = { pagerIndex: Int, parent: ViewGroup, viewType: Int ->
                    STRecyclerViewAdapter.ViewHolder(TextView(context).apply {
                        setTextColor(Color.BLACK)
                        setPadding(50, 50, 50, 50)
                        textSize = 20f
                    })
                },
                onRecyclerViewBindViewHolder = { dataList: MutableList<Int>, viewHolder: RecyclerView.ViewHolder, position: Int ->
                    (viewHolder.itemView as TextView).text = "position:$position-data:${dataList[position]}\ndesc:我是钢铁侠"
                }
        )

        /**
         * 初始化 checkBoxGroupView
         *
         * 先初始化 pagerRecyclerView 后初始化 checkBoxGroupView
         * 这样当 checkBoxGroupView.setCheckedWithUpdateViewStatus(0, true) 的时候, 应该能触发 pagerRecyclerView 联动
         */
        val dpPadding = STSystemUtil.getPxFromDp(10f).toInt()
        checkBoxGroupView.initialize(
                enableSingleCheck = true,
                enableFitCenter = true,
                fitCenterMinimumSize = STSystemUtil.screenWidth,
                titleList = allData.flatMap { it.keys.toMutableList() }.toMutableList(),
                createUncheckedItemView = { title: String ->
                    TextView(context).apply {
                        text = title
                        setTextColor(Color.BLACK)
                        setPadding(dpPadding, dpPadding, dpPadding, dpPadding)
                        setBackgroundColor(Color.LTGRAY)
                    }
                },
                updateViewOnCheckChangedListener = { checkBoxGroupView: STCheckBoxGroupView, originViewList: List<View>, checkedViewPositionList: List<Int>, changedViewPositionList: List<Int> ->
                    descTv.text = "当前选中的数组索引:$checkedViewPositionList\n本次改变的数组索引:$changedViewPositionList"
                    changedViewPositionList.forEach { position: Int ->
                        val itemChangedView: TextView = originViewList[position] as TextView
                        val isChecked = checkBoxGroupView.isChecked(position)
                        itemChangedView.setBackgroundColor(if (isChecked) Color.BLUE else Color.LTGRAY)
                        itemChangedView.setTextColor(if (isChecked) Color.WHITE else Color.BLACK)
                    }
                })
        checkBoxGroupView.setCheckedWithUpdateViewStatus(0, true)
    }
}
