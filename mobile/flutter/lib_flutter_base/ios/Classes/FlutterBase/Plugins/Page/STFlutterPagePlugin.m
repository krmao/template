#import "STFlutterPagePlugin.h"
#import "STThreadUtil.h"
#import "STFlutterMultipleViewController.h"

@implementation STFlutterPagePlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"enableExitWithDoubleBackPressed"]) {
        [STThreadUtil runInMainThread:^{
           UIViewController *vc = [self currentViewController];
           if ([vc isKindOfClass:[STFlutterMultipleViewController class]]) {
               BOOL enable = [parameters valueForKey:@"enable"];
               vc.navigationController.interactivePopGestureRecognizer.enabled = enable;
           }
        }];
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

@end
