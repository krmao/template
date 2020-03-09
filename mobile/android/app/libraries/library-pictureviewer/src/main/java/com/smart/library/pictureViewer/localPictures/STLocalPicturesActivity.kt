package com.smart.library.pictureviewer.localpictures

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.smart.library.base.STBaseActivity
import com.smart.library.util.STStorageUtil
import com.smart.library.util.STStorageUtil.LocalPictureTimeSlotInfo.Companion.DATA_TYPE_CONTENT
import com.smart.library.util.STToastUtil
import com.smart.library.pictureviewer.R
import java.util.*

class STLocalPicturesActivity : STBaseActivity(), STLocalPicturesContract.View {
    private val columns = 3

    private val presenter: STLocalPicturesPresenter by lazy { STLocalPicturesPresenter(this) }

    private val refreshLayout: SwipeRefreshLayout by lazy { findViewById<SwipeRefreshLayout>(R.id.refreshLayout) }
    private val recyclerView: RecyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerView) }
    private val stickyHeaderView: LinearLayout by lazy { findViewById<LinearLayout>(R.id.stickyHeaderView) }
    private val adapter: STLocalPicturesLocalPicturesAdapter by lazy { STLocalPicturesLocalPicturesAdapter(this, presenter.getLocalPictureDataList(), columns) }
    private val layoutManager: StaggeredGridLayoutManager by lazy { StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.st_local_pictures_activity)
        refreshLayout.isRefreshing = true
        refreshLayout.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                presenter.loadLocalPicture()
            }
        })
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(onScrollListener)
        adapter.setOnItemClickListener(object : STLocalPicturesLocalPicturesAdapter.OnRecyclerViewItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                if (presenter.getLocalPictureDataList().size > position && position >= 0) {
                    val localPictureTimeSlotInfo: STStorageUtil.LocalPictureTimeSlotInfo? = presenter.getLocalPictureDataList()[position]
                    if (localPictureTimeSlotInfo != null && localPictureTimeSlotInfo.dataType == DATA_TYPE_CONTENT) {
                        STToastUtil.show("$position , ${localPictureTimeSlotInfo.pictureInfo?.path}")
                    }
                }
            }

        })
        recyclerView.adapter = adapter
        presenter.loadLocalPicture()
    }

    override fun refreshLocalPictures(scanLocalDataResult: STStorageUtil.LocalPictureTimeSlotResult?) {
        adapter.setImagePathList(scanLocalDataResult?.localPictureInfoList ?: ArrayList())
        adapter.notifyDataSetChanged()
        refreshLayout.isRefreshing = false
    }

    private val onScrollListener: RecyclerView.OnScrollListener = object : RecyclerView.OnScrollListener() {
        // get the recyclerView first visible item's position,return the size same as span count
        var firstVisiblePosition = IntArray(columns)
        // get the recyclerView last visible item's position,return the size same as span count
        var lastVisiblePosition = IntArray(columns)
        // sticky head view height
        private var stickyHeadHeight = 0

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy == 0) { // the first time show the recyclerView items
                layoutManager.findFirstVisibleItemPositions(firstVisiblePosition)
                val titlePosition = getMinVisiblePosition(firstVisiblePosition)
                if (titlePosition >= 0) { // get the item object
                    val slotInfo: STStorageUtil.LocalPictureTimeSlotInfo? = adapter.getItemObject(titlePosition)
                    if (slotInfo != null) { // set the first title view text content
                        (stickyHeaderView.getChildAt(0) as TextView).text = slotInfo.dataTitle
                        stickyHeaderView.visibility = View.VISIBLE
                        // set the current title position into view tag object
                        stickyHeaderView.tag = titlePosition
                        // retrive the title view height,and set into variable
                        stickyHeadHeight = stickyHeaderView.measuredHeight
                    }
                }
            } else if (dy != 0) { //  pull down the recyclerView then dy<0 , pull up the recyclerView then dy>0
                layoutManager.findFirstVisibleItemPositions(firstVisiblePosition)
                val minVisiblePosition = getMinVisiblePosition(firstVisiblePosition)
                layoutManager.findLastVisibleItemPositions(lastVisiblePosition)
                val maxVisiblePosition = getMaxVisiblePosition(lastVisiblePosition)
                if (minVisiblePosition < 0) {
                    return
                }
                /**
                 * get one title position before current minimum visible item position
                 */
                val beforeFirstItemTitlePosition = presenter.findBeforeTitlePosition(minVisiblePosition)
                /**
                 * get the title position after current minimum visible item position
                 */
                val afterFirstItemTitlePosition = presenter.findAfterTitlePosition(minVisiblePosition)
                // when next title item position after the current minimum visible exist,and not equals current item position
                if (afterFirstItemTitlePosition != Int.MIN_VALUE && afterFirstItemTitlePosition != minVisiblePosition) { // determine whether next title item after the current minimum visible position is visible in the recyclerView
                    if (afterFirstItemTitlePosition <= maxVisiblePosition) { // it means that next title item is visible in the recyclerView now
// find that suitable next title view
                        val nextTitleView = findView(afterFirstItemTitlePosition)
                        if (nextTitleView != null) {
                            val yxis = nextTitleView.y
                            // if next title view is scroll into first title view's area
                            if (yxis <= stickyHeadHeight) { // then fix the first title scroll y
                                stickyHeaderView.scrollTo(0, (stickyHeadHeight - yxis).toInt())
                            } else { // others,next title away from the first title view
// then fix first title view scroll y
                                stickyHeaderView.scrollTo(0, 0)
                            }
                            // set visible to the header view always
                            stickyHeaderView.visibility = View.VISIBLE
                        }
                    }
                }
                /**
                 * Determine whether need to change the title
                 */
// when title item before the current maxinum visible position exist,and position not equals the current header view's tag
                if (beforeFirstItemTitlePosition != Int.MIN_VALUE && stickyHeaderView.tag != null && stickyHeaderView.tag as Int != beforeFirstItemTitlePosition) {
                    /**
                     * it means that should change the title content
                     */
                    (stickyHeaderView.getChildAt(0) as TextView).text = ""
                    // always show the title item that before the current maxinum visible position
                    val wpdi: STStorageUtil.LocalPictureTimeSlotInfo? = presenter.getLocalPictureDataList()[beforeFirstItemTitlePosition]
                    // set new title content
                    (stickyHeaderView.getChildAt(0) as TextView).text = wpdi?.dataTitle
                    if (dy > 0) { // if user pull up the recyclerView,fix the scroll y.Sometimes,because of the next title item may be not visible in current recyclerView,
// so,we should force set header view visible once again
                        stickyHeaderView.visibility = View.VISIBLE
                        stickyHeaderView.scrollTo(0, 0)
                    }
                    // set title position in the tag
                    stickyHeaderView.tag = beforeFirstItemTitlePosition
                    // retrive the sticky head height
                    stickyHeadHeight = stickyHeaderView.measuredHeight
                }
            }
        }

        fun findView(position: Int): View? {
            return layoutManager.findViewByPosition(position)
        }

        fun getMinVisiblePosition(firstVisiblePosition: IntArray?): Int {
            var minValue = Int.MAX_VALUE
            if (firstVisiblePosition != null) {
                for (i in firstVisiblePosition.indices) {
                    minValue = if (firstVisiblePosition[i] < minValue) firstVisiblePosition[i] else minValue
                }
            }
            return minValue
        }

        fun getMaxVisiblePosition(lastVisiblePosition: IntArray?): Int {
            var maxValue = Int.MIN_VALUE
            if (lastVisiblePosition != null) {
                for (i in lastVisiblePosition.indices) {
                    maxValue = if (lastVisiblePosition[i] > maxValue) lastVisiblePosition[i] else maxValue
                }
            }
            return maxValue
        }
    }
}