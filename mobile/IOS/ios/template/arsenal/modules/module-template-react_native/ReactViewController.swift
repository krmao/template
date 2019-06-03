import UIKit
import SnapKit
import React

class ReactViewController: UIViewController {

    private static var startCount: Int = 0

    public static func isAllPagesClosed() -> Bool {
        return ReactViewController.startCount == 0
    }

    override func viewDidLoad() {
        super.viewDidLoad()

        ReactViewController.startCount += 1
        CXLogUtil.d("startCount:\(ReactViewController.startCount)")

        self.initReact()
    }

    private func initReact() {
        // 非分步加载
         self.start()

        // 分步加载
//        self.startWithLoadBusinessBundle()
    }

    private func start() {
        let _rootView: RCTRootView = RCTRootView(
                bridge: ReactManager.bridge,
                moduleName: "smart-travel",
                initialProperties: ["native_params": 1] as [NSObject: AnyObject]
        )

        self.view.addSubview(_rootView)

        _rootView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.top.equalTo(self.view)
            make.bottom.equalTo(self.view)
        }
    }

    private func startWithLoadBusinessBundle() {
        ReactManager.checkLoad {
            ReactManager.loadBundle("business.ios.bundle") { loadSuccess in
                if (loadSuccess) {
                    CXLogUtil.w("load business.ios.bundle success")
                    self.start()
                } else {
                    CXLogUtil.w("load business.ios.bundle failure")
                }
            }
        }
    }

    deinit {
        ReactViewController.startCount -= 1
    }

//    private var count: Int = 0

//    @objc
//    func onButtonClick(button: UIButton) {
//        if (button.tag == 1) {
//
//        } else if (button.tag == 2) {
//            self.count = self.count + 1
//            self.reactRootView.bridge.eventDispatcher().sendAppEvent(withName: "native_event", body: "msg://\(self.count)")
//            //            self.reactRootView.bridge.enqueueJSCall(withName: "native_event", body: "msg://\(self.count++)")
////            self.reactRootView.bridge.enqueueJSCall("react-module-home", method: "componentWillMount", args: [], completion: {
////                print("call back ok")
////            })
//            updateReactProperties(["native_params": 9000 + self.count])
//        }
//    }

//    private lazy var reactRootView: RCTRootView = {
//        let _rootView: RCTRootView = RCTRootView(
//                bridge: ReactManager.bridge,
//                moduleName: "cxj",
//                initialProperties: nil //["native_params": 1] as [NSObject: AnyObject]
//        )
//
//        return _rootView
//    }()
//    func updateReactProperties(_ bundle: MutableMap<String, Any>?) {
//        self.reactRootView.appProperties = bundle
//    }
}
