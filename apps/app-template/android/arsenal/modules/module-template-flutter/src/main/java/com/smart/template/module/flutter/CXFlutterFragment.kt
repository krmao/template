package com.smart.template.module.flutter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXBaseFragment
import io.flutter.facade.Flutter
import io.flutter.view.FlutterView


class CXFlutterFragment : CXBaseFragment() {

    companion object {
        const val KEY_ROUTE = "route"

        @JvmStatic
        fun createFragment(initialRoute: String): CXFlutterFragment {
            val fragment = CXFlutterFragment()
            val args = Bundle()
            args.putString(CXFlutterFragment.KEY_ROUTE, initialRoute)
            fragment.arguments = args
            return fragment
        }
    }

    private val route: String? by lazy {
        arguments?.getString(KEY_ROUTE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): FlutterView? {

        val flutterView = Flutter.createView(activity!!, lifecycle, route)
        flutterView.visibility = View.GONE
        flutterView.addFirstFrameListener {
            FlutterView.FirstFrameListener {
                flutterView.visibility = View.VISIBLE
            }
        }

        return flutterView
    }

}