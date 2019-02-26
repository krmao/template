package com.smart.template.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.widget.recyclerview.CXRecyclerViewAdapter

class ViewPagerAdapter(val context: Context) : PagerAdapter() {

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == p1
    }

    override fun getCount(): Int {
        return 20
    }

    @SuppressLint("InflateParams")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val recyclerView = RecyclerView(context)
        recyclerView.tag = position
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = object : CXRecyclerViewAdapter<String, CXRecyclerViewAdapter.ViewHolder>(context, arrayListOf("1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1")) {
            @SuppressLint("SetTextI18n")
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
                return CXRecyclerViewAdapter.ViewHolder(TextView(context).apply {
                    text = "aslkfafdhasdkf;asdkfjl;asdkjfl;asdkjfal;sdkjfal;sdkjfal;sdkjfl;sdjkf;laskdglwkngen;dmfnaslkfafdhasdkf;asdkfjl;asdkjfl;asdkjfal;sdkjfal;sdkjfal;sdkjfl;sdjkf;laskdglwkngen;dmfn"
                    setTextColor(Color.BLACK)
                    setBackgroundColor(Color.BLUE)
                    setPadding(10, 10, 10, 10)
                    textSize = 15f
                })
            }

            override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
            }

        }
        container.addView(recyclerView)
        return recyclerView
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }
}