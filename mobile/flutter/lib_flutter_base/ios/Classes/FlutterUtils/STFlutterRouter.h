//
//  STFlutterRouter.h
//  Template
//
//  Created by krmao on 2020/12/22.
//  Copyright © 2020 smart. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface STFlutterRouter : NSObject

+ (void)openNativeMine:(NSDictionary * _Nullable)urlParams;
+ (void)openFlutterOrder:(NSDictionary * _Nullable)urlParams;
+ (void)openFlutterPlayer:(NSDictionary * _Nullable)urlParams;
+ (void)openFlutterSettings:(NSDictionary * _Nullable)urlParams;
+ (void)openFlutterBridge:( NSDictionary * _Nullable) urlParams;

@end

