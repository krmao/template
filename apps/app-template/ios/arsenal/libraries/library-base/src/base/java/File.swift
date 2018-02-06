import Foundation

public class File: NSObject {

    private(set) public var absolutePath: String
    private(set) public var path: String

    init(_ filePath: String?) throws {
        if (filePath == nil || filePath!.isNullOrBlank()) {
            throw IllegalArgumentException("path is nil")
        }
        self.absolutePath = filePath!
        self.path = self.absolutePath
    }

    init(_ parentPath: String?, _ name: String) {
        var prefixPath: String = (parentPath ?? ".")
        prefixPath = prefixPath.endsWith("/") ? prefixPath : (prefixPath + "/")

        absolutePath = prefixPath + name
        path = absolutePath
    }

    convenience init(_ parent: File?, _ name: String) {
        self.init(parent?.path, name)
    }

    public func exists() -> Bool {
        return CXFileUtil.fileExists(path)
    }

    public func isDirectory() -> Bool {
        return CXFileUtil.isDirectory(self.path)
    }

    public func isFile() -> Bool {
        return CXFileUtil.isDirectory(self.path)
    }

    public override var description: String {
        return path ?? "nil"
    }

    public func makeDirs() -> Self {
        CXFileUtil.makeDirs(self.path)
        return self
    }

}
