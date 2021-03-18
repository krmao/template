//
//  AppDelegate.m
//  sdfsdf
//
//  Created by Jidong Chen on 2018/10/18.
//  Copyright © 2018年 Jidong Chen. All rights reserved.
//

#import "AppDelegate.h"
#import "UIViewControllerDemo.h"
#import "NativeViewController.h"
#import "MyFlutterBoostDelegate.h"
#import <lib_flutter_base/FlutterBoost.h>
#import <lib_flutter_base/STFlutterBridge.h>
#import <lib_flutter_base/STFlutterMultipleHomeViewController.h>
#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import <LibIosBase/STBridgeDefaultCommunication.h>
#import "lib_flutter_base/LibFlutterBaseMultiplePlugin.h"
#import "FinalBridgeCommunication.h"
@interface AppDelegate ()

@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions{
    //region config bridge
    ConfigBridge *configBridge = [ConfigBridge new];
    configBridge.bridgeHandler = ^(UIViewController * _Nullable viewController, NSString * _Nullable functionName, NSString * _Nullable params, NSString * _Nullable callBackId, BridgeHandlerCallback _Nullable callback){
        NSLog(@"bridgeHandler functionName=%@, params=%@", functionName, params);
        // [STBridgeDefaultCommunication handleBridge:viewController functionName:functionName params:params callBackId:callBackId callback:callback];
        [FinalBridgeCommunication handleBridge:viewController functionName:functionName params:params callBackId:callBackId callback:callback];
    };
    //endregion
    
    //region config
    Config *config = [Config new];
    config.application = application;
    config.appDebug = TRUE;
    config.configBridge = configBridge;
    config.configBundle = ConfigBundle.new;
    config.configBundle.bundleBusHandlerClassMap = @{
        @"flutter" : @"STFlutterBusHandler"
    };
    //endregion

    [STInitializer initialApplication:config];
    
    STFlutterMultipleHomeViewController *homeViewController = nil;
    homeViewController = [[STFlutterMultipleHomeViewController alloc] initWithDartEntrypointFunctionName:@"mainFlutterBridge" argumentsJsonString:nil];

    UINavigationController *rootViewController = [[UINavigationController alloc] initWithRootViewController:homeViewController];
    rootViewController.navigationBarHidden = YES;
    rootViewController.view.backgroundColor = UIColor.clearColor;
    
    self.window = [[UIWindow alloc] initWithFrame: [UIScreen mainScreen].bounds];
    self.window.backgroundColor = UIColor.whiteColor;
    self.window.rootViewController = rootViewController;
    [self.window makeKeyAndVisible];
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}

@end
