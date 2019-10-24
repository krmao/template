package com.smart.library.map.layer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.smart.library.map.R
import com.smart.library.map.model.STMapType
import com.smart.library.util.STSystemUtil
import com.smart.library.util.STToastUtil
import com.smart.library.util.STValueUtil
import com.smart.library.util.STViewUtil
import com.smart.library.widget.recyclerview.STRecyclerViewAdapter
import com.smart.library.widget.recyclerview.helper.STRecyclerViewItemTouchHelperCallback
import kotlinx.android.synthetic.main.st_map_view_control_layout.view.*
import kotlinx.android.synthetic.main.st_map_view_control_layout_users_item.view.*

@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("ViewConstructor")
internal class STPanelView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private var dp1 = STSystemUtil.getPxFromDp(1f).toInt()
    private var dp2 = STSystemUtil.getPxFromDp(2f).toInt()
    private var mapView: STMapView? = null
    private val usersList: MutableList<HashMap<String, String>> = mutableListOf()
    private val usersAdapter: STRecyclerViewAdapter<HashMap<String, String>, STRecyclerViewAdapter.ViewHolder> by lazy {
        object : STRecyclerViewAdapter<HashMap<String, String>, STRecyclerViewAdapter.ViewHolder>(context, usersList) {
            override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
                return ViewHolder(LayoutInflater.from(context).inflate(R.layout.st_map_view_control_layout_users_item, parent, false))
            }

            @SuppressLint("SetTextI18n")
            override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
                val map: HashMap<String, String> = dataList[position]
                viewHolder.itemView.numberTv.text = "$position"
                viewHolder.itemView.nameTv.text = map["name"]
                viewHolder.itemView.addressTv.text = map["address"]
                viewHolder.itemView.addressTv.isSelected = true // 启动跑马灯
                viewHolder.itemView.firstItemTopLine.visibility = if (position == 0) View.VISIBLE else View.GONE
                viewHolder.itemView.itemBottomLine.layoutParams = viewHolder.itemView.itemBottomLine.layoutParams.apply {
                    height = if (position == dataList.size - 1) dp2 else dp1
                }
            }
        }
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.st_map_view_control_layout, this, true)

        clearLocationText()

        usersRecyclerView.adapter = usersAdapter

        // 增加滑动删除与拖动排序功能
        ItemTouchHelper(STRecyclerViewItemTouchHelperCallback(usersAdapter, onDragListener = object : STRecyclerViewItemTouchHelperCallback.OnDragListener {
            override fun onDragBegin(viewHolder: RecyclerView.ViewHolder, actionState: Int) {
            }

            override fun onDragEnd(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            }

        })).attachToRecyclerView(usersRecyclerView)

        resetUsersData()

        hideLoading()

    }

    fun trafficText(): TextView = trafficText
    fun trafficImage(): ImageView = trafficImage
    fun trafficBtn(): View = trafficBtn
    fun settingsBtn(): View = settingsBtn
    fun locationBtn(): View = locationBtn
    fun switchMapButton(): View = switchMapBtn
    fun switchThemeButton(): View = switchThemeBtn

    fun showLoading() {
        poiBtn.isClickable = false
        switchMapButton().isClickable = false
        trafficBtn().isClickable = false
        locationBtn().isClickable = false
        settingsBtn().isClickable = false
        STViewUtil.animateAlphaToVisibility(View.VISIBLE, 300, {}, loadingLayout)
    }

    fun resetUsersData() {
        usersAdapter.add(
                arrayListOf(
                        hashMapOf(
                                "name" to "当前位置",
                                "address" to "沭阳县青少年广场北门"
                        ),
                        hashMapOf(
                                "name" to "小强哥",
                                "address" to "苏州市吴中区太湖东路280号"
                        ),
                        hashMapOf(
                                "name" to "二毛哥",
                                "address" to "苏州市相城区富元路2888号"
                        ),
                        hashMapOf(
                                "name" to "大毛哥",
                                "address" to "上海市长宁区金钟路968号"
                        ),
                        hashMapOf(
                                "name" to "二姨哥",
                                "address" to "苏州市吴江区开平路789号"
                        ),
                        hashMapOf(
                                "name" to "大毛哥",
                                "address" to "上海市长宁区金钟路968号"
                        ),
                        hashMapOf(
                                "name" to "小雨哥",
                                "address" to "上海市松江区仓桥镇三新北路900弄230号"
                        ),
                        hashMapOf(
                                "name" to "回家睡觉",
                                "address" to "上海市闵行区东川路800号"
                        )
                )
        )

    }


    @SuppressLint("SetTextI18n")
    fun clearLocationText() {
        myLocationTV.visibility = View.GONE
        myLocationTV.text = "当前坐标:\n"
        myLocationTV.setOnLongClickListener(null)
    }

    @SuppressLint("SetTextI18n")
    fun setButtonClickedListener(_mapView: STMapView) {
        mapView = _mapView
        setSwitchMapBtnStyle()
        setTrafficBtnStyle()
        setPoiBtnStyle()
        clearLocationText()

        mapView?.setOnLocationChangedListener {
            if (it?.isValid() == true) {
                myLocationTV.visibility = View.VISIBLE

                val label = "当前坐标"
                val contentString = "${it.type}\n${STValueUtil.formatDecimal(it.latitude, 6)}, ${STValueUtil.formatDecimal(it.longitude, 6)}"
                myLocationTV.text = "$label: $contentString"

                myLocationTV.setOnLongClickListener {
                    if (STSystemUtil.copyToClipboard(label, contentString)) {
                        STToastUtil.show("$label 已复制到剪贴板")
                    }
                    true
                }
            }
        }

        switchMapButton().setOnClickListener {
            mapView?.switchTo(if (mapView?.mapType() == STMapType.BAIDU) STMapType.GAODE else STMapType.BAIDU) { _: Boolean, _: STMapType ->
                setSwitchMapBtnStyle()
            }
        }
        switchThemeButton().setOnClickListener {
            mapView?.switchTheme()
        }
        trafficBtn().setOnClickListener {
            setTrafficBtnStyle(!isTrafficEnabled())
        }

        locationBtn.setOnClickListener(mapView?.onLocationButtonClickedListener())
        locationBtn.setOnLongClickListener(mapView?.onLocationButtonLongClickedListener())

        poiBtn.setOnClickListener {
            setPoiBtnStyle(!isShowMapPoi())
        }
    }

    fun isTrafficEnabled(): Boolean = mapView?.isTrafficEnabled() ?: false
    fun isShowMapPoi(): Boolean = mapView?.isShowMapPoi() ?: true

    private fun setPoiBtnStyle(showMapPoi: Boolean = isShowMapPoi()) {
        mapView?.showMapPoi(showMapPoi)
        poiImage.setImageResource(if (showMapPoi) R.drawable.st_poi_on else R.drawable.st_poi)
        poiText.setTextColor(Color.parseColor(if (showMapPoi) "#1296db" else "#000000"))
    }

    private fun setTrafficBtnStyle(isTrafficEnabled: Boolean = isTrafficEnabled()) {
        mapView?.enableTraffic(isTrafficEnabled)
        trafficImage().setImageResource(if (isTrafficEnabled) R.drawable.st_traffic_on else R.drawable.st_traffic)
        trafficText().setTextColor(Color.parseColor(if (isTrafficEnabled) "#1296db" else "#000000"))
    }

    private fun setSwitchMapBtnStyle() {
        switchMapBtnImage.setImageResource(if (mapView?.mapType() == STMapType.BAIDU) R.drawable.st_gaode_on else R.drawable.st_baidu_on)
        switchMapBtnTV.text = if (mapView?.mapType() == STMapType.BAIDU) "切换高德" else "切换百度"
        switchMapBtnTV.setTextColor(Color.parseColor(if (mapView?.mapType() == STMapType.BAIDU) "#1296db" else "#d81e06"))
    }

    fun hideLoading() {
        poiBtn.isClickable = true
        switchMapButton().isClickable = true
        trafficBtn().isClickable = true
        locationBtn().isClickable = true
        settingsBtn().isClickable = true
        STViewUtil.animateAlphaToVisibility(View.GONE, 300, {}, loadingLayout)
    }
}