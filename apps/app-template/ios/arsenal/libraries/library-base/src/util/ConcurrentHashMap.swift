import Foundation

typealias ConcurrentHashMap = Dictionary
typealias MutableMap = Dictionary
typealias HashMap = Dictionary
typealias Map = Dictionary


typealias MutableSet = Set
typealias MutableList = Array

extension Dictionary {

    public var size: Int {
        get {
            return self.count
        }
    }

    public mutating func remove(_ key: Dictionary.Key?) {
        if (key) {
            removeValue(forKey: key!)
        }
    }

    public func keys() -> Set<Dictionary.Key> {
        return Set<Dictionary.Key>(arrayLiteral: keys)
    }

    public func apply(_ callback: (Dictionary.Element) -> Dictionary.Element) -> Dictionary.Element {
        return callback(self)
    }


}

extension Set {
    public var size: Int {
        get {
            return self.count
        }
    }

    public func isEmpty() -> Bool {
        return isEmpty
    }

    public func isNotEmpty() -> Bool {
        return !self.isEmpty
    }

    mutating func add(_ value: Set.Element) {
        self.insert(value)
    }

    /*mutating func remove(_ obj: Set.Element) {
        if let index = index(of: obj) {
            remove(at: index)
        }
    }*/
}

extension Collection {
    public var size: Int {
        get {
            return self.count
        }
    }

    public func isEmpty() -> Bool {
        return self.isEmpty
    }

    public func isNotEmpty() -> Bool {
        return !self.isEmpty
    }

    public func firstOrNull() -> Self.Element? {
        return self.first
    }

    public func firstOrNull(_ predicate: (Self.Element?) throws -> Bool) -> Self.Element? {
        return self.first(predicate)
    }
}

extension MutableCollection {
    public func mapNotNull<T>(_ transform: (Self.Element) throws -> T?) -> Array<T> {
        let values = self.map {
            try? transform($0)
        }.filter {
            $0 != nil
        }
        return values
    }
}

extension Array {
    public var size: Int {
        get {
            return self.count
        }
    }

    public func firstOrNull() -> Array.Element? {
        return self.first
    }

    public func isNotEmpty() -> Bool {
        return !self.isEmpty
    }

    public func toMutableList() -> MutableList<Array.Element> {
        return self
    }

    public func sortedByDescending() -> MutableList<Array.Element> {
        return self.sorted { (v: Element, v1: Element) in

        }
    }

    mutating func add(_ position: Int, _ value: Array.Element) {
        self.insert(value, at: position)

    }

    mutating func add(_ value: Array.Element) {
        self.append(value)

    }

    mutating func remove(_ obj: Array.Element) {
        if let index = index(of: obj) {
            remove(at: index)
        }
    }
}