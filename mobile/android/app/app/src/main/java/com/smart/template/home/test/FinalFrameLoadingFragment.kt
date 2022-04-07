package com.smart.template.home.test

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STActivity
import com.smart.library.base.STActivityDelegate
import com.smart.library.base.STBaseFragment
import com.smart.library.widget.loading.STFrameLoadingLayout
import com.smart.template.R
import kotlinx.android.synthetic.main.final_activity_fragment.*
import kotlinx.android.synthetic.main.final_frame_loading_fragment.*

@Suppress("unused", "DEPRECATION")
class FinalFrameLoadingFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.final_frame_loading_fragment, container, false)
    }

    var count = 1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn.setOnClickListener {
            when{
                count % 3 == 0 -> frame_loading.showView(STFrameLoadingLayout.ViewType.LOADING)
                count % 3 == 1 -> frame_loading.showView(STFrameLoadingLayout.ViewType.FAILURE)
                count % 3 == 2 -> frame_loading.showView(STFrameLoadingLayout.ViewType.NO_DATA)
            }
            count++
        }

        btn.postDelayed({
            btn.callOnClick()
        },2000)
    }

    companion object {
        fun goTo(activity: Context?) {
            STActivity.startActivity(activity, FinalFrameLoadingFragment::class.java)
        }
    }
}