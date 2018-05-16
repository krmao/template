import UIKit
import SnapKit
import React

class ReactViewController: UIViewController {

    private var count: Int = 0

    @objc
    func onButtonClick(button: UIButton) {
        if (button.tag == 1) {

        } else if (button.tag == 2) {

            self.count = self.count+1
                         self.reactRootView.bridge.eventDispatcher().sendAppEvent(withName:"native_event" , body: "msg://\(self.count)")
            //            self.reactRootView.bridge.enqueueJSCall(withName: "native_event", body: "msg://\(self.count++)")
//            self.reactRootView.bridge.enqueueJSCall("react-module-home", method: "componentWillMount", args: [], completion: {
//                print("call back ok")
//            })

            updateReactProperties(["native_params": 9000 + self.count])
        }
    }
    private lazy var btn_menu: UIButton = {
        var _btn = UIButton(type: .roundedRect)
        _btn.setTitle("菜单", for: .normal)
        _btn.tag = 1
        _btn.addTarget(self, action: #selector(onButtonClick(button:)), for: .touchUpInside)
        return _btn
    }()

    private lazy var btn_send: UIButton = {
        var _btn = UIButton(type: .roundedRect)
        _btn.setTitle("发送", for: .normal)
        _btn.tag = 2
        _btn.addTarget(self, action: #selector(onButtonClick(button:)), for: .touchUpInside)
        return _btn
    }()

    private lazy var reactRootView: RCTRootView = {
        let jsCodeLocation = URL(string: "http://10.47.57.114:8081/index.bundle?platform=ios")
//        let jsCodeLocation = URL(string: "http://10.47.60.35:8081/index.bundle?platform=ios")

        let _rootView: RCTRootView = RCTRootView(
                bundleURL: jsCodeLocation,
                moduleName: "react-module-home",
//                moduleName: "chexiangjia",
                initialProperties: ["native_params": 1] as [NSObject: AnyObject],
                launchOptions: nil
        )

        return _rootView
    }()

    override func viewDidLoad() {
        super.viewDidLoad()

        self.view.addSubview(self.reactRootView)
        self.view.addSubview(self.btn_menu)
        self.view.addSubview(self.btn_send)

        self.reactRootView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.top.equalTo(self.view)
            make.bottom.equalTo(self.view)
        }

        self.btn_menu.snp.makeConstraints { (make) in
            make.width.height.equalTo(60)
            make.bottom.right.equalTo(self.view).offset(-15)
        }

        self.btn_send.snp.makeConstraints { (make) in
            make.width.height.equalTo(60)
            make.right.equalTo(self.view).offset(-15)
            make.bottom.equalTo(self.view).offset(-80)
        }
    }

//     * 重新设置 react 属性, 并重新渲染 react 界面


    func updateReactProperties(_ bundle: MutableMap<String, Any>?) {
        self.reactRootView.appProperties = bundle
    }
}
