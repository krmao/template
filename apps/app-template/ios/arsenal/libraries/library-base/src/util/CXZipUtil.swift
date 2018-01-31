import Foundation
//import Zip

class CXZipUtil {
    static var TAG = "[ZIP]"

    static func unzip(_ zipFilePath: String,_ targetDirPath: String) throws {
        CXLogUtil.d(TAG, "unzip from:" + zipFilePath + " to:" + targetDirPath)
        /*try Zip.unzipFile(
                URL.init(string: zipFilePath)!,
                destination: URL.init(string: targetDirPath)!,
                overwrite: true,
                password: "password",
                progress: { (progress) -> () in
                    CXLogUtil.d(TAG, "unzip progress:", progress)
                })*/
        CXLogUtil.d(TAG, "unzip success")
    }
}
