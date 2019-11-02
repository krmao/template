package com.smart.template.home.map

import com.smart.template.home.map.model.STMapMode
import com.smart.template.home.map.presenter.STMapCityPresenter
import com.smart.template.home.map.presenter.STMapMultiHorizontalPresenter
import com.smart.template.home.map.layer.STMapLayerManager

class STMapActivity {

    private val targetMode: STMapMode = STMapMode.CITY

    init {

        if (targetMode == STMapMode.CITY) {
            STMapLayerManager.push(STMapCityPresenter())
            STMapLayerManager.push(STMapMultiHorizontalPresenter())
        }


    }
}