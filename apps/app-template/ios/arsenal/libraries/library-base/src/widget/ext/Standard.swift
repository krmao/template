import Foundation

class TextUtils {
    static func isEmpty(_ text: String?) -> Bool {
        return text == nil || text!.trim().count == 0
    }
}

extension NSObject {
    let TAG: String = "\(type(of: self))"
}

typealias MutableList = Array

extension MutableList<T> {
    func add(_ position: Int, value: T) {

    }

    func remove(model: T) {

    }
}