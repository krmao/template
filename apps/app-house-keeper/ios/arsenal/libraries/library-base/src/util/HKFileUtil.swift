import Foundation

class HKFileUtil {
    static let TAG = "[file]"

    static func deleteFile(_ path: String?) {
        do {
            try FileManager.default.removeItem(atPath: path!)
            HKLogUtil.d(TAG + ":deleteFile", "[文件删除成功] path:", path!, " , fileExists:", HKFileUtil.fileExists(path))
        } catch {
            HKLogUtil.d(TAG + ":deleteFile", "[文件删除失败] path:", path!, " , fileExists:", HKFileUtil.fileExists(path), error)
        }
    }

    static func deleteDirectory(_ path: String?) {
        do {
            try FileManager.default.removeItem(atPath: path!)
            HKLogUtil.d(TAG + ":deleteDirectory", "[目录删除成功] path:", path!, " , fileExists:", HKFileUtil.fileExists(path))
        } catch {
            HKLogUtil.d(TAG + ":deleteDirectory", "[目录删除失败] path:", path!, " , fileExists:", HKFileUtil.fileExists(path), error)
        }
        HKFileUtil.makeDirs(path)
    }

    static func makeDirs(_ path: String?) {
        do {
            try FileManager.default.createDirectory(atPath: path!, withIntermediateDirectories: true, attributes: nil)
            HKLogUtil.d(TAG + ":makeDirs", "[路径创建成功] path:", path!, " , fileExists:", HKFileUtil.fileExists(path))
        } catch {
            HKLogUtil.d(TAG + ":makeDirs", "[路径创建失败] path:", path!, " , fileExists:", HKFileUtil.fileExists(path), error)
        }
    }

    static func copy(_ fromFilePath: String, _ toFilePath: String) throws {
        HKLogUtil.d(TAG + ":copy-start", "from:", fromFilePath, " , fileExists:", FileManager.default.fileExists(atPath: fromFilePath))
        try FileManager.default.copyItem(atPath: fromFilePath, toPath: toFilePath)
        HKLogUtil.d(TAG + ":cop-end", "to:", toFilePath, " , fileExists:", FileManager.default.fileExists(atPath: toFilePath))
    }

    static func printDirs(_ dirPath: String) {
        let files = FileManager.default.subpaths(atPath: dirPath)

        HKLogUtil.d(TAG + ":printDirs", "============================================")
        HKLogUtil.d(TAG + ":printDirs", "dirPath:", dirPath)
        HKLogUtil.d(TAG + ":printDirs", "============================================")
        if (files != nil) {
            for file in files! {
                HKLogUtil.d(TAG + ":printDirs", file)
            }
        }
        HKLogUtil.d(TAG + ":printDirs", "============================================")
    }

    static func fileExists(_ atPath: String?) -> Bool {
        return atPath != nil && !atPath!.isEmpty && FileManager.default.fileExists(atPath: atPath!)
    }
}
