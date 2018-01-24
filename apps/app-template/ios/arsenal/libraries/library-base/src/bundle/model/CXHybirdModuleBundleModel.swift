class CXHybirdModuleBundleModel {
    var moduleName: String
    var moduleConfigList: MutableList<CXHybirdModuleConfigModel> = mutableListOf()
    var moduleNextConfig: CXHybirdModuleConfigModel? = nil
}
