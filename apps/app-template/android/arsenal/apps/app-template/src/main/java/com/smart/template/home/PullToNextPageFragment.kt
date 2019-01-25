package com.smart.template.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXActivity
import com.smart.library.base.CXBaseFragment
import com.smart.template.R
import kotlinx.android.synthetic.main.home_fragment_pull_to_next_page.*

class PullToNextPageFragment : CXBaseFragment() {

    companion object {
        fun goTo(context: Context?) {
            CXActivity.start(context, PullToNextPageFragment::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment_pull_to_next_page, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleBar.right0BgView.setOnClickListener {
            pullToNextPageView.smoothScrollTo(pullToNextPageView.pageIndex + 1)
        }
        titleBar.right1BgView.setOnClickListener {
            pullToNextPageView.smoothScrollTo(pullToNextPageView.pageIndex - 1)
        }
    }
}
