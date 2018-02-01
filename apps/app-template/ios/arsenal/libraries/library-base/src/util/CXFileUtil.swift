import Foundation

class CXFileUtil {
    static let TAG = "[file]"

    static let docDir = (NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true) as NSArray)[0] as! String + "/"

    static func deleteFile(_ file: File?) {
        if (file == nil) {
            return
        }
        do {
            try! FileManager.default.removeItem(atPath: file!.path!)
            CXLogUtil.d(TAG + ":deleteFile", "[文件删除成功] path:" + file!.path! + " , fileExists:" + "\(CXFileUtil.fileExists(file!.path))")
        } catch {
            CXLogUtil.d(TAG + ":deleteFile", "[文件删除失败] path:" + file!.path! + " , fileExists:" + "\(CXFileUtil.fileExists(file!.path))", error)
        }
    }

    static func deleteFile(_ path: String?) {
        do {
            try FileManager.default.removeItem(atPath: path!)
            CXLogUtil.d(TAG + ":deleteFile", "[文件删除成功] path:" + path! + " , fileExists:" + "\(CXFileUtil.fileExists(path))")
        } catch {
            CXLogUtil.d(TAG + ":deleteFile", "[文件删除失败] path:" + path! + " , fileExists:" + "\(CXFileUtil.fileExists(path))", error)
        }
    }

    static func deleteDirectory(_ path: String?) {
        do {
            try FileManager.default.removeItem(atPath: path!)
            CXLogUtil.d(TAG + ":deleteDirectory", "[目录删除成功] path:" + path! + " , fileExists:" + "\(CXFileUtil.fileExists(path))")
        } catch {
            CXLogUtil.d(TAG + ":deleteDirectory", "[目录删除失败] path:" + path! + " , fileExists:" + "\(CXFileUtil.fileExists(path))", error)
        }
        CXFileUtil.makeDirs(path)
    }

    static func makeDirs(_ path: String?) {
        do {
            try FileManager.default.createDirectory(atPath: path!, withIntermediateDirectories: true, attributes: nil)
            CXLogUtil.d(TAG + ":makeDirs", "[路径创建成功] path:" + path! + " , fileExists:" + "\(CXFileUtil.fileExists(path))")
        } catch {
            CXLogUtil.d(TAG + ":makeDirs", "[路径创建失败] path:" + path! + " , fileExists:" + "\(CXFileUtil.fileExists(path))", error)
        }
    }

    static func copy(_ fromFilePath: String, _ toFilePath: String) throws {
        CXLogUtil.d(TAG + ":copy-start", "from:" + fromFilePath + " , fileExists:\(FileManager.default.fileExists(atPath: fromFilePath))")
        try FileManager.default.copyItem(atPath: fromFilePath, toPath: toFilePath)
        CXLogUtil.d(TAG + ":copy-end", "to:" + toFilePath + " , fileExists:\(FileManager.default.fileExists(atPath: toFilePath))")
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

    static func printDirs(_ dirPath: String) {
        let files = FileManager.default.subpaths(atPath: dirPath)

        CXLogUtil.d(TAG + ":printDirs ============================================")
        CXLogUtil.d(TAG + ":printDirs dirPath:", dirPath)
        CXLogUtil.d(TAG + ":printDirs ============================================")
        if (files != nil) {
            for file in files! {
                CXLogUtil.d(TAG + ":printDirs", file)
            }
        }
        CXLogUtil.d(TAG + ":printDirs", "============================================")
    }

    static func fileExists(_ atPath: String?) -> Bool {
        return atPath != nil && !atPath!.isEmpty && FileManager.default.fileExists(atPath: atPath!)
    }
}
