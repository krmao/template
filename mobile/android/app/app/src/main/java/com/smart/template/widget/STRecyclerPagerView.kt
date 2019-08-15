package com.smart.template.widget

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.smart.library.util.STLogUtil
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter

class STRecyclerPagerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    companion object {

        @JvmStatic
        @JvmOverloads
        fun <M> createDefaultRecyclerView(
                context: Context,
                initPagerDataList: MutableList<PagerModel<M>>,
                requestLoadMore: (pagerIndex: Int, requestIndex: Int, requestSize: Int, callback: (MutableList<M>?) -> Unit) -> Unit,
                pagerIndex: Int,
                onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
                onRecyclerViewBindViewHolder: (pagerModel: PagerModel<M>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
                viewLoadFailure: ((parent: ViewGroup, viewType: Int) -> View?)? = { _, _ -> STEmptyLoadingWrapper.createDefaultFooterView(context, "加载出错了") },
                viewLoading: ((parent: ViewGroup, viewType: Int) -> View?)? = { _, _ -> STEmptyLoadingWrapper.createDefaultFooterView(context, "数据加载中...") },
                viewNoMore: ((parent: ViewGroup, viewType: Int) -> View?)? = { _, _ -> STEmptyLoadingWrapper.createDefaultFooterView(context, "没有更多了...") },
                viewEmpty: ((parent: ViewGroup, viewType: Int) -> View?)? = { _, _ -> STEmptyLoadingWrapper.createDefaultEmptyView(context, "数据维护中...") },
                viewEmptyLoading: ((parent: ViewGroup, viewType: Int) -> View?)? = { _, _ -> STEmptyLoadingWrapper.createDefaultEmptyView(context, "数据加载中...") }
        ): View {
            val pagerModel: PagerModel<M> = initPagerDataList[pagerIndex]

            val recyclerView = STRecyclerView(context)
            recyclerView.layoutManager = LinearLayoutManager(context)
            val originAdapter = object : STRecyclerViewAdapter<M, RecyclerView.ViewHolder>(context, pagerModel.dataList) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = onRecyclerViewCreateViewHolder.invoke(pagerIndex, parent, viewType)
                override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) = onRecyclerViewBindViewHolder.invoke(pagerModel, viewHolder, position)
            }

