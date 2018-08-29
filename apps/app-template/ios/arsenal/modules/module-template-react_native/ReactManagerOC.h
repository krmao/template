//
// Created by smart on 2017/10/27.
// Copyright (c) 2017 com.smart. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <RxCocoa/_RX.h>
#import <React/RCTBridge.h>
#import <React/RCTBridge+Private.h>

@interface ReactManagerOC : NSObject

+ (void)loadBundle:(RCTBridge *)bridge bundleFullName:(NSString *)bundleFullName;

+ (void)loadBundle:(RCTBridge *)bridge bundleFullName:(NSString *)bundleFullName callback:(void (^)(BOOL))callback;

@end
