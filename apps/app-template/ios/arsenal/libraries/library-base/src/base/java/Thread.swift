import Foundation

class Thread {
    var name: String?

    static func currentThread() -> Thread {
        return Thread()
    }
}
