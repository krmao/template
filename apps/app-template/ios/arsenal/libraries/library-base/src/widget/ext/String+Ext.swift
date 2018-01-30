import Foundation

extension String {
    public func replace(_ target: String, _ withString: String) -> String {
        return self.replacingOccurrences(of: target, with: withString, options: NSString.CompareOptions.literal, range: nil)
    }

    public func substring(from index: Int?) -> String {
        if index != nil && self.characters.count > index! {
            let startIndex = self.index(self.startIndex, offsetBy: index!)
            let subString = self[startIndex..<self.endIndex]
            return String(subString)
        } else {
            return self
        }
    }

    public func trim() -> String {
        return self.trimmingCharacters(in: .whitespacesAndNewlines)
    }

    public func isEmpty() -> Bool {
        return TextUtils.isEmpty(self)
    }

    public func isNotBlank() -> Bool {
        return !isEmpty()
    }

    public func isNullOrBlank() -> Bool {
        return isEmpty()
    }

    public func toFloatOrNull() -> Float? {
        return Float(self)
    }

    var data: Data {
        get {
            return Data(utf8)
        }
    }

    var base64Encoded: Data {
        get {
            return data.base64EncodedData()
        }
    }

    var base64Decoded: Data? {
        get {
            return Data(base64Encoded: self)
        }
    }

    var urlEncoded: String? {
        get {
            return self.addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)
        }
    }

    var urlDecoded: String? {
        get {
            return self.removingPercentEncoding
        }
    }
}
