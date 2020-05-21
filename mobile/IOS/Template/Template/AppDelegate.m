//
//  AppDelegate.m
//  Template
//
//  Created by krmao on 2020/5/21.
//  Copyright © 2020 smart. All rights reserved.
//

#import "AppDelegate.h"
#import <FlutterPluginRegistrant/GeneratedPluginRegistrant.h>
#import "STNavigationController.h"

@interface AppDelegate ()

@end

@implementation AppDelegate


- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    // Override point for customization after application launch.
    if (@available(iOS 13.0, *)) {
        // [[UIDevice currentDevice].systemVersion doubleValue] >= 13.0
    } else {
        // [[UIDevice currentDevice].systemVersion doubleValue] < 13.0
        self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
        self.window.backgroundColor = UIColor.whiteColor;
        self.window.rootViewController = [AppDelegate initRootViewController];
        [self.window makeKeyAndVisible];
    }
    return YES;
}

+ (UIViewController *) initRootViewController {
    /*
    self.flutterEngine = [[FlutterEngine alloc] initWithName:@"my flutter engine"];
     [[self.flutterEngine navigationChannel] invokeMethod:@"setInitialRoute" arguments:@"smart://template/flutter?page=bridge&params="];
     [self.flutterEngine run];
     [GeneratedPluginRegistrant registerWithRegistry:self.flutterEngine];
     FlutterViewController *flutterViewController = [[FlutterViewController alloc] initWithEngine:self.flutterEngine nibName:nil bundle:nil];
      */

      FlutterViewController *flutterViewController = [[FlutterViewController alloc] initWithProject:nil nibName:nil bundle:nil];
      [GeneratedPluginRegistrant registerWithRegistry:flutterViewController];
      [flutterViewController setInitialRoute:@"smart://template/flutter?page=bridge&params="];

     FlutterMethodChannel* methodChannel = [FlutterMethodChannel methodChannelWithName:@"smart.template.flutter/method"
                                             binaryMessenger:flutterViewController.binaryMessenger];

     [methodChannel setMethodCallHandler:^(FlutterMethodCall* call, FlutterResult result) {
         NSLog(@"method=%@, arguments=%@",call.method, call.arguments);
     }];

     STNavigationController *rootViewNavigationController = [[STNavigationController alloc] initWithRootViewController:flutterViewController];
     rootViewNavigationController.navigationBarHidden = YES;
    return rootViewNavigationController;
}


#pragma mark - UISceneSession lifecycle


- (UISceneConfiguration *)application:(UIApplication *)application configurationForConnectingSceneSession:(UISceneSession *)connectingSceneSession options:(UISceneConnectionOptions *)options  API_AVAILABLE(ios(13.0)){
    // Called when a new scene session is being created.
    // Use this method to select a configuration to create the new scene with.
    return [[UISceneConfiguration alloc] initWithName:@"Default Configuration" sessionRole:connectingSceneSession.role];
}


- (void)application:(UIApplication *)application didDiscardSceneSessions:(NSSet<UISceneSession *> *)sceneSessions  API_AVAILABLE(ios(13.0)){
    // Called when the user discards a scene session.
    // If any sessions were discarded while the application was not running, this will be called shortly after application:didFinishLaunchingWithOptions.
    // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
}


@end
