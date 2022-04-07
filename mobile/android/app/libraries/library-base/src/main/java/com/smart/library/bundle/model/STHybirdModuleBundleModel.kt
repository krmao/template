package com.smart.library.bundle.model

import androidx.annotation.Keep

//@Keep
data class STHybirdModuleBundleModel(
    var moduleName: String,
    var moduleConfigList: MutableList<STHybirdModuleConfigModel> = mutableListOf(),
    var moduleNextConfig: STHybirdModuleConfigModel? = null
)
