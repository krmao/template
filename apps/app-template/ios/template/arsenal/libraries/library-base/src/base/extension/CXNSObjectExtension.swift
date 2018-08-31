import Foundation

extension NSObject {
    static var TAG: String {
        return "[\(String(describing: self).substringBefore(":").substringAfter("."))]"
    }

    var TAG: String {
        return "[\(String(describing: self).substringBefore(":").substringAfter("."))]"
    }

    public func hashCode() -> Int {
        return self.hashValue
    }
}