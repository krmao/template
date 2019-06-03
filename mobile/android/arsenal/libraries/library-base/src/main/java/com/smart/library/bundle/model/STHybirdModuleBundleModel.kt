package com.smart.library.bundle.model

data class STHybirdModuleBundleModel(
    var moduleName: String,
    var moduleConfigList: MutableList<STHybirdModuleConfigModel> = mutableListOf(),
    var moduleNextConfig: STHybirdModuleConfigModel? = null
)
