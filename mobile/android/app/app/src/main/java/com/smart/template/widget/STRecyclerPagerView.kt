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
        fun <M> createDefaultRecyclerView(
                context: Context,
                pagerDataList: MutableList<MutableList<M>>,
                onRecyclerViewLoadMorePageSize: Int = 10,
                onRecyclerViewLoadMore: (pagerIndex: Int, recyclerPageIndex: Int, recyclerPageSize: Int, callback: (MutableList<M>?) -> Unit) -> Unit,
                pagerIndex: Int,
                onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
                onRecyclerViewBindViewHolder: (dataList: MutableList<M>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit
        ): View {
            val recyclerView = STRecyclerView(context)
            recyclerView.layoutManager = LinearLayoutManager(context)

            val originAdapter = object : STRecyclerViewAdapter<M, RecyclerView.ViewHolder>(context, pagerDataList[pagerIndex]) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = onRecyclerViewCreateViewHolder.invoke(pagerIndex, parent, viewType)
                override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) = onRecyclerViewBindViewHolder.invoke(dataList, viewHolder, position)
            }
            val adapterWrapper = STEmptyLoadingWrapper(originAdapter)

            // custom loading views
            adapterWrapper.viewNoMore = adapterWrapper.createDefaultFooterView("-- 呵呵, 真的没有更多了 --")
            adapterWrapper.viewLoadFailure = adapterWrapper.createDefaultFooterView("啊哟, 加载失败了哟")
            adapterWrapper.viewLoading = adapterWrapper.createDefaultFooterView("哼哈, 火速请求中...")

            // onLoadMore listener
            var pageIndex = 0
            adapterWrapper.onLoadMoreListener = {

                val toPageIndex = pageIndex + 1
                STLogUtil.d("request", "start request -> pagerIndex:$pagerIndex, currentIndex:$pageIndex, requestIndex:$toPageIndex")

                onRecyclerViewLoadMore.invoke(pagerIndex, toPageIndex, onRecyclerViewLoadMorePageSize) {
                    recyclerView.post {
                        when {
                            it == null -> { // load failure
                                adapterWrapper.showLoadFailure()
                                STLogUtil.e("request", "request response  -> pagerIndex:$pagerIndex, currentIndex:$pageIndex")
                            }
                            it.isNotEmpty() -> { // load success
                                adapterWrapper.add(it)
                                pageIndex = toPageIndex
                                STLogUtil.v("request", "response success  -> pagerIndex:$pagerIndex, currentIndex:$pageIndex")
                            }
                            else -> { // load no more
                                adapterWrapper.showNoMore()
                                STLogUtil.i("request", "response empty  -> pagerIndex:$pagerIndex, currentIndex:$pageIndex")
                            }
                        }
                    }
                }
            }

            recyclerView.adapter = adapterWrapper
            return recyclerView
        }

    }

    @JvmOverloads
    fun <T> initialize(
            pagerDataList: MutableList<MutableList<T>>,
            onRecyclerViewLoadMore: (pagerIndex: Int, recyclerPageIndex: Int, recyclerPageSize: Int, callback: (MutableList<T>?) -> Unit) -> Unit,
            onRecyclerViewLoadMorePageSize: Int = 10,
            onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
            onRecyclerViewBindViewHolder: (dataList: MutableList<T>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
            createRecyclerView: ((context: Context, pagerIndex: Int) -> View)? = { context: Context, pagerIndex: Int ->
                createDefaultRecyclerView(
                        context,
                        pagerDataList,
                        onRecyclerViewLoadMorePageSize,
                        onRecyclerViewLoadMore,
                        pagerIndex,
                        onRecyclerViewCreateViewHolder,
                        onRecyclerViewBindViewHolder
                )
            },
            createPagerView: ((context: Context, pagerIndex: Int) -> View)? = null
    ) {
        pageMargin = 0
        offscreenPageLimit = pagerDataList.size
        adapter = STPagerRecyclerViewAdapter(context, pagerDataList, onRecyclerViewLoadMorePageSize, onRecyclerViewLoadMore, { currentItem }, onRecyclerViewCreateViewHolder, onRecyclerViewBindViewHolder, createRecyclerView, createPagerView)
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
        checkBoxGroupView.addUpdateViewOnCheckChangedListener { _, originViewList, checkedViewPositionList, changedViewPositionList ->
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
            private val pagerDataList: MutableList<MutableList<M>>,
            private val onRecyclerViewLoadMorePageSize: Int = 10,
            private val onRecyclerViewLoadMore: (pagerIndex: Int, recyclerPageIndex: Int, recyclerPageSize: Int, callback: (MutableList<M>?) -> Unit) -> Unit,
            private val currentPosition: () -> Int,
            private var onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
            private var onRecyclerViewBindViewHolder: (dataList: MutableList<M>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
            private var createRecyclerView: ((context: Context, pagerIndex: Int) -> View)? = null,
            private var createPagerView: ((context: Context, pagerIndex: Int) -> View)? = null
    ) : PagerAdapter() {

        override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

        override fun getCount(): Int = pagerDataList.size

        override fun instantiateItem(container: ViewGroup, pagerIndex: Int): Any {
            return (createPagerView?.invoke(context, pagerIndex) ?: createRecyclerView?.invoke(context, pagerIndex) ?: createDefaultRecyclerView(context, pagerDataList, onRecyclerViewLoadMorePageSize, onRecyclerViewLoadMore, pagerIndex, onRecyclerViewCreateViewHolder, onRecyclerViewBindViewHolder)).apply {
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
}

