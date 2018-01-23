import Foundation
import Zip

class HKZipUtil {
    static var TAG = "[ZIP]"

    static func unzip(_ zipFilePath: String, targetDirPath: String) throws {
        HKLogUtil.d(TAG, "unzip from:" + zipFilePath + " to:" + targetDirPath)
        try Zip.unzipFile(
                URL.init(string: zipFilePath)!,
                destination: URL.init(string: targetDirPath)!,
                overwrite: true,
                password: "password",
                progress: { (progress) -> () in
                    HKLogUtil.d(TAG, "unzip progress:", progress)
                })
        HKLogUtil.d(TAG, "unzip success")
    }
}
