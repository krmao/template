//
// Created by smart on 2017/10/27.
// Copyright (c) 2017 com.smart. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <RxCocoa/_RX.h>

@interface CXReflectUtilOC : NSObject

+ (SEL)getObjectMethodSelectorByName:(NSObject *)object methodName:(NSString *)methodName paramsCount:(int)paramsCount;

+ (id)executeMethod:(NSObject *)object methodSelector:(SEL)methodSelector params:(NSArray<id> *)params;

+ (id)invokeStaticMethod:(NSString *)className methodName:(NSString *)methodName params:(NSArray<id> *)params;

+ (id)invokeObjectMethod:(NSObject *)object methodName:(NSString *)methodName params:(NSArray<id> *)params;

+ (id)setObjectField:(NSObject *)object fieldName:(NSString *)fieldName value:(id)value;

@end
