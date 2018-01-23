import UIKit
import WebKit
import SnapKit

class HybirdUIWebViewController: UIViewController {

    public static func goTo(fromViewController: UIViewController, url: String) {
        let toViewController = HybirdUIWebViewController.init(url: url)
        if let navigationController = fromViewController as? UINavigationController {
            navigationController.pushViewController(toViewController, animated: true)
        } else if fromViewController.navigationController != nil {
            fromViewController.navigationController?.pushViewController(toViewController, animated: true)
        } else {
            fromViewController.present(toViewController, animated: true)
        }
    }

    private var indexPath: String = ""

    init(url: String) {
        super.init(nibName: nil, bundle: nil)
        self.indexPath = url
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    private lazy var webView: HKUIWebView = {
        var _webView = HKUIWebView(self.navigationController)
        return _webView
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        print("viewDidLoad")
        self.view.addSubview(webView)
        self.navigationItem.title = "housekeeper"

        webView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.top.equalTo(self.view)
            make.bottom.equalTo(self.view)
        }

        self.webView.loadRequest(URLRequest(url: URL(string: self.indexPath)!))
    }

    deinit {
        self.webView.delegate = nil
    }
}
