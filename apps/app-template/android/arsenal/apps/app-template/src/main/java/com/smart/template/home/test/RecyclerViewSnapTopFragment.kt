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
import com.smart.library.widget.recyclerview.CXLoadMoreWrapper
import com.smart.library.widget.recyclerview.CXRecyclerViewAdapter
import com.smart.library.widget.recyclerview.CXRecyclerViewItemDecoration
import com.smart.library.widget.recyclerview.CXViewHolder
import com.smart.library.widget.recyclerview.snap.CXSnapGravityHelper
import com.smart.template.R
import kotlinx.android.synthetic.main.home_fragment_recycler_view_drag_and_transfer_item_days.view.*
import kotlinx.android.synthetic.main.home_recycler_view_snap_top.*

@Suppress("DEPRECATION")
class RecyclerViewSnapTopFragment : CXBaseFragment() {

    companion object {
        fun goTo(context: Context?) {
            CXActivity.start(context, RecyclerViewSnapTopFragment::class.java)
        }
    }

    private var flag = true
    private var pageIndex = 0
    private var pageSize = 10
    private fun getDataList(): MutableList<String> {
        val toPageIndex = pageIndex + 1
        val tmpList = ((pageIndex * pageSize) until toPageIndex * pageSize).map { "第 $it 天" }.toMutableList()
        pageIndex = toPageIndex
        return tmpList
    }

    @Suppress("PrivatePropertyName")
    private val adapter: CXRecyclerViewAdapter<String, CXViewHolder> by lazy {
        object : CXRecyclerViewAdapter<String, CXViewHolder>(context, getDataList()) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): CXViewHolder {
                return CXViewHolder(LayoutInflater.from(context).inflate(R.layout.home_fragment_recycler_view_drag_and_transfer_item_days, container, false))
            }

            override fun onBindViewHolder(viewHolder: CXViewHolder, position: Int) {
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

        val adapterWrapper = CXLoadMoreWrapper<String, CXViewHolder>(context, adapter)
        adapterWrapper.setOnLoadListener(object : CXLoadMoreWrapper.OnLoadListener {
            override fun onRetry() {
                onLoadMore()
            }

            override fun onLoadMore() {
                recyclerView.postDelayed({
                    if (flag) {
                        adapterWrapper.add(getDataList())
                        if (adapterWrapper.itemCount >= 40) {
                            adapterWrapper.showNoMoreView()
                        } else {
                            adapterWrapper.showLoading()
                        }
                        flag = false
                    } else {
                        adapterWrapper.showLoadFailure()
                        flag = true
                    }
                }, 2000)
            }
        })

        recyclerView.adapter = adapterWrapper

        // LinearSnapHelper().attachToRecyclerView(recyclerView)

        CXSnapGravityHelper(
                Gravity.TOP,
                false,
                true,
                object : CXSnapGravityHelper.SnapListener {
                    override fun onSnap(position: Int) {
                        Log.d("Snapped", position.toString() + "")
                    }
                }
        ).attachToRecyclerView(recyclerView)

    }
}
