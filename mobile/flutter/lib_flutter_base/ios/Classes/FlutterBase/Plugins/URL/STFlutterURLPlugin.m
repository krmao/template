#import "STFlutterURLPlugin.h"
#import <Flutter/Flutter.h>
#import "STValueUtil.h"
#import "FlutterBoostPlugin.h"
#import "STFlutterUtils.h"

@implementation STFlutterURLPlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"openURL"]) {
        NSString * schemaUrl = [STValueUtil convertToNilIfNull: [parameters valueForKey:@"url"]];
        [STFlutterUtils openNewFlutterViewControllerBySchema:[self currentViewController] schemaUrl:schemaUrl];
        result(@"OK");
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

@end
