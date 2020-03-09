package com.smart.library.widget.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * 适用于少量fragment的场景(例如 tab),fragment不会被销毁
 *
 * <p>This version of the pager is best for use when there are a handful of
 * typically more static fragments to be paged through, such as a set of tabs.
 * The fragment of each page the user visits will be kept in memory, though its
 * view hierarchy may be destroyed when not visible.  This can result in using
 * a significant amount of memory since fragment instances can hold on to an
 * arbitrary amount of state.  For larger sets of pages, consider
 * {@link FragmentStatePagerAdapter}.
 */
@Suppress("unused", "DEPRECATION")
class STFragmentPagerAdapter(fragmentManager: FragmentManager, var dataList: List<Fragment>) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment = dataList[position]

    override fun getCount(): Int = dataList.size
}
