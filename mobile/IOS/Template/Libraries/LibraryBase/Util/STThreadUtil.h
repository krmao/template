//
//  STThreadUtil.h
//  Template
//
//  Created by krmao on 2020/12/24.
//  Copyright © 2020 smart. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface STThreadUtil : NSObject

+ (void)runInMainThread:(dispatch_block_t)block;

+ (void)runInMainThreadAsync:(dispatch_block_t)block;

+ (void)runInBackgroundThread:(dispatch_block_t)block;

@end

NS_ASSUME_NONNULL_END
