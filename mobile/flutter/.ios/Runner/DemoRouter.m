//
//  DemoRouter.m
//  Runner
//
//  Created by Jidong Chen on 2018/10/22.
//  Copyright © 2018年 The Chromium Authors. All rights reserved.
//

#import "DemoRouter.h"
#import <flutter_boost/FlutterBoost.h>
#import "MineViewController.h"

@implementation DemoRouter

+ (DemoRouter *)sharedRouter
{
    static id instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
  }

+ (UIViewController*) visibleViewController{
    UIViewController *topViewController = [[[UIApplication sharedApplication] delegate] window].rootViewController;
    while (true)
    {
        if (topViewController.presentedViewController) {
            topViewController = topViewController.presentedViewController;
        } else if ([topViewController isKindOfClass:[UINavigationController class]]) {
            UINavigationController *nav = (UINavigationController *)topViewController;
            topViewController = nav.topViewController;
        } else if ([topViewController isKindOfClass:[UITabBarController class]]) {
            UITabBarController *tab = (UITabBarController *)topViewController;
            topViewController = tab.selectedViewController;
        } else {
            break;
        }
    }
    return topViewController;
}

- (void)openPage:(NSString *)name params:(NSDictionary *)params animated:(BOOL)animated completion:(void (^)(BOOL))completion
{
    UIViewController *visibleViewController = [DemoRouter visibleViewController];
    
    if ([name isEqualToString:@"flutter://native/mine"]) {
        if(visibleViewController.navigationController){
            [visibleViewController.navigationController pushViewController:[MineViewController new] animated:YES];
        }else{
             [visibleViewController presentViewController:[MineViewController alloc] animated:YES completion:^{}];
        }
    }else{
        if([params[@"present"] boolValue] || !visibleViewController.navigationController){
            FLBFlutterViewContainer *vc = FLBFlutterViewContainer.new;
            [vc setName:name params:params];
            [visibleViewController presentViewController:vc animated:animated completion:^{}];
        }else{
            FLBFlutterViewContainer *vc = FLBFlutterViewContainer.new;
            [vc setName:name params:params];
            [visibleViewController.navigationController pushViewController:vc animated:animated];
        }
    }
}

- (BOOL)accessibilityEnable
{
    return YES;
}


- (void)closePage:(NSString *)uid animated:(BOOL)animated params:(NSDictionary *)params completion:(void (^)(BOOL))completion
{
    FLBFlutterViewContainer *vc = (id)self.navigationController.presentedViewController;
    if([vc isKindOfClass:FLBFlutterViewContainer.class] && [vc.uniqueIDString isEqual: uid]){
        [vc dismissViewControllerAnimated:animated completion:^{}];
    }else{
        [self.navigationController popViewControllerAnimated:animated];
    }
}
@end
