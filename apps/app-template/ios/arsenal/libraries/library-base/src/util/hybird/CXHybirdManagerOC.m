//
// Created by maokangren on 2017/10/27.
// Copyright (c) 2017 com.xixi. All rights reserved.
//

#import "CXHybirdManagerOC.h"

@implementation HKHybirdManagerOC {
    
}

+ (NSString*) invoke: (NSString*)className  methodName:(NSString*)methodName params:(NSArray<NSString *>*)params{
    void *result = nil;
    Class clazz = NSClassFromString(className);
    
    NSString * _methodName = [NSString stringWithString:methodName];
    for (int i=0;i<params.count;i++) {
        _methodName = [_methodName stringByAppendingString:@":"];
    }
    NSLog(@"#1 wapper methodName with ':' = %@", _methodName);
    SEL methodSelector = NSSelectorFromString(_methodName);
    
    NSMethodSignature * methodSignature = [clazz instanceMethodSignatureForSelector: methodSelector];
    
    NSLog(@"#2 numberOfArguments:%lu", (unsigned long)methodSignature.numberOfArguments);
    NSLog(@"#2 methodReturnLength:%lu", (unsigned long)methodSignature.methodReturnLength);
    NSLog(@"#2 methodReturnType:%s", methodSignature.methodReturnType);
    
    if (params.count == (methodSignature.numberOfArguments - 2)) {
        NSLog(@"#3 方法参数数量匹配 ! numberOfArguments(%lu-2) == params.count(%lu)", (unsigned long)methodSignature.numberOfArguments,(unsigned long)params.count);
        NSInvocation* invocation = [NSInvocation invocationWithMethodSignature: methodSignature];
        [invocation setSelector: methodSelector];
        for (int i=0;i<params.count;i++) {
            NSString * param = params[i];
            [invocation setArgument:&param atIndex:i+2];
        }
        [invocation invokeWithTarget:[[clazz alloc] init]];
        
        if (methodSignature.methodReturnLength != 0) {//有返回值
            [invocation getReturnValue:&result];
        }
    } else {
        NSLog(@"#3 方法参数数量不匹配 ! numberOfArguments(%lu-2) != params.count(%lu)", (unsigned long)methodSignature.numberOfArguments,(unsigned long)params.count);
    }
    NSString * _result = (__bridge NSString *)result; //这里的result 不会自动释放，所以做特殊处理，否则 crash
    NSLog(@"#3 return %@", _result);
    return _result;
}

@end
    

