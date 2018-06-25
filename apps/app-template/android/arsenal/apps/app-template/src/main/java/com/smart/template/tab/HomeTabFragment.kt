package com.smart.template.tab

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXBaseFragment
import com.smart.library.widget.viewpager.CXFragmentPagerAdapter
import com.smart.template.*
import kotlinx.android.synthetic.main.home_tab_fragment.*

class HomeTabFragment : CXBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.home_tab_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        content_vp.offscreenPageLimit = 5
        content_vp.adapter = CXFragmentPagerAdapter(context, childFragmentManager,
            listOf(
                HomeFragment(),
                ReactNativeFragment(),
                HybirdFragment(),
                ImFragment(),
                MineFragment(),
                SettingFragment()
            )
        )
        content_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                tab_layout.getTabAt(position)?.select()
            }

        })
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                content_vp.setCurrentItem(tab.position, false)
            }
        })
    }
}
