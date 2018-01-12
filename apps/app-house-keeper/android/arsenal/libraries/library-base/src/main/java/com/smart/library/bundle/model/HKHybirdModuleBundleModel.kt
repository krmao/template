package com.smart.library.bundle.model

data class HKHybirdModuleBundleModel(
    var moduleName: String,
    var moduleConfigList: MutableList<HKHybirdModuleConfigModel> = mutableListOf(),
    var moduleNextConfig: HKHybirdModuleConfigModel? = null
)
