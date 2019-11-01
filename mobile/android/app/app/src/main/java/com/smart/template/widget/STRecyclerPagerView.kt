package com.smart.template.widget

import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import com.smart.library.util.STLogUtil
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.library.widget.recyclerview.STRecyclerViewLinearStartItemDecoration
import com.smart.library.widget.recyclerview.snap.STSnapGravityHelper

/**
 * Snap.START/Snap.END 以及 decoratedStart/startAfterPadding 与 reverseLayout 的方向一致
 *
 * 滚动到头部, 无论是 START/END 取第一个
 * 滚动到尾部, 无论是 START/END 取最后一个 // loadingView -1
 *
 * reverseLayout==true,   Snap.START, 滚动到头部时, 应该 onSnap(findFirstCompletelyVisibleItemPosition)
 * reverseLayout==true,   Snap.END,   滚动到头部时, 应该 onSnap(findFirstCompletelyVisibleItemPosition)
 * reverseLayout==true,   Snap.START, 滚动到尾部时, 应该 onSnap(findLastCompletelyVisibleItemPosition-1)
 * reverseLayout==true,   Snap.END,   滚动到尾部时, 应该 onSnap(findLastCompletelyVisibleItemPosition-1)
 *
 *
 * reverseLayout==false,  Snap.START, 滚动到头部时, 应该 onSnap(findFirstCompletelyVisibleItemPosition)
 * reverseLayout==false,  Snap.END,   滚动到头部时, 应该 onSnap(findFirstCompletelyVisibleItemPosition)
 * reverseLayout==false,  Snap.START, 滚动到尾部时, 应该 onSnap(findLastCompletelyVisibleItemPosition-1)
 * reverseLayout==false,  Snap.END,   滚动到尾部时, 应该 onSnap(findLastCompletelyVisibleItemPosition-1)
 */
@Suppress("MemberVisibilityCanBePrivate")
class STRecyclerPagerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    companion object {

        @JvmStatic
        @JvmOverloads
        fun createDefaultRecyclerViewItemDecoration(dividerPadding: Int = 0, startPadding: Int = 0, enableWrapperLoading: Boolean = true): RecyclerView.ItemDecoration {
            return STRecyclerViewLinearStartItemDecoration(dividerPadding, startPadding, enableWrapperLoading)
        }

        @JvmStatic
        @JvmOverloads
        fun <M> createDefaultRecyclerView(
                context: Context,
                initPagerDataList: MutableList<PagerModel<M>>,
                requestLoadMore: (pagerIndex: Int, requestIndex: Int, requestSize: Int, callback: (MutableList<M>?) -> Unit) -> Unit,
                pagerIndex: Int,
                onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
                onRecyclerViewBindViewHolder: (pagerModel: PagerModel<M>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
                snap: STSnapGravityHelper.Snap = STSnapGravityHelper.Snap.CENTER,
                recyclerViewOrientation: Int = LinearLayoutManager.VERTICAL,
                startPadding: Int = 0,
                dividerPadding: Int = 0,
                enableWrapperLoading: Boolean = true,
                onSnap: (pagerIndex: Int, position: Int, data: M) -> Unit,
                viewLoadFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
                viewLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
                viewNoMore: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
                viewEmpty: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
                viewEmptyLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
        ): View {
            val pagerModel: PagerModel<M> = initPagerDataList[pagerIndex]

            val recyclerView = STRecyclerView(context)
            recyclerView.setBackgroundColor(Color.TRANSPARENT)
            recyclerView.addItemDecoration(STRecyclerViewLinearStartItemDecoration(dividerPadding, startPadding, enableWrapperLoading))
            recyclerView.layoutManager = LinearLayoutManager(context, recyclerViewOrientation, false)
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
            adapterWrapper.enableChangeAnimations(false)

            val snapGravityHelper: STSnapGravityHelper by lazy {
                STSnapGravityHelper(
                        snap
                ) { position: Int ->
                    if (position >= 0 && position < adapterWrapper.innerData().size) {
                        onSnap.invoke(pagerIndex, position, adapterWrapper.innerData()[position])
                    }
                }
            }
            adapterWrapper.onInnerDataChanged = {
                // snapGravityHelper.forceSnap() // force snap after inner data changed
            }

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
            recyclerView.tag = TagModel(pagerIndex, snapGravityHelper)

            // gravity snap
            snapGravityHelper.attachToRecyclerView(recyclerView)

            return recyclerView
        }
    }

