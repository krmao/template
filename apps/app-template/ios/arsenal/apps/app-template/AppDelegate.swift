import UIKit
import MBProgressHUD
import Alamofire

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    public static var debug: Bool {
        get {
            return true
        }
    }

    public static var isApplicationVisible: Bool {
        get {
            return UIApplication.shared.applicationState == UIApplicationState.active
        }
    }

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        CXLogUtil.i("application init start DEBUG:\(CXConfig.DEBUG)-->")

        NSSetUncaughtExceptionHandler { exception in
            print(exception)
            print(exception.callStackSymbols.joined(separator: "\n"))
        }

        // Override point for customization after application launch.
        window = UIWindow(frame: UIScreen.main.bounds)
        let rootViewController = UINavigationController()
        rootViewController.setNavigationBarHidden(true, animated: false)

        window?.rootViewController = rootViewController
        window?.backgroundColor = UIColor("#FFEFEFEF")
        window?.makeKeyAndVisible()

        URLProtocol.registerClass(CXUIWebViewURLProtocol.self)
        // CXURLProtocol.registerSchemeForWKWebView("http", "https")
        // CXHybirdBridge.addNativeClass("hybird://hybird:1234", "native", NSStringFromClass(CXHybirdMethods.self))


        CXDialogUtil.showProgress()

        //异步 所有模块配置文件下载器
        var allConfiger: ((_ configUrl: String, _  callback: @escaping  (_ configList: MutableList<CXHybirdModuleConfigModel>?) -> Void?) -> Void?)? = { (configUrl, callback) in
            CXRepository.downloadHybirdAllModuleConfigurations(
                    url: configUrl,
                    success: { response in
                        callback(response)
                    },
                    failure: { message in
                        callback(nil)
                    }
            )
        }
        var configer: ((_ configUrl: String, _  callback: @escaping  (_ config: CXHybirdModuleConfigModel?) -> Void?) -> Void?)? = { (configUrl, callback) in

            callback(CXRepository.downloadHybirdModuleConfigurationSync(
                    url: configUrl,
                    timeoutInterval: 0.2
            ))

            /*CXRepository.downloadHybirdModuleConfiguration(
                    url: configUrl,
                    timeoutInterval: 0.2, //200 ms
                    success: { response in
                        callback(response)
                    },
                    failure: { message in
                        callback(nil)
                    }
            )*/
        }
        let downloader: ((_ downloadUrl: String, _ file: File?, _ callback: @escaping  (File?) -> Void?) -> Void?)? = { (downloadUrl, file, callback) in

            let destination: DownloadRequest.DownloadFileDestination = { _, _ in
                return (URL.init(fileURLWithPath: file?.path ?? ".", isDirectory: false), [.removePreviousFile, .createIntermediateDirectories])
            }

            Alamofire.download(downloadUrl, to: destination).response { response in

                debugPrint(response)

                if (response.error != nil) {
                    callback(nil)
                } else {
                    callback(file)
                }
            }

            return Void()

        }

        let allConfigUrl: String = "http://10.47.58.14:8080/background/files/all.json"

        CXHybird.initialize(
                debug: true,
                initStrategy: CXHybirdInitStrategy.DOWNLOAD,
                allConfigUrl: allConfigUrl,
                allConfiger: allConfiger,
                configer: configer,
                downloader: downloader) { (list: MutableList?) -> Void in

            CXDialogUtil.hideProgress()

            CXToastUtil.show("初始化成功")


            rootViewController.pushViewController(HybirdUIWebViewController("https://h.jia.chexiangpre.com/cx/cxj/cxjappweb/buyMealCard/index.shtml#/cardList"), animated: false)
        }


        CXLogUtil.i("application init end <--")
        return true
    }

    func applicationWillResignActive(_ application: UIApplication) {
        // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
        // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
        // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.


    }

    func applicationWillEnterForeground(_ application: UIApplication) {
        // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.

        //CXHybird.checkAllUpdate()
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
}

