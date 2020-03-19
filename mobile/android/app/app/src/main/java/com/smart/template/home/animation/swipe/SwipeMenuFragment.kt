package com.smart.template.home.animation.swipe

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STToastUtil
import com.smart.template.R
import kotlinx.android.synthetic.main.swipe_item_fragment.*
import java.util.*

class SwipeMenuFragment : STBaseFragment() {

    companion object {
        fun goTo(context: Context?) {
            STActivity.start(context, SwipeMenuFragment::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swipe_item_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataList = ArrayList<SwipeModel>()
        for (i in 0..19) {
            dataList.add(SwipeModel("" + i))
        }
        val layoutManager = LinearLayoutManager(context)
        val adapter = InnerAdapter(context, dataList)
        adapter.onSwipeListener = object : OnSwipeListener {
            override fun onDel(pos: Int) {
                if (pos >= 0 && pos < dataList.size) {
                    STToastUtil.show("删除:$pos")
                    dataList.removeAt(pos)
                    adapter.notifyItemRemoved(pos) //推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }

            override fun onTop(pos: Int) {
                if (pos > 0 && pos < dataList.size) {
                    val swipeModel: SwipeModel = dataList[pos]
                    dataList.remove(swipeModel)
                    adapter.notifyItemInserted(0)
                    dataList.add(0, swipeModel)
                    adapter.notifyItemRemoved(pos + 1)
                    if (layoutManager.findFirstVisibleItemPosition() == 0) {
                        recyclerView.scrollToPosition(0)
                    }
                    //notifyItemRangeChanged(0,holder.getAdapterPosition()+1);
                }
            }
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        recyclerView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val viewCache = SwipeMenuLayout.viewCache
                viewCache?.smoothClose()
            }
            false
        }
    }

    class InnerAdapter(private val context: Context?, private val dataList: List<SwipeModel>) : RecyclerView.Adapter<InnerViewHolder>() {
        var onSwipeListener: OnSwipeListener? = null
        private val inflater: LayoutInflater = LayoutInflater.from(context)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerViewHolder {
            return InnerViewHolder(inflater.inflate(R.layout.item_cst_swipe, parent, false))
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: InnerViewHolder, position: Int) {
            (holder.itemView as SwipeMenuLayout).setIos(false).isLeftSwipe = position % 2 == 0 //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
            holder.content.text = "${dataList.get(position).name} ${if (position % 2 == 0) "我右白虎" else "我左青龙"}"

            holder.content.setOnLongClickListener { v ->
                Toast.makeText(context, "longclig", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onLongClick() called with: v = [$v]")
                false
            }
            holder.btnUnRead.visibility = if (position % 3 == 0) View.GONE else View.VISIBLE
            holder.btnDelete.setOnClickListener {
                if (null != onSwipeListener) {
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((SwipeMenuLayout) holder.itemView).quickClose();
                    //((SwipeMenuLayout) holder.itemView).quickClose();
                    onSwipeListener?.onDel(holder.adapterPosition)
                }
            }
            //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
            holder.content.setOnClickListener { v ->
                Toast.makeText(context, "onClick:" + dataList.get(holder.adapterPosition).name, Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onClick() called with: v = [$v]")
            }
            holder.btnTop.setOnClickListener {
                if (null != onSwipeListener) {
                    onSwipeListener?.onTop(holder.adapterPosition)
                }
            }
        }

        override fun getItemCount(): Int = dataList.size
    }

    /**
     * 和Activity通信的接口
     */
    interface OnSwipeListener {
        fun onDel(pos: Int)
        fun onTop(pos: Int)
    }

    class SwipeModel(var name: String)

    class InnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content: TextView by lazy { itemView.findViewById<View>(R.id.content) as TextView }
        val btnDelete: Button by lazy { itemView.findViewById<View>(R.id.btnDelete) as Button }
        val btnUnRead: Button by lazy { itemView.findViewById<View>(R.id.btnUnRead) as Button }
        val btnTop: Button by lazy { itemView.findViewById<View>(R.id.btnTop) as Button }

    }

}
