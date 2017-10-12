import UIKit
import SSZipArchive
import RxCocoa
import RxSwift
import MBProgressHUD

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?


    func rgb(rgbValue: Int) -> (UIColor) {
         return UIColor(red: ((CGFloat)((rgbValue & 0xFF0000) >> 16)) / 255.0,
                                                  green: ((CGFloat)((rgbValue & 0xFF00) >> 8)) / 255.0,
                                                    blue: ((CGFloat)(rgbValue & 0xFF)) / 255.0,
                             alpha: 1.0)
         }
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
        window = UIWindow(frame: UIScreen.main.bounds)
        window?.rootViewController = UINavigationController.init()
        window?.backgroundColor =  UIColor.argb(rgbHexValue:0xcccccc)//rgb(rgbValue:0xcccccc)
        //UIColor.colorWithHexString(hexValue:"123")
        window?.makeKeyAndVisible()
        unZipHtml()
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


    let subscriptions = CompositeDisposable()

    func unZipHtml() {
        let docDirs = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true) as NSArray
        let docDir = docDirs[0] as! String
        
        let progress = MBProgressHUD.showAdded(to: self.window!, animated: true)
//        progress.bezelView.style = MBProgressHUDBackgroundStyle.blur
//        progress.bezelView.backgroundColor = .black
//        progress.activityIndicatorColor = .white
        
        _ = subscriptions.insert(Observable<Any>.create { observer in
                    let fileManager = FileManager.default
                    let bundleZipPath = Bundle.main.path(forResource: "bundle", ofType: "zip", inDirectory: "assets") ?? ""
                    print("[hybird]", "bundleZipPath:", bundleZipPath)
            
                    do {
                        try fileManager.removeItem(atPath: docDir)
                        print("[hybird]", "remove dic success")
                        let files = fileManager.subpaths(atPath: docDir)
                        print("[hybird]", "============================================")
                        if ((files) != nil) {
                            for file in files! {
                                print("[hybird]", "file:", file)
                            }
                        }
                        print("[hybird]", "============================================")
                    } catch {
                        print("[hybird]", "remove dic failure")
                    }
            
            
                    let unzipResult = SSZipArchive.unzipFile(atPath: bundleZipPath, toDestination: docDir)
                    print("[hybird]", "unzipResult:", unzipResult)
                    let files = fileManager.subpaths(atPath: docDir)
                    print("[hybird]", "============================================")
                    if (files != nil) {
                        for file in files! {
                            print("[hybird]", "file:", file)
                        }
                    }
                    print("[hybird]", "============================================")
                    observer.onNext(0)
                    return Disposables.create()
                }
                .subscribeOn(ConcurrentDispatchQueueScheduler(qos: .background))
                .observeOn(MainScheduler.instance)
                .subscribe(
                        onNext: { index in
                            print("[hybird]", "onNext")
                            (self.window?.rootViewController as! UINavigationController).pushViewController(HybirdWebViewController2.init(url: "file://" + docDir + "/index.html"), animated: false)
                            progress.hide(animated: true)
                        },
                        onError: { error in
                            print("[hybird]", "onError", error)
                            progress.hide(animated: true)
                        },
                        onCompleted: nil,
                        onDisposed: nil
                ))

        //subscriptions.dispose()
    }
}

