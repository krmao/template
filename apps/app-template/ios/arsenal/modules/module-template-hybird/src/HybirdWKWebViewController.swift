import UIKit
import WebKit
import SnapKit

class HybirdWKWebViewController: UIViewController {

    public static func goTo(fromViewController: UIViewController, url: String) {
        let toViewController = HybirdWKWebViewController.init(url: url)
        if let navigationController = fromViewController as? UINavigationController {
            navigationController.pushViewController(toViewController, animated: true)
        } else if fromViewController.navigationController != nil {
            fromViewController.navigationController?.pushViewController(toViewController, animated: true)
        } else {
            fromViewController.present(toViewController, animated: true)
        }
    }

    private var indexPath: String = ""
    private lazy var webView: HKWKWebView = {
        var _webView = HKWKWebView(self.navigationController)
        return _webView
    }()

    init(url: String) {
        super.init(nibName: nil, bundle: nil)
        self.indexPath = url
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    /*lazy var barLeftItem: UIBarButtonItem = {
        var _barLeftItem = UIBarButtonItem.init(title: "返回", style: UIBarButtonItemStyle.done, target: self, action: #selector(barClickedEvents))
        return _barLeftItem
    }()*/

    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.addSubview(webView)
        // self.navigationItem.leftBarButtonItem = barLeftItem //在此处 webView.canGoBack 已经是最新状态，可以加入逻辑判断
        // self.navigationItem.rightBarButtonItems = [barRightItem, barRightItem1]
        self.navigationItem.title = "template"
        webView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.top.equalTo(self.view).offset(0)
            make.bottom.equalTo(self.view)
        }

        webView.load(self.indexPath)
    }

    override var prefersStatusBarHidden: Bool {
        return false //self.navigationController?.navigationBar.isHidden ?? true
    }

    /*@objc func barClickedEvents(barItem: UIBarButtonItem) {
        print("barClickedEvents")
        if (barItem == barLeftItem) {
            if (webView.canGoBack) {
                webView.goBack()
            } else {
                //exit(0)
            }
        } else {
        }
    }*/

    deinit {
        self.webView.navigationDelegate = nil
        self.webView.uiDelegate = nil
    }
}
