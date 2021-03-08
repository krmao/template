//
//  STFlutterRouter.m
//  Template
//
//  Created by krmao on 2020/12/22.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STFlutterRouter.h"
#import <Flutter/Flutter.h>
#import "FlutterBoost.h"

@implementation STFlutterRouter

+ (void)openNativeMine:(NSDictionary * _Nullable)urlParams {
    [[FlutterBoost instance] open:@"native_mine" arguments:urlParams];
}

+ (void)openFlutterOrder:(NSDictionary * _Nullable)urlParams {
    [[FlutterBoost instance] open:@"flutter_order" arguments:urlParams];
}

+ (void)openFlutterPlayer:(NSDictionary * _Nullable)urlParams {
    [[FlutterBoost instance] open:@"flutter_player" arguments:urlParams];
}

+ (void)openFlutterSettings:(NSDictionary * _Nullable)urlParams {
    [[FlutterBoost instance] open:@"flutter_settings" arguments:urlParams];
}

+ (void)openFlutterBridge:( NSDictionary * _Nullable) urlParams {
    [[FlutterBoost instance] open:@"flutter_bridge" arguments:urlParams];
}

@end
