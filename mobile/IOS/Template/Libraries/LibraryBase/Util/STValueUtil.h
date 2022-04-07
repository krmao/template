//
//  STValueUtil.h
//  Template
//
//  Created by krmao on 2020/12/25.
//  Copyright © 2020 smart. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STValueUtil : NSObject

+ (nullable id) convertToNilIfNull:(nullable id)value;
+ (BOOL) isValueExist:(nullable id)value;

@end

NS_ASSUME_NONNULL_END
