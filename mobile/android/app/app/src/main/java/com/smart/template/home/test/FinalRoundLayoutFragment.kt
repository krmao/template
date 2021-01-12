package com.smart.template.home.test

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STActivity
import com.smart.library.base.STActivityDelegate
import com.smart.library.base.STBaseFragment
import com.smart.template.R
import kotlinx.android.synthetic.main.final_activity_fragment.*

@Suppress("unused", "DEPRECATION")
class FinalRoundLayoutFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.final_activity_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun goTo(activity: Context?) {
            STActivity.startActivity(activity, FinalRoundLayoutFragment::class.java)
        }
    }
}