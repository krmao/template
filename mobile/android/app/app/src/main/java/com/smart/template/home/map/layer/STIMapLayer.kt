package com.smart.template.home.map.layer

interface STIMapLayer {
    fun onCreate()

    fun layerType(): STMapLayerType

    fun onResume()

    fun onPause()

    fun onDestroy()
}