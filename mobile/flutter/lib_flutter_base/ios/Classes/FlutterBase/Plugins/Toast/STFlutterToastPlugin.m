#import "STFlutterToastPlugin.h"

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
#import <LibIosBase/STEventManager.h>

@implementation STFlutterToastPlugin

- (void)callFunction:(UIViewController *)currentViewController
        functionName:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"show"]) {
        NSString * message = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"message"]];
        [STToastUtil show:message];
        result(nil);
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}
@end
