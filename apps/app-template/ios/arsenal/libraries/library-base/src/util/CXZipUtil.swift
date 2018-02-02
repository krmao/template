import Foundation

import Zip

class CXZipUtil {
    static var TAG = "[ZIP]"

    static func unzip(_ fromFile: File?, _ toFile: File?) throws {
        try unzip(fromFile?.path, toFile?.path)
    }

    static func unzip(_ fromPath: String?, _ toPath: String?) throws {
        if (TextUtils.isEmpty(fromPath) || TextUtils.isEmpty(toPath)) {
            CXLogUtil.e("unzip failure")
            return
        }

        CXLogUtil.d(TAG, "unzip from:\(fromPath) to:\(toPath)")
        try Zip.unzipFile(
                URL.init(string: fromPath!)!,
                destination: URL.init(string: toPath!)!,
                overwrite: true,
                password: "password",
                progress: { (progress) -> () in
                    CXLogUtil.d("unzip progress:", progress)
                })
        CXLogUtil.d(TAG, "unzip success")
    }
}
