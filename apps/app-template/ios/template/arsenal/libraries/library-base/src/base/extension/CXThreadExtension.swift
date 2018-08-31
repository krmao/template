import Foundation

extension Thread {

    public static func currentThread() -> Thread {
        return Thread.current
    }

}