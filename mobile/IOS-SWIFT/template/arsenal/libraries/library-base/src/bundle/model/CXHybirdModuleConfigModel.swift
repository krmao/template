import Foundation

public class CXHybirdModuleConfigModel: NSObject, Codable {
    public var moduleName: String = ""
    public var moduleVersion: String = "" //只分当前版本与线上最新版本
    public var moduleDebug: Bool = false //只下发到测试机
    public var moduleUpdateStrategy: CXHybirdUpdateStrategy = CXHybirdUpdateStrategy.ONLINE
    public var moduleMainUrl: String = ""
    public var moduleConfigUrl: String = ""
    public var moduleDownloadUrl: String = ""
    public var moduleZipMd5: String = ""
    public var moduleFilesMd5: MutableMap<String, String> = MutableMap<String, String>()

    func equals(other: CXHybirdModuleConfigModel?) -> Bool {
        return moduleVersion == other?.moduleVersion && moduleName == other?.moduleName
    }

    static var invalidConfigModel: CXHybirdModuleConfigModel = CXHybirdModuleConfigModel()

    public override func isEqual(_ object: Any?) -> Bool {
        guard let obj = object as? CXHybirdModuleConfigModel else {
            return false
        }
        return self.moduleVersion == obj.moduleVersion && self.moduleName == obj.moduleName
    }

    //MARK: - Equatable
    /*public static func ==(lhs: CXHybirdModuleConfigModel, rhs: CXHybirdModuleConfigModel) -> Bool {
        CXLogUtil.e("== 比较操作")
        return lhs.moduleVersion == rhs.moduleVersion && lhs.moduleName == rhs.moduleName
    }*/

    public override var description: String {
        return """
            \n
            --------
            moduleName: \(self.moduleName)
            moduleVersion: \(self.moduleVersion)
            moduleDebug: \(self.moduleDebug)
            moduleUpdateStrategy: \(self.moduleUpdateStrategy)
            moduleMainUrl: \(self.moduleMainUrl)
            moduleConfigUrl: \(self.moduleConfigUrl)
            moduleDownloadUrl: \(self.moduleDownloadUrl)
            moduleZipMd5: \(self.moduleZipMd5)
            moduleFilesMd5: \(self.moduleFilesMd5)
            \n
        """
    }
}
