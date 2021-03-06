package com.smart.template.home.tab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.smart.library.base.STBaseFragment
import com.smart.library.widget.viewpager.STFragmentPagerAdapter
import com.smart.template.R
import com.smart.template.home.FinalFlutterFragment
import com.smart.template.home.FinalHomeFragment
import com.smart.template.home.FinalHybirdFragment
import com.smart.template.home.FinalReactNativeFragment
import kotlinx.android.synthetic.main.final_tab_fragment.*

class FinalHomeTabFragment : STBaseFragment(), STBaseFragment.OnBackPressedListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.final_tab_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        content_vp.offscreenPageLimit = 5
        content_vp.adapter = STFragmentPagerAdapter(
            childFragmentManager,
            listOf(
                FinalHomeFragment(),
                FinalReactNativeFragment(),
                FinalHybirdFragment(),
                FinalFlutterFragment()
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

    override fun onBackPressed(): Boolean {
        if (content_vp.currentItem > 0) {
            content_vp.currentItem = 0
            return true
        }
        return false
    }
}
