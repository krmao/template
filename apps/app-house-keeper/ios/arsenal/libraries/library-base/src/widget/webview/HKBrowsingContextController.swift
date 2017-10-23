//
// Created by maokangren on 2017/10/23.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation
import UIKit
import WebKit

class HKContextControllerClass {
    class func registerSchemeSelector() -> Selector {
        return NSSelectorFromString("WKBrowsingContextController.registerSchemeForCustomProtocol")
    }

    class func unregisterSchemeSelector() -> Selector {
        return NSSelectorFromString("unregisterSchemeForCustomProtocol")
    }

    //WKBrowsingContextController registerSchemeForCustomProtocol 私有
    class func wk_registerScheme(_ scheme:String) {
        
        
        let browsingContextControllerClass:AnyClass? = NSClassFromString("WKBrowsingContextController")
        let browsingContextControllerClassType = browsingContextControllerClass as? NSObject.Type
        let xxx=browsingContextControllerClassType?.init()
        
        xxx?.perform(self.registerSchemeSelector(), with: scheme)
        
        
//        xxx?.performSelector(onMainThread: self.registerSchemeSelector(), with: "https", waitUntilDone: true)
        
//        let mirror = Mirror(reflecting: browsingContextControllerClass as Any)
//        print("对象类型:\(mirror.subjectType)")
//        print("对象子元素的个数:\(mirror.children.count)")
//        print("--- 对象子元素的属性名和属性值分别如下 ---")
//        for case let (label?, value) in mirror.children {
//            print("属性:\(label)    值:\(value)")
//        }
        
    }
}
