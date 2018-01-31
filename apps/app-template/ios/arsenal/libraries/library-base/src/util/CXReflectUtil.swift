import Foundation

class CXReflectUtil {
    private static let TAG = "[reflect]"

    /**
     * 1: 被反射的类 必须继承 NObject
     * 2: 被反射的方法 必须添加 @objc 标签 , #注意, 必须加 static
     * 3: 为了方便处理返回值，这里强制返回 Any?, 因为不加返回值 takeUnretainedValue 会 crash
     * 4: 关于返回值的可选类型
     *    ->  NSNumber for Bool, Integer, Float and Double attributes,
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
    private static func invokeByPerform(_ className: String, _ methodName: String, _ params: Any...) -> Any? {
        var result: Any? = nil
        if let cls = NSClassFromString(className) as? NSObject.Type {
            let sel = Selector(methodName + ":")
            if cls.responds(to: sel) {
                let tmpValue = cls.perform(sel, with: params)
                if (tmpValue != nil) {
                    if (tmpValue?.takeUnretainedValue() is NSString) {
                        result = tmpValue?.takeUnretainedValue() as? NSString
                        CXLogUtil.d(TAG, "invoke success, result:NSString != nil\(result)")
                    } else if (tmpValue?.takeUnretainedValue() is NSNumber) {
                        result = (tmpValue?.takeUnretainedValue() as? NSNumber)?.stringValue
                        CXLogUtil.d(TAG, "invoke success, result:NSNumber != nil\(result)")
                    } else {
                        CXLogUtil.d(TAG, "invoke success, result:UNKnowType != nil\(result)")
                    }
                } else {
                    CXLogUtil.d(TAG, "invoke success, result:nil\(result)")
                }
            } else {
                CXLogUtil.e(TAG, "the method:\(methodName) of class:\(className) is not exist!")
            }
        } else {
            CXLogUtil.e(TAG, "class:\(className) is not exist!")
        }
        return result
    }

    /**
     * 不限参数
     * 1: 被反射的类 必须继承 NObject
     * 2: 被反射的方法 必须添加 @objc 标签 , #注意, 不要加 static
     *
     */
    public static func invoke(_ className: String, _ methodName: String, _ params: [String]) -> String? {
        CXLogUtil.d(TAG, ">>>>>>>>>>-------------------->>>>>>>>>>")
        CXLogUtil.d(TAG, "\(className).\(methodName) \(params)")
        let result: String? = CXHybirdManagerOC.invoke(className, methodName: methodName, params: params)
        CXLogUtil.d(TAG, "return \(String(describing: result))")
        CXLogUtil.d(TAG, "<<<<<<<<<<--------------------<<<<<<<<<<")
        return result
    }

