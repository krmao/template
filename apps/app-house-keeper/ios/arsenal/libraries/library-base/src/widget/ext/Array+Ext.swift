import Foundation

extension Array {
    public func getOrNull(_ index: Int) -> Element? {
        return (index >= 0 && index <= self.endIndex) ? self[index] : nil
    }
}
