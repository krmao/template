//
// Created by smart on 2017/10/27.
// Copyright (c) 2017 com.smart. All rights reserved.
//

#import "CXReflectUtilOC.h"

@implementation CXReflectUtilOC {

}

+ (SEL)getObjectMethodSelectorByName:(NSObject *)object methodName:(NSString *)methodName paramsCount:(int)paramsCount {
    SEL finalMethodSelector = nil;
    unsigned int outCount = 0;
    Method *allMethods = class_copyMethodList([object class], &outCount);
    for (unsigned int i = 0; i < outCount; i++) {
        // 获取方法名
        Method tmpMethod = allMethods[i];
        unsigned int argumentCount = method_getNumberOfArguments(tmpMethod) - 2;

        SEL methodSelector = method_getName(tmpMethod);
        NSString *itemMethodName = [NSStringFromSelector(methodSelector) componentsSeparatedByString:@":"][0];


        if (itemMethodName != nil && itemMethodName.length > 0 && [itemMethodName isEqualToString:methodName] && argumentCount == paramsCount) {
            finalMethodSelector = methodSelector;

            NSLog(@"findMethod=%@, argumentCount=%d", itemMethodName, argumentCount);

            // 获取参数
            char argumentType[512] = {};
            NSLog(@"argumentCount = %d", argumentCount);

            for (unsigned int j = 0; j < argumentCount; j++) {
                // 参数类型
                method_getArgumentType(tmpMethod, j, argumentType, 512);
                NSLog(@"argumentType = %s", argumentType);
                memset(argumentType, '\0', strlen(argumentType));
            }

            // 获取方法返回值类型
            char returnType[512] = {};
            method_getReturnType(tmpMethod, returnType, 512);
            NSLog(@"returnType = %s", returnType);
        }
    }
    free(allMethods);
    return finalMethodSelector;
}

+ (id)invokeObjectMethod:(NSObject *)object methodName:(NSString *)methodName params:(NSArray<id> *)params {
    SEL methodSelector = [self getObjectMethodSelectorByName:object methodName:methodName paramsCount:(int)params.count];
    return [self executeMethod:object methodSelector:methodSelector params:params];
}

+ (id)executeMethod:(NSObject *)object methodSelector:(SEL)methodSelector params:(NSArray<id> *)params {
    Class clazz = object.class;
    id returnValue = nil;
    if (methodSelector != nil) {
        NSLog(@"#1 find methodSelector");
        NSMethodSignature *methodSignature = [clazz instanceMethodSignatureForSelector:methodSelector];
        NSLog(@"#2 numberOfArguments:%lu", (unsigned long) methodSignature.numberOfArguments);
        NSLog(@"#2 methodReturnLength:%lu", (unsigned long) methodSignature.methodReturnLength);
        NSLog(@"#2 methodReturnType:%s", methodSignature.methodReturnType);

        if (params.count == (methodSignature.numberOfArguments - 2)) {
            NSLog(@"#3 方法参数数量匹配 ! numberOfArguments(%lu-2) == params.count(%lu)", (unsigned long) methodSignature.numberOfArguments, (unsigned long) params.count);
            NSInvocation *invocation = [NSInvocation invocationWithMethodSignature:methodSignature];
            [invocation setSelector:methodSelector];
            for (unsigned int i = 0; i < params.count; i++) {
                id param = params[i];
                [invocation setArgument:&param atIndex:i + 2];
            }

            if ([object respondsToSelector:methodSelector]) {
                NSLog(@"#respondsToSelector true");

                NSLog(@"#invokeWithTarget start");
                [invocation invokeWithTarget:object];
                NSLog(@"#invokeWithTarget end");

                if (methodSignature.methodReturnLength != 0) {//有返回值
                    void *result = nil;
                    [invocation getReturnValue:&result];
                    returnValue = (__bridge id) result; //这里的result 不会自动释放，所以做特殊处理，否则 crash
                }

            } else {
                NSLog(@"#respondsToSelector false");
            }

        } else {
            NSLog(@"#3 方法参数数量不匹配 ! numberOfArguments(%lu-2) != params.count(%lu)", (unsigned long) methodSignature.numberOfArguments, (unsigned long) params.count);
        }
    } else {
        NSLog(@"#1 can't find methodSelector");
    }
    NSLog(@"##return %@", returnValue);
    return returnValue;
}

+ (id)invokeStaticMethod:(NSString *)className methodName:(NSString *)methodName params:(NSArray<id> *)params {
    Class clazz = NSClassFromString(className);
    NSString *_methodName = [NSString stringWithString:methodName];
    for (int i = 0; i < params.count; i++) {
        _methodName = [_methodName stringByAppendingString:@":"];
    }
    NSLog(@"#1 wrapper methodName with ':' = %@", _methodName);
    SEL methodSelector = NSSelectorFromString(_methodName);
    return [self executeMethod:[[clazz alloc] init] methodSelector:methodSelector params:params];
}

+ (id)setObjectField:(NSObject *)object fieldName:(NSString *)fieldName value:(id)value {
    return nil;
}

@end
    

