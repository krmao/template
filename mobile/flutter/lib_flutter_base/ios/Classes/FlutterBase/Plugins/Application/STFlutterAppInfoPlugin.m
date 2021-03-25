#import "STFlutterAppInfoPlugin.h"
#import "STFlutterPagePlugin.h"
#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import <LibIosBase/STThreadUtil.h>
#import <LibIosBase/STStringUtil.h>
#import <LibIosBase/STSystemUtil.h>

@implementation STFlutterAppInfoPlugin

- (void)callFunction:(UIViewController *)currentViewController
        functionName:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result {
    if ([functionName isEqualToString:@"getAppInfo"]) {
        result([self getAppInfo:currentViewController]);
    } else {
        result(FlutterMethodNotImplemented);
    }
}

// value must not be nil
// @see attempt to insert nil object from objects[1]
- (NSDictionary *)getAppInfo:(UIViewController *)currentViewController {
    return @{
         @"debug": [NSNumber numberWithBool:TRUE],
         @"osVersion":[NSString stringWithFormat:@"IOS_%@", [UIDevice currentDevice].systemVersion ?: @""],
         @"deviceType":[NSString stringWithFormat:@"IOS_%@", [STSystemUtil deviceMachineTypeName] ?: @""],
         @"deviceName":[NSString stringWithFormat:@"IOS_%@", [[UIDevice currentDevice] name] ?: @""],
         @"versionName": [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"]  ?: @"",
         @"pageInfo": @{
             @"uniqueId": [STFlutterPagePlugin getUniqueId:currentViewController] ?: @"",
             @"paramsJsonObjectString": [STFlutterPagePlugin getCurrentPageInitArguments:currentViewController] ?: @"",
         },
     };
}
@end
