package com.smart.template.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration.ORIENTATION_UNDEFINED
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.smart.library.base.STBaseApplication
import com.smart.library.base.STBaseFragment
import com.smart.library.map.layer.STDialogManager
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.bus.STBusManager
import com.smart.library.util.image.STImageManager
import com.smart.library.util.rx.RxBus
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.library.widget.recyclerview.snap.STSnapGravityHelper
import com.smart.template.R
import com.smart.template.home.tab.HomeTabActivity
import com.smart.template.home.test.*
import com.smart.template.widget.STCheckBoxGroupView
import com.smart.template.widget.STRecyclerPagerView
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.home_recycler_item_poi.view.*
import org.jetbrains.anko.async

class HomeFragment : STBaseFragment() {

    private var lastConfigOrientation: Int = ORIENTATION_UNDEFINED
    private var disposable: Disposable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        behavior.setOnClickListener {
            startActivity(Intent(context, STBehaviorActivity::class.java))
        }
        horizontalPager.setOnClickListener {
            startActivity(Intent(context, STHorizontalPagerActivity::class.java))
        }
        behaviorScrolling.setOnClickListener {
            startActivity(Intent(context, STBehaviorScrollingActivity::class.java))
        }
        bottomSheet.setOnClickListener {
            startActivity(Intent(context, STBehaviorBottomSheetActivity::class.java))
        }
        baiduMap.setOnClickListener {
            MapFragment.goTo(activity, useBaidu = true)
        }
        gaodeMap.setOnClickListener {
            MapFragment.goTo(activity, useBaidu = false)
        }
        text1.setOnClickListener {
            val loadingDialog: Dialog? = STDialogManager.createLoadingDialog(context, "规划中...", canceledOnTouchOutside = true)
            loadingDialog?.show()
        }
        dialogLoading.setOnClickListener {
            val loadingDialog: Dialog? = STDialogManager.createLoadingDialog(context)
            loadingDialog?.show()
        }
        text2.setOnClickListener {
            STToastUtil.show("${STBaseApplication.INSTANCE.resources.displayMetrics.density}:${STSystemUtil.getDpFromPx(STSystemUtil.statusBarHeight)}")
        }
        text3.setOnClickListener {
            STToastUtil.show("${STSystemUtil.statusBarHeight}")
            STLogUtil.w("statusBarHeight", "statusBarHeight=${STSystemUtil.statusBarHeight}")
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
        pagerScrollTop.setOnClickListener {
            val pagerIndex = 3
            pagerRecyclerView.scrollToRecyclerViewPosition(pagerIndex, 0)
        }
        pagerScrollBottom.setOnClickListener {
            val pagerIndex = 3
            val dataList = pagerRecyclerView.getRecyclerViewDataList<Int>(pagerIndex)
            pagerRecyclerView.scrollToRecyclerViewPosition(pagerIndex, dataList.size - 1)
        }
        pagerScrollCenter.setOnClickListener {
            val pagerIndex = 3
            val dataList = pagerRecyclerView.getRecyclerViewDataList<Int>(pagerIndex)
            pagerRecyclerView.scrollToRecyclerViewPosition(pagerIndex, dataList.size / 2 - 1)
        }
        pagerScroll7000.setOnClickListener {
            pagerRecyclerView.scrollToRecyclerViewPosition(pagerIndex = 3, recyclerItemData = 7000)
        }
        pagerScroll0.setOnClickListener {
            pagerRecyclerView.scrollToRecyclerViewPosition(pagerIndex = 3, recyclerItemData = 0)
        }
        pagerScroll1000.setOnClickListener {
            pagerRecyclerView.scrollToRecyclerViewPosition(pagerIndex = 3, recyclerItemData = 1000)
        }

        fun getScare(pagerIndex: Int): Int {
            return when (pagerIndex) {
                0 -> 1
                1 -> 10
                2 -> 100
                3 -> 1000
                else -> 0
            }
        }

        resetPagerData.setOnClickListener {
            pagerRecyclerView.reset(mutableListOf(
                    STRecyclerPagerView.PagerModel(1, 1, mutableListOf(0), "1-1"),
                    STRecyclerPagerView.PagerModel(1, 2, mutableListOf(0, 1 * getScare(1)), "10-2"),
                    STRecyclerPagerView.PagerModel(1, 3, mutableListOf(0, 1 * getScare(2), 2 * getScare(2)), "100-3"),
                    STRecyclerPagerView.PagerModel(1, 10, mutableListOf(
                            0,
                            1 * getScare(3),
                            2 * getScare(3),
                            3 * getScare(3),
                            4 * getScare(3),
                            5 * getScare(3),
                            6 * getScare(3),
                            7 * getScare(3),
                            8 * getScare(3),
                            9 * getScare(3)
                    ), "1000-10")
            ))
        }
        clearPagerData.setOnClickListener {
            pagerRecyclerView.clearAll()
        }

        // 模拟数据
        // 服务端 requestNextIndex 从 0 开始算第一页
        val allData: MutableList<STRecyclerPagerView.PagerModel<Int>> =
                mutableListOf(
                        STRecyclerPagerView.PagerModel(1, 1, mutableListOf(0), "景点"),
                        STRecyclerPagerView.PagerModel(1, 2, mutableListOf(0, 1 * getScare(1)), "美食"),
                        STRecyclerPagerView.PagerModel(1, 3, mutableListOf(0, 1 * getScare(2), 2 * getScare(2)), "酒店"),
                        STRecyclerPagerView.PagerModel(1, 10, mutableListOf(
                                0,
                                1 * getScare(3),
                                2 * getScare(3),
                                3 * getScare(3),
                                4 * getScare(3),
                                5 * getScare(3),
                                6 * getScare(3),
                                7 * getScare(3),
                                8 * getScare(3),
                                9 * getScare(3)
                        ), "购物")
                )

        var requestCount = 0

        // 初始化 pagerRecyclerView
        pagerRecyclerView.connectToCheckBoxGroupView(checkBoxGroupView)
        pagerRecyclerView.initialize(
                initPagerDataList = allData,
                requestLoadMore = { pagerIndex: Int, requestNextIndex: Int, requestSize: Int, callback: (MutableList<Int>?) -> Unit ->
                    /**
                     * requestIndex 服务端默认从 0开始 算第一页, 1算第二页
                     * requestNextIndex = requestIndex + 1
                     */
                    async {
                        requestCount++
                        Thread.sleep(1000)
                        when {
                            requestCount % 5 == 0 -> { // 请求失败
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

                                val scare = getScare(pagerIndex)
                                val nextValue: Int = requestNextIndex * requestSize * scare

                                (0 until requestSize).forEach {
                                    list.add(nextValue + it * scare)
                                }
                                callback.invoke(list)
                            }
                        }
                    }
                },
                onRecyclerViewCreateViewHolder = { _: Int, parent: ViewGroup, _: Int ->
                    STRecyclerViewAdapter.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_item_poi, parent, false))
                },
                onRecyclerViewBindViewHolder = { pagerModel: STRecyclerPagerView.PagerModel<Int>, viewHolder: RecyclerView.ViewHolder, position: Int ->
                    viewHolder.itemView.tv_title_item_map.text = "上海东方明珠塔:${pagerModel.dataList[position]}"
                    STImageManager.show(viewHolder.itemView.iv_item_map, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565851968832&di=b73c29d745a1454381ea2276e0707d72&imgtype=0&src=http%3A%2F%2Fzz.fangyi.com%2FR_Img%2Fnews%2F8%2F2016_1%2F9%2F20160109173836_4593.jpg")
                },
                snap = STSnapGravityHelper.Snap.START,
                onSnap = { pagerIndex: Int, position: Int, data: Int ->
                    snapTv.text = "当前页面:$pagerIndex, 列表索引: $position 选中数值: $data"
                },
                viewEmpty = { parent: ViewGroup, viewType: Int ->
                    STEmptyLoadingWrapper.createDefaultEmptyView(context, "当前区域内没有结果\n请移动或缩放地图后重新搜索")
                }
        )

