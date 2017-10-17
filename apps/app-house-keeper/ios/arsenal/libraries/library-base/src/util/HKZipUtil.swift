import Foundation
import SSZipArchive

class HKZipUtil {
    static func unzip(_ zipFilePath: String, targetDirPath: String) {
        SSZipArchive.unzipFile(atPath: zipFilePath, toDestination: targetDirPath)
    }
}
