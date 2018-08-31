import UIKit
import Foundation

@objc
class CXConfig: NSObject {

#if DEBUG
    static let DEBUG = true
#else
    static let DEBUG = false
#endif

    static let IPHONE: Bool = UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiom.phone

    static let IPHONE4_OR_LESS: Bool = IPHONE && UIScreen.main.bounds.size.height < 568
    static let IPHONE5: Bool = IPHONE && UIScreen.main.bounds.size.height == 568
    static let IPHONE6: Bool = IPHONE && UIScreen.main.bounds.size.height == 667
    static let IPHONE6P: Bool = IPHONE && UIScreen.main.bounds.size.height == 736
    static let IPHONEX: Bool = IPHONE && UIScreen.main.bounds.size.height == 812

    static let NAVIGATION_HEIGHT: CGFloat = 44 // prefersLargeTitles ? 96 : 44
    static let STATUS_BAR_HEIGHT: CGFloat = UIApplication.shared.statusBarFrame.height // IS_IPHONEX ? 44 : 20
    static let NAVIGATION_AND_STATUS_BAR_HEIGHT: CGFloat = NAVIGATION_HEIGHT + STATUS_BAR_HEIGHT

    static let TAB_BAR_HEIGHT: CGFloat = STATUS_BAR_HEIGHT > 20 ? 83 : (IPHONE6P ? 64 : 49) // 适配iPhone x 底部高度

    static let SCREEN_WIDTH = UIScreen.main.bounds.width
    static let SCREEN_HEIGHT = UIScreen.main.bounds.height

}
