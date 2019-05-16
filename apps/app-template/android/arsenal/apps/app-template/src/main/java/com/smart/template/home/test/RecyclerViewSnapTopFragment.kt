package com.smart.template.home.test

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXActivity
import com.smart.library.base.CXBaseFragment
import com.smart.library.util.CXLogUtil
import com.smart.library.util.CXSystemUtil
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

    private var pageIndex = 0
    private var pageSize = 6
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

        // divider between items
        recyclerView.addItemDecoration(CXRecyclerViewItemDecoration(5))

        val adapterWrapper = CXLoadMoreWrapper<String, CXViewHolder>(context, adapter)

        // custom loading views
        adapterWrapper.viewNoMore = CXLoadMoreWrapper.createDefaultFooterView(context, "呵呵, 真的没有更多了", CXSystemUtil.getPxFromDp(40f).toInt(), Color.DKGRAY)
        adapterWrapper.viewLoadFailure = CXLoadMoreWrapper.createDefaultFooterView(context, "呵呵, 加载失败了哟", CXSystemUtil.getPxFromDp(40f).toInt())
        adapterWrapper.viewLoading = CXLoadMoreWrapper.createDefaultFooterView(context, "呵呵, 火速请求中...", CXSystemUtil.getPxFromDp(40f).toInt())

        // onLoadMore listener
        var flag = true
        adapterWrapper.setOnLoadMoreListener {
            recyclerView.postDelayed({
                if (flag) {
                    adapterWrapper.add(getDataList())
                    if (adapterWrapper.itemCount >= 20) {
                        adapterWrapper.showNoMore()
                    } else {
                        adapterWrapper.showLoading()
                    }
                    flag = false
                } else {
                    adapterWrapper.showLoadFailure()
                    flag = true
                }
            }, 1000)
        }

        recyclerView.adapter = adapterWrapper

        // gravity snap
        CXSnapGravityHelper(
                Gravity.TOP,
                object : CXSnapGravityHelper.SnapListener {
                    override fun onSnap(position: Int) {
                        CXLogUtil.e("Snapped", position.toString())
                    }
                },
                debug = true
        ).attachToRecyclerView(recyclerView)

    }
}
