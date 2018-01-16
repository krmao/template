package com.smart.library.bundle.model

data class CXHybirdModuleBundleModel(
    var moduleName: String,
    var moduleConfigList: MutableList<CXHybirdModuleConfigModel> = mutableListOf(),
    var moduleNextConfig: CXHybirdModuleConfigModel? = null
)
