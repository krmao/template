import Foundation

class CXCacheManager {
    static func getChildCacheDir(_ name: String) -> File? {
        return File(CXFileUtil.docDir + name)
    }
}