//
// Created by maokangren on 2018/8/27.
// Copyright (c) 2018 com.smart. All rights reserved.
//

import React
import Foundation

/**
 * ios react native 分步加载
 * https://github.com/karanjthakkar/react-native/compare/v0.56.0...rn_lazy_bundle_loading
 * https://medium.com/react-native-training/lazy-bundle-loading-in-react-native-5f717b65482a
 */
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
                let fileURL = CXFileUtil.getFileURLFromResource("base.ios.bundle")!
                CXLogUtil.i(TAG, "fileURL=\(fileURL), isExists=\(CXFileUtil.fileExists(fileURL.path))")
                _bridge = RCTBridge.init(bundleURL: fileURL, moduleProvider: nil, launchOptions: nil)
            }
            return _bridge
        }
    }

    public static func loadBundle(_ bundleFullName: String?) {
        ReactManagerOC.loadBundle(_bridge, bundleFullName: bundleFullName)
    }
}