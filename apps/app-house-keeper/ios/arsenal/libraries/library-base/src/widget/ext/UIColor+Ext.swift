import Foundation
import UIKit

/**
 * alpha 是在颜色值的开始而不是末尾，为了与 Android 操作系统一致
 */
extension UIColor {

    private static let namesMap = [
        "aliceblue": "#FFF0F8FF",
        "antiquewhite": "#FFFAEBD7",
        "aqua": "#FF00FFFF",
        "aquamarine": "#FF7FFFD4",
        "azure": "#FFF0FFFF",
        "beige": "#FFF5F5DC",
        "bisque": "#FFFFE4C4",
        "black": "#FF000000",
        "blanchedalmond": "#FFFFEBCD",
        "blue": "#FF0000FF",
        "blueviolet": "#FF8A2BE2",
        "brown": "#FFA52A2A",
        "burlywood": "#FFDEB887",
        "cadetblue": "#FF5F9EA0",
        "chartreuse": "#FF7FFF00",
        "chocolate": "#FFD2691E",
        "coral": "#FFFF7F50",
        "cornflowerblue": "#FF6495ED",
        "cornsilk": "#FFFFF8DC",
        "crimson": "#FFDC143C",
        "cyan": "#FF00FFFF",
        "darkblue": "#FF00008B",
        "darkcyan": "#FF008B8B",
        "darkgoldenrod": "#FFB8860B",
        "darkgray": "#FFA9A9A9",
        "darkgrey": "#FFA9A9A9",
        "darkgreen": "#FF006400",
        "darkkhaki": "#FFBDB76B",
        "darkmagenta": "#FF8B008B",
        "darkolivegreen": "#FF556B2F",
        "darkorange": "#FFFF8C00",
        "darkorchid": "#FF9932CC",
        "darkred": "#FF8B0000",
        "darksalmon": "#FFE9967A",
        "darkseagreen": "#FF8FBC8F",
        "darkslateblue": "#FF483D8B",
        "darkslategray": "#FF2F4F4F",
        "darkslategrey": "#FF2F4F4F",
        "darkturquoise": "#FF00CED1",
        "darkviolet": "#FF9400D3",
        "deeppink": "#FFFF1493",
        "deepskyblue": "#FF00BFFF",
        "dimgray": "#FF696969",
        "dimgrey": "#FF696969",
        "dodgerblue": "#FF1E90FF",
        "firebrick": "#FFB22222",
        "floralwhite": "#FFFFFAF0",
        "forestgreen": "#FF228B22",
        "fuchsia": "#FFFF00FF",
        "gainsboro": "#FFDCDCDC",
        "ghostwhite": "#FFF8F8FF",
        "gold": "#FFFFD700",
        "goldenrod": "#FFDAA520",
        "gray": "#FF808080",
        "grey": "#FF808080",
        "green": "#FF008000",
        "greenyellow": "#FFADFF2F",
        "honeydew": "#FFF0FFF0",
        "hotpink": "#FFFF69B4",
        "indianred": "#FFCD5C5C",
        "indigo": "#FF4B0082",
        "ivory": "#FFFFFFF0",
        "khaki": "#FFF0E68C",
        "lavender": "#FFE6E6FA",
        "lavenderblush": "#FFFFF0F5",
        "lawngreen": "#FF7CFC00",
        "lemonchiffon": "#FFFFFACD",
        "lightblue": "#FFADD8E6",
        "lightcoral": "#FFF08080",
        "lightcyan": "#FFE0FFFF",
        "lightgoldenrodyellow": "#FFFAFAD2",
        "lightgray": "#FFD3D3D3",
        "lightgrey": "#FFD3D3D3",
        "lightgreen": "#FF90EE90",
        "lightpink": "#FFFFB6C1",
        "lightsalmon": "#FFFFA07A",
        "lightseagreen": "#FF20B2AA",
        "lightskyblue": "#FF87CEFA",
        "lightslategray": "#FF778899",
        "lightslategrey": "#FF778899",
        "lightsteelblue": "#FFB0C4DE",
        "lightyellow": "#FFFFFFE0",
        "lime": "#FF00FF00",
        "limegreen": "#FF32CD32",
        "linen": "#FFFAF0E6",
        "magenta": "#FFFF00FF",
        "maroon": "#FF800000",
        "mediumaquamarine": "#FF66CDAA",
        "mediumblue": "#FF0000CD",
        "mediumorchid": "#FFBA55D3",
        "mediumpurple": "#FF9370D8",
        "mediumseagreen": "#FF3CB371",
        "mediumslateblue": "#FF7B68EE",
        "mediumspringgreen": "#FF00FA9A",
        "mediumturquoise": "#FF48D1CC",
        "mediumvioletred": "#FFC71585",
        "midnightblue": "#FF191970",
        "mintcream": "#FFF5FFFA",
        "mistyrose": "#FFFFE4E1",
        "moccasin": "#FFFFE4B5",
        "navajowhite": "#FFFFDEAD",
        "navy": "#FF000080",
        "oldlace": "#FFFDF5E6",
        "olive": "#FF808000",
        "olivedrab": "#FF6B8E23",
        "orange": "#FFFFA500",
        "orangered": "#FFFF4500",
        "orchid": "#FFDA70D6",
        "palegoldenrod": "#FFEEE8AA",
        "palegreen": "#FF98FB98",
        "paleturquoise": "#FFAFEEEE",
        "palevioletred": "#FFD87093",
        "papayawhip": "#FFFFEFD5",
        "peachpuff": "#FFFFDAB9",
        "peru": "#FFCD853F",
        "pink": "#FFFFC0CB",
        "plum": "#FFDDA0DD",
        "powderblue": "#FFB0E0E6",
        "purple": "#FF800080",
        "rebeccapurple": "#FF663399",
        "red": "#FFFF0000",
        "rosybrown": "#FFBC8F8F",
        "royalblue": "#FF4169E1",
        "saddlebrown": "#FF8B4513",
        "salmon": "#FFFA8072",
        "sandybrown": "#FFF4A460",
        "seagreen": "#FF2E8B57",
        "seashell": "#FFFFF5EE",
        "sienna": "#FFA0522D",
        "silver": "#FFC0C0C0",
        "skyblue": "#FF87CEEB",
        "slateblue": "#FF6A5ACD",
        "slategray": "#FF708090",
        "slategrey": "#FF708090",
        "snow": "#FFFFFAFA",
        "springgreen": "#FF00FF7F",
        "steelblue": "#FF4682B4",
        "tan": "#FFD2B48C",
        "teal": "#FF008080",
        "thistle": "#FFD8BFD8",
        "tomato": "#FFFF6347",
        "turquoise": "#FF40E0D0",
        "violet": "#FFEE82EE",
        "wheat": "#FFF5DEB3",
        "white": "#FFFFFFFF",
        "whitesmoke": "#FFF5F5F5",
        "yellow": "#FFFFFF00",
        "yellowgreen": "#FF9ACD32"
    ]


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

