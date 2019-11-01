package com.smart.template.home.map.model

interface STIMapModePresenter {
    fun enter()

    fun mapMode(): STMapMode

    fun exit()
}