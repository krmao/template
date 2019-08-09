package com.smart.template.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.bus.STBusManager
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.template.R
import com.smart.template.home.test.*
import com.smart.template.widget.STCheckBoxGroupView
import kotlinx.android.synthetic.main.home_fragment.*

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

        val pagerDataList = mutableListOf(
                mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9),
                mutableListOf(10, 20),
                mutableListOf(100, 200, 300, 400, 500, 600, 700)
        )

        val onRecyclerViewCreateViewHolder = { pagerIndex: Int, parent: ViewGroup, viewType: Int ->
            STRecyclerViewAdapter.ViewHolder(TextView(context).apply {
                setTextColor(Color.BLACK)
                setPadding(50, 50, 50, 50)
                textSize = 20f
            })
        }
        val onRecyclerViewBindViewHolder = { dataList: MutableList<Int>, viewHolder: STRecyclerViewAdapter.ViewHolder, position: Int ->
            (viewHolder.itemView as TextView).text = "position:$position-data:${dataList[position]}\ndesc:我是钢铁侠"
        }
        pagerRecyclerView.connectToCheckBoxGroupView(checkBoxGroupView)
        pagerRecyclerView.initialize(
                pagerDataList = pagerDataList,
                onRecyclerViewCreateViewHolder = onRecyclerViewCreateViewHolder,
                onRecyclerViewBindViewHolder = onRecyclerViewBindViewHolder
        )

        /**
         * 先初始化 pagerRecyclerView 后初始化 checkBoxGroupView
         * 这样当 checkBoxGroupView.setCheckedWithUpdateViewStatus(0, true) 的时候, 应该能触发 pagerRecyclerView 联动
         */
        val titleList: MutableList<String> = arrayListOf("景点", "餐馆", "酒店")

        val dpPadding = STSystemUtil.getPxFromDp(10f).toInt()
        checkBoxGroupView.initialize(
                enableSingleCheck = true,
                enableFitCenter = true,
                fitCenterMinimumSize = STSystemUtil.screenWidth,
                titleList = titleList,
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

        checkBoxGroupView.add("全部", 0)
        checkBoxGroupView.add("火车站", "汽车站", "机场", "热门路线", "旅拍景点")
        checkBoxGroupView.setCheckedWithUpdateViewStatus(0, true)
    }
}
