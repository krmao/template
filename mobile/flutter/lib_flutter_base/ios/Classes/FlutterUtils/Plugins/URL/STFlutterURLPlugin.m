#import "STFlutterURLPlugin.h"
#import <Flutter/Flutter.h>
#import "STValueUtil.h"
#import "FlutterBoostPlugin.h"

@implementation STFlutterURLPlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"openURL"]) {
        // NSString * url = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"url"]];
        // NSDictionary * urlParams = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"urlParams"]];
        // NSDictionary * exts = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"exts"]];
        
//        [FlutterBoostPlugin open:url urlParams:urlParams exts:exts onPageFinished:^(NSDictionary *result) {
//            NSLog(@"call me when page finished, and your result is:%@", result);
//        } completion:^(BOOL f) {
//            NSLog(@"page is opened");
//        }];

        result(FlutterMethodNotImplemented);
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

@end
