//
//  FlutterRouter.h
//  App
//
//  Created by krmao on 2019/8/28.
//  Copyright © 2019 smart. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <FlutterBoost.h>

static const NSString * _Nonnull __strong URL_SETTINGS = @"flutter://flutter/settings";
static const NSString * _Nonnull __strong URL_ORDER = @"flutter://flutter/order";
static const NSString * _Nonnull __strong URL_MINE = @"flutter://native/mine";

NS_ASSUME_NONNULL_BEGIN

@interface FlutterRouter : NSObject<FLBPlatform>

@property (nonatomic,strong) UINavigationController *navigationController;

+ (instancetype)sharedInstance;

@end

NS_ASSUME_NONNULL_END
