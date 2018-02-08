import UIKit
import WebKit
import SnapKit

class HybirdUIWebViewController: UIViewController {

    public static func goTo(fromViewController: UIViewController, url: String) {
        let toViewController = HybirdUIWebViewController(url)
        if let navigationController = fromViewController as? UINavigationController {
            navigationController.pushViewController(toViewController, animated: true)
        } else if fromViewController.navigationController != nil {
            fromViewController.navigationController?.pushViewController(toViewController, animated: true)
        } else {
            fromViewController.present(toViewController, animated: true)
        }
    }

    private var indexPath: String = ""

    public init(_ url: String) {
        super.init(nibName: nil, bundle: nil)
        self.indexPath = url
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    private lazy var webView: CXUIWebView = {
        var _webView = CXUIWebView(self.navigationController)
        return _webView
    }()

    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.addSubview(webView)
        self.navigationItem.title = "template"

        webView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.top.equalTo(self.view)
            make.bottom.equalTo(self.view)
        }

        //self.webView.loadRequest(URLRequest(url: URL(string: self.indexPath)!))
        self.webView.loadURL(self.indexPath)
    }

    deinit {
        self.webView.delegate = nil
    }
}
