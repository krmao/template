//
//  UIColor+Ext.swift
//  TFUILib_Swift
//
//  Created by xiayiyong on 16/9/20.
//  Copyright © 2016年 上海赛可电子商务有限公司. All rights reserved.
//  from https://github.com/thii/SwiftHEXColors/blob/swift-3.0 https://github.com/yeahdongcn/UIColor-Hex-Swift

//例子
//var strokeColor = UIColor(hexString: "#ffcc00")
//var fillColor = UIColor(hexString: "#ffcc00dd")
//var backgroundColor = UIColor(hexString: "#FFF") // Supports shorthand 3 character representation
//var menuTextColor = UIColor(hexString: "#013E") // Supports shorthand 4 character representation (with alpha)
//var hexString = UIColor.redColor().hexString(false) // "#FF0000"
//

import UIKit
import Foundation

public func hexColor (hex:String, alpha: CGFloat) -> UIColor {
    return UIColor(hexString: hex, alpha: alpha) ?? UIColor.white
}

extension UIColor {
    
    public convenience init(r: CGFloat, g: CGFloat, b: CGFloat, a: CGFloat = 1) {
        self.init(red: r / 255.0, green: g / 255.0, blue: b / 255.0, alpha: a)
    }
    
    public convenience init(hex3: UInt32, alpha: CGFloat = 1) {
        let divisor = CGFloat(15)
        let red     = CGFloat((hex3 & 0xF00) >> 8) / divisor
        let green   = CGFloat((hex3 & 0x0F0) >> 4) / divisor
        let blue    = CGFloat( hex3 & 0x00F      ) / divisor
        self.init(red: red, green: green, blue: blue, alpha: alpha)
    }
    
    public convenience init(hex4: UInt32) {
        let divisor = CGFloat(15)
        let red     = CGFloat((hex4 & 0xF000) >> 12) / divisor
        let green   = CGFloat((hex4 & 0x0F00) >>  8) / divisor
        let blue    = CGFloat((hex4 & 0x00F0) >>  4) / divisor
        let alpha   = CGFloat( hex4 & 0x000F       ) / divisor
        self.init(red: red, green: green, blue: blue, alpha: alpha)
    }
    
    public convenience init(hex6: UInt32, alpha: CGFloat = 1) {
        let divisor = CGFloat(255)
        let red     = CGFloat((hex6 & 0xFF0000) >> 16) / divisor
        let green   = CGFloat((hex6 & 0x00FF00) >>  8) / divisor
        let blue    = CGFloat( hex6 & 0x0000FF       ) / divisor
        self.init(red: red, green: green, blue: blue, alpha: alpha)
    }
    
    //fileprivate
    public convenience init(hex8: UInt32) {
        let divisor = CGFloat(255)
        let red     = CGFloat((hex8 & 0xFF000000) >> 24) / divisor
        let green   = CGFloat((hex8 & 0x00FF0000) >> 16) / divisor
        let blue    = CGFloat((hex8 & 0x0000FF00) >>  8) / divisor
        let alpha   = CGFloat( hex8 & 0x000000FF       ) / divisor
        self.init(red: red, green: green, blue: blue, alpha: alpha)
    }
    
    public convenience init?(hexString: String, alpha: CGFloat = 1) {
        var hex = hexString.trimmingCharacters(in: NSCharacterSet.whitespacesAndNewlines).uppercased()
        
        if hex.hasPrefix("#") {
            hex = hex.substring(from: hex.index(hex.startIndex, offsetBy: 1))
        }
        
        if hex.hasPrefix("0X") {
            hex = hex.substring(from: hex.index(hex.startIndex, offsetBy: 2))
        }
        
        guard let hexValue = UInt32(hex, radix: 16) else {
            self.init()
            return nil
        }
        
        switch hex.characters.count
        {
        case 3:
            self.init(hex3: hexValue, alpha:alpha)
        case 4:
            self.init(hex4: hexValue)
        case 6:
            self.init(hex6: hexValue, alpha:alpha)
        case 8:
            self.init(hex8: hexValue)
        default:
            self.init()
            return nil
        }
    }
    
