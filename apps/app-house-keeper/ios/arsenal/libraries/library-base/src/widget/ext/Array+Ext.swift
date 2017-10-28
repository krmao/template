import Foundation

extension Array {
    public func getOrNull(_ index: Int) -> Element? {
        return (index >= 0 && index <= (self.count - 1)) ? self[index] : nil
    }
}
