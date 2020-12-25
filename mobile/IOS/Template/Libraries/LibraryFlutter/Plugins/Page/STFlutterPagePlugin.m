#import "STFlutterPagePlugin.h"
#import "STFlutterViewController.h"
#import "STThreadUtil.h"

@implementation STFlutterPagePlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
    NSDictionary *parameters = arguments;
    
    if ([functionName isEqualToString:@"enableExitWithDoubleBackPressed"]) {
        [STThreadUtil runInMainThread:^{
           UIViewController *vc = [self currentViewController];
           if ([vc isKindOfClass:[STFlutterViewController class]]) {
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
