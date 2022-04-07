#import "STFlutterEnvPlugin.h"

#import <LibIosBase/STInitializer.h>
#import <LibIosBase/STJsonUtil.h>
#import <LibIosBase/STThreadUtil.h>
#import <LibIosBase/STStringUtil.h>
#import <LibIosBase/STSystemUtil.h>
#import <LibIosBase/STToastUtil.h>
#import <LibIosBase/STValueUtil.h>
#import <LibIosBase/STBridgeDefaultCommunication.h>
#import <LibIosBase/STNetworkUtil.h>
#import <LibIosBase/STURLManager.h>

@implementation STFlutterEnvPlugin

- (void)callFunction:(UIViewController *)currentViewController
        functionName:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    if ([functionName isEqualToString:@"getEnv"]) {
        result(@{@"envType": [[STURLManager sharedInstance] currentEnvironmentTypeString] });
    }else if ([functionName isEqualToString:@"getNetworkType"]){
        result(@{@"networkType": [STNetworkUtil sharedInstance].networkTypeInfo });
    }else {
        result(FlutterMethodNotImplemented);
    }
}

@end
