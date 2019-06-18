import Foundation

class TextUtils {
    static func isEmpty(_ text: String?) -> Bool {
        return text == nil || text!.trim().count == 0
    }
}
