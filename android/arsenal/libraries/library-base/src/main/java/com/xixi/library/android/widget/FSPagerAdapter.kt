package com.xixi.library.android.widget

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import java.util.*

abstract class FSPagerAdapter<ItemEntity> : PagerAdapter {

    protected var TAG = javaClass.simpleName

    protected lateinit var mDataList: ArrayList<ItemEntity>
    protected lateinit var mContext: Context

    private constructor() {}

    constructor(context: Context, dataList: ArrayList<ItemEntity>) {
        mContext = context
        mDataList = dataList
    }

    override fun getCount(): Int {
        return mDataList.size
    }

    override fun getItemPosition(`object`: Any?): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun saveState(): Parcelable? {
        return null
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemPagerView = getView(mDataList[position], position, container)
        container.addView(itemPagerView)
        return itemPagerView
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any) {
        container?.removeView(`object` as View)
    }

    // 只需要返回 convertView 即可
    protected abstract fun getView(itemEntity: ItemEntity, position: Int, container: ViewGroup): View
}
