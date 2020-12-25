//
//  STValueUtil.m
//  Template
//
//  Created by krmao on 2020/12/25.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STValueUtil.h"

@implementation STValueUtil

+ (nullable id) convertToNilIfNull:(nullable id)value{
    __typeof__(value) t = (value);
    return (id)t == [NSNull null] ? (__typeof(value))nil : t;
}

+ (BOOL) isValueExist:(nullable id)value{
    return value && ![value isKindOfClass:[NSNull class]];
}

@end
