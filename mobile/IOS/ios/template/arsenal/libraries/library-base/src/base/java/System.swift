import Foundation

class System {
    static func currentTimeMillis() -> Int {
        return Int(Date().timeIntervalSince1970 * 1000)
    }
}
