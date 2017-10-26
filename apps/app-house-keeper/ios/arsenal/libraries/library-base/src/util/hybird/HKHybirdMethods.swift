//
// Created by maokangren on 2017/10/26.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation

class HKHybirdMethods {
    private static let TAG = "[HybirdMethods]"
    func showToast(_ message: String) {
        //HKToastUtil.show(message)
        HKLogUtil.d(HKHybirdMethods.TAG, "showToast",message)
    }
    
    func putToLocal(_ key: String,_ value: String) {
        //HKPreferencesUtil.putString(key, value)
    }
    
    func getFromLocal(_ key: String) {
        getFromLocal(key,nil)
    }
    
    func getFromLocal(_ key: String, _ default: String?) {
        //HKPreferencesUtil.getString(key, default)
    }
    
    func putToMemory(_ module: String,_ key: String,_ value: String) {
        //HKCacheManager.put(module, key, value)
    }
    
    func getFromMemory(_ module: String,_ key: String)-> String?{
        return getFromMemory(module, key ,nil)
    }
    
    func getFromMemory(_ module: String, _ key: String, _ default: String?)-> String?{
        //HKCacheManager.get(module, key, default)
        return nil
     }
    
    func removeFromMemory(_ module: String) {
        //HKCacheManager.remove(module)
    }
    
    func removeFromMemory(_ module:String , _ key: String) {
        //HKCacheManager.remove(module, key)
    }
    
    func isNetworkAvailable() -> Bool {
        //HKNetworkUtil.isNetworkAvailable()
        return false
    }
    
}
