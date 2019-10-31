package com.smart.template.home

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.smart.library.base.STBaseActivity
import com.smart.library.map.layer.STMapView
import com.smart.library.map.model.STLatLng
import com.smart.library.map.model.STMapType
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.image.STImageManager
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.library.widget.recyclerview.snap.STSnapGravityHelper
import com.smart.template.R
import com.smart.template.widget.STCheckBoxGroupView
import com.smart.template.widget.STRecyclerPagerView
import kotlinx.android.synthetic.main.home_recycler_item_poi.view.*
import kotlinx.android.synthetic.main.st_horizontal_view_pager_activity.*
import org.jetbrains.anko.async

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class STHorizontalPagerActivity : STBaseActivity() {

    private val mapView: STMapView by lazy { mapBaiduView }
    private var locationLatLng: STLatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_horizontal_view_pager_activity)

        // init map -- level 1
        mapView.initialize(mapType = STMapType.BAIDU)
        mapView.onCreate(this, savedInstanceState)
        mapView.setOnLocationChangedListener { locationLatLng = it }

        // init bottom sheet child views -- level 4
        initBottomSheetChildViews()

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    @SuppressLint("SetTextI18n")
    private fun initBottomSheetChildViews() {
        fun getScare(pagerIndex: Int): Int {
            return when (pagerIndex) {
                0 -> 1
                1 -> 10
                2 -> 100
                3 -> 1000
                else -> 0
            }
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
                    STRecyclerViewAdapter.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_item_horizontal_poi, parent, false).apply {
                        layoutParams = layoutParams.apply {
                            width = STSystemUtil.screenWidth - STSystemUtil.getPxFromDp(70f).toInt()
                        }
                    })
                },
                onRecyclerViewBindViewHolder = { pagerModel: STRecyclerPagerView.PagerModel<Int>, viewHolder: RecyclerView.ViewHolder, position: Int ->
                    viewHolder.itemView.tv_title_item_map.text = "上海东方明珠塔:${pagerModel.dataList[position]}"
                    STImageManager.show(viewHolder.itemView.iv_item_map, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565851968832&di=b73c29d745a1454381ea2276e0707d72&imgtype=0&src=http%3A%2F%2Fzz.fangyi.com%2FR_Img%2Fnews%2F8%2F2016_1%2F9%2F20160109173836_4593.jpg")
                },
                snap = STSnapGravityHelper.Snap.CENTER, // 锚点到中间
                orientation = LinearLayout.HORIZONTAL,
                startPadding = STSystemUtil.getPxFromDp(25f).toInt(),
                dividerPadding = STSystemUtil.getPxFromDp(25f).toInt(), // 减小 viewHolder 宽度可以与 viewPager setPageMargin 一样的效果, 两边都漏出一点
                onSnap = { pagerIndex: Int, position: Int, data: Int ->
                    snapTv.text = "当前页面:$pagerIndex, 列表索引: $position 选中数值: $data"
                },
                viewEmpty = { parent: ViewGroup, viewType: Int ->
                    STEmptyLoadingWrapper.createDefaultEmptyView(this, "当前区域内没有结果\n请移动或缩放地图后重新搜索")
                },
                viewLoadFailure = { parent: ViewGroup, viewType: Int ->
                    STEmptyLoadingWrapper.createDefaultFooterView(this, "加\n载\n出\n错\n了", orientation = LinearLayout.HORIZONTAL, backgroundColor = Color.LTGRAY, textSize = 14f)
                },
                viewLoading = { parent: ViewGroup, viewType: Int ->
                    STEmptyLoadingWrapper.createDefaultFooterLoadingView(this, "加\n载\n中\n...", orientation = LinearLayout.HORIZONTAL, backgroundColor = Color.LTGRAY)
                },
                viewNoMore = { parent: ViewGroup, viewType: Int ->
                    STEmptyLoadingWrapper.createDefaultFooterView(this, "没\n有\n更\n多\n了", orientation = LinearLayout.HORIZONTAL, backgroundColor = Color.LTGRAY, textSize = 14f)
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
                    TextView(this).apply {
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
    }
}