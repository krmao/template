#import "STFlutterEnvPlugin.h"

@implementation STFlutterEnvPlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"getEnv"]) {
        result(@{@"envType": [[STURLManager sharedInstance] currentEnvironmentTypeString] });
    }else if ([functionName isEqualToString:@"getNetworkType"]){
        result(@{@"networkType": [STNetworkUtil sharedInstance].networkTypeInfo });
    }else {
        result(FlutterMethodNotImplemented);
    }
}

@end
