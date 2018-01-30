import Foundation

class CXLogUtil {
    static let ERROR = 1
    static let WARN = 2
    static let INFO = 3
    static let DEBUG = 4
    static let VERBOSE = 5

    static func d(_ items: Any?...) {
        print(items)
    }
    
    static func e(_ items: Any?...) {
        print(items)
    }
    
    static func w(_ items: Any?...) {
        print(items)
    }

    static func j(_ items: Any?...) {
        print(items)
    }
    
    static func v(_ items: Any?...) {
        print(items)
    }
    static func i(_ items: Any?...) {
        print(items)
    }
}
