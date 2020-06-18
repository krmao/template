#import "ReactBridgeModule.h"
#import "STCommunication.h"
#import <React/RCTLog.h>

#pragma clang diagnostic push
#pragma ide diagnostic ignored "UnusedClassInspection"

@implementation ReactBridgeModule

RCT_EXPORT_MODULE(ReactBridge);

/**
 * https://reactnative.dev/docs/native-modules-ios#exporting-constants
 *
 * 重写 constantsToExport 务必 重写 requiresMainQueueSetup 并 return YES,
 * constantsToExport 返回的字典中 value 不能存在 nil, 否则会陷入无限循环或者奔溃白屏
 * 所以 nil 最好返回空字符串
 *
 * + (BOOL)requiresMainQueueSetup {
 *     return YES;
 * }
 *
 */
- (NSDictionary *)constantsToExport {
    return @{
            @"SDK_INT": [NSString stringWithFormat:@"%f", [STSystemUtil systemVersion]],
            @"versionCode": [NSString stringWithFormat:@"%@",[STSystemUtil appBuildVersion]],
            @"versionName":  [NSString stringWithFormat:@"%@",[STSystemUtil appVersion]],
            @"appName":  [NSString stringWithFormat:@"%@",[STSystemUtil appName]],
            @"screenWidth": [NSString stringWithFormat:@"%f", [STSystemUtil deviceScreenWidth]],
            @"screenHeight": [NSString stringWithFormat:@"%f", [STSystemUtil deviceScreenHeight]],
            @"isSdCardExist": [NSString stringWithFormat:@"%d", true],
            @"statusBarHeight": [NSString stringWithFormat:@"%f", [STSystemUtil deviceStatusBarHeight]],
            @"statusBarHeightByDensity": [NSString stringWithFormat:@"%f", [STSystemUtil deviceStatusBarHeight] / [STSystemUtil deviceDensity]],
            @"density": [NSString stringWithFormat:@"%f", [STSystemUtil deviceDensity]]
    };
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

RCT_REMAP_METHOD(callNative,
            function:
            (NSString *) functionName
            jsonStr:
            (NSString *) data
            findEventsWithResolver:
            (RCTPromiseResolveBlock) resolve
            rejecter:
            (RCTPromiseRejectBlock) reject
) {
    RCTLogInfo(@"callNative functionName=%@, data=%@", functionName, data);

    [STCommunication callFunction:functionName jsonStr:data callback:^(id result) {
        resolve(result);
    }];
}

@end

#pragma clang diagnostic pop
