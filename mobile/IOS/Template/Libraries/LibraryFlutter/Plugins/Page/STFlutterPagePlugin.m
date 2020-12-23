#import "STFlutterPagePlugin.h"
#import "STFlutterViewController.h"

@implementation STFlutterPagePlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
   if ([functionName isEqualToString:@"enableExitWithDoubleBackPressed"]) {
        // TODO MAIN THREAD
        UIViewController *vc = [self currentViewController];
        if ([vc isKindOfClass:[STFlutterViewController class]]) {
            vc.navigationController.interactivePopGestureRecognizer.enabled = YES;
        }
    } else if ([functionName isEqualToString:@"disableNativeDragBack"]) {
        // TODO MAIN THREAD
        UIViewController *vc = [self currentViewController];
        if ([vc isKindOfClass:[STFlutterViewController class]]) {
            vc.navigationController.interactivePopGestureRecognizer.enabled = NO;
        }
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

@end
