package com.xixi.fruitshop.android.module.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.FrameLayout
import com.xixi.library.android.base.FSBaseActivity

class HomeActivity : FSBaseActivity() {

    companion object {
        val HOME_URL: String = "http://damaitemai.techqing.com/mobile/index.php"
        val HOME_CATEGORY_URL: String = "http://damaitemai.techqing.com/mobile/index.php?m=category"
        val HOME_SEARCH_URL: String = "http://damaitemai.techqing.com/mobile/index.php?m=search"
        val HOME_CART_URL: String = "http://damaitemai.techqing.com/mobile/index.php?m=cart"
        val HOME_USER_URL: String = "http://damaitemai.techqing.com/mobile/index.php?m=user"


        val URL_PREFIX = "http://damaitemai.techqing.com/mobile/index.php?"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableSwipeBack = false
        enableExitWithDoubleBackPressed = true
        super.onCreate(savedInstanceState)


        setContentView(FrameLayout(this))
        val bundle: Bundle = Bundle()
        bundle.putString("url", HOME_URL)
        bundle.putBoolean("hideBackAtFirstPage", true)

        val fragment: Fragment = HomeFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction().add(android.R.id.content, fragment, HomeFragment::javaClass.name).commitAllowingStateLoss()
    }
}
