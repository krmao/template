import Foundation

public class CXHybirdModuleBundleModel: NSObject {
    public var moduleName: String = ""
    public var moduleConfigList: MutableList<CXHybirdModuleConfigModel> = MutableList<CXHybirdModuleConfigModel>()
    public var moduleNextConfig: CXHybirdModuleConfigModel? = nil

    public init(_ name: String, _ list: MutableList<CXHybirdModuleConfigModel>) {
        self.moduleName = name
        self.moduleConfigList = list
    }

    public init(_ name: String, _ moduleNextConfig: CXHybirdModuleConfigModel?) {
        self.moduleName = name
        self.moduleNextConfig = moduleNextConfig
    }


    public override var description: String {
        return "(name:\(self.moduleName) configSize:\(self.moduleConfigList.size)) nextConfig:\(moduleNextConfig?.moduleName ?? "nil")"
    }

}