    public convenience init?(hex: UInt32, alpha: CGFloat = 1) {
        if (0x000 ... 0xFFF) ~= hex
        {
            self.init(hex3: hex, alpha:alpha)
        }
        else if (0x0000 ... 0xFFFF) ~= hex
        {
            self.init(hex4: hex)
        }
        else if (0x000000 ... 0xFFFFFF) ~= hex
        {
            self.init(hex6: hex, alpha:alpha)
        }
        else if (0x00000000 ... 0xFFFFFFFF) ~= hex
        {
            self.init(hex8: hex)
        }
        else
        {
            self.init()
            return nil
        }
    }
}

extension UIColor {
    
    public func colorComponents() -> (CGFloat, CGFloat, CGFloat, CGFloat) {
        var red: CGFloat = 0
        var green: CGFloat = 0
        var blue: CGFloat = 0
        var alpha: CGFloat = 0
        self.getRed(&red, green: &green, blue: &blue, alpha: &alpha)
        return (red, green, blue, alpha)
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
    
    public func hexString(alpha: Bool=true) -> String {
        var r: CGFloat = 0
        var g: CGFloat = 0
        var b: CGFloat = 0
        var a: CGFloat = 0
        self.getRed(&r, green: &g, blue: &b, alpha: &a)
        
        if (alpha)
        {
            return String(format: "#%02X%02X%02X%02X", Int(r * 255), Int(g * 255), Int(b * 255), Int(a * 255))
        }
        else
        {
            return String(format: "#%02X%02X%02X", Int(r * 255), Int(g * 255), Int(b * 255))
        }
    }
}

extension UIColor {
    
    public var image:UIImage? {
        let rect = CGRect(x: 0, y: 0, width: 1, height: 1)
        UIGraphicsBeginImageContextWithOptions(CGSize(width: 1, height: 1), false, 0)
        self.setFill()
        UIRectFill(rect)
        let image: UIImage? = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return image
    }
    
    public var hexString: String {
        var r:CGFloat = 0
        var g:CGFloat = 0
        var b:CGFloat = 0
        var a:CGFloat = 0
        
        getRed(&r, green: &g, blue: &b, alpha: &a)
        let rgb: Int = (Int)(r*255)<<16 | (Int)(g*255)<<8 | (Int)(b*255)<<0
        return NSString(format:"#%06x", rgb).uppercased as String
    }
    
    public var alpha: CGFloat {
        var a: CGFloat = 0
        getRed(nil, green: nil, blue: nil, alpha: &a)
        return a
    }
    
    public var r255: Int {
        var r: CGFloat = 0
        getRed(&r, green: nil, blue: nil, alpha: nil)
        return Int(r * 255)
    }
    
    public var g255: Int {
        var g: CGFloat = 0
        getRed(nil, green: &g, blue: nil, alpha: nil)
        return Int(g * 255)
    }
    
    public var b255: Int {
        var b: CGFloat = 0
        getRed(nil, green: nil, blue: &b, alpha: nil)
        return Int(b * 255)
    }
    
    public var r: CGFloat {
        var r: CGFloat = 0
        getRed(&r, green: nil, blue: nil, alpha: nil)
        return r
    }
    
    public var g: CGFloat {
        var g: CGFloat = 0
        getRed(nil, green: &g, blue: nil, alpha: nil)
        return g
    }
    
    public var b: CGFloat {
        var b: CGFloat = 0
        getRed(nil, green: nil, blue: &b, alpha: nil)
        return b
    }
    
}

extension UIColor {
    
    public class var random: UIColor {
        return UIColor(
            red: CGFloat(arc4random_uniform(256)) / 255.0,
            green: CGFloat(arc4random_uniform(256)) / 255.0,
            blue: CGFloat(arc4random_uniform(256)) / 255.0,
            alpha: 1.0)
    }
    
}

