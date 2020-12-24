#import "STFlutterPagePlugin.h"
#import "STFlutterViewController.h"
#import "STThreadUtil.h"

@implementation STFlutterPagePlugin

- (void)callFunction:(NSString *)functionName
           arguments:(id)arguments
              result:(FlutterResult)result{
   if ([functionName isEqualToString:@"enableExitWithDoubleBackPressed"]) {
       [STThreadUtil runInMainThread:^{
           UIViewController *vc = [self currentViewController];
           if ([vc isKindOfClass:[STFlutterViewController class]]) {
               vc.navigationController.interactivePopGestureRecognizer.enabled = NO;
           }
       }];
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

@end
