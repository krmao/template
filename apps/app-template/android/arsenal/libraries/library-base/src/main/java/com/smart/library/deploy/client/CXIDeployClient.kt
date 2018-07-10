package com.smart.library.deploy.client

interface CXIDeployClient {
    fun check(url: String?, callback: (() -> Unit?)? = null)
    fun isModuleOpened(moduleName: String?): Boolean
    fun isEnableBSDiffPatch(): Boolean
    fun getModule(moduleName: String?)
}