//
// Created by smart on 2017/10/26.
// Copyright (c) 2017 com.smart. All rights reserved.
//

import Foundation

class CXHybirdMethods: NSObject {
    
    @objc
    public func showToast(_ msg: String) {
    }

    @objc
    public func showToast(_ msg: String, _ time: String) -> String? {
        return "I am back now ..."
    }

    @objc
    public func putToLocal(_ key: String, _ value: String) -> Any? {
        return nil
    }

    @objc
    public func getFromLocal(_ key: String) -> Any? {
        return self.getFromLocal(key, nil)
    }

    @objc
    public func getFromLocal(_ key: String, _ defaultValue: String?) -> Any? {
        return "krmao"
    }

    @objc
    public func putToMemory(_ module: String, _ key: String, _ value: String) -> Any? {
        return nil
    }

    @objc
    public func getFromMemory(_ module: String, _ key: String) -> String? {
        return getFromMemory(module, key, nil)
    }

    @objc
    public func getFromMemory(_ module: String, _ key: String, _ defaultValue: String?) -> String? {
        return nil
    }

    @objc
    public func removeFromMemory(_ module: String) -> Any? {
        return nil
    }

    @objc
    public func removeFromMemory(_ module: String, _ key: String) -> Any? {
        return nil
    }

    @objc
    public func isNetworkAvailable() -> Bool {
        return false
    }
}
