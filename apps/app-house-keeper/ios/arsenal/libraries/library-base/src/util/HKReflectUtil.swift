//
// Created by maokangren on 2017/10/26.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation

class HKReflectUtil {
    private static let TAG = "[reflect]"

    static func invoke(_ className: String, _ methodName: String, _ params: Any...) {
        if let cls = NSClassFromString(className) as? NSObject.Type {
            let sel = Selector(methodName)
            if cls.responds(to: sel) {
                let result: Unmanaged<AnyObject>! = cls.perform(sel, with: params)
                HKLogUtil.d(TAG, "invoke success , result=\(result)")
            } else {
                HKLogUtil.e(TAG, "the method:\(methodName) of class:\(className) is not exist!")
            }
        } else {
            HKLogUtil.e(TAG, "class:\(className) is not exist!")
        }
    }
    
//    static func getParamsCount(_ className: String, _ methodName: String) {
//        if let cls = NSClassFromString(className) as? NSObject.Type {
//            let sel = Selector(methodName)
//
////            if cls.responds(to: sel) {
////                let result: Unmanaged<AnyObject>! = cls.perform(sel, with: params)
////                HKLogUtil.d(TAG, "invoke success , result=\(result)")
////            } else {
////                HKLogUtil.e(TAG, "the method:\(methodName) of class:\(className) is not exist!")
////            }
//        } else {
//            HKLogUtil.e(TAG, "class:\(className) is not exist!")
//        }
//    }
}
