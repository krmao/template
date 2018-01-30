class CXHybirdModuleBundleModel: NSObject {
    var moduleName: String = ""
    var moduleConfigList: MutableList<CXHybirdModuleConfigModel> = MutableList<CXHybirdModuleConfigModel>()
    var moduleNextConfig: CXHybirdModuleConfigModel? = nil

    public init(_ name: String, _ list: MutableList<CXHybirdModuleConfigModel>) {
        self.moduleName = name
        self.moduleConfigList = list
    }

    public init(_ name: String, _ moduleNextConfig: CXHybirdModuleConfigModel?) {
        self.moduleName = name
        self.moduleNextConfig = moduleNextConfig
    }
}
