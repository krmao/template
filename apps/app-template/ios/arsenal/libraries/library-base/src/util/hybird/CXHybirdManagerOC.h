//
// Created by smart on 2017/10/27.
// Copyright (c) 2017 com.smart. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CXHybirdManagerOC : NSObject
//_ className:String, _ methodName:String, _ params:[Any]
+ (NSString*) invoke: (NSString*)className  methodName:(NSString*)methodName params:(NSArray<NSString *>*)params;

@end
