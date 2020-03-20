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
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.library.widget.recyclerview.snap.STSnapGravityPagerHelper
import com.smart.library.widget.recyclerview.snap.STSnapHelper
import com.smart.library.widget.viewpager.STPagerAdapter
import com.smart.template.R
import kotlinx.android.synthetic.main.swipe_item_fragment.*

class SwipeMenuFragment : STBaseFragment() {

    companion object {
        fun goTo(context: Context?) {
            STActivity.start(context, SwipeMenuFragment::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.swipe_item_fragment, container, false)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(recyclerView)
        initRecyclerViewPager()
        initViewPager()
    }

    private fun initViewPager() {
        viewpager.adapter = object : STPagerAdapter<Int>(context, arrayListOf(0, 1)) {
            override fun getView(itemEntity: Int, position: Int, container: ViewGroup): View {
                val contentView = LayoutInflater.from(context).inflate(R.layout.swipe_item, container, false)
                initRecyclerView(contentView.findViewById<RecyclerView>(R.id.recyclerView))
                return contentView
            }
        }
        viewpager.currentItem = 0
    }

    private fun initRecyclerViewPager() {
        STSnapGravityPagerHelper(false, STSnapHelper.Snap.CENTER).attachToRecyclerView(recyclerPagerView)
        recyclerPagerView.adapter = object : STRecyclerViewAdapter<Int, STRecyclerViewAdapter.ViewHolder>(context, arrayListOf(0, 1)) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(context).inflate(R.layout.swipe_item, parent, false))
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                initRecyclerView(holder.itemView.findViewById<RecyclerView>(R.id.recyclerView))
            }
        }
    }

    private val dataList = (0..19).map { SwipeModel("$it") }.toMutableList()

    @SuppressLint("ClickableViewAccessibility")
    private fun initRecyclerView(innerRecyclerView: RecyclerView) {
        val layoutManager = LinearLayoutManager(context)
        val adapter = InnerAdapter(context, dataList)
        adapter.onSwipeMenuClickedListener = object : OnSwipeMenuClickedListener {
            override fun onSwipeMenuClicked(pos: Int) {
                if (pos >= 0 && pos < dataList.size) {
                    STToastUtil.show("删除:$pos")
                    dataList.removeAt(pos)
                    adapter.notifyItemRemoved(pos)
                }
            }
        }
        innerRecyclerView.adapter = adapter
        innerRecyclerView.layoutManager = layoutManager
    }

    class InnerAdapter(private val context: Context?, private val dataList: List<SwipeModel>) : RecyclerView.Adapter<InnerViewHolder>() {
        var onSwipeMenuClickedListener: OnSwipeMenuClickedListener? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder = InnerViewHolder(LayoutInflater.from(context).inflate(R.layout.swipe_item_layout, parent, false))

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            (holder.itemView as SwipeMenuLayout).isRightToLeftSwipe = true
            holder.content.text = "${dataList[position].name} 从右向左滑动 "
            holder.btnUnRead.visibility = if (position % 2 == 0) View.GONE else View.VISIBLE
            holder.btnDelete.setOnClickListener {
                holder.itemView.quickClose()
                onSwipeMenuClickedListener?.onSwipeMenuClicked(holder.adapterPosition)
            }
            holder.content.setOnClickListener {
                STToastUtil.show("you clicked:$position")
            }
        }

        override fun getItemCount(): Int = dataList.size
    }

    interface OnSwipeMenuClickedListener {
        fun onSwipeMenuClicked(pos: Int)
    }

    class SwipeModel(var name: String)

    class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView by lazy { itemView.findViewById<View>(R.id.content) as TextView }
        val btnDelete: Button by lazy { itemView.findViewById<View>(R.id.btnDelete) as Button }
        val btnUnRead: Button by lazy { itemView.findViewById<View>(R.id.btnUnRead) as Button }
    }

}