    var recyclerViewOrientation: Int = LinearLayoutManager.VERTICAL
        internal set

    var dividerPadding: Int = 0
        internal set
    var startPadding: Int = 0
        internal set
    var enableWrapperLoading: Boolean = true
        internal set

    @JvmOverloads
    fun <T> initialize(
            initPagerDataList: MutableList<PagerModel<T>>,
            requestLoadMore: (pagerIndex: Int, requestIndex: Int, requestSize: Int, callback: (MutableList<T>?) -> Unit) -> Unit,
            onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
            onRecyclerViewBindViewHolder: (pagerModel: PagerModel<T>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
            snap: STSnapGravityHelper.Snap = STSnapGravityHelper.Snap.CENTER,
            recyclerViewOrientation: Int = LinearLayoutManager.VERTICAL, // recyclerView 横向滚动 默认禁止 viewPager 横向华东
            startPadding: Int = 0,
            dividerPadding: Int = 0,
            enableWrapperLoading: Boolean = true,
            onSnap: (pagerIndex: Int, position: Int, data: T) -> Unit,
            viewLoadFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            viewLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            viewNoMore: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            viewEmpty: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            viewEmptyLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    ) {
        this.dividerPadding = dividerPadding
        this.startPadding = startPadding
        this.enableWrapperLoading = enableWrapperLoading
        this.recyclerViewOrientation = recyclerViewOrientation
        pageMargin = 0
        offscreenPageLimit = initPagerDataList.size
        if (recyclerViewOrientation == LinearLayoutManager.HORIZONTAL) { // recyclerView 横向滚动 默认禁止 viewPager 横向华东
            enableDrag = false
        }
        adapter = STPagerRecyclerViewAdapter(
                context,
                initPagerDataList,
                requestLoadMore,
                { currentItem },
                onRecyclerViewCreateViewHolder,
                onRecyclerViewBindViewHolder,
                snap,
                recyclerViewOrientation,
                startPadding,
                dividerPadding,
                enableWrapperLoading,
                onSnap,
                viewLoadFailure,
                viewLoading,
                viewNoMore,
                viewEmpty,
                viewEmptyLoading
        )
    }

    @JvmOverloads
    fun switchRecyclerViewOrientation(recyclerViewOrientation: Int = (if (this.recyclerViewOrientation == LinearLayoutManager.VERTICAL) LinearLayoutManager.HORIZONTAL else LinearLayoutManager.VERTICAL), keepPosition: Boolean = true, callback: ((orientation: Int) -> Unit)? = null) {
        STLogUtil.w("recyclerViewOrientation", "current ${this.recyclerViewOrientation} will switch to $recyclerViewOrientation  0=horizontal 1=vertical")
        if (recyclerViewOrientation != this.recyclerViewOrientation) {
            this.recyclerViewOrientation = recyclerViewOrientation
            getRecyclerViews()?.forEach {
                // reset item decoration
                if (recyclerViewOrientation == LinearLayoutManager.VERTICAL) {
                    it.getItemDecorationAt(0)?.apply {
                        it.removeItemDecoration(this)
                    }
                } else {
                    it.addItemDecoration(STRecyclerViewLinearStartItemDecoration(dividerPadding, startPadding, enableWrapperLoading))
                }

                // reset snap
                val snapGravityHelper: STSnapGravityHelper? = (it.tag as? TagModel)?.snapGravityHelper
                (it.tag as? TagModel)?.snapGravityHelper?.switchSnap(if (recyclerViewOrientation == LinearLayoutManager.VERTICAL) STSnapGravityHelper.Snap.START else STSnapGravityHelper.Snap.CENTER)

                // reset layout manager
                (it.layoutManager as? LinearLayoutManager)?.orientation = recyclerViewOrientation
                it.adapter = getRecyclerViewLoadingAdapter(it)

                // reset position
                if (keepPosition) {
                    STLogUtil.w("forceSnap", "will targetPosition:${snapGravityHelper?.lastSnappedPosition ?: RecyclerView.NO_POSITION}")
                    snapGravityHelper?.forceSnap()
                }
            }
            STLogUtil.e("recyclerViewOrientation", "current ${this.recyclerViewOrientation}")
        }
        callback?.invoke(this.recyclerViewOrientation)
    }

    @JvmOverloads
    fun clearAll(requestNextIndex: Int = 0) {
        val innerAdapter: STPagerRecyclerViewAdapter<*>? = adapter as? STPagerRecyclerViewAdapter<*>
        if (innerAdapter != null) {
            (0 until innerAdapter.count).forEach { pagerIndex ->
                (findViewWithTag<STRecyclerView>(TagModel(pagerIndex))?.adapter as? STEmptyLoadingWrapper<*>)?.removeAll()
                innerAdapter.getItem(pagerIndex).requestNextIndex = requestNextIndex
            }
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun getRecyclerViews(): List<STRecyclerView>? {
        return (0 until adapter.count).mapNotNull { getRecyclerView(it) }
    }


    @JvmOverloads
    @Suppress("MemberVisibilityCanBePrivate")
    fun getRecyclerView(pagerIndex: Int = currentItem): STRecyclerView? {
        return findViewWithTag(TagModel(pagerIndex))
    }

    @JvmOverloads
    @Suppress("MemberVisibilityCanBePrivate")
    fun getRecyclerViewSnapGravityHelper(pagerIndex: Int = currentItem): STSnapGravityHelper? {
        return (getRecyclerView(pagerIndex)?.tag as? TagModel)?.snapGravityHelper
    }

    @JvmOverloads
    fun forceSnap(pagerIndex: Int = currentItem, targetPosition: Int) {
        getRecyclerViewSnapGravityHelper(pagerIndex)?.forceSnap(targetPosition)
    }

    @JvmOverloads
    fun getSnappedPosition(pagerIndex: Int = currentItem): Int {
        return getRecyclerViewSnapGravityHelper(pagerIndex)?.lastSnappedPosition ?: RecyclerView.NO_POSITION
    }

    @JvmOverloads
    @Suppress("UNCHECKED_CAST")
    fun <T> getRecyclerViewLoadingAdapter(pagerIndex: Int = currentItem): STEmptyLoadingWrapper<T>? {
        return (getRecyclerView(pagerIndex)?.adapter as? STEmptyLoadingWrapper<T>)
    }

    @Suppress("UNCHECKED_CAST")
    fun getRecyclerViewLoadingAdapter(recyclerView: STRecyclerView?): STEmptyLoadingWrapper<*>? {
        return recyclerView?.adapter as? STEmptyLoadingWrapper<*>
    }

    @JvmOverloads
    fun <T> getRecyclerViewInnerDataList(pagerIndex: Int = currentItem): List<T> {
        return getRecyclerViewLoadingAdapter<T>(pagerIndex)?.innerData() ?: arrayListOf()
    }

    /**
     * 第一个匹配返回, 警惕 多个标签列表存在相同 item 数据, 比如 <全部><景点>, 则全部包含景点里面的数据,
     * 不指定 pagerIndex 情况下默认滚动最先匹配到的页面
     */
    @Suppress("unused")
    @JvmOverloads
    fun <T> scrollToRecyclerViewPosition(recyclerItemData: T, smoothScrollToRecyclerViewPosition: Boolean = true, autoSwitchViewPager: Boolean = true) {
        val innerAdapter: STPagerRecyclerViewAdapter<*>? = adapter as? STPagerRecyclerViewAdapter<*>
        if (innerAdapter != null) {
            for (pagerIndex in (0 until innerAdapter.count)) {
                if (scrollToRecyclerViewPosition(pagerIndex, recyclerItemData, smoothScrollToRecyclerViewPosition, autoSwitchViewPager)) {
                    break // 第一个匹配返回
                }
            }
        }
    }

    @JvmOverloads
    fun <T> scrollToRecyclerViewPosition(pagerIndex: Int, recyclerItemData: T, smoothScrollToRecyclerViewPosition: Boolean = true, autoSwitchViewPager: Boolean = true): Boolean {
        val position: Int = getRecyclerViewInnerDataList<T>(pagerIndex).indexOf(recyclerItemData)
        STLogUtil.w("scrollToRecyclerViewPosition", "position:$position, data:$recyclerItemData")
        return scrollToRecyclerViewPosition(pagerIndex, position, smoothScrollToRecyclerViewPosition, autoSwitchViewPager)
    }

    @JvmOverloads
    fun scrollToRecyclerViewPosition(pagerIndex: Int, position: Int, smoothScrollToRecyclerViewPosition: Boolean = true, autoSwitchViewPager: Boolean = true): Boolean {
        STLogUtil.e("scrollToRecyclerViewPosition", "position:$position")
        val recyclerView: STRecyclerView? = findViewWithTag(TagModel(pagerIndex))
        if (recyclerView != null && position >= 0 && position < recyclerView.adapter?.itemCount ?: 0) {
            if (autoSwitchViewPager && currentItem != pagerIndex) {
                currentItem = pagerIndex
            }
            val snapGravityHelper: STSnapGravityHelper? = (recyclerView.tag as? TagModel)?.snapGravityHelper
            if (snapGravityHelper != null) {
                snapGravityHelper.scrollToPosition(position, smoothScrollToRecyclerViewPosition)
            } else {
                if (smoothScrollToRecyclerViewPosition) {
                    recyclerView.smoothScrollToPosition(position)
                } else {
                    recyclerView.scrollToPosition(position)
                }
            }
            return true
        }
        return false
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> reset(newPagerDataList: MutableList<PagerModel<T>>) {
        val innerAdapter: STPagerRecyclerViewAdapter<*>? = adapter as? STPagerRecyclerViewAdapter<*>
        if (innerAdapter != null) {
            if (newPagerDataList.isNotEmpty() && newPagerDataList.size == innerAdapter.count) {
                (0 until innerAdapter.count).forEach { pagerIndex ->
                    val recyclerView: STRecyclerView? = findViewWithTag(TagModel(pagerIndex))
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

    var enableDrag: Boolean = false
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return if (enableDrag) super.onInterceptTouchEvent(ev) else enableDrag
    }

    private class STPagerRecyclerViewAdapter<M>(
            private val context: Context,
            private val initPagerDataList: MutableList<PagerModel<M>>,
            private val requestLoadMore: (pagerIndex: Int, requestIndex: Int, requestSize: Int, callback: (MutableList<M>?) -> Unit) -> Unit,
            private val currentPosition: () -> Int,
            private val onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
            private val onRecyclerViewBindViewHolder: (pagerModel: PagerModel<M>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
            private val snap: STSnapGravityHelper.Snap = STSnapGravityHelper.Snap.CENTER,
            private val recyclerViewOrientation: Int = LinearLayoutManager.VERTICAL,
            private val startPadding: Int = 0,
            private val dividerPadding: Int = 0,
            private val enableWrapperLoading: Boolean = true,
            private val onSnap: (pagerIndex: Int, position: Int, data: M) -> Unit,
            private val viewLoadFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            private val viewLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            private val viewNoMore: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            private val viewEmpty: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            private val viewEmptyLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    ) : PagerAdapter() {

        override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

        override fun getCount(): Int = initPagerDataList.size

        fun getItem(position: Int): PagerModel<M> = initPagerDataList[position]

        override fun instantiateItem(container: ViewGroup, pagerIndex: Int): Any {
            val recyclerView = createDefaultRecyclerView(
                    context,
                    initPagerDataList,
                    requestLoadMore,
                    pagerIndex,
                    onRecyclerViewCreateViewHolder,
                    onRecyclerViewBindViewHolder,
                    snap,
                    recyclerViewOrientation,
                    startPadding,
                    dividerPadding,
                    enableWrapperLoading,
                    onSnap,
                    viewLoadFailure,
                    viewLoading,
                    viewNoMore,
                    viewEmpty,
                    viewEmptyLoading
            )
            val recyclerViewLayoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            val rootLayout = FrameLayout(context)
            rootLayout.addView(recyclerView, recyclerViewLayoutParams)
            container.addView(rootLayout)
            return rootLayout
        }

        override fun destroyItem(container: ViewGroup, position: Int, view: Any) = container.removeView(view as View)

        /**
         * POSITION_NONE 刷新所有页面
         * POSITION_UNCHANGED 不刷新
         */
        override fun getItemPosition(view: Any): Int {
            val position: Int = ((view as View).tag as? TagModel)?.pagerIndex ?: -1
            return if (position == currentPosition()) POSITION_NONE else POSITION_UNCHANGED
        }
    }

    /**
     * requestIndex 服务端默认从 0开始 算第一页, 1算第二页
     * requestNextIndex = requestIndex + 1
     */
    data class PagerModel<T>(var requestNextIndex: Int, var requestSize: Int, var dataList: MutableList<T>, var extrasData: Any? = null)

    class TagModel @JvmOverloads constructor(val pagerIndex: Int, val snapGravityHelper: STSnapGravityHelper? = null) {

        /**
         * for findViewWithTag
         */
        override fun equals(other: Any?): Boolean {
            if (other == null || other !is TagModel) {
                return false
            }
            return pagerIndex == other.pagerIndex
        }

        override fun hashCode(): Int {
            return pagerIndex
        }
    }
}

