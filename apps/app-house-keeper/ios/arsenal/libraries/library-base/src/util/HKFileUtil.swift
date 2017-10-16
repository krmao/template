//
// Created by maokangren on 2017/10/12.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation

class HKFileUtil {
    static func deleteFile(_ filePath: String?) throws {
        try FileManager.default.removeItem(atPath: filePath ?? "")
    }

    static func deleteDirectory(_ filePath: String?) throws {
        try FileManager.default.removeItem(atPath: filePath ?? "")
    }

    static func copy(_ inputStream: InputStream?, _ toFilePath: String?) {
    }
}
