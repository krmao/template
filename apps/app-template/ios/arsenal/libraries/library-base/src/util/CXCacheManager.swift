import Foundation

class CXCacheManager {
    static func getChildCacheDir(_ name: String) -> File? {
        CXLogUtil.w("getChildCacheDir start")
        let file = File(CXFileUtil.docDir, name)
//        if (!file.exists()) {
            file.makeDirs()
//        }
        CXLogUtil.w("getChildCacheDir end")
        return file
    }
}