            val adapterWrapper: STEmptyLoadingWrapper<M> = STEmptyLoadingWrapper(originAdapter)
            // custom loading views
            adapterWrapper.viewNoMore = viewNoMore
            adapterWrapper.viewLoadFailure = viewLoadFailure
            adapterWrapper.viewLoading = viewLoading
            adapterWrapper.viewEmpty = viewEmpty
            adapterWrapper.viewEmptyLoading = viewEmptyLoading
            // onLoadMore listener
            adapterWrapper.onLoadMoreListener = {
                STLogUtil.d("request", "start request -> pagerIndex:$pagerIndex, requestNextIndex:${pagerModel.requestNextIndex}")
                requestLoadMore.invoke(pagerIndex, pagerModel.requestNextIndex, pagerModel.requestSize) {
                    recyclerView.post {
                        when {
                            it == null -> { // load failure
                                adapterWrapper.showLoadFailure()
                                STLogUtil.e("request", "request response  -> pagerIndex:$pagerIndex, requestNextIndex:${pagerModel.requestNextIndex}")
                            }
                            it.isNotEmpty() -> { // load success
                                adapterWrapper.add(it)
                                pagerModel.requestNextIndex++
                                STLogUtil.v("request", "response success  -> pagerIndex:$pagerIndex, requestNextIndex:${pagerModel.requestNextIndex}")
                            }
                            else -> { // load no more
                                adapterWrapper.showNoMore()
                                STLogUtil.i("request", "response empty  -> pagerIndex:$pagerIndex, requestNextIndex:${pagerModel.requestNextIndex}")
                            }
                        }
                    }
                }
            }
            recyclerView.adapter = adapterWrapper
            recyclerView.tag = pagerIndex
            return recyclerView
        }
    }

    @JvmOverloads
    fun <T> initialize(
            initPagerDataList: MutableList<PagerModel<T>>,
            requestLoadMore: (pagerIndex: Int, requestIndex: Int, requestSize: Int, callback: (MutableList<T>?) -> Unit) -> Unit,
            onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
            onRecyclerViewBindViewHolder: (pagerModel: PagerModel<T>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
            createRecyclerView: ((context: Context, pagerIndex: Int) -> View)? = { context: Context, pagerIndex: Int ->
                createDefaultRecyclerView(
                        context,
                        initPagerDataList,
                        requestLoadMore,
                        pagerIndex,
                        onRecyclerViewCreateViewHolder,
                        onRecyclerViewBindViewHolder
                )
            },
            createPagerView: ((context: Context, pagerIndex: Int) -> View)? = null
    ) {
        pageMargin = 0
        offscreenPageLimit = initPagerDataList.size
        adapter = STPagerRecyclerViewAdapter(context, initPagerDataList, requestLoadMore, { currentItem }, onRecyclerViewCreateViewHolder, onRecyclerViewBindViewHolder, createRecyclerView, createPagerView)
    }

    @JvmOverloads
    fun clearAll(requestNextIndex: Int = 0) {
        val innerAdapter = adapter as? STPagerRecyclerViewAdapter<*>
        if (innerAdapter != null) {
            (0 until (adapter?.count ?: 0)).forEach { pagerIndex ->
                (findViewWithTag<STRecyclerView>(pagerIndex)?.adapter as? STEmptyLoadingWrapper<*>)?.removeAll()
                innerAdapter.getItem(pagerIndex).requestNextIndex = requestNextIndex
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> reset(newPagerDataList: MutableList<PagerModel<T>>) {
        val innerAdapter = adapter as? STPagerRecyclerViewAdapter<*>
        if (innerAdapter != null) {
            if (newPagerDataList.isNotEmpty() && newPagerDataList.size == innerAdapter.count) {
                (0 until (adapter?.count ?: 0)).forEach { pagerIndex ->
                    val recyclerView: STRecyclerView? = findViewWithTag(pagerIndex)
                    val loadingWrapper = recyclerView?.adapter as? STEmptyLoadingWrapper<T>
                    if (loadingWrapper != null) {
                        val oldPagerModel = innerAdapter.getItem(pagerIndex)
                        val newPagerModel = newPagerDataList[pagerIndex]

                        oldPagerModel.requestNextIndex = newPagerModel.requestNextIndex
                        oldPagerModel.requestSize = newPagerModel.requestSize
                        oldPagerModel.extrasData = newPagerModel.extrasData

                        loadingWrapper.removeAll()
                        loadingWrapper.add(newPagerModel.dataList)
                        recyclerView.scrollToPosition(0)
                    }
                }
            }
        }
    }

    /**
     * STCheckBoxGroupView 务必是单选模式
     */
    fun connectToCheckBoxGroupView(checkBoxGroupView: STCheckBoxGroupView) {
        addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageSelected(position: Int) {
                checkBoxGroupView.setCheckedWithUpdateViewStatus(position, true)
            }
        })
        checkBoxGroupView.addUpdateViewOnCheckChangedListener { _, _, checkedViewPositionList, _ ->
            if (checkedViewPositionList.size == 1) {
                val toPagerIndex: Int = checkedViewPositionList[0]
                if (toPagerIndex != currentItem) {
                    setCurrentItem(toPagerIndex, false)
                }
            }
        }
    }

    private class STPagerRecyclerViewAdapter<M> @JvmOverloads constructor(
            private val context: Context,
            private val initPagerDataList: MutableList<PagerModel<M>>,
            private val requestLoadMore: (pagerIndex: Int, requestIndex: Int, requestSize: Int, callback: (MutableList<M>?) -> Unit) -> Unit,
            private val currentPosition: () -> Int,
            private var onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
            private var onRecyclerViewBindViewHolder: (pagerModel: PagerModel<M>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
            private var createRecyclerView: ((context: Context, pagerIndex: Int) -> View)? = null,
            private var createPagerView: ((context: Context, pagerIndex: Int) -> View)? = null
    ) : PagerAdapter() {

        override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

        override fun getCount(): Int = initPagerDataList.size

        fun getItem(position: Int): PagerModel<M> = initPagerDataList[position]

        override fun instantiateItem(container: ViewGroup, pagerIndex: Int): Any {
            return (createPagerView?.invoke(context, pagerIndex) ?: createRecyclerView?.invoke(context, pagerIndex) ?: createDefaultRecyclerView(context, initPagerDataList, requestLoadMore, pagerIndex, onRecyclerViewCreateViewHolder, onRecyclerViewBindViewHolder)).apply {
                tag = pagerIndex
                container.addView(this)
            }
        }

        override fun destroyItem(container: ViewGroup, position: Int, view: Any) = container.removeView(view as View)

        /**
         * POSITION_NONE 刷新所有页面
         * POSITION_UNCHANGED 不刷新
         */
        override fun getItemPosition(view: Any): Int {
            val position: Int = (view as View).tag as Int
            return if (position == currentPosition()) POSITION_NONE else POSITION_UNCHANGED
        }
    }

    /**
     * requestIndex 服务端默认从 0开始 算第一页, 1算第二页
     * requestNextIndex = requestIndex + 1
     */
    data class PagerModel<T>(var requestNextIndex: Int, var requestSize: Int, var dataList: MutableList<T>, var extrasData: Any? = null)
}

