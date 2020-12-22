//
//  STFlutterRouter.m
//  Template
//
//  Created by krmao on 2020/12/22.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STFlutterRouterHandler.h"
#import "STListViewController.h"
#import <flutter_boost/FlutterBoost.h>

@interface STFlutterRouterHandler()
@end

@implementation STFlutterRouterHandler

- (void)openNativeVC:(NSString *)name
           urlParams:(NSDictionary *)params
                exts:(NSDictionary *)exts{
    UIViewController *vc = nil;
    
    if([name isEqualToString:@"native_mine"]){
        vc = [STListViewController new];
    }
    
    BOOL animated = [exts[@"animated"] boolValue];
    if([params[@"present"] boolValue]){
        [self.navigationController presentViewController:vc animated:animated completion:^{
        }];
    }else{
        [self.navigationController pushViewController:vc animated:animated];
    }
}

#pragma mark - Boost 1.5
- (void)open:(NSString *)name
   urlParams:(NSDictionary *)params
        exts:(NSDictionary *)exts
  completion:(void (^)(BOOL))completion
{
    if ([name containsString:@"native_"]) {//打开native页面
        [self openNativeVC:name urlParams:params exts:exts];
        return;
    }
    
    BOOL animated = [exts[@"animated"] boolValue];
    FLBFlutterViewContainer *vc = FLBFlutterViewContainer.new;
    [vc setName:name params:params];
    [self.navigationController pushViewController:vc animated:animated];
    if(completion) completion(YES);
}

- (void)present:(NSString *)name
   urlParams:(NSDictionary *)params
        exts:(NSDictionary *)exts
  completion:(void (^)(BOOL))completion
{
    BOOL animated = [exts[@"animated"] boolValue];
    FLBFlutterViewContainer *vc = FLBFlutterViewContainer.new;
    [vc setName:name params:params];
    [self.navigationController presentViewController:vc animated:animated completion:^{
        if(completion) completion(YES);
    }];
}

- (void)close:(NSString *)uid
       result:(NSDictionary *)result
         exts:(NSDictionary *)exts
   completion:(void (^)(BOOL))completion
{
    BOOL animated = [exts[@"animated"] boolValue];
    animated = YES;
    FLBFlutterViewContainer *vc = (id)self.navigationController.presentedViewController;
    if([vc isKindOfClass:FLBFlutterViewContainer.class] && [vc.uniqueIDString isEqual: uid]){
        [vc dismissViewControllerAnimated:animated completion:^{}];
    }else{
        [self.navigationController popViewControllerAnimated:animated];
    }
}
@end
