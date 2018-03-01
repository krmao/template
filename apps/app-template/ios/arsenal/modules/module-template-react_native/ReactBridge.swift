import Foundation
import React

@objc(ReactBridge)
class ReactBridge: RCTEventEmitter {

    @objc
    override public func constantsToExport() -> [AnyHashable: Any]! {
        return ["someKey": "someValue"]
    }

    /**
     * 也可以使用回调 Callback, 不过 Promise 更加的简洁
     * @see com.facebook.react.bridge.Callback
     */
    @objc
    func callNative(_ data: String?, resolver resolve: @escaping RCTPromiseResolveBlock, rejecter reject: @escaping RCTPromiseRejectBlock) -> Void {
        CXLogUtil.e("callNative", "系统检测到 react 正在调用 native 参数为:\(data ?? "null")")

        do {
            // try resolve(data)
            throw IllegalArgumentException("some error")
        } catch {
            CXLogUtil.e("callNative", "react 调用 native 出错 参数为:\(data ?? "null)")", error)
            reject("code", "msg", error)
        }
    }

    override func supportedEvents() -> [String]! {
        return []
    }

//    static func callReact(reactContext: ReactContext?, eventName: String, data: Any?) {
//        CXLogUtil.w("callReact", "\(data)")
//        reactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.type)?.emit(eventName, data)
//    }

}
