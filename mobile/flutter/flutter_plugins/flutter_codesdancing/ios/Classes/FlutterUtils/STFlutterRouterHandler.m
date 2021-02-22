//
//  STFlutterRouter.m
//  Template
//
//  Created by krmao on 2020/12/22.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STFlutterRouterHandler.h"
#import "FlutterBoost.h"

@interface STFlutterRouterHandler()
@end

@implementation STFlutterRouterHandler

#pragma mark - Boost 1.5
- (void)open:(NSString *)name
   urlParams:(NSDictionary *)params
        exts:(NSDictionary *)exts
  completion:(void (^)(BOOL))completion
{
    [STFlutterRouterHandler open:self.navigationController name:name urlParams:params exts:exts completion:completion];
}

- (void)present:(NSString *)name
   urlParams:(NSDictionary *)params
        exts:(NSDictionary *)exts
  completion:(void (^)(BOOL))completion
{
    BOOL animated = [exts[@"animated"] boolValue];
    FLBFlutterViewContainer *vc = FLBFlutterViewContainer.new;
    [vc setName:name params:params];
    [vc.view setBackgroundColor:UIColor.clearColor]; // 背景透明
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

+ (UIViewController *)open:(UINavigationController *)from
        name:(NSString *)name
   urlParams:(NSDictionary *)params
        exts:(NSDictionary *)exts
  completion:(void (^)(BOOL))completion
{
    BOOL animated = [exts[@"animated"] boolValue];
    UIViewController *viewController = [STFlutterRouterHandler createViewController:name urlParams:params exts:exts];
    if([params[@"present"] boolValue]){
        [from presentViewController:viewController animated:animated completion:^{
            if(completion) completion(YES);
        }];
    }else{
        [from pushViewController:viewController animated:animated];
        if(completion) completion(YES);
    }

    return viewController;
}

+ (UIViewController *)createViewController:(nullable NSString *)name
   urlParams:(nullable NSDictionary *)params
        exts:(nullable NSDictionary *)exts
{
    UIViewController * viewController;
    if ([name containsString:@"native_"]) {
        // if([name isEqualToString:@"native_mine"]){
        //     viewController = [STListViewController new];
        // }
    }else{
        FLBFlutterViewContainer *vc = FLBFlutterViewContainer.new;
        [vc setName:name params:params];
        [vc.view setBackgroundColor:UIColor.clearColor]; // 背景透明
        viewController = vc;
    }
    return viewController;
}

@end

