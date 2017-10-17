//
// Created by maokangren on 2017/10/16.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation

class HKSystemUtil {
    static var versionCode: Int {
        get {
            return Bundle.main.object(forInfoDictionaryKey: "versionCode") as! Int
        }
    }

    static var versionName: String {
        get {
            return Bundle.main.object(forInfoDictionaryKey: "versionName") as! String
        }
    }
}
