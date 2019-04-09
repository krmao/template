package com.smart.template.home.test

import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
                    it.startDrag(ClipData.newPlainText("", ""), View.DragShadowBuilder(it), it, 0)
                    it.visibility = View.INVISIBLE
                    true
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_fragment_recycler_view_drag_and_transfer, container, false)
    }

    private var backgroundView: View? = null
    private var dataOriginPosition: Int = -1
    private var dataNewPosition: Int = -1 // -1 默认值, -2 目标recyclerView 已添加, 则原始目标recyclerView 需要删除对应项
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewDays.addItemDecoration(CXRecyclerViewItemDecoration(10))
        recyclerViewDays.adapter = daysAdapter
        recyclerViewDays.tag = "recyclerViewDays"

        recyclerViewDayDetail.addItemDecoration(CXRecyclerViewItemDecoration(10))
        recyclerViewDayDetail.adapter = dayDetailAdapter
        recyclerViewDays.tag = "recyclerViewDayDetail"

        recyclerViewDays.setOnDragListener { dragView, dragEvent ->

            // 松手时在 原始 recyclerView 边界之内 ACTION_DRAG_STARTED -> ACTION_DRAG_ENTERED -> ACTION_DROP        -> ACTION_DRAG_ENDED
            // 松手时在 原始 recyclerView 边界之外 ACTION_DRAG_STARTED -> ACTION_DRAG_ENTERED -> ACTION_DRAG_EXITED -> ACTION_DRAG_ENDED
            when (dragEvent.action) {
                // 第一次长按开始 drag 会触发
                DragEvent.ACTION_DRAG_STARTED -> CXLogUtil.w(TAG, "days ACTION_DRAG_STARTED")
                // 进入 原始 recyclerView 边界会触发, 第一次长按开始 drag 也会触发
                DragEvent.ACTION_DRAG_ENTERED -> CXLogUtil.w(TAG, "days ACTION_DRAG_ENTERED")
                // 离开 原始 recyclerView 边界会触发
                DragEvent.ACTION_DRAG_EXITED -> {
                    CXLogUtil.w(TAG, "days ACTION_DRAG_EXITED")
                    backgroundView?.setBackgroundColor(resources.getColor(R.color.orange_700))
                    dataNewPosition = -1
                }

                // 长按后移动会连续触发
                DragEvent.ACTION_DRAG_LOCATION -> {
                    val onTopOfView = recyclerViewDays.findChildViewUnder(dragEvent.x, dragEvent.y)
                    onTopOfView?.let {
                        dataNewPosition = recyclerViewDays.getChildAdapterPosition(it)
                        backgroundView?.setBackgroundColor(resources.getColor(R.color.orange_700))
                        backgroundView = it.findViewById<TextView>(R.id.textViewDays)
                        backgroundView?.setBackgroundColor(resources.getColor(R.color.blue_700))
                        Unit
                    }
                    CXLogUtil.d(TAG, "days ACTION_DRAG_LOCATION $dataNewPosition , ${daysList.getOrNull(dataNewPosition)}")
                }

                // 松手时如果在 原始 recyclerView 边界内会触发, 边界之外不会触发
                DragEvent.ACTION_DROP -> {
                    CXLogUtil.w(TAG, "days ACTION_DROP dataNewPosition=$dataNewPosition, dataOriginPosition=$dataOriginPosition")
                    backgroundView?.setBackgroundColor(resources.getColor(R.color.orange_700))

                    if (dataOriginPosition != -1 && dataNewPosition != -1) {
                        daysAdapter.add(dayDetailList[dataOriginPosition], dataNewPosition)
                        recyclerViewDays.scrollToPosition(dataNewPosition)
                        dataNewPosition = -2 // must be remove
                    }
                }

                //松手后最后一个事件, 一定会触发
                DragEvent.ACTION_DRAG_ENDED -> {
                    CXLogUtil.w(TAG, "days ACTION_DRAG_ENDED $dataNewPosition")
                    backgroundView = null
                }
                else -> {
                    CXLogUtil.w(TAG, "days else ${dragEvent.action}")
                }
            }
            true
        }

        recyclerViewDayDetail.setOnDragListener { dragView, dragEvent ->
            // val destRecyclerView = dragView as RecyclerView
            // CXLogUtil.v(TAG, "dayD dragView:${destRecyclerView.tag}")
            val draggingItemView = dragEvent.localState as View
            try {
                dataOriginPosition = recyclerViewDayDetail.getChildAdapterPosition(draggingItemView)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // 松手时在 原始 recyclerView 边界之内 ACTION_DRAG_STARTED -> ACTION_DRAG_ENTERED -> ACTION_DROP        -> ACTION_DRAG_ENDED
            // 松手时在 原始 recyclerView 边界之外 ACTION_DRAG_STARTED -> ACTION_DRAG_ENTERED -> ACTION_DRAG_EXITED -> ACTION_DRAG_ENDED
            when (dragEvent.action) {
                // 第一次长按开始 drag 会触发
                DragEvent.ACTION_DRAG_STARTED -> CXLogUtil.w(TAG, "dayD ACTION_DRAG_STARTED")
                // 进入 原始 recyclerView 边界会触发, 第一次长按开始 drag 也会触发
                DragEvent.ACTION_DRAG_ENTERED -> CXLogUtil.w(TAG, "dayD ACTION_DRAG_ENTERED")
                // 离开 原始 recyclerView 边界会触发
                DragEvent.ACTION_DRAG_EXITED -> CXLogUtil.w(TAG, "dayD ACTION_DRAG_EXITED")
                // 长按后移动会连续触发
                DragEvent.ACTION_DRAG_LOCATION -> CXLogUtil.d(TAG, "dayD ACTION_DRAG_LOCATION")

                // 松手时如果在 原始 recyclerView 边界内会触发, 边界之外不会触发
                DragEvent.ACTION_DROP -> {
                    dataOriginPosition = -1
                    CXLogUtil.w(TAG, "dayD ACTION_DROP dataOriginPosition=$dataOriginPosition")
                }

                //松手后最后一个事件, 一定会触发
                DragEvent.ACTION_DRAG_ENDED -> {
                    CXLogUtil.w(TAG, "dayD ACTION_DRAG_ENDED dataNewPosition=$dataNewPosition, dataOriginPosition=$dataOriginPosition")
                    if (dataNewPosition == -2 && dataOriginPosition != -1) {
                        dayDetailAdapter.remove(dataOriginPosition)
                        dataNewPosition = -1
                    }
                    dataOriginPosition = -1
                }
                else -> {
                    CXLogUtil.w(TAG, "dayD else ${dragEvent.action}")
                }
            }
            true
        }
    }
}
