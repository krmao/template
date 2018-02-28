import UIKit
import SnapKit
import React

class ReactViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.addSubview(webView)

        let jsCodeLocation = URL(string: "http://10.47.56.224:8081/index.bundle?platform=ios")

        let mockData: NSDictionary = [
            "native_params": 1
        ]

        let reactRootView = RCTRootView(
                bundleURL: jsCodeLocation,
                moduleName: "react-module-home",
                initialProperties: mockData as [NSObject: AnyObject],
                launchOptions: nil
        )

        reactRootView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.top.equalTo(self.view)
            make.bottom.equalTo(self.view)
        }
    }
}
