import UIKit
import SnapKit
import React

class ReactViewController: UIViewController {

    private var count: Int = 0

    @objc
    func onButtonClick(button: UIButton) {
        if (button.tag == 1) {

        } else if (button.tag == 2) {
            self.count = self.count + 1
            self.reactRootView.bridge.eventDispatcher().sendAppEvent(withName: "native_event", body: "msg://\(self.count)")
            //            self.reactRootView.bridge.enqueueJSCall(withName: "native_event", body: "msg://\(self.count++)")
//            self.reactRootView.bridge.enqueueJSCall("react-module-home", method: "componentWillMount", args: [], completion: {
//                print("call back ok")
//            })
            updateReactProperties(["native_params": 9000 + self.count])
        }
    }

    private lazy var reactRootView: RCTRootView = {
        let _rootView: RCTRootView = RCTRootView(
                bridge: ReactManager.bridge,
                moduleName: "cxj",
                initialProperties: nil //["native_params": 1] as [NSObject: AnyObject]
        )

        return _rootView
    }()

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.addSubview(self.reactRootView)

        self.reactRootView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.top.equalTo(self.view)
            make.bottom.equalTo(self.view)
        }
    }

    func updateReactProperties(_ bundle: MutableMap<String, Any>?) {
        self.reactRootView.appProperties = bundle
    }
}
