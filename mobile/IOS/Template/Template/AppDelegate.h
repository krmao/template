//
//  AppDelegate.h
//  Template
//
//  Created by krmao on 2020/5/21.
//  Copyright © 2020 smart. All rights reserved.
//

#import <UIKit/UIKit.h>
@import Flutter;

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow * _Nonnull window;
@property (strong, nonatomic) FlutterEngine * _Nonnull flutterEngine;

+ (UIViewController *_Nonnull) initRootViewController;

@end

