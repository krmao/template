import UIKit
import MBProgressHUD

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        CXLogUtil.i("application init start -->")

        // Override point for customization after application launch.
        window = UIWindow(frame: UIScreen.main.bounds)
        let rootViewController = UINavigationController()
        rootViewController.setNavigationBarHidden(true, animated: false)

        window?.rootViewController = rootViewController
        window?.backgroundColor = UIColor("#FFEFEFEF")
        window?.makeKeyAndVisible()

        URLProtocol.registerClass(CXUIWebViewURLProtocol.self)
        // CXURLProtocol.registerSchemeForWKWebView("http", "https")

        // let progress = MBProgressHUD.showAdded(to: self.window!, animated: true)
        // progress.bezelView.style = MBProgressHUDBackgroundStyle.blur
        // progress.bezelView.backgroundColor = UIColor("#CC000000")
        // progress.activityIndicatorColor = UIColor("#FFEFEFEF")

        // CXHybirdBridge.addNativeClass("hybird://hybird:1234", "native", NSStringFromClass(CXHybirdMethods.self))

        CXHybird.initialize(debug: true,
                initStrategy: CXHybirdInitStrategy.LOCAL) { (list: MutableList?) -> Void in

            CXLogUtil.e("初始化成功 -> \(list)")

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
    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    }

    func applicationWillTerminate(_ application: UIApplication) {
        // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    }
}

