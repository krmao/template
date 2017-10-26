import Foundation

extension Data {
    var string: String? {
        get {
            return String(data: self, encoding: .utf8)
        }
    }
}