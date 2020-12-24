//
//  STThreadUtil.m
//  Template
//
//  Created by krmao on 2020/12/24.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STThreadUtil.h"

@implementation STThreadUtil

+ (void)runInMainThread:(dispatch_block_t)block{
    if (block) {
        if ([[NSThread currentThread]  isMainThread]) {
            block();
        }else{
            dispatch_sync(dispatch_get_main_queue(), ^{
                block();
            });
        }
    }
}

+ (void)runInMainThreadAsync:(dispatch_block_t)block{
    dispatch_async(dispatch_get_main_queue(), ^{
        block();
    });
}

+ (void)runInBackgroundThread:(dispatch_block_t)block{
    if (block) {
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            block();
        });
    }
}

@end
