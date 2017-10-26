import Foundation

extension Dictionary {
    public func containsKey(_ key: Key) -> Bool {
        return self[key] != nil
    }
}