    //IMP(Implementation)->          指向方法实现的指针   IMP 有返回值    VIMP 无返回值   (EXC_BAD_ACCESS)
    //unsafeBitCast:                将第一个参数类型转换成第二个参数的类型, 由于UnsafePointer不安全，所以不必遵守类型转换的检查
    //UnsafePointer:                const int*          c语言中的 不可变指针
    //UnsafeMutablePointer:         int*                c语言中的 可变指针
    //
    private static func invokeTest(_ className: String, _ methodName: String, _ params: [String]) -> String? {
        CXLogUtil.d(TAG, "invokeByNSInvocation:start \(className).\(methodName)(\(params)")
        let result = CXHybirdManagerOC.invoke(className, methodName: methodName, params: params)
        let clazz = NSClassFromString(className) as? NSObject.Type
        let methodSelector = Selector(("showToast:"))//Selector(methodName + (params.isEmpty ? "" : ":"))

        if let methodInvocationWithMethodSignature: Method = class_getClassMethod(NSClassFromString("NSInvocation"), NSSelectorFromString("invocationWithMethodSignature:")) {
            if let methodSignatureForSelector: Method = class_getClassMethod(NSClassFromString("NSInvocation"), NSSelectorFromString("methodSignatureForSelector:")) {
                //========================================================================================================================================================================================================
                // #1 let methodSignature:NSMethodSignature = clazz(CXHybirdMethods).methodSignatureForSelector(methodSelector)
                //========================================================================================================================================================================================================
                let methodSignatureForSelectorFunc = unsafeBitCast(method_getImplementation(methodSignatureForSelector), to: (@convention(c)(Any?, Selector, Selector) -> Any).self )
                let methodSignature = methodSignatureForSelectorFunc(clazz, NSSelectorFromString("methodSignatureForSelector:"), methodSelector)
                CXLogUtil.d(TAG, "invokeByNSInvocation #1 let methodSignature:NSMethodSignature = clazz(CXHybirdMethods).methodSignatureForSelector(methodSelector)")
                //========================================================================================================================================================================================================
                // #2 let invocation:NSInvocation = NSInvocation.invocationWithMethodSignature(methodSignature)
                //========================================================================================================================================================================================================
                let invocationWithMethodSignatureFunc = unsafeBitCast(method_getImplementation(methodInvocationWithMethodSignature), to: ( @convention(c) (AnyClass?, Selector, Any?) -> Any).self)
                let invocation = invocationWithMethodSignatureFunc(NSClassFromString("NSInvocation"), NSSelectorFromString("invocationWithMethodSignature:"), methodSignature) as! NSObject
                CXLogUtil.d(TAG, "invokeByNSInvocation #2 let invocation:NSInvocation = NSInvocation.invocationWithMethodSignature(methodSignature)")
                //========================================================================================================================================================================================================
                // #3 invocation.setSelector(methodSelector)
                //========================================================================================================================================================================================================
                let setSeclectorFunc = unsafeBitCast(class_getMethodImplementation(NSClassFromString("NSInvocation"), NSSelectorFromString("setSelector:")), to: ( @convention(c) (Any, Selector, Selector) -> Void).self)
                setSeclectorFunc(invocation, NSSelectorFromString("setSelector:"), methodSelector)
                CXLogUtil.d(TAG, "invokeByNSInvocation #3 invocation.setSelector(methodSelector)")
                //========================================================================================================================================================================================================
                // #4 invocation.setArgument(&value, atIndex:2) //value 必须是引用，而不是值本身
                //========================================================================================================================================================================================================
                let setArgumentFunc = unsafeBitCast(class_getMethodImplementation(NSClassFromString("NSInvocation"), NSSelectorFromString("setArgument:atIndex:")), to: ( @convention(c)(Any, Selector, OpaquePointer, NSInteger) -> Void).self)
                //                for (index, param) in params.enumerated() {
                //                    var _param = param
                //                    //swift 不能使用 & 符号直接获取地址来进行操作,但是可以借助 withUnsafePointer 这个辅助方法, 接受两个参数，第一个是 inout 的任意类型，第二个是一个闭包;
                //                    //swift 会将第一个输入转换为指针，然后将这个转换后的 Unsafe 的指针作为参数，去调用闭包。
                //                    withUnsafePointer(to: &_param) { it in
                //                        setArgumentFunc(invocation, NSSelectorFromString("setArgument:atIndex:"), OpaquePointer(it), index + 2)
                //                    }
                //                }
                CXLogUtil.d(TAG, "invokeByNSInvocation #4 invocation has setArgument(&value, atIndex:2) function ?\(invocation.responds(to: NSSelectorFromString("setArgument:atIndex:")))")
                var msg = "hahahahahahah"
                withUnsafePointer(to: &msg) {
                    setArgumentFunc(invocation, NSSelectorFromString("setArgument:atIndex:"), OpaquePointer($0), 2) //[-1,0,1,2]
                }
                CXLogUtil.d(TAG, "invokeByNSInvocation #4 invocation.setArgument(&value, atIndex:2) //value 必须是引用，而不是值本身")
                //========================================================================================================================================================================================================
                // #5 invocation.invokeWithTarget(clazz)
                //========================================================================================================================================================================================================
                invocation.perform(NSSelectorFromString("invokeWithTarget:"), with: clazz?.init())
                CXLogUtil.d(TAG, "invokeByNSInvocation #5 invocation.invokeWithTarget(clazz?.init())")
            }
        }
        CXLogUtil.d(TAG, "invokeByNSInvocation:result=\(result)")
        return result
    }

    private static func extractMethodFrom(owner: AnyObject, selector: Selector) -> ((_ params: [String]) -> AnyObject)? {
        let method: Method?
        if owner is AnyClass {
            method = class_getClassMethod(owner as? AnyClass, selector)!
        } else {
            method = class_getInstanceMethod(type(of: owner), selector)!
        }
        guard method != nil else {
            return nil
        }
        let implementation = method_getImplementation(method!)
        typealias Function = @convention(c)(AnyObject, Selector, Any) -> Unmanaged<AnyObject>
        let function = unsafeBitCast(implementation, to: Function.self)
        return { _params in
            function(owner, selector, _params).takeUnretainedValue()
        }
    }
}
