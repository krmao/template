package com.smart.library.map.layer

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.smart.library.map.R
import com.smart.library.map.model.STMapType
import kotlinx.android.synthetic.main.st_map_view_control_layout.view.*


@SuppressLint("ViewConstructor")
internal class STMapControlView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, mapView: STMapView) : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.st_map_view_control_layout, this, true)
        locationBtn.setOnClickListener(mapView.onLocationButtonClickedListener())

        toggleBtn.setOnClickListener {
            mapView.switchTo(if (mapView.mapType() == STMapType.BAIDU) STMapType.GAODE else STMapType.BAIDU)
        }
    }
}