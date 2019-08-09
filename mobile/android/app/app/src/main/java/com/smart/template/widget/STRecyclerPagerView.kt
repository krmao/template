package com.smart.template.widget

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter

class STRecyclerPagerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {

    companion object {

        @JvmStatic
        fun <M> createDefaultRecyclerView(
                context: Context,
                pagerDataList: MutableList<MutableList<M>>,
                pagerIndex: Int,
                onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> STRecyclerViewAdapter.ViewHolder,
                onRecyclerViewBindViewHolder: (dataList: MutableList<M>, viewHolder: STRecyclerViewAdapter.ViewHolder, position: Int) -> Unit
        ): View {
            val recyclerView = STRecyclerView(context)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = object : STRecyclerViewAdapter<M, STRecyclerViewAdapter.ViewHolder>(context, pagerDataList[pagerIndex]) {
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = onRecyclerViewCreateViewHolder.invoke(pagerIndex, parent, viewType)
                override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) = onRecyclerViewBindViewHolder.invoke(dataList, viewHolder, position)
            }
            return recyclerView
        }

    }

    init {
        pageMargin = 0
        offscreenPageLimit = Integer.MAX_VALUE
    }

    @JvmOverloads
    fun <T> initialize(
            pagerDataList: MutableList<MutableList<T>>,
            onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> STRecyclerViewAdapter.ViewHolder,
            onRecyclerViewBindViewHolder: (dataList: MutableList<T>, viewHolder: STRecyclerViewAdapter.ViewHolder, position: Int) -> Unit,
            createRecyclerView: ((context: Context, pagerIndex: Int) -> View)? = { context: Context, pagerIndex: Int ->
                createDefaultRecyclerView(
                        context,
                        pagerDataList,
                        pagerIndex,
                        onRecyclerViewCreateViewHolder,
                        onRecyclerViewBindViewHolder
                )
            },
            createPagerView: ((context: Context, pagerIndex: Int) -> View)? = null
    ) {
        adapter = STPagerRecyclerViewAdapter(context, pagerDataList, { currentItem }, onRecyclerViewCreateViewHolder, onRecyclerViewBindViewHolder, createRecyclerView, createPagerView)
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
            private val currentPosition: () -> Int,
            private var onRecyclerViewCreateViewHolder: (pagerIndex: Int, parent: ViewGroup, viewType: Int) -> STRecyclerViewAdapter.ViewHolder,
            private var onRecyclerViewBindViewHolder: (dataList: MutableList<M>, viewHolder: STRecyclerViewAdapter.ViewHolder, position: Int) -> Unit,
            private var createRecyclerView: ((context: Context, pagerIndex: Int) -> View)? = null,
            private var createPagerView: ((context: Context, pagerIndex: Int) -> View)? = null
    ) : PagerAdapter() {

        override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

        override fun getCount(): Int = pagerDataList.size

        override fun instantiateItem(container: ViewGroup, pagerIndex: Int): Any {
            return (createPagerView?.invoke(context, pagerIndex) ?: createRecyclerView?.invoke(context, pagerIndex) ?: createDefaultRecyclerView(context, pagerDataList, pagerIndex, onRecyclerViewCreateViewHolder, onRecyclerViewBindViewHolder)).apply {
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

