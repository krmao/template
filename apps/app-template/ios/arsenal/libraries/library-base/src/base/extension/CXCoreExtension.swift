import Foundation

public typealias ConcurrentHashMap = Dictionary
public typealias MutableMap = Dictionary
public typealias HashMap = Dictionary
public typealias Map = Dictionary


public typealias MutableSet = Set
public typealias MutableList = Array

extension Dictionary {

    public var size: Int {
        get {
            return self.count
        }
    }

    public mutating func remove(_ key: Dictionary.Key?) {
        if (key != nil) {
            removeValue(forKey: key!)
        }
    }

    public func containsKey(_ key: Key) -> Bool {
        return self[key] != nil
    }

    public func keys() -> Set<Dictionary.Key> {
//        return Set<Dictionary.Key>(arrayLiteral: keys)
        return Set<Dictionary.Key>()

    }

//    public func apply(_ callback: (Dictionary.Element) -> Dictionary.Element) -> Dictionary.Element {
//        return callback(self)
//    }


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
            return self.count as! Int
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

    public func firstOrNull(where predicate: (Self.Element) throws -> Bool) rethrows -> Self.Element? {
//        return self.first(predicate)
        return nil
    }
}

extension MutableCollection {
    public func mapNotNull<T>(_ transform: (Self.Element) throws -> T?) -> Array<T> {
        let values: [T] = self.map {
            try! transform($0)!
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

    public func getOrNull(_ index: Int) -> Element? {
        return (index >= 0 && index <= (self.count - 1)) ? self[index] : nil
    }

    public func sortedByDescending() -> MutableList<Array.Element> {
//        return self.sorted { (v: Element, v1: Element) in
//
//        }
        return self
    }

    mutating func add(_ position: Int, _ value: Array.Element) {
        self.insert(value, at: position)

    }

    mutating func add(_ value: Array.Element) {
        self.append(value)

    }

    mutating func remove(where predicate: (Array.Element) throws -> Bool) {
        if let i = try? index(where: predicate) {
            remove(at: i!)
        }
    }

    mutating func remove<T: Equatable>(_ obj: T) {
        if let i = try? index(where: { $0 is T && ($0 as! T) == obj }) {
            remove(at: i!)
        }
    }

    mutating func contains<T: Equatable>(_ obj: T) -> Bool {
        return contains(where: { $0 is T && ($0 as! T) == obj })
    }
}