    public convenience init(_ hexStr: String) {
        var _hexStr: String = hexStr.trimmingCharacters(in: NSCharacterSet.whitespacesAndNewlines).uppercased()
        if _hexStr.hasPrefix("#") {
            _hexStr = (_hexStr as NSString).substring(from: 1)
        }
        if _hexStr.hasPrefix("0x") || _hexStr.hasPrefix("0X") {
            _hexStr = (_hexStr as NSString).substring(from: 2)
        }
        if _hexStr.count == 3 || _hexStr.count == 4 {
            var hex4 = String(repeating: "F", count: 4 - _hexStr.count)
            hex4.append(_hexStr)
            var hexUInt32: UInt32 = 0
            self.init(hex4: Scanner(string: hex4).scanHexInt32(&hexUInt32) ? hexUInt32 : 0x0000)
        } else if _hexStr.count == 6 || _hexStr.count == 8 {
            var hex8 = String(repeating: "F", count: 8 - _hexStr.count)
            hex8.append(_hexStr)
            var hexUInt32: UInt32 = 0
            self.init(hex8: Scanner(string: hex8).scanHexInt32(&hexUInt32) ? hexUInt32 : 0x00000000)
        } else {
            self.init(hex4: 0x0000)
        }
    }

    public convenience init?(name: String) {
        let cleanedName = name.replacingOccurrences(of: " ", with: "").lowercased()
        if let hexStr = UIColor.namesMap[cleanedName] {
            self.init(hexStr)
        } else {
            return nil
        }
    }
}
