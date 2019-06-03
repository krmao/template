package com.smart.library.widget.viewpager

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import java.util.*

/**
 * 适合大量的fragment页面的情况，会销毁不可见的fragment
 *
 * <p>This version of the pager is more useful when there are a large number
 * of pages, working more like a list view.  When pages are not visible to
 * the user, their entire fragment may be destroyed, only keeping the saved
 * state of that fragment.  This allows the pager to hold on to much less
 * memory associated with each visited page as compared to
 * {@link FragmentPagerAdapter} at the cost of potentially more overhead when
 * switching between pages.
 */
@Suppress("unused")
class STFragmentStatePagerAdapter(var context: Context, fragmentManager: FragmentManager, var dataList: ArrayList<Fragment>) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = dataList[position]

    override fun getCount(): Int = dataList.size
}
