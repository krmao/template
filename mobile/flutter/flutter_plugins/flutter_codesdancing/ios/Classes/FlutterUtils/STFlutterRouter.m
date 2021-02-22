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
    [FlutterBoostPlugin open:@"native_mine" urlParams:urlParams ?: @{kPageCallBackId:@"MycallbackId#1"} exts:@{@"animated":@(YES)} onPageFinished:^(NSDictionary *result) {
        NSLog(@"call me when page finished, and your result is:%@", result);
    } completion:^(BOOL f) {
        NSLog(@"page is opened");
    }];
}

+ (void)openFlutterOrder:(NSDictionary * _Nullable)urlParams {
    [FlutterBoostPlugin open:@"flutter_order" urlParams:urlParams ?: @{kPageCallBackId:@"MycallbackId#1"} exts:@{@"animated":@(YES)} onPageFinished:^(NSDictionary *result) {
        NSLog(@"call me when page finished, and your result is:%@", result);
    } completion:^(BOOL f) {
        NSLog(@"page is opened");
    }];
}

+ (void)openFlutterPlayer:(NSDictionary * _Nullable)urlParams {
    [FlutterBoostPlugin open:@"flutter_player" urlParams:urlParams ?: @{kPageCallBackId:@"MycallbackId#1"} exts:@{@"animated":@(YES)} onPageFinished:^(NSDictionary *result) {
        NSLog(@"call me when page finished, and your result is:%@", result);
    } completion:^(BOOL f) {
        NSLog(@"page is opened");
    }];
}

+ (void)openFlutterSettings:(NSDictionary * _Nullable)urlParams {
    [FlutterBoostPlugin open:@"flutter_settings" urlParams:urlParams ?: @{kPageCallBackId:@"MycallbackId#1"} exts:@{@"animated":@(YES)} onPageFinished:^(NSDictionary *result) {
        NSLog(@"call me when page finished, and your result is:%@", result);
    } completion:^(BOOL f) {
        NSLog(@"page is opened");
    }];
}

+ (void)openFlutterBridge:( NSDictionary * _Nullable) urlParams {
    [FlutterBoostPlugin open:@"flutter_bridge" urlParams:urlParams ?: @{kPageCallBackId:@"MycallbackId#1"} exts:@{@"animated":@(YES)} onPageFinished:^(NSDictionary *result) {
        NSLog(@"call me when page finished, and your result is:%@", result);
    } completion:^(BOOL f) {
        NSLog(@"page is opened");
    }];
}

@end
