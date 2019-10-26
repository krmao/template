package com.smart.template.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.smart.library.base.STBaseActivity
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.template.R
import kotlinx.android.synthetic.main.st_behavior_bottom_sheet_activity.*

class STBehaviorScrollingActivity : STBaseActivity() {

    private val adapter: STRecyclerViewAdapter<String, STRecyclerViewAdapter.ViewHolder> by lazy {
        object : STRecyclerViewAdapter<String, STRecyclerViewAdapter.ViewHolder>(this, (1..100).map { "I Love You for time $it" }.toMutableList()) {
            override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(this@STBehaviorScrollingActivity).inflate(android.R.layout.test_list_item, null))
            }

            override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
                p0.itemView.findViewById<TextView>(android.R.id.text1).text = dataList[p1]
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_behavior_scrolling_activity)
        recyclerView.adapter = adapter
    }
}