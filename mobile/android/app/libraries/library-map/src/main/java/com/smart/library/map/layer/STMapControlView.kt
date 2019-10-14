package com.smart.library.map.layer

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.smart.library.map.R
import com.smart.library.map.model.STMapType
import com.smart.library.util.STViewUtil
import kotlinx.android.synthetic.main.st_map_view_control_layout.view.*

@Suppress("MemberVisibilityCanBePrivate")
@SuppressLint("ViewConstructor")
internal class STMapControlView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {

    private var mapView: STMapView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.st_map_view_control_layout, this, true)
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
        switchMapButton().isClickable = false
        trafficBtn().isClickable = false
        locationBtn().isClickable = false
        settingsBtn().isClickable = false
        STViewUtil.animateAlphaToVisibility(View.VISIBLE, 300, {}, loadingLayout)
    }

    fun setButtonClickedListener(_mapView: STMapView) {
        mapView = _mapView
        setSwitchMapBtnStyle()
        setTrafficBtnStyle()

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
    }

    fun isTrafficEnabled(): Boolean = mapView?.isTrafficEnabled() ?: false

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
        switchMapButton().isClickable = true
        trafficBtn().isClickable = true
        locationBtn().isClickable = true
        settingsBtn().isClickable = true
        STViewUtil.animateAlphaToVisibility(View.GONE, 300, {}, loadingLayout)
    }
}