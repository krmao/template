#import "STFlutterEnvPlugin.h"

@implementation STFlutterEnvPlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    if ([functionName isEqualToString:@"getEnv"]) {
        result(@{@"envType":@"uat"});
    }else if ([functionName isEqualToString:@"getNetworkType"]){
        result(@{@"networkType":@"wifi"});
    }else {
        result(FlutterMethodNotImplemented);
    }
}

@end
