import Foundation
import UIKit

extension UIColor {

    public class var random: UIColor {
        return UIColor(
                red: CGFloat(arc4random_uniform(256)) / 255.0,
                green: CGFloat(arc4random_uniform(256)) / 255.0,
                blue: CGFloat(arc4random_uniform(256)) / 255.0,
                alpha: 1.0)
    }

    //#argb
    public func toHexString() -> String {
        var a: CGFloat = 0
        var r: CGFloat = 0
        var g: CGFloat = 0
        var b: CGFloat = 0
        self.getRed(&r, green: &g, blue: &b, alpha: &a)
        return String(format: "#%02X%02X%02X%02X", Int(a * 255), Int(r * 255), Int(g * 255), Int(b * 255))
    }

    public func toImage(size: CGSize = CGSize(width: 1, height: 1)) -> UIImage? {
        let rect = CGRect(x: 0, y: 0, width: size.width, height: size.height)
        UIGraphicsBeginImageContextWithOptions(size, false, 0)
        self.setFill()
        UIRectFill(rect)
        let image: UIImage? = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return image
    }

    //#rgb
    public convenience init(hex3: UInt32) {
        self.init(hex4: hex3 + 0xF000)
    }

    //#argb
    public convenience init(hex4: UInt32) {
        self.init(
                red: ((CGFloat)((hex4 & 0x0F00) >> 8)) / 15.0,
                green: ((CGFloat)((hex4 & 0x00F0) >> 4)) / 15.0,
                blue: ((CGFloat)(hex4 & 0x000F)) / 15.0,
                alpha: ((CGFloat)((hex4 & 0xF000) >> 12)) / 15.0
        )
    }

    //#rgb
    public convenience init(hex6: UInt32) {
        self.init(hex8: hex6 & 0xFFFFFFFF)
    }

    //#argb
    public convenience init(hex8: UInt32) {
        self.init(
                red: ((CGFloat)((hex8 & 0xFF0000) >> 16)) / 255.0,
                green: ((CGFloat)((hex8 & 0xFF00) >> 8)) / 255.0,
                blue: ((CGFloat)(hex8 & 0xFF)) / 255.0,
                alpha: ((CGFloat)((hex8 & 0xFF000000) >> 24)) / 255.0
        )
    }

    public convenience init(hexStr: String) {
        var _hexStr: String = hexStr.trimmingCharacters(in: NSCharacterSet.whitespacesAndNewlines).uppercased()
        if _hexStr.hasPrefix("#") {
            _hexStr = (_hexStr as NSString).substring(from: 1)
        }
        if _hexStr.hasPrefix("0x") || _hexStr.hasPrefix("0X") {
            _hexStr = (_hexStr as NSString).substring(from: 2)
        }
        if _hexStr.count == 3 || _hexStr.count == 4 {
            var hex4 = String.init(repeating: "F", count: 4 - _hexStr.count)
            hex4.append(_hexStr)
            var hexUInt32: UInt32 = 0
            self.init(hex4: Scanner(string: hex4).scanHexInt32(&hexUInt32) ? hexUInt32 : 0x0000)
        } else if _hexStr.count == 6 || _hexStr.count == 8 {
            var hex8 = String.init(repeating: "F", count: 8 - _hexStr.count)
            hex8.append(_hexStr)
            let scanner = Scanner(string: hex8)
            var hexUInt32: UInt32 = 0
            self.init(hex8: Scanner(string: hex8).scanHexInt32(&hexUInt32) ? hexUInt32 : 0x00000000)
        } else {
            self.init(hex4: 0x0000)
        }
    }
}
