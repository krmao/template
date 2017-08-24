package com.xixi.fruitshop.android.module.home.tab

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xixi.fruitshop.android.module.home.*
import com.xixi.library.android.base.FSBaseFragment
import com.xixi.library.android.widget.viewpager.FSFragmentPagerAdapter
import kotlinx.android.synthetic.main.home_tab_fragment.*

class HomeTabFragment : FSBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.home_tab_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        content_vp.adapter = FSFragmentPagerAdapter(context, childFragmentManager,
                listOf(
                        HomeFragment(),
                        HybirdFragment(),
                        MineFragment(),
                        SettingFragment()
                )
        )
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabSelected(tab: TabLayout.Tab) {
                content_vp.currentItem = tab.position
            }
        })
    }
}
