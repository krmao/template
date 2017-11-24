package com.smart.library.widget.viewpager

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import java.util.*

@Suppress("unused", "MemberVisibilityCanPrivate")
abstract class HKPagerAdapter<ItemEntity> : PagerAdapter {

    protected var TAG: String? = HKPagerAdapter::class.java.simpleName

    protected lateinit var mDataList: ArrayList<ItemEntity>
    protected lateinit var mContext: Context

    private constructor()

    constructor(context: Context, dataList: ArrayList<ItemEntity>) {
        mContext = context
        mDataList = dataList
    }

    override fun getCount(): Int = mDataList.size

    override fun getItemPosition(`object`: Any?): Int = PagerAdapter.POSITION_NONE

    override fun saveState(): Parcelable? = null

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) = Unit

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemPagerView = getView(mDataList[position], position, container)
        container.addView(itemPagerView)
        return itemPagerView
    }

    override fun isViewFromObject(view: View, any: Any): Boolean = view == any

    override fun destroyItem(container: ViewGroup?, position: Int, any: Any) {
        container?.removeView(any as View)
    }

    // 只需要返回 convertView 即可
    protected abstract fun getView(itemEntity: ItemEntity, position: Int, container: ViewGroup): View
}
