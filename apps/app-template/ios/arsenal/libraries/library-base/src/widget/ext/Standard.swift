import Foundation

extension NSObject {
    static let TAG: String = "\(type(of: self))"

    public func hashCode() -> Int {
        return self.hashValue
    }
}