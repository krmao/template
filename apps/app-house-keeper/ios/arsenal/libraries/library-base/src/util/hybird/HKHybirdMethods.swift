//
// Created by maokangren on 2017/10/26.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation

class HKHybirdMethods: NSObject {
    private static let TAG = "[HybirdMethods]"

    @objc
    public func showToast(_ msg: String) {
        HKLogUtil.d(HKHybirdMethods.TAG, "not static -> showToast", "msg:\(msg)")
    }

    @objc
    public func showToast(_ msg: String, _ time: String) -> String? {
        HKLogUtil.d(HKHybirdMethods.TAG, "not static -> showToast", "msg:\(msg + time)")
        return "I am back now ..."
    }

    @objc
    public func putToLocal(_ key: String, _ value: String) -> Any? {
        HKLogUtil.d(HKHybirdMethods.TAG, "putToLocal", key, value)
        return nil
    }

    @objc
    public func getFromLocal(_ key: String) -> Any? {
        return self.getFromLocal(key, nil)
    }

    @objc
    public func getFromLocal(_ key: String, _ defaultValue: String?) -> Any? {
        HKLogUtil.d(HKHybirdMethods.TAG, "getFromLocal", key, defaultValue)
        return "krmao"
    }

    @objc
    public func putToMemory(_ module: String, _ key: String, _ value: String) -> Any? {
        HKLogUtil.d(HKHybirdMethods.TAG, "putToMemory", key, value)
        return nil
    }

    @objc
    public func getFromMemory(_ module: String, _ key: String) -> String? {
        return getFromMemory(module, key, nil)
    }

    @objc
    public func getFromMemory(_ module: String, _ key: String, _ defaultValue: String?) -> String? {
        HKLogUtil.d(HKHybirdMethods.TAG, "getFromMemory", key, defaultValue)
        return nil
    }

    @objc
    public func removeFromMemory(_ module: String) -> Any? {
        HKLogUtil.d(HKHybirdMethods.TAG, "removeFromMemory", module)
        return nil
    }

    @objc
    public func removeFromMemory(_ module: String, _ key: String) -> Any? {
        HKLogUtil.d(HKHybirdMethods.TAG, "removeFromMemory", module, key)
        return nil
    }

    @objc
    public func isNetworkAvailable() -> Bool {
        HKLogUtil.d(HKHybirdMethods.TAG, "isNetworkAvailable")
        return false
    }
}
