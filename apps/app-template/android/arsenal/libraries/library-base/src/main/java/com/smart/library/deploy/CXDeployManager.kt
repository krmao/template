package com.smart.library.deploy

import com.smart.library.deploy.model.CXDeployType

/**
 * 动态部署管理器
 *
 *
 * android              hotfix + dynamic deployment(contain hotfix)
 * hybird               hotfix + dynamic deployment(contain hotfix)
 * react-native         hotfix + dynamic deployment(contain hotfix)
 *
 */
object CXDeployManager {

    private val supportTypes: MutableSet<CXDeployType> = mutableSetOf()

    fun initialize(supportTypes: MutableSet<CXDeployType>) {
        this.supportTypes.addAll(supportTypes)
    }

    fun check() {

    }

    fun checkSync() {

    }

}
