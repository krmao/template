package com.smart.template.home.test

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smart.library.base.CXActivity
import com.smart.library.base.CXBaseFragment
import com.smart.library.util.CXLogUtil
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
    private var pageSize = 10
    private fun getDataList(): MutableList<String> {
        val toPageIndex = pageIndex + 1
        val tmpList = ((pageIndex * pageSize) until toPageIndex * pageSize).map { "第 $it 天" }.toMutableList()
        pageIndex = toPageIndex
        return tmpList
    }

    @Suppress("PrivatePropertyName")
    private val adapter: CXRecyclerViewAdapter<String, RecyclerView.ViewHolder> by lazy {
        object : CXRecyclerViewAdapter<String, RecyclerView.ViewHolder>(context, getDataList()) {
            override fun onCreateViewHolder(container: ViewGroup, position: Int): CXViewHolder {
                return CXViewHolder(LayoutInflater.from(context).inflate(R.layout.home_fragment_recycler_view_drag_and_transfer_item_days, container, false))
            }

            override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
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

        val adapterWrapper = CXLoadMoreWrapper(adapter)

        // custom loading views
        adapterWrapper.viewNoMore = adapterWrapper.createDefaultFooterView("-- 呵呵, 真的没有更多了 --")
        adapterWrapper.viewLoadFailure = adapterWrapper.createDefaultFooterView("啊哟, 加载失败了哟")
        adapterWrapper.viewLoading = adapterWrapper.createDefaultFooterView("哼哈, 火速请求中...")

        // onLoadMore listener
        var flag = true
        adapterWrapper.onLoadMoreListener = {
            recyclerView.postDelayed({
                if (flag) {
                    if (adapterWrapper.itemCount >= 30) {
                        adapterWrapper.showNoMore()

                        // test removeAll
                        recyclerView.postDelayed({
                            adapterWrapper.removeAll()

                            // test disable
                            recyclerView.postDelayed({
                                adapterWrapper.enable = false

                                // test add one
                                recyclerView.postDelayed({
                                    adapterWrapper.add("0 test")

                                    // test remove one
                                    recyclerView.postDelayed({
                                        adapterWrapper.remove(0)

                                        // test addAll
                                        recyclerView.postDelayed({
                                            pageIndex = 0
                                            adapterWrapper.add(getDataList())
                                            adapterWrapper.enable = true
                                            adapterWrapper.showLoading()
                                        }, 3000)
                                    }, 3000)
                                }, 3000)
                            }, 3000)
                        }, 3000)

                    } else {
                        adapterWrapper.add(getDataList())
                        // adapterWrapper.showLoading()
                    }
                    if (adapterWrapper.itemCount == 20 + 1) flag = false
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
