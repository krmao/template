package com.smart.template.home.test

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.STActivity
import com.smart.library.base.STBaseFragment
import com.smart.library.util.STLogUtil
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.template.R
import kotlinx.android.synthetic.main.final_fragment_recycler_view_drag_and_transfer.*
import kotlinx.android.synthetic.main.final_fragment_recycler_view_drag_and_transfer_item_day_detail.view.*
import kotlinx.android.synthetic.main.final_fragment_recycler_view_drag_and_transfer_item_days.view.*

@Suppress("DEPRECATION")
class FinalRecyclerViewDragAndTransferFragment : STBaseFragment() {

    companion object {
        fun goTo(context: Context?) {
            STActivity.startActivity(context, FinalRecyclerViewDragAndTransferFragment::class.java)
        }
    }

    @Suppress("PrivatePropertyName")
    private val TAG = "dragRecyclerView"
    private val daysList = mutableListOf("第一天", "第二天", "第三天", "第四天", "第五天", "第六天", "第七天", "第八天", "第九天", "第十天", "第十一天")
    private val daysAdapter by lazy {
        object : STRecyclerViewAdapter<String, STRecyclerViewAdapter.ViewHolder>(context, daysList) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
                return STRecyclerViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.final_fragment_recycler_view_drag_and_transfer_item_days, container, false))
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
                viewHolder.itemView.textViewDays.text = dataList[position]
            }
        }
    }

    private val dayDetailList = mutableListOf("苏州", "南京", "宿迁", "徐州", "常州", "淮安", "泰州", "常熟", "昆山", "上海", "浙江", "黄山", "四川", "东北", "武汉", "安徽", "洛阳", "杭州")
    private val dayDetailAdapter by lazy {
        object : STRecyclerViewAdapter<String, STRecyclerViewAdapter.ViewHolder>(context, dayDetailList) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
                return STRecyclerViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.final_fragment_recycler_view_drag_and_transfer_item_day_detail, container, false))
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
                viewHolder.itemView.textViewDayDetail.text = dataList[position]
                viewHolder.itemView.setOnLongClickListener {
                    STLogUtil.w(TAG, "start drag ...")
                    recyclerViewTransferManager?.startDrag(it)
                    // it.visibility = View.INVISIBLE remove 时会导致底部空白没有刷新
                    true
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.final_fragment_recycler_view_drag_and_transfer, container, false)
    }

    private var recyclerViewTransferManager: FinalRecyclerViewTransferManager? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewDays.adapter = daysAdapter
        recyclerViewDays.tag = "recyclerViewDays"

        recyclerViewDayDetail.adapter = dayDetailAdapter
        recyclerViewDays.tag = "recyclerViewDayDetail"

        recyclerViewTransferManager = FinalRecyclerViewTransferManager(recyclerViewDayDetail, recyclerViewDays, { _: RecyclerView, fromPosition: Int ->
            dayDetailAdapter.remove(fromPosition)
        }) { _: RecyclerView, fromPosition: Int, toPosition: Int ->
            daysAdapter.add(dayDetailList[fromPosition], toPosition)
        }
    }
}
