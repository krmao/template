package com.smart.template.home.map.layer

interface STIMapLayer {
    fun enter()

    fun layer(): STMapLayerType

    fun onResume()

    fun onPause()

    fun exit()
}