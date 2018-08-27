//
// Created by maokangren on 2018/8/27.
// Copyright (c) 2018 com.smart. All rights reserved.
//

import React
import Foundation

class ReactManager {
    public static let TAG: String = "[REACT_NATIVE]"

    public static let KEY_RN_CALL_NATIVE_PARAMS_HASH_MAP: String = "KEY_RN_CALL_NATIVE_PARAMS_HASH_MAP"
    public static let KEY_RN_CALL_NATIVE_RESULT_HASH_MAP: String = "KEY_RN_CALL_NATIVE_RESULT_HASH_MAP"
    public static let KEY_NATIVE_CALL_RN_PARAMS: String = "params"


    public static var _bridge: RCTBridge? = nil
    public static var bridge: RCTBridge? {
        get {
            if (_bridge == nil) {

                // let url = URL(string: "http://10.47.57.114:8081/index.bundle?platform=ios")

                let fileURL = CXFileUtil.getFileURLFromResource("index.ios.jsbundle")!
                CXLogUtil.i(TAG, "fileURL=\(fileURL), isExists=\(CXFileUtil.fileExists(fileURL.path))")
                _bridge = RCTBridge(bundleURL: fileURL, moduleProvider: nil, launchOptions: nil)

            }
            return _bridge
        }
    }

}
