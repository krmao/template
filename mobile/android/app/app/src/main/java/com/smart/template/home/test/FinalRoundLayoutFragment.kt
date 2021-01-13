package com.smart.template.home.test

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STToastUtil
import com.smart.template.R
import kotlinx.android.synthetic.main.final_round_fragment.*

@Suppress("unused", "DEPRECATION")
class FinalRoundLayoutFragment : STBaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.final_round_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        click_btn.setOnClickListener {
            STToastUtil.show("clicked")
        }
        textView.setOnClickListener {
            STToastUtil.show("you clicked me!")
        }
        textView.addOnLeftDrawableTouchUpListener {
            STToastUtil.show("left")
        }
        textView.addOnTopDrawableTouchUpListener {
            STToastUtil.show("top")
        }
        textView.addOnRightDrawableTouchUpListener {
            STToastUtil.show("right")
        }
        textView.addOnBottomDrawableTouchUpListener {
            STToastUtil.show("bottom")
        }
    }

    companion object {
        fun goTo(activity: Context?) {
            STActivity.startActivity(activity, FinalRoundLayoutFragment::class.java)
        }
    }
}