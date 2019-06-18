import Foundation

class CXFileUtil {
    static let TAG = "[file]"

    //应该将所有的应用程序数据文件写入到这个目录下。这个目录用于存储用户数据。该路径可通过配置实现iTunes共享文件。可被iTunes备份。
    static let docDir: String = (NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true) as NSArray)[0] as! String + "/"

    //这个目录下有两个子目录：
    //Preferences 目录：包含应用程序的偏好设置文件。您不应该直接创建偏好设置文件，而是应该使用NSUserDefaults类来取得和设置应用程序的偏好.
    //Caches 目录：用于存放应用程序专用的支持文件，保存应用程序再次启动过程中需要的信息。
    //可创建子文件夹。可以用来放置您希望被备份但不希望被用户看到的数据。该路径下的文件夹，除Caches以外，都会被iTunes备份。
    static let libDir: String = (NSSearchPathForDirectoriesInDomains(.libraryDirectory, .userDomainMask, true) as NSArray)[0] as! String + "/"

    //用于存放应用程序专用的支持文件，保存应用程序再次启动过程中需要的信息。
    static let cacheDir: String = (NSSearchPathForDirectoriesInDomains(.cachesDirectory, .userDomainMask, true) as NSArray)[0] as! String + "/"

    //这个目录用于存放临时文件，保存应用程序再次启动过程中不需要的信息。该路径下的文件不会被iTunes备份。
    //尾部包含 '/'
    static let tmpDir: String = NSTemporaryDirectory()

    //获取沙盒主目录路径
    static let homeDir: String = NSHomeDirectory()

    static func getFilePathFromResource(_  fileFullName: String?) -> String? {
        return Bundle.main.path(forResource: fileFullName, ofType: nil)
    }

    static func getFileURLFromResource(_  fileFullName: String?) -> URL? {
        return Bundle.main.url(forResource: fileFullName, withExtension: nil)
    }

    static func getFileContentFromResource(_  fileFullName: String?) -> String {
        return CXFileUtil.readTextFromFile(getFilePathFromResource(fileFullName))
    }

    static func getTempFile(_ name: String) -> File {
        return File(CXFileUtil.tmpDir, name)
    }

    static func deleteFile(_ file: File?) {
        if (file == nil) {
            return
        }
        do {
            try FileManager.default.removeItem(atPath: file!.path)
        } catch {
            CXLogUtil.d(":deleteFile", "[文件删除失败] path:\(file!.path), fileExists:" + "\(CXFileUtil.fileExists(file!.path))", error)
        }
    }

    public static func isDirectory(_ path: String?) -> Bool {
        if (path == nil || path!.isNullOrBlank()) {
            return false
        }

        let fileManager = FileManager.default
        var isDir: ObjCBool = false
        if fileManager.fileExists(atPath: path!, isDirectory: &isDir) {
            if isDir.boolValue {
                // file exists and is a directory
                return true
            } else {
                // file exists and is not a directory
            }
        } else {
            // file does not exist
        }
        return false
    }

    public static func isFile(_ path: String?) -> Bool {
        if (path == nil || path!.isNullOrBlank()) {
            return false
        }

        let fileManager = FileManager.default
        var isDir: ObjCBool = false
        if fileManager.fileExists(atPath: path!, isDirectory: &isDir) {
            if isDir.boolValue {
                // file exists and is a directory
            } else {
                // file exists and is not a directory
                return true
            }
        } else {
            // file does not exist
        }
        return false
    }

    static func deleteFile(_ path: String?) {
        do {
            try FileManager.default.removeItem(atPath: path!)
            CXLogUtil.d(":deleteFile", "[文件删除成功] path:" + path! + " , fileExists:" + "\(CXFileUtil.fileExists(path))")
        } catch {
            CXLogUtil.d(":deleteFile", "[文件删除失败] path:" + path! + " , fileExists:" + "\(CXFileUtil.fileExists(path))", error)
        }
    }

    static func deleteDirectory(_ path: String?) {
        do {
            try FileManager.default.removeItem(atPath: path!)
            CXLogUtil.d(":deleteDirectory", "[目录删除成功] path:" + path! + " , fileExists:" + "\(CXFileUtil.fileExists(path))")
        } catch {
            CXLogUtil.d(":deleteDirectory", "[目录删除失败] path:" + path! + " , fileExists:" + "\(CXFileUtil.fileExists(path))", error)
        }
        CXFileUtil.makeDirs(path)
    }

    static func deleteDirectory(_ dir: File?) {
        deleteDirectory(dir?.path)
    }

    static func makeDirs(_ path: String?) -> Bool {
        do {
            try FileManager.default.createDirectory(atPath: path!, withIntermediateDirectories: true, attributes: nil)
            return true
        } catch {
            CXLogUtil.e("makeDirs:[路径创建失败] path:\(path) fileExists:" + "\(CXFileUtil.fileExists(path))", error)
        }
        return false
    }

    static func copy(_ fromFilePath: String, _ toFile: File?) throws {
        return try copy(fromFilePath, toFile?.path)
    }

    static func copy(_ fromFilePath: String, _ toFilePath: String?) throws {
        if (toFilePath != nil && toFilePath!.isNotBlank()) {
            try FileManager.default.copyItem(atPath: fromFilePath, toPath: toFilePath!)
        }
    }

    static func readTextFromFile(_ filePath: String?) -> String {
        var content: String = ""
        if (filePath != nil && filePath!.isNotEmpty()) {
            do {
                content = try String(contentsOfFile: filePath!, encoding: .utf8)
            } catch {
                CXLogUtil.e("readTextFromFile failure", error)
            }
        }
        return content
    }

    static func printDirs(_ dirFile: File?) {
        printDirs(dirFile?.path)
    }

    static func printDirs(_ dirPath: String?) {
        if (dirPath == nil && dirPath!.isNullOrBlank()) {
            CXLogUtil.v(":printDirs ============================================")
            CXLogUtil.v(":printDirs error dirPath :\(dirPath)")
            CXLogUtil.v(":printDirs ============================================")
            return
        }
        let files = FileManager.default.subpaths(atPath: dirPath!)

        CXLogUtil.v(":printDirs ============================================")
        CXLogUtil.v(":printDirs dirPath:\(dirPath)")
        CXLogUtil.v(":printDirs ============================================")
        if (files != nil) {
            for file in files! {
                CXLogUtil.v(":printDirs file:\(file)")
            }
        }
        CXLogUtil.v(":printDirs ============================================")
    }

    static func fileExists(_ path: String?) -> Bool {
        return path != nil && path!.isNotBlank() && FileManager.default.fileExists(atPath: path!)
    }


    static func getFileList(_ dirPath: String?) -> List<File> {
        var fileList = List<File>()
        if (dirPath == nil && dirPath!.isNullOrBlank()) {
            CXLogUtil.v("getFileList dirPath is nil or empty dirPath:\(dirPath)")
            return fileList
        }
        let childFiles = FileManager.default.subpaths(atPath: dirPath!)
        if (childFiles != nil) {
            for childFilePath in childFiles! {
                if let childFile = try? File(dirPath! + "/" + childFilePath) {
                    if (childFile.isFile()) {
                        fileList.add(childFile)
                    }
                }
            }
        }
        return fileList
    }

    static func getFileList(_ dir: File?) -> List<File> {
        return getFileList(dir?.path)
    }
}
