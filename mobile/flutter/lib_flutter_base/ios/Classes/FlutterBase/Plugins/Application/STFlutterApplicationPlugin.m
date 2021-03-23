#import "STFlutterApplicationPlugin.h"
#import "STSystemUtil.h"
#import "STFlutterPagePlugin.h"

@implementation STFlutterApplicationPlugin

- (void)callFunction:(UIViewController *)currentViewController
        functionName:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result {
    if ([functionName isEqualToString:@"getApplicationConstants"]) {
        result([self getApplicationConstants:currentViewController]);
    } else if ([functionName isEqualToString:@"getDeviceInfo"]) {
        result([self deviceInfo]);
    } else {
        result(FlutterMethodNotImplemented);
    }
}

- (NSDictionary *)getApplicationConstants:(UIViewController *)currentViewController {
    NSMutableDictionary *constantsToExport = [NSMutableDictionary dictionary];
    [constantsToExport setValue:[self deviceInfo] forKey:@"deviceInfo"];
    [constantsToExport setValue:[self applicationInfo] forKey:@"applicationInfo"];
    [constantsToExport setValue:[STFlutterPagePlugin getUniqueId:currentViewController] forKey:@"uniqueId"];
    [constantsToExport setValue:[STFlutterPagePlugin getCurrentPageInitArguments:currentViewController] forKey:@"argumentsJsonString"];
    return constantsToExport;
}

- (NSDictionary *)deviceInfo {
    return @{
         @"osVersion":[NSString stringWithFormat:@"iOS_%@", [UIDevice currentDevice].systemVersion],
         @"deviceType":[STSystemUtil deviceMachineTypeName],
         @"deviceName":[[UIDevice currentDevice] name]?:@"",
     };
}

- (NSDictionary *)applicationInfo {
    return @{
        @"debug": [NSNumber numberWithBool:TRUE],
        @"versionCode": @"0.0.1",
        @"versionName": [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"],//显示版本号
    };
}
@end
