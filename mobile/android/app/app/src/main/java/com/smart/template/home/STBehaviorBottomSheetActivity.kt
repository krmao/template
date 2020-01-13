package com.smart.template.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.smart.library.base.STBaseActivity
import com.smart.library.base.STBaseApplication
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil
import com.smart.library.util.image.STImageManager
import com.smart.library.widget.behavior.STBottomSheetBackdropBehavior
import com.smart.library.widget.behavior.STBottomSheetCallback
import com.smart.library.widget.behavior.STBottomSheetViewPagerBehavior
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.library.widget.recyclerview.snap.STSnapHelper
import com.smart.template.R
import com.smart.template.home.widget.STCheckBoxGroupView
import com.smart.template.home.widget.STRecyclerPagerView
import kotlinx.android.synthetic.main.home_recycler_item_poi.view.*
import kotlinx.android.synthetic.main.st_behavior_bottom_sheet_activity.*
import org.jetbrains.anko.async

@Suppress("UNUSED_ANONYMOUS_PARAMETER")
class STBehaviorBottomSheetActivity : STBaseActivity() {

    private val handler: Handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_behavior_bottom_sheet_activity)

        // init bottomSheet behavior -- level 2
        val bottomSheetParentHeight: Int = STSystemUtil.screenHeight + STSystemUtil.statusBarHeight
        val bottomSheetPeekHeight: Int = STBaseApplication.INSTANCE.resources.getDimensionPixelSize(R.dimen.bottomSheetPeekHeight)
        val bottomSheetHalfExpandTop: Int = (STSystemUtil.screenHeight * 0.35f).toInt()
        val bottomSheetExpandTop: Int = STBaseApplication.INSTANCE.resources.getDimensionPixelSize(R.dimen.bottomSheetExpandTop)
        val bottomSheetCollapsedTop: Int = bottomSheetParentHeight - bottomSheetPeekHeight

        pagerRecyclerView.enableDrag = true // must not set in onStateChanged
        val bottomSheetBehavior: STBottomSheetViewPagerBehavior<LinearLayout> = STBottomSheetViewPagerBehavior.from(bottomSheetLayout)
        val behaviorBottomSheetCallback = STBottomSheetCallback(handler, bottomSheetLayout, bottomSheetBehavior, true, bottomSheetExpandTop, bottomSheetHalfExpandTop, bottomSheetCollapsedTop, 30f,
                onStateChanged = { bottomSheet: View, newState: Int ->
                    when (newState) {
                        STBottomSheetViewPagerBehavior.STATE_DRAGGING -> {
                        }
                        else -> {
                        }
                    }
                }
        )
        bottomSheetBehavior.setBottomSheetCallback(behaviorBottomSheetCallback)
        bottomSheetBehavior.peekHeight = bottomSheetPeekHeight
        bottomSheetBehavior.bindViewPager(pagerRecyclerView)
        bottomSheetBehavior.bottomSheetHalfExpandTop = bottomSheetHalfExpandTop

        // init backdrop behavior and reset viewpager height -- level 3
        val backdropBehavior: STBottomSheetBackdropBehavior<*> = STBottomSheetBackdropBehavior.from(viewPager)
        backdropBehavior.bottomSheetBehavior = bottomSheetBehavior
        backdropBehavior.bottomSheetBehaviorClass = LinearLayout::class.java
        viewPager.adapter = BackdropImagesPagerAdapter(this)
        viewPager.layoutParams = viewPager.layoutParams.apply {
            height = behaviorBottomSheetCallback.bottomSheetHalfExpandTop
        }

        // init bottom sheet child views -- level 4
        initBottomSheetChildViews()

        // init floating action button -- level 5
        floatingActionButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
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
                requestLoadMore = { refresh: Boolean, pagerIndex: Int, requestIndex: Int, requestSize: Int, lastRequest: Any?, lastResponse: Any?, callback: (lastRequest: Any?, lastResponse: Any?, MutableList<Int>?) -> Unit ->
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
                                callback.invoke(null, null, null)
                            }
                            requestCount % 10 == 0 -> { // 没有更多数据
                                STLogUtil.d("request", "没有更多数据")
                                callback.invoke(null, null, mutableListOf())
                            }
                            else -> { // 请求成功
                                STLogUtil.d("request", "请求成功")
                                val list = mutableListOf<Int>()

                                val scare = getScare(pagerIndex)
                                val nextValue: Int = 1 * requestSize * scare

                                (0 until requestSize).forEach {
                                    list.add(nextValue + it * scare)
                                }
                                callback.invoke(null, null, list)
                            }
                        }
                    }

                    Unit
                },
                onRecyclerViewCreateViewHolder = { _: Int, parent: ViewGroup, _: Int ->
                    STRecyclerViewAdapter.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.home_recycler_item_poi, parent, false))
                },
                onRecyclerViewBindViewHolder = { pagerModel: STRecyclerPagerView.PagerModel<Int>, viewHolder: RecyclerView.ViewHolder, position: Int ->
                    viewHolder.itemView.tv_title_item_map.text = "上海东方明珠塔:${pagerModel.dataList[position]}"
                    STImageManager.show(viewHolder.itemView.iv_item_map, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565851968832&di=b73c29d745a1454381ea2276e0707d72&imgtype=0&src=http%3A%2F%2Fzz.fangyi.com%2FR_Img%2Fnews%2F8%2F2016_1%2F9%2F20160109173836_4593.jpg")
                },
                snap = STSnapHelper.Snap.START,
                onSnap = { pagerIndex: Int, position: Int, data: Int ->
                    snapTv.text = "当前页面:$pagerIndex, 列表索引: $position 选中数值: $data"
                },
                viewEmpty = { parent: ViewGroup, viewType: Int, orientation: Int ->
                    STEmptyLoadingWrapper.createDefaultEmptyView(this, "当前区域内没有结果\n请移动或缩放地图后重新搜索")
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

    private class BackdropImagesPagerAdapter(mContext: Context) : PagerAdapter() {
        private var mLayoutInflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int {
            return 3
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object` as LinearLayout
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val itemView = mLayoutInflater.inflate(R.layout.st_behavior_pager_item, container, false)
            val imageView = itemView.findViewById<View>(R.id.imageView) as SimpleDraweeView
            STImageManager.show(imageView, "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1565851968832&di=b73c29d745a1454381ea2276e0707d72&imgtype=0&src=http%3A%2F%2Fzz.fangyi.com%2FR_Img%2Fnews%2F8%2F2016_1%2F9%2F20160109173836_4593.jpg")
            container.addView(itemView)
            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
            container.removeView(view as LinearLayout)
        }
    }
}