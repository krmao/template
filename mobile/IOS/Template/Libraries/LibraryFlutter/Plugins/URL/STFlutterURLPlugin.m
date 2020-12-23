#import "STFlutterURLPlugin.h"
#import <Flutter/Flutter.h>

@implementation STFlutterURLPlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    if ([functionName isEqualToString:@"openURL"]) {
        NSDictionary *args = arguments;
        NSString *url = args[@"url"]; // urlParams exts
        UIViewController *currentViewController = [self currentViewController];
        // TODO OPEN
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

@end
