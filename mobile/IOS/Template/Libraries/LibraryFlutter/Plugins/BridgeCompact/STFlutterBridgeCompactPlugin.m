//
//  STFlutterBridgeCompactPlugin.m
//  Template
//
//  Created by krmao on 2020/12/23.
//  Copyright © 2020 smart. All rights reserved.
//

#import "STFlutterBridgeCompactPlugin.h"
#import "STToastUtil.h"
#import "STCommunication.h"

@implementation STFlutterBridgeCompactPlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
   if ([functionName isEqualToString:@"callNative"]) {
       NSString * methodName = [((NSDictionary *)arguments) valueForKey:@"functionName"];
       NSString * params = [((NSDictionary *)arguments) valueForKey:@"params"];
       NSLog(@"functionName methodName=%@, params=%@",methodName, params);
       
       [STCommunication callFunction:methodName jsonStr:params callback:^(id tmpResult) {
           result(tmpResult);
       }];
       
   } else {
       result(FlutterMethodNotImplemented);
   }
}

@end
