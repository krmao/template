package com.smart.template.home.test

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXActivity
import com.smart.library.base.CXBaseFragment
import com.smart.library.widget.recyclerview.CXRecyclerViewAdapter
import com.smart.library.widget.recyclerview.CXRecyclerViewItemDecoration
import com.smart.template.R
import com.smart.library.widget.recyclerview.snap.CXSnapGravityHelper
import kotlinx.android.synthetic.main.home_fragment_recycler_view_drag_and_transfer_item_days.view.*
import kotlinx.android.synthetic.main.home_recycler_view_snap_top.*

@Suppress("DEPRECATION")
class RecyclerViewSnapTopFragment : CXBaseFragment() {

    companion object {
        fun goTo(context: Context?) {
            CXActivity.start(context, RecyclerViewSnapTopFragment::class.java)
        }
    }

    private var pageIndex = 0
    private var pageSize = 10
    private fun getDataList(): MutableList<String> {
        val toPageIndex = pageIndex + 1
        val tmpList = ((pageIndex * pageSize)..(toPageIndex * pageSize)).map { "第 $it 天" }.toMutableList()
        pageIndex = toPageIndex
        return tmpList
    }

    @Suppress("PrivatePropertyName")
    private val adapter by lazy {
        object : CXRecyclerViewAdapter<String, CXRecyclerViewAdapter.ViewHolder>(context, getDataList()) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_fragment_recycler_view_drag_and_transfer_item_days, container, false))
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
                viewHolder.itemView.textViewDays.text = dataList[position]
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.home_recycler_view_snap_top, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.addItemDecoration(CXRecyclerViewItemDecoration(5))
        recyclerView.adapter = adapter
        // LinearSnapHelper().attachToRecyclerView(recyclerView)

        CXSnapGravityHelper(
                Gravity.TOP,
                false,
                object : CXSnapGravityHelper.SnapListener {
                    override fun onSnap(position: Int) {
                        Log.d("Snapped", position.toString() + "")
                    }
                }
        ).attachToRecyclerView(recyclerView)
    }
}
