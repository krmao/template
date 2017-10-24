import UIKit
import WebKit
import SnapKit

class HybirdWebViewControllerV2: UIViewController {

    static func goTo(fromViewController: UIViewController, url: String) {
        let toViewController = HybirdWebViewControllerV2.init(url: url)
        if let navigationController = fromViewController as? UINavigationController {
            navigationController.pushViewController(toViewController, animated: true)
        } else if fromViewController.navigationController != nil {
            fromViewController.navigationController?.pushViewController(toViewController, animated: true)
        } else {
            fromViewController.present(toViewController, animated: true)
        }
    }

    var indexPath: String = ""

    init(url: String) {
        super.init(nibName: nil, bundle: nil)
        self.indexPath = url
    }

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }

    lazy var webView: HKUIWebView = {
        var _webView = HKUIWebView()
        _webView.isMultipleTouchEnabled = true
        return _webView
    }()

    public class var sdk_INT: Int {
        return Int(UIDevice.current.systemVersion.components(separatedBy: ".").first!)!
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        print("viewDidLoad")
        self.view.addSubview(webView)
        self.navigationItem.title = "housekeeper"

        webView.snp.makeConstraints { (make) in
            make.width.equalTo(self.view)
            make.top.equalTo(self.view).offset(0)
            make.bottom.equalTo(self.view)
        }
        
        // 适配ios11 navigationBar 隐藏的情况下
        // self.navigationController?.navigationBar.isTranslucent = false
        // self.webView.scrollView.contentInsetAdjustmentBehavior = .never
        // and set webView.backgroundColor after webViewDidFinishLoad
        
        // 适配ios11 navigationBar 显示的情况下
        // self.navigationController?.navigationBar.isTranslucent = false
        // self.webView.scrollView.contentInsetAdjustmentBehavior = .never

        // self.automaticallyAdjustsScrollViewInsets = true
        self.navigationController?.navigationBar.isTranslucent = false
        //== ios 11.0 以上 撑满底部 =============================================================
        if #available(iOS 11.0, *) {
            self.webView.scrollView.contentInsetAdjustmentBehavior = .never
        }
        //== ios 11.0 以上 撑满底部 =============================================================
        //== navigationBar.isHidden:true 状态栏颜色及 marginTop =================================
        if self.navigationController?.navigationBar.isHidden ?? false {
            self.webView.scrollView.contentInset = UIEdgeInsets(top: HKSystemUtil.statusBarHeight, left: 0, bottom: 0, right: 0)
            self.webView.backgroundColor = .white
            self.webView.setOnGetBodyBackgroundListener{( _ bodyBGColorString : String? ) -> Void in
                print("bodyColorString",bodyBGColorString ?? "")
                self.webView.backgroundColor = UIColor.init(name: bodyBGColorString!)
            }
        }
        //== navigationBar.isHidden:true 状态栏颜色及 marginTop =================================
        
        self.webView.loadRequest(URLRequest(url: URL(string: self.indexPath)!))
    }

    deinit {
        self.webView.delegate = nil
    }
}
