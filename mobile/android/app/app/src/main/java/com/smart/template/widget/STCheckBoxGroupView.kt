package com.smart.template.widget

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.SparseArray
import android.view.View
import android.view.View.OnClickListener
import android.widget.LinearLayout
import com.smart.library.util.STLogUtil

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
@Suppress("MemberVisibilityCanBePrivate", "unused")
class STCheckBoxGroupView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {

    /**
     * 单选/多选 模式, 单选模式下设置多个选中会抛出异常
     */
    var enableSingleCheck = true
    private var enableFitCenter = true // item 数量 不足时居中, 等分宽度, 需设置 minWidth

    private var updateViewOnCheckChangedList: MutableList<(checkBoxGroupView: STCheckBoxGroupView, originViewList: List<View>, checkedViewPositionList: List<Int>, changedViewPositionList: List<Int>) -> Unit> = mutableListOf()
    private var createUncheckedItemView: ((title: String) -> View)? = null

    private val itemViewList: MutableList<View> = arrayListOf()
    private val onItemClickListener: OnClickListener = OnClickListener { itemView ->
        STLogUtil.d("origin checked:${isChecked(itemView)}")
        setCheckedWithUpdateViewStatus(itemView, !isChecked(itemView))
        STLogUtil.d("final checked:${isChecked(itemView)}")
    }

    /**
     * 初始化时全部不选中, 不会触发 updateViewOnCheckChanged
     * 务必手动调用 setCheckedWithUpdateViewStatus
     * @param enableFitCenter 宽度不足时 等分剩余空间, 必须设置 fitCenterMinimumSize
     * @param fitCenterMinimumSize enableFitCenter==true 的时候必须设置, 宽度不足时 等分剩余空间
     */
    fun initialize(enableSingleCheck: Boolean, enableFitCenter: Boolean, fitCenterMinimumSize: Int = 0, titleList: List<String>, createUncheckedItemView: (title: String) -> View, updateViewOnCheckChangedListener: (checkBoxGroupView: STCheckBoxGroupView, originViewList: List<View>, checkedViewPositionList: List<Int>, changedViewPositionList: List<Int>) -> Unit) {
        this.enableSingleCheck = enableSingleCheck
        this.enableFitCenter = enableFitCenter

        if (orientation == HORIZONTAL) {
            minimumWidth = fitCenterMinimumSize
        } else {
            minimumHeight = fitCenterMinimumSize
        }

        this.createUncheckedItemView = createUncheckedItemView

        addUpdateViewOnCheckChangedListener(updateViewOnCheckChangedListener)

        add(*titleList.toTypedArray())
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
    fun add(vararg titles: String) {
        titles.forEach { title ->
            add(title, itemViewList.size)
        }
    }

    /**
     * add 时 不选中, 不会触发 updateViewOnCheckChanged
     * 务必手动调用 setCheckedWithUpdateViewStatus
     */
    fun add(title: String, position: Int) {
        if (position >= 0 && position <= itemViewList.size) {
            val createUncheckedItemView: ((title: String) -> View)? = createUncheckedItemView
            if (createUncheckedItemView != null && title.isNotBlank()) {
                val uncheckedItemView: View = createUncheckedItemView(title)
                uncheckedItemView.tag = false // 初始化强制不选中, 不触发 updateViewOnCheckChanged
                uncheckedItemView.setOnClickListener(onItemClickListener)

                if (enableFitCenter) {
                    val indexAtChildren: Int = getIndexAtChildren(position)
                    if (itemViewList.isEmpty()) {
                        addDividerViewForFitCenter(0) // 第一个主动添加 dividerView
                    }
                    addView(uncheckedItemView, indexAtChildren)
                    addDividerViewForFitCenter(indexAtChildren + 1) // 因为每次添加 itemView 都会在后面追加一个 dividerView, 所以第一个需要额外观照
                } else {
                    addView(uncheckedItemView, position)
                }
                itemViewList.add(position, uncheckedItemView)
            }
        }
    }

    /**
     * 标签栏布局, 为了最优雅的平分
     * position -> 0           1           2           3           4           5           6           7           8
     * Views    -> DividerView RadioButton DividerView RadioButton DividerView RadioButton DividerView RadioButton DividerView  // 第一个最后一个为 DividerView
     * 在 itemViewList 中的索引 映射 children 中的索引
     */
    private fun getIndexAtChildren(position: Int): Int {
        return if (enableFitCenter) position * 2 + 1 else position
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
    fun setCheckedWithUpdateViewStatus(itemView: View?, checked: Boolean) {
        if (itemView != null) {
            val changeViewList = setCheckedWithoutUpdateViewStatus(itemView, checked)
            if (changeViewList.isNotEmpty()) {
                updateViewOnCheckChanged(changeViewList)
            }
        }
    }

    /**
     * @return changeViewList
     */
    @Throws(IllegalStateException::class)
    private fun setCheckedWithoutUpdateViewStatus(checkedList: SparseArray<Boolean>): List<View> {
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

    private fun setCheckedWithoutUpdateViewStatus(position: Int, checked: Boolean): List<View> = setCheckedWithoutUpdateViewStatus(getItemView(position), checked)

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
        if (itemView?.tag != null) {
            return itemView.tag as? Boolean ?: false
        }
        return false
    }

    /**
     * 当标签 宽度总和 不足父 View 宽度 的时候, 标签平分剩余空间，利用 该view 进行伸缩剩余空间
     */
    private fun addDividerViewForFitCenter(indexAtChildren: Int) {
        val dividerView: View = View(context).apply { setBackgroundColor(Color.TRANSPARENT) }
        addView(dividerView, indexAtChildren, LayoutParams(0, 0).apply { weight = 1f })
    }

    private fun setCheckedWithoutUpdateViewStatus(itemView: View?, checked: Boolean): List<View> {
        val changeViewList: MutableList<View> = mutableListOf()
        if (itemView != null) {
            if (itemView.tag == null || itemView.tag as? Boolean != checked) {
                if (enableSingleCheck && checked) { // 单选模式下, 选中之前清除其它标签的选中状态
                    changeViewList.addAll(uncheckAll())
                }
                itemView.tag = checked
                changeViewList.add(itemView)
            }
        }
        return changeViewList
    }

    private fun updateViewOnCheckChanged(changeViewList: List<View>) {
        this.updateViewOnCheckChangedList.forEach {
            it.invoke(this, itemViewList, itemViewList.filter { isChecked(it) }.map { itemViewList.indexOf(it) }, changeViewList.map { itemViewList.indexOf(it) }.toList())
        }
    }

    private fun getItemView(position: Int): View? {
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

            val changeList: List<View> = setCheckedWithoutUpdateViewStatus(itemView, false)
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

}