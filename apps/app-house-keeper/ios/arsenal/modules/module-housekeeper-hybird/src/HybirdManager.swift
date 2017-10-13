//
// Created by maokangren on 2017/10/13.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

import Foundation
import MBProgressHUD
import SSZipArchive
import RxCocoa
import RxSwift

class HybirdManager {

    static func reInstallBundle(view: UIView, fromViewController: UIViewController) {
        let docDirs = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true) as NSArray
        let docDir = docDirs[0] as! String

        let progress = MBProgressHUD.showAdded(to: view, animated: true)
        progress.bezelView.style = MBProgressHUDBackgroundStyle.blur
        progress.bezelView.backgroundColor = UIColor("#CC000000")
        progress.activityIndicatorColor = UIColor("#FFEFEFEF")

        _ = Observable<Any>.create { observer in
                    let bundleZipPath = Bundle.main.path(forResource: "bundle", ofType: "zip", inDirectory: "assets") ?? ""
                    print("[hybird]", "bundleZipPath:", bundleZipPath)

                    do {
                        try FileManager.default.removeItem(atPath: docDir)
                        print("[hybird]", "remove dic success")
                        printDirs(docDir)
                    } catch {
                        print("[hybird]", "remove dic failure")
                    }

                    let unzipResult = SSZipArchive.unzipFile(atPath: bundleZipPath, toDestination: docDir)
                    print("[hybird]", "unzipResult:", unzipResult)

                    printDirs(docDir)

                    observer.onNext(0)
                    return Disposables.create()
                }
                .subscribeOn(ConcurrentDispatchQueueScheduler(qos: .background))
                .observeOn(MainScheduler.instance)
                .subscribe(
                        onNext: { index in
                            print("[hybird]", "onNext")
                            (fromViewController as! UINavigationController).pushViewController(HybirdWebViewController2.init(url: "file://" + docDir + "/index.html"), animated: false)
                            progress.hide(animated: true)
                        },
                        onError: { error in
                            print("[hybird]", "onError", error)
                            progress.hide(animated: true)
                        },
                        onCompleted: nil,
                        onDisposed: nil
                )
    }

    static func printDirs(_ dirPath: String) {
        let files = FileManager.default.subpaths(atPath: dirPath)
        print("[hybird:dirs]", "============================================")
        if (files != nil) {
            for file in files! {
                print("[hybird]", "file:", file)
            }
        }
        print("[hybird:dirs]", "============================================")
    }
}
