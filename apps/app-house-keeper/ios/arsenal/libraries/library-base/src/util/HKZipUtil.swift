//
// Created by maokangren on 2017/10/12.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation
import SSZipArchive

class HKZipUtil {
    static func unzip(_ zipFilePath: String, targetDirPath: String) {
        SSZipArchive.unzipFile(atPath: zipFilePath, toDestination: targetDirPath)
    }
}
