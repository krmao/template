package com.smart.template.home.test

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXActivity
import com.smart.library.base.CXBaseFragment
import com.smart.library.util.CXLogUtil
import com.smart.library.widget.recyclerview.CXRecyclerViewAdapter
import com.smart.library.widget.recyclerview.CXRecyclerViewItemDecoration
import com.smart.template.R
import kotlinx.android.synthetic.main.home_fragment_recycler_view_drag_and_transfer.*
import kotlinx.android.synthetic.main.home_fragment_recycler_view_drag_and_transfer_item_day_detail.view.*
import kotlinx.android.synthetic.main.home_fragment_recycler_view_drag_and_transfer_item_days.view.*

@Suppress("DEPRECATION")
class RecyclerViewDragAndTransferFragment : CXBaseFragment() {

    companion object {
        fun goTo(context: Context?) {
            CXActivity.start(context, RecyclerViewDragAndTransferFragment::class.java)
        }
    }

    @Suppress("PrivatePropertyName")
    private val TAG = "dragRecyclerView"
    private val daysList = mutableListOf("第一天", "第二天", "第三天", "第四天", "第五天", "第六天", "第七天", "第八天", "第九天", "第十天", "第十一天")
    private val daysAdapter by lazy {
        object : CXRecyclerViewAdapter<String, CXRecyclerViewAdapter.ViewHolder>(context, daysList) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
                return CXRecyclerViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_fragment_recycler_view_drag_and_transfer_item_days, container, false))
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
                viewHolder.itemView.textViewDays.text = dataList[position]
            }
        }
    }

    private val dayDetailList = mutableListOf("苏州", "南京", "宿迁", "徐州", "常州", "淮安", "泰州", "常熟", "昆山", "上海", "浙江", "黄山", "四川", "东北", "武汉", "安徽", "洛阳", "杭州")
    private val dayDetailAdapter by lazy {
        object : CXRecyclerViewAdapter<String, CXRecyclerViewAdapter.ViewHolder>(context, dayDetailList) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
                return CXRecyclerViewAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_fragment_recycler_view_drag_and_transfer_item_day_detail, container, false))
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
                viewHolder.itemView.textViewDayDetail.text = dataList[position]
                viewHolder.itemView.setOnLongClickListener {
                    CXLogUtil.w(TAG, "start drag ...")
                    recyclerViewTransferManager?.startDrag(it)
                    // it.visibility = View.INVISIBLE remove 时会导致底部空白没有刷新
                    true
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment_recycler_view_drag_and_transfer, container, false)
    }

    private var recyclerViewTransferManager: CXRecyclerViewTransferManager? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewDays.addItemDecoration(CXRecyclerViewItemDecoration(10))
        recyclerViewDays.adapter = daysAdapter
        recyclerViewDays.tag = "recyclerViewDays"

        recyclerViewDayDetail.addItemDecoration(CXRecyclerViewItemDecoration(10))
        recyclerViewDayDetail.adapter = dayDetailAdapter
        recyclerViewDays.tag = "recyclerViewDayDetail"

        recyclerViewTransferManager = CXRecyclerViewTransferManager(recyclerViewDayDetail, recyclerViewDays, { fromRecyclerView: RecyclerView, fromPosition: Int ->
            dayDetailAdapter.remove(fromPosition)
        }) { toRecyclerView: RecyclerView, fromPosition: Int, toPosition: Int ->
            daysAdapter.add(dayDetailList[fromPosition], toPosition)
        }
    }
}
