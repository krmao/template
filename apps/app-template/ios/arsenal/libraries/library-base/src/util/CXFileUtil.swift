import Foundation

class CXFileUtil {
    static let TAG = "[file]"

    static let docDir: String = (NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true) as NSArray)[0] as! String + "/"

    static func deleteFile(_ file: File?) {
        if (file == nil) {
            return
        }
        do {
            try! FileManager.default.removeItem(atPath: file!.path)
            CXLogUtil.d(":deleteFile", "[文件删除成功] path:\(file!.path), fileExists:" + "\(CXFileUtil.fileExists(file!.path))")
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
        CXLogUtil.d("makeDirs:start")
        do {
            try FileManager.default.createDirectory(atPath: path!, withIntermediateDirectories: true, attributes: nil)
            CXLogUtil.d("makeDirs:[路径创建成功] path:\(path) fileExists:" + "\(CXFileUtil.fileExists(path))")
            return true
        } catch {
            CXLogUtil.d("makeDirs:[路径创建失败] path:\(path) fileExists:" + "\(CXFileUtil.fileExists(path))", error)
        }
        return false
    }

    static func copy(_ fromFilePath: String, _ toFile: File?) throws {
        return try copy(fromFilePath, toFile?.path)
    }

    static func copy(_ fromFilePath: String, _ toFilePath: String?) throws {
        CXLogUtil.d(":copy-start from: \(fromFilePath) fileExists:\(CXFileUtil.fileExists(fromFilePath))")
        if (toFilePath != nil && toFilePath!.isNotBlank()) {
            try FileManager.default.copyItem(atPath: fromFilePath, toPath: toFilePath!)
        }
        CXLogUtil.d(":copy-end to:\(toFilePath) fileExists:\(CXFileUtil.fileExists(toFilePath))")
    }

    static func readTextFromFile(_ filePath: String?) -> String {
        CXLogUtil.i("readTextFromFile start -> \(filePath)")
        var content: String = ""
        if (filePath != nil && filePath!.isNotEmpty()) {
            do {
                content = try String(contentsOfFile: filePath!, encoding: .utf8)
                CXLogUtil.i("readTextFromFile success ->")
            } catch {
                CXLogUtil.e("readTextFromFile failure", error)
            }
        }
        CXLogUtil.i("readTextFromFile end   <-")
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
}
