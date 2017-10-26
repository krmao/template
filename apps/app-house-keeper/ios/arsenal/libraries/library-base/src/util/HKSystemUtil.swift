import Foundation
import UIKit

class HKSystemUtil {
    static var versionCode: Int {
        get {
            return Bundle.main.object(forInfoDictionaryKey: "versionCode") as! Int
        }
    }

    static var versionName: String {
        get {
            return Bundle.main.object(forInfoDictionaryKey: "versionName") as! String
        }
    }

    static var isIPhoneX: Bool {
        get {
            return UIScreen.main.currentMode != nil ? UIScreen.main.currentMode?.size.height == 2436 : false
        }
    }

    static var statusBarHeight: CGFloat {
        get {
            let statusBarSize = UIApplication.shared.statusBarFrame.size
            return Swift.min(statusBarSize.width, statusBarSize.height)
        }
    }

    static var sdk_INT: Int {
        get {
            return Int(UIDevice.current.systemVersion.components(separatedBy: ".").first!)!
        }
    }
}
