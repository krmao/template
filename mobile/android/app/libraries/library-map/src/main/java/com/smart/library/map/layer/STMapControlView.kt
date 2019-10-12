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
internal class STMapControlView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, mapView: STMapView) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.st_map_view_control_layout, this, true)


        switchMapButton().setOnClickListener {
            mapView.switchTo(if (mapView.mapType() == STMapType.BAIDU) STMapType.GAODE else STMapType.BAIDU)
        }
        switchThemeButton().setOnClickListener {
            mapView.switchTheme()
        }

        setLocationBtnListener(mapView.onLocationButtonClickedListener())

        hideLoading()
    }

    fun settingsBtn(): View = settingsBtn
    fun locationBtn(): View = locationBtn
    fun switchMapButton(): View = switchMapBtn
    fun switchThemeButton(): View = switchThemeBtn

    fun setLocationBtnListener(onLocationButtonClickedListener: OnClickListener) {
        locationBtn().setOnClickListener(onLocationButtonClickedListener)
    }

    fun showLoading() {
        switchMapButton().isClickable = false
        locationBtn().isClickable = false
        settingsBtn().isClickable = false
        STViewUtil.animateAlphaToVisibility(View.VISIBLE, 300, {}, loadingLayout)
    }

    fun hideLoading() {
        switchMapButton().isClickable = true
        locationBtn().isClickable = true
        settingsBtn().isClickable = true
        STViewUtil.animateAlphaToVisibility(View.GONE, 300, {}, loadingLayout)
    }
}