        /**
         * 初始化 checkBoxGroupView
         *
         * 先初始化 pagerRecyclerView 后初始化 checkBoxGroupView
         * 这样当 checkBoxGroupView.setCheckedWithUpdateViewStatus(0, true) 的时候, 应该能触发 pagerRecyclerView 联动
         */
        val dpPadding = STSystemUtil.getPxFromDp(5f).toInt()
        val dpMargin = STSystemUtil.getPxFromDp(15f).toInt()
        checkBoxGroupView.initialize(
                minimumWidthOrHeight = STSystemUtil.screenWidth,
                titleList = allData.map
                { it.extrasData as? String ?: "" }.toMutableList(),
                createUncheckedItemView =
                { title: String ->
                    TextView(context).apply {
                        text = title
                        textSize = 14f
                        gravity = Gravity.CENTER
                        @Suppress("DEPRECATION")
                        setTextColor(resources.getColorStateList(R.color.home_checkbox_selected_color))
                        setBackgroundResource(R.drawable.home_checkbox_bg_selector)
                        setPadding(dpPadding, dpPadding, dpPadding, dpPadding)
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                            weight = 2f
                            leftMargin = dpMargin
                            rightMargin = dpMargin
                        }
                    }
                },
                updateViewOnCheckChangedListener =
                { checkBoxGroupView: STCheckBoxGroupView, originViewList: List<View>, checkedViewPositionList: List<Int>, changedViewPositionList: List<Int> ->
                    descTv.text = "当前选中的数组索引:$checkedViewPositionList\n本次改变的数组索引:$changedViewPositionList"
                    changedViewPositionList.forEach { position: Int ->
                        val itemChangedView: TextView = originViewList[position] as TextView
                        val isChecked = checkBoxGroupView.isChecked(position)
                        itemChangedView.isSelected = isChecked
                    }
                })
        checkBoxGroupView.setCheckedWithUpdateViewStatus(0, true)

        lastConfigOrientation = resources.configuration.orientation

        STLogUtil.v("home", "init width:${STSystemUtil.screenWidth}, height:${STSystemUtil.screenHeight}")
        disposable = RxBus.toObservable(HomeTabActivity.ConfigurationEvent::class.java).subscribe { configurationEvent ->
            val newConfigOrientation = configurationEvent.newConfig?.orientation ?: ORIENTATION_UNDEFINED
            if (newConfigOrientation != ORIENTATION_UNDEFINED && ORIENTATION_UNDEFINED != lastConfigOrientation) {
                STLogUtil.w("home", "onConfigurationChanged width:${STSystemUtil.screenWidth}, height:${STSystemUtil.screenHeight}")
                checkBoxGroupView.minimumWidth = STSystemUtil.screenWidth
                lastConfigOrientation = newConfigOrientation
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }

}
