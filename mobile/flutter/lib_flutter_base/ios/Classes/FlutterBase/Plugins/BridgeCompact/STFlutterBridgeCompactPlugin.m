//
//  STFlutterBridgeCompactPlugin.m
//  Template
//
//  Created by krmao on 2020/12/23.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STFlutterBridgeCompactPlugin.h"

#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import <LibIosBase/STThreadUtil.h>
#import <LibIosBase/STStringUtil.h>
#import <LibIosBase/STSystemUtil.h>
#import <LibIosBase/STToastUtil.h>
#import <LibIosBase/STValueUtil.h>
#import <LibIosBase/STBridgeDefaultCommunication.h>

@implementation STFlutterBridgeCompactPlugin

- (void)callFunction:(UIViewController *)currentViewController functionName:(NSString *)functionName arguments:(id)arguments result:(FlutterResult)result{
    NSDictionary *parameters = arguments;

    if ([functionName isEqualToString:@"callNative"]) {
        NSString * functionName = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"functionName"]];
        NSString * params = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"params"]];
        NSLog(@"functionName functionName=%@, params=%@",functionName, params);

        if([STInitializer sharedInstance].configBridge.bridgeHandler){
            [STInitializer sharedInstance].configBridge.bridgeHandler(nil ,functionName ,params ,nil ,^(NSString * _Nullable callBackId, NSString * _Nullable resultJsonString) {
                result(resultJsonString);
            });
        }else{
            [STBridgeDefaultCommunication handleBridge:nil functionName:functionName params:params callBackId:nil callback:^(NSString * _Nullable callBackId, NSString * _Nullable resultJsonString) {
                result(resultJsonString);
            }];
        }
    } else {
        result(FlutterMethodNotImplemented);
    }
}

@end
