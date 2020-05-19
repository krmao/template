//
//  AppDelegate.m
//  App
//
//  Created by krmao on 2019/6/18.
//  Copyright © 2019 smart. All rights reserved.
//

// Used to connect plugins (only if you have plugins with iOS platform code).
#import <FlutterPluginRegistrant/GeneratedPluginRegistrant.h>

#import "AppDelegate.h"
#import "MainViewController.h"
#import "STNavigationController.h"

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary<UIApplicationLaunchOptionsKey, id> *)launchOptions {
  
    self.flutterEngine = [[FlutterEngine alloc] initWithName:@"my flutter engine"];
    // Runs the default Dart entrypoint with a default Flutter route.
    [self.flutterEngine run];
    // Used to connect plugins (only if you have plugins with iOS platform code).
    [GeneratedPluginRegistrant registerWithRegistry:self.flutterEngine];
  
    // FlutterViewController *flutterViewController = [[FlutterViewController alloc] initWithEngine:self.flutterEngine nibName:nil bundle:nil];
    FlutterViewController *flutterViewController = [[FlutterViewController alloc] initWithProject:nil nibName:nil bundle:nil];
     [GeneratedPluginRegistrant registerWithRegistry:flutterViewController];
    [flutterViewController setInitialRoute:@"smart://template/flutter?page=bridge&params="];
    STNavigationController *rootViewNavigationController = [[STNavigationController alloc] initWithRootViewController:flutterViewController];
    // STNavigationController *rootViewNavigationController = [[STNavigationController alloc] initWithRootViewController: [[MainViewController alloc] init]];
    
    rootViewNavigationController.navigationBarHidden = YES;
    
    self.uiWindow = [[UIWindow alloc] initWithFrame: [UIScreen mainScreen].bounds];
    [self.uiWindow setBackgroundColor:[UIColor whiteColor]];
    [self.uiWindow makeKeyAndVisible];
    [self.uiWindow setRootViewController:rootViewNavigationController];
    
    [[[UIApplication sharedApplication] delegate] setWindow:self.uiWindow];
    return YES;
    
}

- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and invalidate graphics rendering callbacks. Games should use this method to pause the game.
    NSLog(@"applicationWillResignActive");
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    NSLog(@"applicationDidEnterBackground");
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the active state; here you can undo many of the changes made on entering the background.
    NSLog(@"applicationWillEnterForeground");
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
    NSLog(@"applicationDidBecomeActive");
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    NSLog(@"applicationWillTerminate");
}

@end
