package com.smart.library.map.layer

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
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

    fun settingsBtn(): View = settingsBtn
    fun locationBtn(): View = locationBtn
    fun switchMapButton(): View = switchMapBtn
    fun switchThemeButton(): View = switchThemeBtn

    fun showLoading() {
        switchMapButton().isClickable = false
        locationBtn().isClickable = false
        settingsBtn().isClickable = false
        STViewUtil.animateAlphaToVisibility(View.VISIBLE, 300, {}, loadingLayout)
    }

    fun setButtonClickedListener(_mapView: STMapView) {
        mapView = _mapView
        setSwitchMapBtnText()

        switchMapButton().setOnClickListener {
            mapView?.switchTo(if (mapView?.mapType() == STMapType.BAIDU) STMapType.GAODE else STMapType.BAIDU) { _: Boolean, _: STMapType ->
                setSwitchMapBtnText()
            }
        }
        switchThemeButton().setOnClickListener {
            mapView?.switchTheme()
        }

        locationBtn.setOnClickListener(mapView?.onLocationButtonClickedListener())
        locationBtn.setOnLongClickListener(mapView?.onLocationButtonLongClickedListener())
    }

    private fun setSwitchMapBtnText() {
        switchMapBtnTV.text = if (mapView?.mapType() == STMapType.BAIDU) "切换高德" else "切换百度"
    }

    fun hideLoading() {
        switchMapButton().isClickable = true
        locationBtn().isClickable = true
        settingsBtn().isClickable = true
        STViewUtil.animateAlphaToVisibility(View.GONE, 300, {}, loadingLayout)
    }
}