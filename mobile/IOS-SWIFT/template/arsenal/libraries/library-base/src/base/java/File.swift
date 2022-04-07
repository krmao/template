import Foundation

public class File: NSObject {

    private(set) public var absolutePath: String
    private(set) public var path: String
    private(set) public var name: String


    init(_ filePath: String?) throws {
        if (filePath == nil || filePath!.isNullOrBlank()) {
            throw IllegalArgumentException("path is nil")
        }
        self.absolutePath = filePath!
        self.path = self.absolutePath
        self.name = self.absolutePath.components(separatedBy: "/").last ?? ""
    }

    init(_ parentPath: String?, _ name: String) {
        var prefixPath: String = parentPath ?? "."
        prefixPath = prefixPath.endsWith("/") ? prefixPath : (prefixPath + "/")

        absolutePath = prefixPath + name
        path = absolutePath
        self.name = name
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
        return CXFileUtil.isFile(self.path)
    }

    public override var description: String {
        return path ?? "nil"
    }

    public func makeDirs() -> Self {
        CXFileUtil.makeDirs(self.path)
        return self
    }

    public func getChildList(dir: File?) -> List<File> {
        if (self.isDirectory()) {
            return CXFileUtil.getFileList(self)
        } else {
            return List<File>()
        }
    }

}
