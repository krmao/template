package com.smart.library.flutter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import kotlinx.android.synthetic.main.flutter_fragment_k.*

class KFragment : STBaseFragment() {

    companion object {
        fun goTo(context: Context?) {
            STActivity.start(context, KFragment::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.flutter_fragment_k, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        empty_view.setOnClickListener {
            FlutterActivity.goTo(activity, "")
        }
    }

}