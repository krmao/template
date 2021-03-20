//
//  STFlutterBridgeCompactPlugin.m
//  Template
//
//  Created by krmao on 2020/12/23.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STFlutterBridgeCompactPlugin.h"
#import "STToastUtil.h"
#import "STBridgeDefaultCommunication.h"
#import "STValueUtil.h"
#import "STInitializer.h"

@implementation STFlutterBridgeCompactPlugin

- (void)callFunction:(NSString *)functionName arguments:(id)arguments result:(FlutterResult)result{
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
