package com.smart.template.home.map

import com.smart.template.home.map.layer.STMapLayerManager
import com.smart.template.home.map.layer.STMapLayerType
import com.smart.template.home.map.layer.impl.STMapLayerCity

class STMapActivity {
    private val layerType: STMapLayerType = STMapLayerType.CITY

    init {
        if (layerType == STMapLayerType.CITY) {
            STMapLayerManager.push(STMapLayerCity())
        }
    }
}