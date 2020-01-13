package com.smart.template.home.widget

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
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
import com.smart.library.widget.recyclerview.STDividerItemDecoration
import com.smart.library.widget.recyclerview.STEmptyLoadingWrapper
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.library.widget.recyclerview.STRecyclerViewLinearStartItemDecoration
import com.smart.library.widget.recyclerview.snap.STSnapGravityHelper
import com.smart.library.widget.recyclerview.snap.STSnapGravityPagerHelper
import com.smart.library.widget.recyclerview.snap.STSnapHelper
import com.smart.template.R

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
@Suppress("MemberVisibilityCanBePrivate", "unused", "UNUSED_ANONYMOUS_PARAMETER")
class STRecyclerPagerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    var recyclerViewOrientation: Int = LinearLayoutManager.VERTICAL
        internal set

    var dividerPadding: Int = 0
        internal set
    var startPadding: Int = 0
        internal set
    var viewLoadingEnabled: Boolean = true
        internal set

    @JvmOverloads
    fun <T> initialize(
            initPagerDataList: MutableList<PagerModel<T>>,
            requestLoadMore: (refresh: Boolean, pagerIndex: Int, requestIndex: Int, requestSize: Int, lastRequest: Any?, lastResponse: Any?, callback: (lastRequest: Any?, lastResponse: Any?, MutableList<T>?) -> Unit) -> Unit,
            onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
            onRecyclerViewBindViewHolder: (pagerModel: PagerModel<T>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
            snap: STSnapHelper.Snap = STSnapHelper.Snap.CENTER,
            recyclerViewOrientation: Int = LinearLayoutManager.VERTICAL, // recyclerView 横向滚动 默认禁止 viewPager 横向华东
            startPadding: Int = 0,
            dividerPadding: Int = 0,
            recyclerViewItemDecorationHandler: ((pagerIndex: Int, recyclerViewOrientation: Int, startPadding: Int, dividerPadding: Int) -> RecyclerView.ItemDecoration?)? = null,
            enableHorizontalSnapOnlySmoothScrollOneItem: Boolean = true,
            onSnap: (pagerIndex: Int, position: Int, data: T) -> Unit,
            viewLoadingEnabled: Boolean = false,
            viewLoadFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            viewLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            viewNoMore: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            viewEmpty: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            viewEmptyNone: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            viewEmptyLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            viewEmptyLoadingFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    ) {
        this.dividerPadding = dividerPadding
        this.startPadding = startPadding
        this.viewLoadingEnabled = viewLoadingEnabled
        this.recyclerViewOrientation = recyclerViewOrientation
        pageMargin = 0
        offscreenPageLimit = initPagerDataList.size
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
                recyclerViewItemDecorationHandler,
                enableHorizontalSnapOnlySmoothScrollOneItem,
                onSnap,
                viewLoadingEnabled,
                viewLoadFailure,
                viewLoading,
                viewNoMore,
                viewEmpty,
                viewEmptyNone,
                viewEmptyLoading,
                viewEmptyLoadingFailure
        )

        enableDrag = recyclerViewOrientation != LinearLayoutManager.HORIZONTAL
    }

    @JvmOverloads
    fun switchRecyclerViewOrientation(recyclerViewOrientation: Int = (if (this.recyclerViewOrientation == LinearLayoutManager.VERTICAL) LinearLayoutManager.HORIZONTAL else LinearLayoutManager.VERTICAL), keepPosition: Boolean = true, callback: ((orientation: Int) -> Unit)? = null) {
        if (recyclerViewOrientation != this.recyclerViewOrientation) {
            this.recyclerViewOrientation = recyclerViewOrientation

            setBackgroundColor(if (recyclerViewOrientation == LinearLayoutManager.HORIZONTAL) Color.TRANSPARENT else Color.WHITE)

            getRecyclerViews().forEach {
                // reset item decoration
                if (recyclerViewOrientation == LinearLayoutManager.VERTICAL) {
                    it.getItemDecorationAt(0)?.apply {
                        it.removeItemDecoration(this)
                    }
                } else {
                    it.addItemDecoration(STRecyclerViewLinearStartItemDecoration(dividerPadding, startPadding, viewLoadingEnabled))
                }

                // reset snap
                val snapGravityHelper: STSnapHelper? = getSnapHelper(it.tag as? TagModel)
                snapGravityHelper?.switchSnap(if (recyclerViewOrientation == LinearLayoutManager.VERTICAL) STSnapHelper.Snap.START else STSnapHelper.Snap.CENTER)

                // reset layout manager
                (it.layoutManager as? LinearLayoutManager)?.orientation = recyclerViewOrientation
                it.adapter = getRecyclerViewLoadingAdapter(it)

                // reset position
                if (keepPosition) {
                    snapGravityHelper?.forceSnap()
                }
            }
        }
        callback?.invoke(this.recyclerViewOrientation)
    }

    @JvmOverloads
    fun clearAll(requestNextIndex: Int = 0) {
        val innerAdapter: STPagerRecyclerViewAdapter<*>? = adapter as? STPagerRecyclerViewAdapter<*>
        if (innerAdapter != null) {
            (0 until innerAdapter.count).forEach { pagerIndex ->
                (findViewWithTag<RecyclerView>(TagModel(pagerIndex))?.adapter as? STEmptyLoadingWrapper<*>)?.removeAll()
                innerAdapter.getItem(pagerIndex).requestNextIndex = requestNextIndex
            }
        }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun getRecyclerViews(): List<RecyclerView> {
        return (0 until (adapter?.count ?: 0)).mapNotNull { getRecyclerView(it) }
    }

    @Suppress("MemberVisibilityCanBePrivate")
    fun <M> getPagerViews(): List<PagerView<M>> {
        return (0 until (adapter?.count ?: 0)).mapNotNull { getPagerView<M>(it) }
    }

    @JvmOverloads
    @Suppress("MemberVisibilityCanBePrivate")
    fun getRecyclerView(pagerIndex: Int = currentItem): RecyclerView? {
        return findViewWithTag(TagModel(pagerIndex))
    }

    @JvmOverloads
    @Suppress("MemberVisibilityCanBePrivate")
    fun <M> getPagerView(pagerIndex: Int = currentItem): PagerView<M>? {
        return findViewWithTag("pagerView-$pagerIndex")
    }

    @JvmOverloads
    @Suppress("MemberVisibilityCanBePrivate")
    fun getRecyclerViewSnapGravityHelper(pagerIndex: Int = currentItem): STSnapHelper? {
        return getSnapHelper(getRecyclerView(pagerIndex)?.tag as? TagModel)
    }

    fun getSnapHelper(tagModel: TagModel?): STSnapHelper? {
        return tagModel?.snapGravityHelper
    }

    @JvmOverloads
    fun forceSnap(pagerIndex: Int = currentItem, targetPosition: Int) {
        getRecyclerViewSnapGravityHelper(pagerIndex)?.forceSnap(targetPosition)
    }

    @JvmOverloads
    fun getSnappedPosition(pagerIndex: Int = currentItem): Int {
        return getRecyclerViewSnapGravityHelper(pagerIndex)?.lastSnappedPosition() ?: RecyclerView.NO_POSITION
    }

    @JvmOverloads
    @Suppress("UNCHECKED_CAST")
    fun <T> getRecyclerViewLoadingAdapter(pagerIndex: Int = currentItem): STEmptyLoadingWrapper<T>? {
        return (getRecyclerView(pagerIndex)?.adapter as? STEmptyLoadingWrapper<T>)
    }

    @Suppress("UNCHECKED_CAST")
    fun getRecyclerViewLoadingAdapter(recyclerView: RecyclerView?): STEmptyLoadingWrapper<*>? {
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
    fun <T> scrollToRecyclerViewPosition(recyclerItemData: T, smoothScrollToRecyclerViewPosition: Boolean = true, autoSwitchViewPager: Boolean = true, needOnSnapIfUseSnapGravityHelper: Boolean = true) {
        val innerAdapter: STPagerRecyclerViewAdapter<*>? = adapter as? STPagerRecyclerViewAdapter<*>
        if (innerAdapter != null) {
            for (pagerIndex in (0 until innerAdapter.count)) {
                if (scrollToRecyclerViewPosition(pagerIndex, recyclerItemData, smoothScrollToRecyclerViewPosition, autoSwitchViewPager, needOnSnapIfUseSnapGravityHelper)) {
                    break // 第一个匹配返回
                }
            }
        }
    }

    @JvmOverloads
    fun <T> scrollToRecyclerViewPosition(pagerIndex: Int, recyclerItemData: T, smoothScrollToRecyclerViewPosition: Boolean = true, autoSwitchViewPager: Boolean = true, needOnSnapIfUseSnapGravityHelper: Boolean = true): Boolean {
        val position: Int = getRecyclerViewInnerDataList<T>(pagerIndex).indexOf(recyclerItemData)
        return scrollToRecyclerViewPosition(pagerIndex, position, smoothScrollToRecyclerViewPosition, autoSwitchViewPager, needOnSnapIfUseSnapGravityHelper)
    }

    @JvmOverloads
    fun scrollToRecyclerViewPosition(pagerIndex: Int, position: Int, smoothScrollToRecyclerViewPosition: Boolean = true, autoSwitchViewPager: Boolean = true, needOnSnapIfUseSnapGravityHelper: Boolean = true): Boolean {
        val recyclerView: RecyclerView? = findViewWithTag(TagModel(pagerIndex))
        if (recyclerView != null && position >= 0 && position < recyclerView.adapter?.itemCount ?: 0) {
            if (autoSwitchViewPager && currentItem != pagerIndex) {
                currentItem = pagerIndex
            }
            val snapGravityHelper: STSnapHelper? = getSnapHelper((recyclerView.tag as? TagModel))
            if (snapGravityHelper != null) {
                if (needOnSnapIfUseSnapGravityHelper) {
                    snapGravityHelper.scrollToPositionWithOnSnap(position, smoothScrollToRecyclerViewPosition)
                } else {
                    snapGravityHelper.scrollToPositionWithoutOnSnap(position)
                }
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
                    val recyclerView: RecyclerView? = findViewWithTag(TagModel(pagerIndex))
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

    class InnerOnPageChangeListener(var checkBoxGroupView: STCheckBoxGroupView? = null) : OnPageChangeListener {
        private var isPageChanged = false
        private var currentPosition: Int = -1
        var enableOnPageSelectedAfterAnimationEnd: Boolean = false

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageScrollStateChanged(state: Int) {
            when (state) {
                SCROLL_STATE_IDLE -> {
                    if (isPageChanged && currentPosition != -1) {
                        isPageChanged = false
                        if (enableOnPageSelectedAfterAnimationEnd) {
                            callChangePage()
                        }
                    }
                }
                SCROLL_STATE_DRAGGING -> {
                }
                SCROLL_STATE_SETTLING -> {
                }
            }
        }

        override fun onPageSelected(position: Int) {
            currentPosition = position
            isPageChanged = true
            if (!enableOnPageSelectedAfterAnimationEnd) {
                callChangePage()
            }
        }

        fun callChangePage() {
            checkBoxGroupView?.setCheckedWithUpdateViewStatus(currentPosition, true) //this will be called when animation ends
        }
    }

    private val onPageChangeListener = InnerOnPageChangeListener()
    var enableOnPageSelectedAfterAnimationEnd: Boolean = false
        set(value) {
            field = value
            onPageChangeListener.enableOnPageSelectedAfterAnimationEnd = value
        }

    private val onUpdateViewOnCheckChangedListener = { checkBoxGroupView: STCheckBoxGroupView, originViewList: List<View>, checkedViewPositionList: List<Int>, changedViewPositionList: List<Int> ->
        if (checkedViewPositionList.size == 1) {
            val toPagerIndex: Int = checkedViewPositionList[0]
            if (toPagerIndex >= 0) {
                setCurrentItem(toPagerIndex, false)
            }
        }
    }

    /**
     * STCheckBoxGroupView 务必是单选模式
     */
    fun connectToCheckBoxGroupView(checkBoxGroupView: STCheckBoxGroupView) {

        removeOnPageChangeListener(onPageChangeListener)
        addOnPageChangeListener(onPageChangeListener)
        onPageChangeListener.checkBoxGroupView = checkBoxGroupView

        checkBoxGroupView.removeUpdateViewOnCheckChangedListener(onUpdateViewOnCheckChangedListener)
        checkBoxGroupView.addUpdateViewOnCheckChangedListener(onUpdateViewOnCheckChangedListener)
    }

    private var onInterceptTouchEventHandler: ((ev: MotionEvent?) -> Unit)? = null
    fun setOnInterceptTouchEventHandler(onInterceptTouchEventHandler: (ev: MotionEvent?) -> Unit) {
        this.onInterceptTouchEventHandler = onInterceptTouchEventHandler
    }

    var enableDrag: Boolean = false
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        onInterceptTouchEventHandler?.invoke(ev)
        return if (enableDrag) super.onInterceptTouchEvent(ev) else enableDrag
    }

    class PagerView<M>(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

        var isLoad: Boolean = false
            private set
        var pagerModel: PagerModel<M>? = null
            private set
        var adapterWrapper: STEmptyLoadingWrapper<M>? = null
            private set

        val recyclerView by lazy { RecyclerView(context) }

        fun callLoadMore(refresh: Boolean) {
            adapterWrapper?.callLoadMore(refresh)
        }

        fun isInnerDataEmpty(): Boolean = adapterWrapper?.isInnerDataEmpty() ?: true
        fun getInnerDataList(): MutableList<M>? = adapterWrapper?.innerData()
        fun lazyLoad() {
            val innerPagerModel = pagerModel
            if (!isLoad && innerPagerModel != null) {
                synchronized(isLoad) {
                    if (!isLoad) {
                        adapterWrapper?.resetDataList(innerPagerModel.dataList)
                        isLoad = true
                    }
                }
            }
        }

        fun init(pagerModel: PagerModel<M>,
                 requestLoadMore: (refresh: Boolean, pagerIndex: Int, requestIndex: Int, requestSize: Int, lastRequest: Any?, lastResponse: Any?, callback: (lastRequest: Any?, lastResponse: Any?, MutableList<M>?) -> Unit) -> Unit,
                 pagerIndex: Int,
                 onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
                 onRecyclerViewBindViewHolder: (pagerModel: PagerModel<M>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
                 snap: STSnapHelper.Snap = STSnapHelper.Snap.CENTER,
                 recyclerViewOrientation: Int = LinearLayoutManager.VERTICAL,
                 startPadding: Int = 0,
                 dividerPadding: Int = 0,
                 recyclerViewItemDecorationHandler: ((pagerIndex: Int, recyclerViewOrientation: Int, startPadding: Int, dividerPadding: Int) -> RecyclerView.ItemDecoration?)? = null,
                 enableHorizontalSnapOnlySmoothScrollOneItem: Boolean = true,
                 onSnap: (pagerIndex: Int, position: Int, data: M) -> Unit,
                 viewLoadingEnabled: Boolean = false,
                 viewLoadFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
                 viewLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
                 viewNoMore: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
                 viewEmpty: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
                 viewEmptyNone: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
                 viewEmptyLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
                 viewEmptyLoadingFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null) {

            isLoad = false

            this.pagerModel = pagerModel

            val itemViewCacheSize = 30
            val extraLayoutSpace = 512

            recyclerView.layoutManager = object : LinearLayoutManager(context, recyclerViewOrientation, false) {
                override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
                    return extraLayoutSpace
                }

                /**
                 * bug 原因以及较为省事的 解决方案
                 * https://bugly.qq.com/v2/crash-reporting/crashes/900006740/215833?pid=1
                 * https://www.jianshu.com/p/2eca433869e9
                 */
                override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                    try {
                        super.onLayoutChildren(recycler, state)
                    } catch (e: IndexOutOfBoundsException) {
                        STLogUtil.e("STEmptyLoadingWrapper", "onLayoutChildren exception", e)
                    }
                }
            }
            val originAdapter = object : STRecyclerViewAdapter<M, RecyclerView.ViewHolder>(context, arrayListOf() /*pagerModel.dataList*/) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = onRecyclerViewCreateViewHolder.invoke(pagerIndex, parent, viewType)
                override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) = onRecyclerViewBindViewHolder.invoke(pagerModel, viewHolder, position)
            }

            val adapterWrapper: STEmptyLoadingWrapper<M> = STEmptyLoadingWrapper(originAdapter)
            // custom loading views
            adapterWrapper.viewNoMore = viewNoMore
            adapterWrapper.viewLoadFailure = viewLoadFailure
            adapterWrapper.viewLoading = viewLoading
            adapterWrapper.viewEmpty = viewEmpty
            adapterWrapper.viewEmptyNone = viewEmptyNone
            adapterWrapper.viewEmptyLoading = viewEmptyLoading
            adapterWrapper.viewEmptyLoadingFailure = viewEmptyLoadingFailure
            adapterWrapper.enableChangeAnimations(false)
            adapterWrapper.enableLoadMore = viewLoadingEnabled

            val snapGravityHelper: STSnapHelper = if (recyclerViewOrientation == LinearLayoutManager.HORIZONTAL && enableHorizontalSnapOnlySmoothScrollOneItem) {
                STSnapGravityPagerHelper(viewLoadingEnabled, snap) { position: Int ->
                    if (position >= 0 && position < adapterWrapper.innerData().size) {
                        onSnap.invoke(pagerIndex, position, adapterWrapper.innerData()[position])
                    }
                }
            } else {
                STSnapGravityHelper(viewLoadingEnabled, snap) { position: Int ->
                    if (position >= 0 && position < adapterWrapper.innerData().size) {
                        onSnap.invoke(pagerIndex, position, adapterWrapper.innerData()[position])
                    }
                }
            }

            adapterWrapper.onInnerDataChanged = {
                // snapGravityHelper.forceSnap() // force snap after inner data changed
            }

            // onLoadMore listener
            adapterWrapper.onLoadMoreListener = { refresh: Boolean ->
                requestLoadMore.invoke(refresh, pagerIndex, pagerModel.requestNextIndex, pagerModel.requestSize, pagerModel.lastRequest, pagerModel.lastResponse) { lastRequest: Any?, lastResponse: Any?, newList: MutableList<M>? ->
                    when {
                        newList == null -> { // load failure
                            adapterWrapper.showLoadFailure()
                        }
                        newList.isNotEmpty() -> { // load success
                            adapterWrapper.add(newList)
                            adapterWrapper.completelyLoadMoreRequesting()

                            pagerModel.requestNextIndex++
                            if (lastRequest != null) {
                                pagerModel.lastRequest = lastRequest
                            }
                            if (lastResponse != null) {
                                pagerModel.lastResponse = lastResponse
                            }
                        }
                        else -> { // load no more
                            adapterWrapper.showNoMore()
                        }
                    }
                }
            }

            recyclerView.setItemViewCacheSize(itemViewCacheSize)
            recyclerView.setHasFixedSize(false)
            recyclerView.isDrawingCacheEnabled = true
            recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH

            val itemDecoration: RecyclerView.ItemDecoration =
                    recyclerViewItemDecorationHandler?.invoke(pagerIndex, recyclerViewOrientation, startPadding, dividerPadding)
                            ?: if (recyclerViewOrientation == LinearLayoutManager.HORIZONTAL) {
                                STRecyclerViewLinearStartItemDecoration(dividerPadding, startPadding, false)
                            } else {
                                STDividerItemDecoration(context, recyclerViewOrientation).apply {
                                    ContextCompat.getDrawable(context, R.drawable.st_drawable_horizontal_line_e5e5e5_margin_16dp)?.let {
                                        setDrawable(it)
                                    }
                                }
                            }
            recyclerView.addItemDecoration(itemDecoration)

            recyclerView.tag = TagModel(pagerIndex, snapGravityHelper)
            recyclerView.adapter = adapterWrapper

            // gravity snap
            snapGravityHelper.attachToRecyclerView(recyclerView)

            val recyclerViewLayoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            addView(recyclerView, recyclerViewLayoutParams)

            this.adapterWrapper = adapterWrapper
            this.tag = "pagerView-$pagerIndex"
        }
    }

    private class STPagerRecyclerViewAdapter<M>(
            private val context: Context,
            private val initPagerDataList: MutableList<PagerModel<M>>,
            private val requestLoadMore: (refresh: Boolean, pagerIndex: Int, requestIndex: Int, requestSize: Int, lastRequest: Any?, lastResponse: Any?, callback: (lastRequest: Any?, lastResponse: Any?, MutableList<M>?) -> Unit) -> Unit,
            private val currentPosition: () -> Int,
            private val onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> RecyclerView.ViewHolder,
            private val onRecyclerViewBindViewHolder: (pagerModel: PagerModel<M>, viewHolder: RecyclerView.ViewHolder, position: Int) -> Unit,
            private val snap: STSnapHelper.Snap = STSnapHelper.Snap.CENTER,
            private val recyclerViewOrientation: Int = LinearLayoutManager.VERTICAL,
            private val startPadding: Int = 0,
            private val dividerPadding: Int = 0,
            private val recyclerViewItemDecorationHandler: ((pagerIndex: Int, recyclerViewOrientation: Int, startPadding: Int, dividerPadding: Int) -> RecyclerView.ItemDecoration?)? = null,
            private val enableHorizontalSnapOnlySmoothScrollOneItem: Boolean = true,
            private val onSnap: (pagerIndex: Int, position: Int, data: M) -> Unit,
            private val viewLoadingEnabled: Boolean = false,
            private val viewLoadFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            private val viewLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            private val viewNoMore: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            private val viewEmpty: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            private val viewEmptyNone: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            private val viewEmptyLoading: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null,
            private val viewEmptyLoadingFailure: ((parent: ViewGroup, viewType: Int, orientation: Int) -> View?)? = null
    ) : PagerAdapter() {

        override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

        override fun getCount(): Int = initPagerDataList.size

        fun getItem(position: Int): PagerModel<M> = initPagerDataList[position]

        override fun instantiateItem(container: ViewGroup, pagerIndex: Int): Any {
            val pagerView = PagerView<M>(context)
            pagerView.init(initPagerDataList[pagerIndex], requestLoadMore, pagerIndex, onRecyclerViewCreateViewHolder, onRecyclerViewBindViewHolder, snap, recyclerViewOrientation, startPadding, dividerPadding, recyclerViewItemDecorationHandler, enableHorizontalSnapOnlySmoothScrollOneItem, onSnap, viewLoadingEnabled, viewLoadFailure, viewLoading, viewNoMore, viewEmpty, viewEmptyNone, viewEmptyLoading, viewEmptyLoadingFailure)
            container.addView(pagerView, LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            return pagerView
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
    data class PagerModel<T>(var requestNextIndex: Int = 0, var requestSize: Int = 0, var dataList: MutableList<T>, var extrasData: Any? = null, var lastRequest: Any? = null, var lastResponse: Any? = null, var lastPosition: Int? = 0) {
        override fun toString(): String {
            return "(title=$extrasData,size=${dataList.size},lastPosition=$lastPosition)"
        }
    }

    class TagModel @JvmOverloads constructor(val pagerIndex: Int, val snapGravityHelper: STSnapHelper? = null) {

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


