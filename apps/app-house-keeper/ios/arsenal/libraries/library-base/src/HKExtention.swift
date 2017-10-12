import Foundation
import UIKit

extension UIColor {
    static func rgb(rgbHexValue: UInt32) -> (UIColor) {
        return UIColor(
                red: ((CGFloat)((rgbHexValue & 0xFF0000) >> 16)) / 255.0,
                green: ((CGFloat)((rgbHexValue & 0xFF00) >> 8)) / 255.0,
                blue: ((CGFloat)(rgbHexValue & 0xFF)) / 255.0,
                alpha: 1.0
        )
    }

    static func argb(rgbHexValue: UInt32) -> (UIColor) {
        return UIColor(
                red: ((CGFloat)((rgbHexValue & 0xFF0000) >> 16)) / 255.0,
                green: ((CGFloat)((rgbHexValue & 0xFF00) >> 8)) / 255.0,
                blue: ((CGFloat)(rgbHexValue & 0xFF)) / 255.0,
                alpha: ((CGFloat)((rgbHexValue & 0xFF000000) >> 24)) / 255.0)
    }

    static var randomColor: UIColor {
        get {
            let red = CGFloat(arc4random() % 256) / 255.0
            let green = CGFloat(arc4random() % 256) / 255.0
            let blue = CGFloat(arc4random() % 256) / 255.0
            return UIColor(red: red, green: green, blue: blue, alpha: 1.0)
        }
    }

    static func colorWithHexString(hexValue: String) -> UIColor {
        var _hexStr: String = hexValue.trimmingCharacters(in: NSCharacterSet.whitespacesAndNewlines).uppercased()
        print("hexValue", hexValue)
        if _hexStr.hasPrefix("#") {
            _hexStr = (_hexStr as NSString).substring(from: 1)
        }
        if _hexStr.hasPrefix("0x") || _hexStr.hasPrefix("0X") {
            _hexStr = (_hexStr as NSString).substring(from: 2)
        }
        
        if _hexStr.count == 3 {
            
//            if( _hexStr[0] == _hexStr[1] && _hexStr[1] == _hexStr[2]){
            
//            }
        }
        else if _hexStr.count == 6 {
            
        }
        else if _hexStr.count == 8 {
            
        }
        else {
            return .clear
        }
        
        var fixHexStr = String.init(repeating: "F", count: 8 - _hexStr.count)
        fixHexStr.append(_hexStr)

        
        print("fixHexStr",fixHexStr)

        let scanner = Scanner(string: fixHexStr)
        var rgbHexValue: UInt32 = 0
        if scanner.scanHexInt32(&rgbHexValue) == false {
            return UIColor.clear
        } else {
            return UIColor.rgb(rgbHexValue: rgbHexValue)
        }
    }
}
