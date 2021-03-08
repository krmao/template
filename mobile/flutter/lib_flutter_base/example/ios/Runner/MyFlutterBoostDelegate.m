//
//  MyFlutterBoostDelegate.m
//  Runner
//
//  Created by wubian on 2021/1/21.
//  Copyright © 2021 The Chromium Authors. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "MyFlutterBoostDelegate.h"
#import "UIViewControllerDemo.h"
#import <lib_flutter_base/FlutterBoost.h>

@implementation MyFlutterBoostDelegate


- (void) pushNativeRoute:(NSString *) pageName arguments:(NSDictionary *) arguments {
    NSLog(@"pushNativeRoute");
    BOOL animated = [arguments[@"animated"] boolValue];
    BOOL present= [arguments[@"present"] boolValue];
    UIViewControllerDemo *nvc = [[UIViewControllerDemo alloc] initWithNibName:@"UIViewControllerDemo" bundle:[NSBundle mainBundle]];
    if(present){
        [self.navigationController presentViewController:nvc animated:animated completion:^{
        }];
    }else{
        [self.navigationController pushViewController:nvc animated:animated];
    }
}

- (void) pushFlutterRoute:(NSString *) pageName arguments:(NSDictionary *) arguments {
    NSLog(@"pushFlutterRoute");
    FlutterEngine* engine =  [[FlutterBoost instance ] engine];
    engine.viewController = nil;
    
    FBFlutterViewContainer *vc = FBFlutterViewContainer.new ;
    
    [vc setName:pageName params:arguments];
    
    BOOL animated = [arguments[@"animated"] boolValue];
    BOOL present= [arguments[@"present"] boolValue];
    if(present){
        [self.navigationController presentViewController:vc animated:animated completion:^{
        }];
    }else{
        [self.navigationController pushViewController:vc animated:animated];

    }
}

- (void) popRoute:(NSString *)uniqueId {
    NSLog(@"popRoute");
    FBFlutterViewContainer *vc = (id)self.navigationController.presentedViewController;
    
    if([vc isKindOfClass:FBFlutterViewContainer.class] && [vc.uniqueIDString isEqual: uniqueId]){
        [vc dismissViewControllerAnimated:YES completion:^{}];
    }else{
        [self.navigationController popViewControllerAnimated:YES];
    }
    
}

@end


