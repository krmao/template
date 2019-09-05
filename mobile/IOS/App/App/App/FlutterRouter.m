//
//  FlutterRouter.m
//  App
//
//  Created by krmao on 2019/8/28.
//  Copyright © 2019 smart. All rights reserved.
//

#import "FlutterRouter.h"
#import "MineViewController.h"
#import "STFlutterViewController.h"

@implementation FlutterRouter

// --------------------------------------------------
// 单例通用写法
// --------------------------------------------------
+ (instancetype)sharedInstance {
    static id instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[super allocWithZone:NULL] init];
    });
    return instance;
}

+ (instancetype)allocWithZone:(struct _NSZone *)zone {
    return [[self class] sharedInstance];
}

- (id)copy {
    return [[self class] sharedInstance];
}

- (id)mutableCopy {
    return [[self class] sharedInstance];
}
// --------------------------------------------------

- (void)close:(nonnull NSString *)uid result:(nonnull NSDictionary *)result exts:(nonnull NSDictionary *)exts completion:(nonnull void (^)(BOOL))completion { 
    NSLog(@"close uid-> %@", uid);
    NSLog(@"close result-> %@", result);
    NSLog(@"close exts-> %@", exts);
    
    BOOL animated = [exts[@"animated"] boolValue];
    
    FLBFlutterViewContainer *vc = (id)self.navigationController.presentedViewController;
    if([vc isKindOfClass:FLBFlutterViewContainer.class] && [vc.uniqueIDString isEqual: uid]){
        [vc dismissViewControllerAnimated:animated completion:^{}];
        
        FlutterBoostPlugin.sharedInstance
    }else{
        [self.navigationController popViewControllerAnimated:animated];
    }
}

- (void)open:(nonnull NSString *)url urlParams:(nonnull NSDictionary *)urlParams exts:(nonnull NSDictionary *)exts completion:(nonnull void (^)(BOOL))completion { 
    NSLog(@"open url-> %@", url);
    NSLog(@"open urlParams-> %@", urlParams);
    NSLog(@"open exts-> %@", exts);
    
    BOOL animated = [exts[@"animated"] boolValue];
    
    if([URL_MINE isEqualToString:url]){
        MineViewController *vc = [[MineViewController alloc] init];
        if([urlParams[@"present"] boolValue]){
            [self.navigationController presentViewController:vc animated:animated completion:^{
                if(completion) completion(YES);
            }];
        }else{
            [self.navigationController pushViewController:vc animated:animated];
            if(completion) completion(YES);
        }
        return;
    }
    
    STFlutterViewController *flutterViewController = [STFlutterViewController create:url urlParams:urlParams];
    // FLBFlutterViewContainer *vc = FLBFlutterViewContainer.new;
    // [vc setName:url params:urlParams];
    
    if([urlParams[@"present"] boolValue]){
        [self.navigationController presentViewController:flutterViewController animated:animated completion:^{
            if(completion) completion(YES);
        }];
    }else{
        [self.navigationController pushViewController:flutterViewController animated:animated];
        if(completion) completion(YES);
    }
}

@end
