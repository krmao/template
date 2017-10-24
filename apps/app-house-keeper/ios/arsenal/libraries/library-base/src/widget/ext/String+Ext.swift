import Foundation

extension String
{
    public func replace(_ target: String, _ withString: String) -> String
    {
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
}
