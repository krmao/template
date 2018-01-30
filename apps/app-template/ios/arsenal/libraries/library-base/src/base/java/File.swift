import Foundation

class File {

    var absolutePath: String?
    var path: String?

    init(_ path: String) {
        self.absolutePath = path
    }

    init(_ parent: File, _ name: String) {
        self.absolutePath = parent.path + name
    }

    func exists() -> Bool {
        return CXFileUtil.fileExists(absolutePath)
    }

}
