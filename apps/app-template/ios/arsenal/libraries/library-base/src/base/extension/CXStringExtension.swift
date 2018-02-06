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

    /**
     * Returns a substring after the first occurrence of [delimiter].
     * If the string does not contain the delimiter, returns [missingDelimiterValue] which defaults to the original string.
     *
     * 1: origin    = something/example/2/1/image/test.png
     * 2: delimiter = /example/2/1/
     * 3: result    = image/test.png
     */
    public func substringAfter(_ delimiter: String) -> String? {
        CXLogUtil.i("substringAfter:start->\(delimiter)")
        let result = components(separatedBy: delimiter).last
        CXLogUtil.i("substringAfter:end  ->\(result)")
        return result
    }

    public func trim() -> String {
        return self.trimmingCharacters(in: .whitespacesAndNewlines)
    }

    public func isEmpty() -> Bool {
        return TextUtils.isEmpty(self)
    }

    public func startsWith(_ prefix: String, _  ignoreCase: Bool = false) -> Bool {
        return ignoreCase ? self.lowercased().hasPrefix(prefix) : self.hasPrefix(prefix)
    }

    public func endsWith(_ suffix: String, _ ignoreCase: Bool = false) -> Bool {
        return ignoreCase ? self.lowercased().hasSuffix(suffix) : self.hasSuffix(suffix)
    }

    public func isNotBlank() -> Bool {
        return !isEmpty()
    }

    public func isBlank() -> Bool {
        return isEmpty()
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

extension Substring {
    var string: String? {
        get {
            return String(self)
        }
    }
}
