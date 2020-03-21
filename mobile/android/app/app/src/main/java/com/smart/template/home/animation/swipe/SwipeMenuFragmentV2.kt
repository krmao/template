package com.smart.template.home.animation.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STToastUtil
import com.smart.template.R
import kotlinx.android.synthetic.main.swipe_item_fragment.*

class SwipeMenuFragmentV2 : STBaseFragment() {

    companion object {
        fun goTo(context: Context?) {
            STActivity.start(context, SwipeMenuFragmentV2::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.swipe_fragment, container, false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = InnerAdapter(context, (0..19).toMutableList())
    }

    class InnerAdapter(private val context: Context?, private val dataList: List<Int>) : RecyclerView.Adapter<InnerViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder = InnerViewHolder(LayoutInflater.from(context).inflate(R.layout.swipe_layout, parent, false))

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            holder.contentView.text = "${dataList[position]} "
            holder.contentView.setOnClickListener {
                STToastUtil.show("content")
            }
            holder.menuView.setOnClickListener {
                STToastUtil.show("remove")
            }
        }

        override fun getItemCount(): Int = dataList.size
    }

    class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentView: TextView by lazy { itemView.findViewById<View>(R.id.contentView) as TextView }
        val menuView: Button by lazy { itemView.findViewById<View>(R.id.menuView) as Button }
    }

}
