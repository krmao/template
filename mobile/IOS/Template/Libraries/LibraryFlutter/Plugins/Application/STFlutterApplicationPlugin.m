#import "STFlutterApplicationPlugin.h"

@implementation STFlutterApplicationPlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result {
    if ([functionName isEqualToString:@"getApplicationConstants"]) {
        result([self getApplicationConstants]);
    } else if ([functionName isEqualToString:@"getDeviceInfo"]) {
        result([self deviceInfo]);
    } else {
        result(FlutterMethodNotImplemented);
    }
}

- (NSDictionary *)getApplicationConstants {
    NSMutableDictionary *constantsToExport = [NSMutableDictionary dictionary];
    [constantsToExport setValue:[self deviceInfo] forKey:@"deviceInfo"];
    [constantsToExport setValue:[self applicationInfo] forKey:@"applicationInfo"];
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
        @"version": [[NSBundle mainBundle] objectForInfoDictionaryKey:@"CFBundleShortVersionString"],//显示版本号
    };
}
@end
