import Foundation

class HKReflectUtil {
    private static let TAG = "[reflect]"

    /**
     * 1: 被反射的类 必须继承 NObject
     * 2: 被反射的方法 必须添加 @objc 标签
     * 3: 为了方便处理返回值，这里强制返回 Any?, 因为不加返回值 takeUnretainedValue 会 crash
     * 4: 关于返回值的可选类型
     *    ->  NSNumber for Boolean, Integer, Float and Double attributes,
     *    ->  NSString for String attributes,
     *    ->  NSSet for (unordered) to-many relationships.
     *
     * 5: example:
     *
     *     class **** : NSObject{
     *
     *         @objc
     *         public static func **() -> Any?{
     *             return nil
     *         }
     *
     *     }
     *
     */
    static func invoke(_ className: String, _ methodName: String, _ params: Any...) -> Any? {
        var result: Any? = nil
        if let cls = NSClassFromString(className) as? NSObject.Type {
            let sel = Selector(methodName + ":")
            if cls.responds(to: sel) {
                let tmpValue = cls.perform(sel, with: params)
                if (tmpValue != nil) {
                    if (tmpValue?.takeUnretainedValue() is NSString) {
                        result = tmpValue?.takeUnretainedValue() as? NSString
                        HKLogUtil.d(TAG, "invoke success, result:NSString != nil", result ?? "")
                    } else if (tmpValue?.takeUnretainedValue() is NSNumber) {
                        result = (tmpValue?.takeUnretainedValue() as? NSNumber)?.stringValue
                        HKLogUtil.d(TAG, "invoke success, result:NSNumber != nil", result ?? "")
                    } else {
                        HKLogUtil.d(TAG, "invoke success, result:UNKnowType != nil", result ?? "")
                    }
                } else {
                    HKLogUtil.d(TAG, "invoke success, result:nil", result ?? "")
                }
            } else {
                HKLogUtil.e(TAG, "the method:\(methodName) of class:\(className) is not exist!")
            }
        } else {
            HKLogUtil.e(TAG, "class:\(className) is not exist!")
        }
        return result
    }
}
