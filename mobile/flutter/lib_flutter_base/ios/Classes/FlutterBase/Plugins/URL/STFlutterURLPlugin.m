#import "STFlutterURLPlugin.h"
#import <Flutter/Flutter.h>
#import "FlutterBoostPlugin.h"
#import "STFlutterUtils.h"

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

@implementation STFlutterURLPlugin

- (void)callFunction:(UIViewController *)currentViewController
        functionName:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"openURL"]) {
        NSString * schemaUrl = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"url"]];
        [STInitializer openSchema:currentViewController url:schemaUrl callback:^(NSString * _Nullable callBackId, NSString * _Nullable resultJsonString) {
            result(resultJsonString);
        }];
        // [STFlutterUtils openNewFlutterViewControllerBySchema:currentViewController schemaUrl:schemaUrl];
        // result(@"OK");
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

@end
