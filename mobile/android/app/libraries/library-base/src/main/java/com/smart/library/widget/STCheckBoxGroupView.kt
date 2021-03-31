package com.smart.library.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.Keep
import com.smart.library.base.ensureOnGlobalLayoutListener
import com.smart.library.util.STLogUtil
import com.smart.library.util.STSystemUtil

/**
 * 多选/单选 标签 view
 *
 * 初始化 或 add 时默认全部不选中且不触发监听
 * 只有调用 xxxWithUpdateViewStatus 且当发生改变时才触发监听
 *
 * 支持宽度不足时居中显示, 等分剩余宽度, 需要设置 minWidth
 * checkBoxGroupView.minimumWidth = STSystemUtil.screenWidth
 *
 * 支持全部不选中
 * 支持单选/多选
 * 支持同时设置选中/非选中 只触发一次监听
 */
@Suppress("MemberVisibilityCanBePrivate", "unused", "SameParameterValue", "LiftReturnOrAssignment")
//@Keep
class STCheckBoxGroupView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    /**
     * 单选/多选 模式, 单选模式下设置多个选中会抛出异常
     */
    var enableSingleCheck = true
    var mustHaveOneChecked: Boolean = true // 必须有一个是选中的
    private var enableDividerWeight = false // item 数量 不足时居中, 等分宽度, 需设置 minWidth
    private var enableDividerEdgesWeight = false // 有时需求最左边最右边指定宽度
    private var dividerWeight: Float = 0f

    private var updateViewOnCheckChangedList: MutableList<(checkBoxGroupView: STCheckBoxGroupView, originViewList: List<View>, checkedViewPositionList: List<Int>, changedViewPositionList: List<Int>) -> Unit> = mutableListOf()
    private var createUncheckedItemViewWithTabInfo: ((position: Int, tabInfo: TabInfo) -> View)? = null

    private val itemViewList: MutableList<View> = arrayListOf()
    private val onItemClickListener: OnClickListener = OnClickListener { itemView ->
        STLogUtil.d(TAG, "origin checked:${isChecked(itemView)}")
        setCheckedWithUpdateViewStatus(itemView, !isChecked(itemView))
        STLogUtil.d(TAG, "final checked:${isChecked(itemView)}")
    }

    /**
     * 初始化时全部不选中, 不会触发 updateViewOnCheckChanged
     * 务必手动调用 setCheckedWithUpdateViewStatus
     * @param enableDividerWeight 宽度不足时 等分剩余空间, 必须设置 fitCenterMinimumSize
     * @param minimumWidthOrHeight enableDividerWeight==true 的时候必须设置, 宽度不足时 等分剩余空间
     * @param mustHaveOneChecked enableSingleCheck==true 是否必须有一个是选中的，不能反选全部
     */
    @JvmOverloads
    @Deprecated("", ReplaceWith("initializeWithTabInfo(enableSingleCheck, mustHaveOneChecked, enableDividerWeight, enableDividerEdgesWeight, dividerWeight, minimumWidthOrHeight, titleList.map { TabInfo(it) }, { createUncheckedItemView(it.title) }, updateViewOnCheckChangedListener)", "smart.library.widget.GSCheckBoxGroupView.TabInfo"))
    fun initialize(enableSingleCheck: Boolean = true, mustHaveOneChecked: Boolean = true, enableDividerWeight: Boolean = false, enableDividerEdgesWeight: Boolean = false, dividerWeight: Float = 1f, minimumWidthOrHeight: Int = 0, titleList: List<String>, createUncheckedItemView: (title: String) -> View, updateViewOnCheckChangedListener: (checkBoxGroupView: STCheckBoxGroupView, originViewList: List<View>, checkedViewPositionList: List<Int>, changedViewPositionList: List<Int>) -> Unit) {
        initializeWithTabInfo(enableSingleCheck, mustHaveOneChecked, enableDividerWeight, enableDividerEdgesWeight, dividerWeight, minimumWidthOrHeight, titleList.map { TabInfo(title = it) }, { _: Int, tabInfo: TabInfo -> createUncheckedItemView(tabInfo.title) }, updateViewOnCheckChangedListener)
    }

    /**
     * 初始化时全部不选中, 不会触发 updateViewOnCheckChanged
     * 务必手动调用 setCheckedWithUpdateViewStatus
     * @param enableDividerWeight 宽度不足时 等分剩余空间, 必须设置 fitCenterMinimumSize
     * @param minimumWidthOrHeight enableDividerWeight==true 的时候必须设置, 宽度不足时 等分剩余空间
     * @param mustHaveOneChecked enableSingleCheck==true 是否必须有一个是选中的，不能反选全部
     */
    @JvmOverloads
    fun initializeWithTabInfo(enableSingleCheck: Boolean = true, mustHaveOneChecked: Boolean = true, enableDividerWeight: Boolean = false, enableDividerEdgesWeight: Boolean = false, dividerWeight: Float = 1f, minimumWidthOrHeight: Int = 0, tabInfoList: List<TabInfo>, createUncheckedItemViewWithTabInfo: (position: Int, tabInfo: TabInfo) -> View, updateViewOnCheckChangedListener: (checkBoxGroupView: STCheckBoxGroupView, originViewList: List<View>, checkedViewPositionList: List<Int>, changedViewPositionList: List<Int>) -> Unit) {
        this.enableSingleCheck = enableSingleCheck
        this.enableDividerWeight = enableDividerWeight
        this.mustHaveOneChecked = mustHaveOneChecked
        this.enableDividerEdgesWeight = enableDividerEdgesWeight
        this.dividerWeight = dividerWeight

        if (orientation == HORIZONTAL) {
            minimumWidth = minimumWidthOrHeight
        } else {
            minimumHeight = minimumWidthOrHeight
        }

        this.createUncheckedItemViewWithTabInfo = createUncheckedItemViewWithTabInfo

        updateViewOnCheckChangedList.clear()
        addUpdateViewOnCheckChangedListener(updateViewOnCheckChangedListener)

        removeAllViews()
        itemViewList.clear()
        add(*tabInfoList.toTypedArray())
    }

    fun addUpdateViewOnCheckChangedListener(updateViewOnCheckChangedListener: (checkBoxGroupView: STCheckBoxGroupView, originViewList: List<View>, checkedViewPositionList: List<Int>, changedViewPositionList: List<Int>) -> Unit) {
        removeUpdateViewOnCheckChangedListener(updateViewOnCheckChangedListener)
        this.updateViewOnCheckChangedList.add(updateViewOnCheckChangedListener)
    }

    fun removeUpdateViewOnCheckChangedListener(updateViewOnCheckChangedListener: (checkBoxGroupView: STCheckBoxGroupView, originViewList: List<View>, checkedViewPositionList: List<Int>, changedViewPositionList: List<Int>) -> Unit) {
        this.updateViewOnCheckChangedList.remove(updateViewOnCheckChangedListener)
    }

    /**
     * add 时 不选中, 不会触发 updateViewOnCheckChanged
     * 务必手动调用 setCheckedWithUpdateViewStatus
     */
    fun add(vararg tabInfos: TabInfo) {
        tabInfos.forEach { tabInfo ->
            add(tabInfo, itemViewList.size)
        }
    }

    /**
     * add 时 不选中, 不会触发 updateViewOnCheckChanged
     * 务必手动调用 setCheckedWithUpdateViewStatus
     */
    fun add(tabInfo: TabInfo, position: Int) {
        if (position >= 0 && position <= itemViewList.size) {
            val createUncheckedItemViewWithTabInfo: ((position: Int, tabInfo: TabInfo) -> View)? = createUncheckedItemViewWithTabInfo
            if (createUncheckedItemViewWithTabInfo != null) {
                val uncheckedItemView: View = createUncheckedItemViewWithTabInfo(position, tabInfo)
                val textView: TextView? = getInnerTextView(uncheckedItemView)
                textView?.tag = false // 初始化强制不选中, 不触发 updateViewOnCheckChanged
                textView?.includeFontPadding = false
                uncheckedItemView.setOnClickListener(onItemClickListener)

                if (enableDividerWeight) {
                    val indexAtChildren: Int = getIndexAtChildren(position)
                    if (itemViewList.isEmpty()) { // set first divider view weight
                        addDividerViewForFitCenter(0, if (enableDividerEdgesWeight) this.dividerWeight else 0f) // 第一个主动添加 dividerView
                    }
                    if (enableDividerEdgesWeight && itemViewList.isNotEmpty() && position == itemViewList.size) {
                        if (enableDividerEdgesWeight) { // reset last divider view weight
                            (getChildAt(getIndexAtChildren(position) - 1).layoutParams as LayoutParams).weight = this.dividerWeight
                        }
                    }
                    addView(uncheckedItemView, indexAtChildren)
                    addDividerViewForFitCenter(indexAtChildren + 1, if (!enableDividerEdgesWeight && position == itemViewList.size) 0f else this.dividerWeight) // 因为每次添加 itemView 都会在后面追加一个 dividerView, 所以第一个需要额外观照
                } else {
                    addView(uncheckedItemView, position)
                }
                itemViewList.add(position, uncheckedItemView)
            }
        }
    }

    /**
     * 标签栏布局, 为了最优雅的平分
     * position ->             0                       1                       2                       3
     * index    -> 0           1           2           3           4           5           6           7           8
     * Views    -> DividerView RadioButton DividerView RadioButton DividerView RadioButton DividerView RadioButton DividerView  // 第一个最后一个为 DividerView
     * 在 itemViewList 中的索引 映射 children 中的索引
     */
    private fun getIndexAtChildren(position: Int): Int {
        return if (enableDividerWeight) position * 2 + 1 else position
    }

    /**
     * 同时设置所有标签的选中/非选中状态
     * 状态改变的情况下 必然触发 只一次 updateViewOnCheckChanged
     * 数量必须等于所有标签的总和数量, 务必一一对应
     * 确保当前为 非单选 模式, 否则会报错异常
     */
    @Throws(IllegalStateException::class)
    fun setCheckedWithUpdateViewStatus(checkedList: SparseArray<Boolean>) {
        val changeViewList = setCheckedWithoutUpdateViewStatus(checkedList)
        if (changeViewList.isNotEmpty()) {
            updateViewOnCheckChanged(changeViewList)
        }
    }

    /**
     * 状态改变的情况下 必然触发 updateViewOnCheckChanged
     */
    fun setCheckedWithUpdateViewStatus(position: Int, checked: Boolean) = setCheckedWithUpdateViewStatus(getItemView(position), checked)

    /**
     * 状态改变的情况下 必然触发 updateViewOnCheckChanged
     */
    @Suppress("DEPRECATION")
    fun setCheckedWithUpdateViewStatus(itemView: View?, checked: Boolean) {
        STLogUtil.sync { STLogUtil.e(TAG, "setCheckedWithUpdateViewStatus start, thread=${Thread.currentThread().name}") }
        if (itemView != null) {
            val changeViewList = setCheckedWithoutUpdateViewStatus(itemView, checked)
            if (changeViewList.isNotEmpty()) {
                /**
                 * 选中的标签滚动到中间
                 * changeViewList.isNotEmpty() 防止重复调用
                 */
                if (checked) {
                    val itemViewWidth: Int = itemView.width
                    val itemViewLeft: Int = itemView.left

                    val doSmoothScrollToMiddle: (view: View) -> Unit = {
                        STLogUtil.sync { STLogUtil.e(TAG, "setCheckedWithUpdateViewStatus doSmoothScrollToMiddle start, thread=${Thread.currentThread().name}") }
                        val parentView = parent as ViewGroup
                        if (parentView is HorizontalScrollView) {
                            val middleLeftPosition = (STSystemUtil.screenWidth() - it.width) / 2 // 计算控件居正中时距离左侧屏幕的距离
                            val offset = it.left - middleLeftPosition // 正中间位置需要向左偏移的距离
                            parentView.smoothScrollTo(offset, 0)
                            STLogUtil.sync { STLogUtil.e(TAG, "doSmoothScrollToMiddle end it.left=${it.left}, it.width=${it.width}, offset=$offset, middleLeftPosition=$middleLeftPosition, thread=${Thread.currentThread().name}") }
                        } else {
                            STLogUtil.sync { STLogUtil.e(TAG, "doSmoothScrollToMiddle end parent not HorizontalScrollView , thread=${Thread.currentThread().name}") }
                        }
                        STLogUtil.sync { STLogUtil.e(TAG, "setCheckedWithUpdateViewStatus doSmoothScrollToMiddle end, thread=${Thread.currentThread().name}") }
                    }

                    if (itemViewWidth > 0) {
                        STLogUtil.sync { STLogUtil.w(TAG, "itemViewWidth >0 doSmoothScrollToMiddle start itemViewLeft=$itemViewLeft, itemViewWidth=$itemViewWidth, thread=${Thread.currentThread().name}") }
                        doSmoothScrollToMiddle(itemView)
                    } else {
                        STLogUtil.sync { STLogUtil.w(TAG, "itemViewWidth <=0 setOnLayoutListener start itemViewLeft=$itemViewLeft, itemViewWidth=$itemViewWidth, thread=${Thread.currentThread().name}") }
                        STLogUtil.sync { STLogUtil.e(TAG, "setOnLayoutListener start, thread=${Thread.currentThread().name}") }
                        itemView.ensureOnGlobalLayoutListener {
                            STLogUtil.sync { STLogUtil.e(TAG, "setOnLayoutListener on callback start, thread=${Thread.currentThread().name}") }
                            doSmoothScrollToMiddle(it)
                            STLogUtil.sync { STLogUtil.e(TAG, "setOnLayoutListener on callback end, thread=${Thread.currentThread().name}") }
                        }
                        STLogUtil.sync { STLogUtil.e(TAG, "setOnLayoutListener end, thread=${Thread.currentThread().name}") }
                    }
                }

                // 延时执行, 不然出现后续代码未执行的情况
                post {
                    updateViewOnCheckChanged(changeViewList)
                }
            }
        }
        STLogUtil.sync { STLogUtil.e(TAG, "setCheckedWithUpdateViewStatus end, thread=${Thread.currentThread().name}") }
    }

    /**
     * @return changeViewList
     */
    @Throws(IllegalStateException::class)
    fun setCheckedWithoutUpdateViewStatus(checkedList: SparseArray<Boolean>): List<View> {
        val changeViewList: MutableList<View> = arrayListOf()
        if (checkedList.size() > 0 && checkedList.size() == itemViewList.size) {

            var checkedCount = 0 // 是否存在多选
            for (index: Int in 0 until checkedList.size()) {
                if (checkedList[index]) {
                    checkedCount++
                }
            }
            if (enableSingleCheck && checkedCount > 1) {
                throw IllegalStateException("当前未单选状态, 不能多选, enableSingleCheck=$enableSingleCheck")
            } else {
                for (index: Int in 0 until checkedList.size()) {
                    val itemView = getItemView(index)
                    val changeList = setCheckedWithoutUpdateViewStatus(itemView, checkedList[index])
                    if (itemView != null && changeList.isNotEmpty()) {
                        changeViewList.addAll(changeList)
                    }
                }
            }
        }
        return changeViewList
    }

    fun setCheckedWithoutUpdateViewStatus(position: Int, checked: Boolean): List<View> = setCheckedWithoutUpdateViewStatus(getItemView(position), checked)

    /**
     * 状态改变的情况下 必然触发 updateViewOnCheckChanged
     */
    fun uncheckAllWithUpdateViewStatus() {
        val changeViewList: List<View> = uncheckAll()
        if (changeViewList.isNotEmpty()) {
            updateViewOnCheckChanged(changeViewList)
        }
    }

    /**
     * 状态改变的情况下 必然触发 updateViewOnCheckChanged
     * 确保当前为 非单选 模式, 否则会报错异常
     */
    @Throws(IllegalStateException::class)
    fun checkAllWithUpdateViewStatus() {
        val changeViewList: List<View> = checkAll()
        if (changeViewList.isNotEmpty()) {
            updateViewOnCheckChanged(changeViewList)
        }
    }

    /**
     * 是否被选中
     */
    fun isChecked(position: Int): Boolean = isChecked(getItemView(position))

    /**
     * 是否被选中
     */
    fun isChecked(itemView: View?): Boolean {
        val textView = getInnerTextView(itemView)
        if (textView?.tag != null) {
            return textView.tag as? Boolean ?: false
        }
        return false
    }

    /**
     * 当标签 宽度总和 不足父 View 宽度 的时候, 标签平分剩余空间，利用 该view 进行伸缩剩余空间
     */
    private fun addDividerViewForFitCenter(indexAtChildren: Int, weight: Float) {
        val dividerView: View = View(context).apply { setBackgroundColor(Color.TRANSPARENT) }
        addView(dividerView, indexAtChildren, LayoutParams(0, 0).apply { this.weight = weight })
    }

    fun getInnerTextView(itemView: View?): TextView? {
        return (itemView?.tag as? Array<*>)?.getOrNull(0) as? TextView
    }

    fun getInnerFlagView(itemView: View?): View? {
        return (itemView?.tag as? Array<*>)?.getOrNull(1) as? View
    }

    private fun setCheckedWithoutUpdateViewStatus(itemView: View?, checked: Boolean): List<View> {
        val changeViewList: MutableList<View> = mutableListOf()
        if (itemView != null) {

            val textView = getInnerTextView(itemView)
            if (textView?.tag == null || textView.tag as? Boolean != checked) {
                if (enableSingleCheck) {
                    if (checked || !mustHaveOneChecked) {
                        changeViewList.addAll(uncheckAll()) // 单选模式下, 选中之前清除其它标签的选中状态
                        textView?.tag = checked
                        if (!changeViewList.contains(itemView)) {
                            changeViewList.add(itemView)
                        }
                    } else {
                        if (getFirstCheckedItemView() != itemView) {
                            textView?.tag = checked
                            changeViewList.add(itemView)
                        } else {
                            STLogUtil.w("GSCheckBoxGroupView", "mustHaveOneChecked")
                        }
                    }
                } else {
                    textView?.tag = checked
                    changeViewList.add(itemView)
                }
            }
        }
        return changeViewList
    }

    private fun setItemChecked(itemView: View?, checked: Boolean): List<View> {
        val changeViewList: MutableList<View> = mutableListOf()
        if (itemView != null) {
            val textView = getInnerTextView(itemView)
            if (textView?.tag == null || textView.tag as? Boolean != checked) {
                textView?.tag = checked
                changeViewList.add(itemView)
            }
        }
        return changeViewList
    }

    private fun updateViewOnCheckChanged(changeViewList: List<View>) {
        this.updateViewOnCheckChangedList.forEach { listener ->
            listener.invoke(this, itemViewList, itemViewList.filter { isChecked(it) }.map { itemViewList.indexOf(it) }, changeViewList.map { itemViewList.indexOf(it) }.toList())
        }
    }

    fun getFirstCheckedItemView(): View? {
        return itemViewList.firstOrNull { isChecked(it) }
    }

    fun getItemView(position: Int): View? {
        if (position >= 0 && position < itemViewList.size) {
            return getChildAt(getIndexAtChildren(position))
        } else {
            return null
        }
    }

    /**
     * @return changeViewList
     */
    private fun uncheckAll(): List<View> {
        val changeViewList = mutableListOf<View>()
        itemViewList.forEach { itemView ->

            val changeList: List<View> = setItemChecked(itemView, false)
            if (changeList.isNotEmpty()) {
                changeViewList.addAll(changeList)
            }
        }
        return changeViewList
    }

    /**
     * @return changeViewList
     */
    @Throws(IllegalStateException::class)
    private fun checkAll(): List<View> {
        val changeViewList = mutableListOf<View>()
        if (!enableSingleCheck) {
            itemViewList.forEach { itemView ->
                val changeList: List<View> = setCheckedWithoutUpdateViewStatus(itemView, true)
                if (changeList.isNotEmpty()) {
                    changeViewList.addAll(changeList)
                }
            }
        } else {
            throw IllegalStateException("当前未单选状态, 不能多选, enableSingleCheck=$enableSingleCheck")
        }
        return changeViewList
    }

    data class TabInfo(val title: String, val extraInfo: Any? = null)

    companion object {
        const val TAG = "[CHECK_BOX_GROUP]"
    }
}