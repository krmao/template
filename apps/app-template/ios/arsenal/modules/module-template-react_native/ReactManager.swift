//
// Created by smart on 2018/8/27.
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
                _bridge = initBridge()
            }
            return _bridge
        }
    }


    private static var loadTasks: Array<() -> ()> = []

    public static func checkLoad(callback: @escaping () -> ()) {
        if (isHadLoad) {
            callback()
        } else {
            objc_sync_enter(loadTasks)
            loadTasks.add(callback)
            objc_sync_exit(loadTasks)
        }
    }

    private static var isHadLoad = false

    private static func initBridge(callback: ((_ loadSuccess: Bool) -> ())? = nil) -> RCTBridge {
        isHadLoad = false
        var didLoadObserver: NSObjectProtocol? = nil
        didLoadObserver = NotificationCenter.default.addObserver(forName: NSNotification.Name.RCTJavaScriptDidLoad, object: nil, queue: OperationQueue.main) { notification in
            NotificationCenter.default.removeObserver(didLoadObserver)
            CXLogUtil.w("didLoadObserver")
            isHadLoad = true
            executeLoadTasks()
            callback?(true)
        }

        var didFailToLoadObserver: NSObjectProtocol? = nil
        didFailToLoadObserver = NotificationCenter.default.addObserver(forName: NSNotification.Name.RCTJavaScriptDidFailToLoad, object: nil, queue: OperationQueue.main) { notification in
            NotificationCenter.default.removeObserver(didFailToLoadObserver)
            CXLogUtil.w("didFailToLoadObserver")
            isHadLoad = true
            executeLoadTasks()
            callback?(false)
        }

        // let url = URL(string: "http://10.47.57.114:8081/index.bundle?platform=ios")
        let fileURL = CXFileUtil.getFileURLFromResource("base.ios.bundle")!
        CXLogUtil.i(TAG, "fileURL=\(fileURL), isExists=\(CXFileUtil.fileExists(fileURL.path))")
        return RCTBridge.init(bundleURL: fileURL, moduleProvider: nil, launchOptions: nil)
    }

    private static func executeLoadTasks() {
        objc_sync_enter(loadTasks)
        for (index, element) in loadTasks.enumerated().reversed(){
            element()
            let _ = loadTasks.remove(at: index)
        }
        objc_sync_exit(loadTasks)
    }

    public static func loadBundle(_ bundleFullName: String?, _ callback: ((_ loadSuccess: Bool) -> ())? = nil) {
        ReactManagerOC.loadBundle(_bridge, bundleFullName: bundleFullName, callback: callback)
    }

    public static func onDestroy() {
    }
}