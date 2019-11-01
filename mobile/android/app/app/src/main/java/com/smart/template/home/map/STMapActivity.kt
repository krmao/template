package com.smart.template.home.map

import com.smart.template.home.map.model.STMapMode
import com.smart.template.home.map.presenter.STMapCityPresenter
import com.smart.template.home.map.util.STMapModeStackManager

class STMapActivity {

    private val targetMode: STMapMode = STMapMode.CITY

    init {

        if (targetMode == STMapMode.CITY) {
            STMapModeStackManager.pushMapMode(STMapCityPresenter())
        }


    }
}